<!--文章列表-->
<template>
  <div>
    <Row>
      <i-col span="18" style="padding-right: 12px;">
        <Card>
          <p slot="title">
            <Icon type="md-paper"></Icon>
            搜索结果
          </p>
          <List item-layout="vertical" style="font-size: 18px">
            <ListItem v-for="item in reclist" :key="item.id">
              <ListItemMeta :itemid="item.id" :title="item.title" :description="item.content"/>
              <span style="color:#ff1919;">{{ item.categorys +" "+item.tone }}</span>
              <template slot="extra">
                <Tag color="purple" v-for="tag in item.tags" :key="tag">{{ tag }}</Tag>
              </template>
            </ListItem>
          </List>
        </Card>
      </i-col>
      <i-col span="6" class="padding-left-10">
        <Card>
          <p slot="title">
            <Icon type="md-paper-plane"></Icon>
            控制台
          </p>
          <Row style="text-align: center;">
            <i-input v-model="inputKeyWord" placeholder="输入关键字" clearable/>
            <Button long @click="getFresh" :loading="getFreshLoading" icon="md-refresh"
                    type="success" style="margin-top: 5px;">按照关键字搜索古诗
            </Button>
          </Row>
        </Card>
      </i-col>
    </Row>
  </div>
</template>

<script>
  import {formatDate} from "@/libs/filters";

  export default {
    components: {},
    data() {
      return {
        recid: "",
        reclist: [],
        inputKeyWord: "",
        getFreshLoading: false
      };
    },
    computed: {},
    methods: {
      getData() {
        this.getFresh();
      },
      getFresh() {
        this.getFreshLoading = true;
        this.$http.request({
          url: "/article/getRecList",
          params: {
            "keyword": this.inputKeyWord
          },
          method: "get"
        }).then(res => {
          console.log(res);
          this.recid = res.recid;
          this.reclist = res.list.map(item => {
            return item;
          });
          this.getFreshLoading = false;
          this.$Notice.success({
            title: "已获取最新文章数据",
            duration: 1
          });
        });
      }
    },
    mounted() {
      if (sessionStorage.reclist) {
        this.recid = sessionStorage.recid;
        this.reclist = JSON.parse(sessionStorage.reclist);
      } else {
        this.getData();
      }
    },
    destroyed() {
      sessionStorage.recid = this.recid;
      sessionStorage.reclist = JSON.stringify(this.reclist);
    }
  };
</script>
<style lang="less">
</style>
