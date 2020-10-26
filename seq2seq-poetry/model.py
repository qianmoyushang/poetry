
import tensorflow as tf
import seq2seq
import bleu
import reader
import numpy as np
from os import path
import random


class Model():

    def __init__(self, train_input_file, train_target_file,
            test_input_file, test_target_file, vocab_file,
            num_units, layers, dropout,
            batch_size, learning_rate, output_dir,
            save_step = 100, eval_step = 1000,
            param_histogram=False, restore_model=False,
            init_train=True, init_infer=False):
        self.num_units = num_units
        self.layers = layers
        self.dropout = dropout
        self.batch_size = batch_size
        self.learning_rate = learning_rate
        self.save_step = save_step
        self.eval_step = eval_step
        self.param_histogram = param_histogram
        self.restore_model = restore_model
        self.init_train = init_train
        self.init_infer = init_infer

        if init_train:
            self.train_reader = reader.SeqReader(train_input_file,
                    train_target_file, vocab_file, batch_size)
            self.train_reader.start()
            self.train_data = self.train_reader.read()
            self.eval_reader = reader.SeqReader(test_input_file, test_target_file,
                    vocab_file, batch_size)
            self.eval_reader.start()
            self.eval_data = self.eval_reader.read()

        self.model_file = path.join(output_dir, 'model.ckpl')
        self.log_writter = tf.summary.FileWriter(output_dir)

        if init_train:
            self._init_train()
            self._init_eval()

        if init_infer:
            self.infer_vocabs = reader.read_vocab(vocab_file)
            self.infer_vocab_indices = dict((c, i) for i, c in
                    enumerate(self.infer_vocabs))
            # print("====infer_vocabs 52====", self.infer_vocabs[4])
            # print("====infer_vocab_indices 52====", self.infer_vocab_indices)
            self._init_infer()
            self.reload_infer_model()


    def gpu_session_config(self):
        config = tf.ConfigProto()
        config.gpu_options.allow_growth = True
        return config

    # 初始化训练模型
    # Graph计算和表示用的数据流图
    def _init_train(self):
        self.train_graph = tf.Graph()
        with self.train_graph.as_default():
            self.train_in_seq = tf.placeholder(tf.int32, shape=[self.batch_size, None])
            self.train_in_seq_len = tf.placeholder(tf.int32, shape=[self.batch_size])
            self.train_target_seq = tf.placeholder(tf.int32, shape=[self.batch_size, None])
            self.train_target_seq_len = tf.placeholder(tf.int32, shape=[self.batch_size])
            output = seq2seq.seq2seq(self.train_in_seq, self.train_in_seq_len,
                    self.train_target_seq, self.train_target_seq_len,
                    len(self.train_reader.vocabs),
                    self.num_units, self.layers, self.dropout)
            self.train_output = tf.argmax(tf.nn.softmax(output), 2)
            self.loss = seq2seq.seq_loss(output, self.train_target_seq,
                    self.train_target_seq_len)
            # 查看变量
            params = tf.trainable_variables()
            # print("===model: 94===params===",params)
            # 用于解决梯度消失或梯度爆炸问题
            gradients = tf.gradients(self.loss, params)
            clipped_gradients, _ = tf.clip_by_global_norm(
                        gradients, 0.5)
            self.train_op = tf.train.AdamOptimizer(
                    learning_rate=self.learning_rate
                ).apply_gradients(zip(clipped_gradients,params))
            if self.param_histogram:
                for v in tf.trainable_variables():
                    # 用来显示直方图信息 tf.summary.histogram
                    tf.summary.histogram('train_' + v.name, v)
            tf.summary.scalar('loss', self.loss)
            self.train_summary = tf.summary.merge_all()
            self.train_init = tf.global_variables_initializer()
            self.train_saver = tf.train.Saver()
        self.train_session = tf.Session(graph=self.train_graph,
                config=self.gpu_session_config())

    # 初始化模型评估
    def _init_eval(self):
        self.eval_graph = tf.Graph()
        with self.eval_graph.as_default():
            self.eval_in_seq = tf.placeholder(tf.int32, shape=[self.batch_size, None])
            self.eval_in_seq_len = tf.placeholder(tf.int32, shape=[self.batch_size])
            self.eval_output = seq2seq.seq2seq(self.eval_in_seq,
                    self.eval_in_seq_len, None, None,
                    len(self.eval_reader.vocabs),
                    self.num_units, self.layers, self.dropout)
            if self.param_histogram:
                for v in tf.trainable_variables():
                    tf.summary.histogram('eval_' + v.name, v)
            self.eval_summary = tf.summary.merge_all()
            self.eval_saver = tf.train.Saver()
        self.eval_session = tf.Session(graph=self.eval_graph,
                config=self.gpu_session_config())

    # 初始化模型使用，生成古诗前的准备
    def _init_infer(self):
        # 创建GRraph()计算图对象
        self.infer_graph = tf.Graph()
        with self.infer_graph.as_default():
            self.infer_in_seq = tf.placeholder(tf.int32, shape=[1, None])
            self.infer_in_seq_len = tf.placeholder(tf.int32, shape=[1])
            self.infer_output = seq2seq.seq2seq(self.infer_in_seq,
                    self.infer_in_seq_len, None, None,
                    len(self.infer_vocabs),
                    self.num_units, self.layers, self.dropout)

            print("====infer_in_seq ===",self.infer_in_seq)
            print("====infer_in_seq_len ===",self.infer_in_seq_len)
            print("====infer_output ===",self.infer_output)

            self.infer_saver = tf.train.Saver()
        self.infer_session = tf.Session(graph=self.infer_graph,
                config=self.gpu_session_config())


    # 开始训练
    def train(self, epochs, start=0):
        if not self.init_train:
            raise Exception('Train graph is not inited!')
        with self.train_graph.as_default():
            if path.isfile(self.model_file + '.meta') and self.restore_model:
                print("训练之前重新加载模型文件。")
                # 重载模型的参数，继续训练或者用于测试数据
                # print("Reloading model file before training.")
                self.train_saver.restore(self.train_session, self.model_file)
            else:
                self.train_session.run(self.train_init)
            total_loss = 0
            for step in range(start, epochs):
                data = next(self.train_data)
                in_seq = data['in_seq']
                in_seq_len = data['in_seq_len']
                target_seq = data['target_seq']
                target_seq_len = data['target_seq_len']
                output, loss, train, summary = self.train_session.run(
                        [self.train_output, self.loss, self.train_op, self.train_summary],
                        feed_dict={
                            self.train_in_seq: in_seq,
                            self.train_in_seq_len: in_seq_len,
                            self.train_target_seq: target_seq,
                            self.train_target_seq_len: target_seq_len})
                total_loss += loss
                # 将训练步骤和数据写入文件
                self.log_writter.add_summary(summary, step)
                if step % 10 == 0:
                    print("=======step===",step)

                if step % self.save_step == 0:
                    self.train_saver.save(self.train_session, self.model_file)
                    print("保存模型  步骤: %d, loss: %f" % (step,
                        total_loss / self.save_step))
                    # print sample output
                    sid = random.randint(0, self.batch_size-1)
                    input_text = reader.decode_text(in_seq[sid],
                        self.eval_reader.vocabs)
                    output_text = reader.decode_text(output[sid],
                            self.train_reader.vocabs)
                    print("=====train  166 output[%d]========", sid, output[sid])
                    target_text = reader.decode_text(target_seq[sid],
                            self.train_reader.vocabs).split(' ')[1:]
                    target_text = ' '.join(target_text)
                    print('******************************')
                    print('src: ' + input_text)
                    print('output: ' + output_text)
                    print('target: ' + target_text)
                if step % self.eval_step == 0:
                    bleu_score = self.eval(step)
                    print("评估模型  步骤: %d, score: %f, loss: %f" % (
                        step, bleu_score, total_loss / self.save_step))
                    eval_summary = tf.Summary(value=[tf.Summary.Value(
                        tag='bleu', simple_value=bleu_score)])
                    self.log_writter.add_summary(eval_summary, step)
                if step % self.save_step == 0:
                    total_loss = 0

    # 模型评估方法
    def eval(self, train_step):
        with self.eval_graph.as_default():
            self.eval_saver.restore(self.eval_session, self.model_file)
            bleu_score = 0
            target_results = []
            output_results = []
            for step in range(0, self.eval_reader.data_size):
                data = next(self.eval_data)
                in_seq = data['in_seq']
                in_seq_len = data['in_seq_len']
                target_seq = data['target_seq']
                target_seq_len = data['target_seq_len']
                outputs = self.eval_session.run(
                        self.eval_output,
                        feed_dict={
                            self.eval_in_seq: in_seq,
                            self.eval_in_seq_len: in_seq_len})
                for i in range(len(outputs)):
                    output = outputs[i]
                    target = target_seq[i]
                    output_text = reader.decode_text(output,
                            self.eval_reader.vocabs).split(' ')
                    target_text = reader.decode_text(target[1:],
                            self.eval_reader.vocabs).split(' ')
                    prob = int(self.eval_reader.data_size * self.batch_size / 10)
                    target_results.append([target_text])
                    output_results.append(output_text)
                    if random.randint(1, prob) == 1:
                        print("=======eval-step======", step)
                        print('====================')
                        input_text = reader.decode_text(in_seq[i],
                                self.eval_reader.vocabs)
                        print('src:' + input_text)
                        print('output: ' + ' '.join(output_text))
                        print('target: ' + ' '.join(target_text))
            return bleu.compute_bleu(target_results, output_results)[0] * 100

    # 加载训练好的模型
    def reload_infer_model(self):
        with self.infer_graph.as_default():
            self.infer_saver.restore(self.infer_session, self.model_file)

    # 古诗生成
    def infer(self, text):
        input_words = reader.encode_text(text.split(' '), self.infer_vocab_indices)
        if not self.init_infer:
            print("模型未初始化")
        with self.infer_graph.as_default():
            # print("===text==", text.split(' ') + ['</s>'])
            len_text = len(text.split(' '))
            input_words = reader.handleData(input_words, len_text, self.batch_size)
            words_in_seq = input_words
            words_in_seq_len = len(input_words)
            print("===infer_in_seq_len====",self.infer_in_seq_len)

            outputs = self.infer_session.run(self.infer_output,
                    feed_dict={
                        self.infer_in_seq: [words_in_seq],
                        self.infer_in_seq_len: [words_in_seq_len]})
            input_words.append(3)
            input_words.extend(outputs[0])
            in_seq_len = len(input_words)

            output = self.infer_session.run(self.infer_output,
                                             feed_dict={
                                                 self.infer_in_seq: [input_words],
                                                 self.infer_in_seq_len: [in_seq_len]})
            output_top = reader.decode_text(input_words, self.infer_vocabs)
            output_text = reader.decode_text(output[0], self.infer_vocabs)
            # print("return=====",output[0])
            return output_top,output_text

    def head_infer(self, text):
        # input_words = reader.encode_text(text.split(' '), self.infer_vocab_indices)
        input_words = []
        if not self.init_infer:
            print("=====infer 289===== 模型未初始化")
        with self.infer_graph.as_default():
            print("===text==", text.split(' ') + ['</s>'])
            for i in range(5):
                input_words.append(random.randint(4,self.batch_size*10))
            random.shuffle(input_words)
            print("===input_words297==",input_words)
            words_in_seq = input_words
            words_in_seq_len = len(input_words)
            print("===infer_in_seq_len 300====",self.infer_in_seq_len)
            outputs = self.infer_session.run(self.infer_output,
                    feed_dict={
                        self.infer_in_seq: [words_in_seq],
                        self.infer_in_seq_len: [words_in_seq_len]})
            print("=====outputs 306====",outputs)
            input_words.append(3)
            input_words.extend(outputs[0])
            in_seq_len = len(input_words)
            output = self.infer_session.run(self.infer_output,
                                             feed_dict={
                                                 self.infer_in_seq: [input_words],
                                                 self.infer_in_seq_len: [in_seq_len]})
            head = reader.encode_text(text.split(' '), self.infer_vocab_indices)
            output_one, output_two = reader.headrandom(input_words,output[0],head)
            output_top = reader.decode_text(output_one, self.infer_vocabs)
            output_text = reader.decode_text(output_two, self.infer_vocabs)
            return output_top,output_text

