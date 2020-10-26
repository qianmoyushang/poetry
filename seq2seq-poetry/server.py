
from flask import Flask, jsonify, request
# from flask_cors import CORS, cross_origin
from model import Model
# from gevent.wsgi import WSGIServer
import logging

app = Flask(__name__)
# CORS(app)

vocab_file = 'E:/pycharm/seq2seq-poetry/data/couplet/vocabs'
model_dir = 'E:/pycharm/seq2seq-poetry/data/models/output_couplet'

m = Model(
        None, None, None, None, vocab_file,
        num_units=1024, layers=4, dropout=0.2,
        batch_size=32, learning_rate=0.0001,
        output_dir=model_dir,
        restore_model=True, init_train=False, init_infer=True)


@app.route('/poetry/random/<in_str>')
def random_poetry(in_str):
    if len(in_str) == 0 or len(in_str) > 50:
        output = u'您的输入太长了'
    else:
        out_top,output = m.infer(' '.join(in_str))
        out_top = ''.join(out_top.split(' '))
        output = ''.join(output.split(' '))
    print('上句：%s；下句：%s' % (out_top, output))
    return jsonify({'out_top':out_top,'output': output})

@app.route('/poetry/head_random/<in_str>')
def head_randomPoetry(in_str):
    if len(in_str) == 0 or len(in_str) > 50:
        output = u'您的输入太长了'
    else:
        out_top,output = m.head_infer(' '.join(in_str))
        out_top = ''.join(out_top.split(' '))
        output = ''.join(output.split(' '))
    print('上句：%s；下句：%s' % (out_top, output))
    return jsonify({'out_top':out_top,'output': output})



if __name__ == '__main__':
    app.run(
        port=5050,
        debug=True
    )