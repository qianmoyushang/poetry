<template>
  <Row>
      <i-col span="14" style="padding-right: 10px;">
        <Card style="height: 100%">
          <p slot="title">自动回复</p>
          <Scroll :height="400">
          <div class="content-body">
            <Spin fix v-if="spinShow">
              <Icon type="ios-loading" size=30 class="demo-spin-icon-load">加载中....</Icon>
              <div>加载中....</div>
            </Spin>
            <ul class="inHtml" v-for="item in content">
              <li class="reply" v-show="item.replyContent">
                <Avatar style="float: left;" src="https://i.loli.net/2017/08/21/599a521472424.jpg"/>
                <p>{{item.replyContent}}</p>
              </li>
              <li class="ask"  v-show="item.askContent">
                <Avatar style="float: right;background-color: #f56a00" icon="ios-person"/>
                <p>{{item.askContent}}</p>
              </li>
            </ul>
          </div>
          </Scroll>
        </Card>
        <Card>
            <div class="send">
              <i-col span="16" push="1">
                <Input v-model="text" :clearable=true placeholder="请输入内容..." />
              </i-col>
              <i-col span="3" push="2">
                <Button type="success" long icon="md-checkmark" @click="sendContent">发送</Button>
              </i-col>
            </div>
          </Card>
      </i-col>
      <i-col span="10">
        <Card style="height: 200px">
          <p slot="title">未命名Card</p>
          <i-col span="12" push="2">
            <Tag type="dot" color="primary">生成藏头诗</Tag>
            <span style="margin-left: 50px"></span>
            <i-switch v-model="switch1" size="large" :disabled="disabled" @on-change="change">
              <span slot="open">开</span>
              <span slot="close">关</span>
            </i-switch>
          </i-col>
        </Card>
        <!--<Col span="11">-->
        <!--<Card style="height: 300px">-->
          <!--<p slot="title">库内古诗对照</p>-->
          <!--<Button type="info">Info</Button>-->
          <!--<p>Content of card</p>-->

          <!--<Upload-->
            <!--ref="upload"-->
            <!--action="http://localhost:8080/ivan/v1/b/article/importPoetry"-->
            <!--name="excel-file"-->
            <!--:headers="accessToken"-->
            <!--:show-upload-list="true"-->
            <!--:on-format-error="handleFormatError"-->
            <!--:on-success="handleSuccess"-->
            <!--:on-error="handleError"-->
            <!--:format ="['xlsx','xls','txt']">-->
            <!--<Button type="info" icon="ios-cloud-upload-outline">批量导入</Button>-->
          <!--</Upload>-->
        <!--</Card>-->
        <!--</Col>-->
      </i-col>
    </Row>

</template>

<script type="text/ecmascript-6">
  import BScroll from 'better-scroll'
  import {mapGetters} from 'vuex'

  export default {
    components: {
      BScroll
    },
    data () {
      return {
        accessToken:"",
        text: '', // 输入框的文字
        switch1: false,
        disabled: false,
        spinShow: false, //加载显示
        content: [
          {
            askContent: '',
            replyContent: '你好！'
          },
        ]
      }
    },
    computed: {
    },
    methods: {
      handleFormatError(file){
        this.$Notice.warning({
          title: '文件格式不正确',
          desc: '文件 ' + file.name + ' 格式不正确，请上传.xls,.xlsx文件。'
        })
      },
      handleSuccess(res,file){
        if(res.errcode === 0){
          this.dialoLead = false;
          this.$Message.success("数据导入成功！");
          this._getBookList();
          this.$refs.upload.clearFiles()
        }
      },
      handleError(error,file){
        this.$Message.error("数据导入失败！")
      },

      scrollToBottom(){
        let div = document.getElementById('');
        div.scrollTop = div.scrollHeight;
      },
      //藏头诗开关
      change (status) {
        this.switch1 = status;
        this.text = ''
      },
      sendContent () {
        this.spinShow = true;
        if (this.text !== '' && !this.switch1) {
          this.content.push({
            askContent: this.text
          });
          this.$http.request({
            // url: "/reply/random",
            url: "/hb_reply/random_poetry",
            params: {ask: this.text},
            method: "get"
          }).then(res => {
            // console.log("random_poetry===",JSON.stringify(res));
            setTimeout(() => {
              this.content.push({
                replyContent:res.text
              });
              this.$Notice.success({
                title: "询问成功",
                duration: 1
              });
              this.spinShow = false;
            }, 500);
          });
        }else if(this.text !== '' && this.switch1){
          this.content.push({
            askContent: this.text
          });
          this.$http.request({
            url: "/hb_reply/head_poetry",
            params: {ask: this.text},
            method: "get"
          }).then(res => {
            // console.log("head_poetry===",JSON.stringify(res));
            setTimeout(() => {
              this.content.push({
                replyContent:res.text
              });
              this.$Notice.success({
                title: "询问成功",
                duration: 1
              });
              this.spinShow = false;
            }, 500);
          });
        }
        this.text = ''; // 清空输入框的内容
      },
    }
  }
