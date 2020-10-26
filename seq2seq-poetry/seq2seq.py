
import tensorflow as tf
from tensorflow.contrib import rnn

from tensorflow.python.layers import core as layers_core

#获取多隐藏层神经网络单元
def getLayeredCell(layer_size, num_units, input_keep_prob,
        output_keep_prob=1.0):
    # 构建神经网络，
    # DropoutWrapper一种有效的正则化方法，可以有效防止过拟合。
    # BasicLSTMCell 基础的LSTM循环网络单元（神经元个数）
    return rnn.MultiRNNCell([rnn.DropoutWrapper(rnn.BasicLSTMCell(num_units),
        input_keep_prob, output_keep_prob) for i in range(layer_size)])

# 构建编码encoder
def bi_encoder(embed_input, in_seq_len, num_units, layer_size, input_keep_prob):
    # 将输入编码为向量
    bi_layer_size = int(layer_size / 2)
    # 定义了正向的lstm和反向的lstm
    encode_cell_fw = getLayeredCell(bi_layer_size, num_units, input_keep_prob)
    encode_cell_bw = getLayeredCell(bi_layer_size, num_units, input_keep_prob)
    bi_encoder_output, bi_encoder_state = tf.nn.bidirectional_dynamic_rnn(
            cell_fw = encode_cell_fw,
            cell_bw = encode_cell_bw,
            inputs = embed_input,
            sequence_length = in_seq_len,
            dtype = embed_input.dtype,
            time_major = False)

    # 拼接编码输出和状态
    encoder_output = tf.concat(bi_encoder_output, -1)
    encoder_state = []
    for layer_id in range(bi_layer_size):
        encoder_state.append(bi_encoder_state[0][layer_id])
        encoder_state.append(bi_encoder_state[1][layer_id])
    encoder_state = tuple(encoder_state)
    return encoder_output, encoder_state

# 注意力机制
# 定义decoder的rnn神经网络 带有attention机制的多层rnn。
def attention_decoder_cell(encoder_output, in_seq_len, num_units, layer_size,
        input_keep_prob):
    attention_mechanim = tf.contrib.seq2seq.BahdanauAttention(num_units,
            encoder_output, in_seq_len, normalize = True)
    # attention_mechanim = tf.contrib.seq2seq.LuongAttention(num_units,
    #         encoder_output, in_seq_len, scale = True)
    cell = getLayeredCell(layer_size, num_units, input_keep_prob)
    cell = tf.contrib.seq2seq.AttentionWrapper(cell, attention_mechanim,
            attention_layer_size=num_units)
    return cell




def seq2seq(in_seq, in_seq_len, target_seq, target_seq_len, vocab_size,
        num_units, layers, dropout):
    #获取维度信息
    in_shape = tf.shape(in_seq)
    batch_size = in_shape[0]

    if target_seq != None:
        input_keep_prob = 1 - dropout
    else:
        input_keep_prob = 1


    projection_layer=layers_core.Dense(vocab_size, use_bias=False)

    # embedding input and target sequence
    # 嵌入输入和目标序列
    with tf.device('/cpu:0'):
        embedding = tf.get_variable(
                name = 'embedding',
                shape = [vocab_size, num_units])
    embed_input = tf.nn.embedding_lookup(embedding, in_seq, name='embed_input')

    # encode and decode
    encoder_output, encoder_state = bi_encoder(embed_input, in_seq_len,
            num_units, layers, input_keep_prob)



    decoder_cell = attention_decoder_cell(encoder_output, in_seq_len, num_units,
            layers, input_keep_prob)
    batch_size = tf.shape(in_seq_len)[0]
    init_state = decoder_cell.zero_state(batch_size, tf.float32).clone(
            cell_state=encoder_state)

    if target_seq != None:
        embed_target = tf.nn.embedding_lookup(embedding, target_seq,
                name='embed_target')
        helper = tf.contrib.seq2seq.TrainingHelper(
                    embed_target, target_seq_len, time_major=False)
    else:
        # TODO: start tokens and end tokens are hard code
        helper = tf.contrib.seq2seq.GreedyEmbeddingHelper(
                embedding, tf.fill([batch_size], 0), 1)
    decoder = tf.contrib.seq2seq.BasicDecoder(decoder_cell, helper,
            init_state, output_layer=projection_layer)
    # dynamic_decode()是一个Decoder类，主要功能是解码序列生成
    # 依据Encoder进行解码，实现序列的生成（映射)
    outputs, _, _ = tf.contrib.seq2seq.dynamic_decode(decoder,
            maximum_iterations=100)
    if target_seq != None:
        return outputs.rnn_output
    else:
        return outputs.sample_id


def seq_loss(output, target, seq_len):
    target = target[:, 1:]
    cost = tf.nn.sparse_softmax_cross_entropy_with_logits(logits=output,
            labels=target)
    batch_size = tf.shape(target)[0]
    loss_mask = tf.sequence_mask(seq_len, tf.shape(output)[1])
    cost = cost * tf.to_float(loss_mask)
    return tf.reduce_sum(cost) / tf.to_float(batch_size)