</script>

<style scoped>
  .demo-spin-icon-load{
    animation: ani-demo-spin 1s linear infinite;
  }
  .demo-spin-container{
    display: inline-block;
    width: 200px;
    height: 100px;
    position: relative;
    border: 1px solid #eee;
  }
  .chatroom{
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    z-index: 19;
    background-color: #ebebeb;
  }
  .back{
    background: #1e2b39;
    height: 50px;
    color: #fff;
    position: fixed;
    width: 100%;
  }
  .back .img{
    position: absolute;
    top: 25px;
    margin-top: -8px;
    left: 14px;
  }
  .back .dissname{
    position: absolute;
    font-size: 20px;
    top: 25px;
    margin-top: -10px;
    left: 50px;
    padding-left: 10px;
    border-left: 1px solid #000;
  }
  .back .logo{
    position: absolute;
    font-size: 20px;
    top: 30px;
    margin-top: -15px;
    right: 20px;
  }
  .content{
    position: fixed;
    top: 50px;
    bottom: 50px;
    left: 0;
    right: 0;
    /*border: 1px solid red;*/
  }
  .content-wrapper{
    height: 100%;
    overflow: hidden;
  }
  .content-top{
    font-size: 14px;
    color: rgba(153,153,153,0.6);
    text-align: center;
    margin-top: 4px;
  }
  .content-body{
    overflow: auto;
    /*position: relative;*/
    padding: 20px 10px;
    overflow: hidden;
    /*border: 1px solid blue;*/
  }
  .content-body li {
    position: relative;
    overflow: hidden;
    margin-bottom: 15px;
    line-height: 28px;
  }
  .inHtml img {
    position: relative;
    width: 30px;
    height: 30px;
  }
  .ask {
    text-align: right;
  }
  .reply {
    text-align: left;
  }
  .ask img {
    float: right;
    margin-left: 15px;
  }
  .reply img {
    float: left;
    margin-right: 15px;
  }
  .reply p{
    border-radius: 8px;
    text-align: left;
    margin-left: 8px;
    font: 14px 'Microsoft YaHei';
    line-height: 30px;
  }
  .ask p {
    border-radius: 8px;
    float: right;
    padding: 3px 10px;
    max-width: 400px;
    margin-right: 8px;
    background: #19BE6B;
    color: #fff;
  }
  .reply p {
    left: 2pc;
    float: left;
    padding: 3px 10px;
    max-width: 190px;
    background: #dcdee2;
  }
  .bottom{
    position: fixed;
    height: 50px;
    bottom: 0;
    width: 40%;
    /*left: 0;*/
    /*right: 0;*/
    background-color: #fff;
  }
  .send{
    display: flex;
  }
  .sText{
    flex: 6;
    height: 30px;
    margin: 10px;
    border: 0;
    padding-left: 8px;
    border-bottom: 1px solid rgba(153,153,153,0.8);
    /*border: 1px solid rgba(153,153,153,0.8);*/
    font-size: 15px;
  }
  .sText.active{
    background-color: red;
  }
  .btn{
    flex: 1;
    width: 65px;
    height: 30px;
    margin: 10px 10px;
    border: 0;
    border-radius: 5px;
    /*float: right;*/
    text-align: center;
    font-size: 18px;
    color: white;
    background-color: #09BB07;
  }

  .slide-enter-active,.slide-leave-active{
    transition: all 0.3s;
  }
  .slide-enter,.slide-leave-to{
    transform: translate3d(100%, 0, 0);
  }
</style>
