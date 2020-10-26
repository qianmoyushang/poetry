<template>

  <Row>

    <!--<Table :columns="columns" :data="data" :show-header=false :disabled-hover=false>-->
      <!--<template /slot-scope="{ row, index }" slot="name">-->
      <!--<Input type="text" v-model="editName" v-if="editIndex === index" />-->
      <!--<span v-else>{{ row.name }}</span>-->
      <!--</template>-->

      <!--<template slot-scope="{ row, index }" slot="age">-->
      <!--<Input type="text" v-model="editAge" v-if="editIndex === index" />-->
      <!--<span v-else>{{ row.age }}</span>-->
      <!--</template>-->
    <!--</Table>-->

    <Card :bordered="false" :dis-hover="true" class="card-tree">
      <p slot="title">
        <Icon type="md-help"></Icon>
        机器回复
      </p>
      <Row>
        <i-col span="14" push="3">
          <Input placeholder="我要问机器人" v-model="askStr"/>
        </i-col>
        <i-col span="3" push="4">
          <Button type="success" long @click="getAnswer">提问</Button>
        </i-col>
      </Row>
      <Row style="margin-top: 10px;margin-bottom: 40px;">
        <i-col span="10" push="3">
          <List size="small" :split=false item-layout="horizontal">
            <ListItem v-for="item in answerList1" :key="item.id">
              <ListItemMeta
                :avatar="item.avatar"
                :title="item.answer"
                :description="item.title"></ListItemMeta>
            </ListItem>
          </List>
        </i-col>
        <!--<i-col span="10" push="3">-->
          <!--<List size="small" :split=false item-layout="vertical">-->
            <!--<ListItem v-for="item in answerList2" :key="item.id">-->
              <!--<ListItemMeta-->
                <!--:avatar="item.avatar"-->
                <!--:title="item.answer"-->
                <!--:description="item.title"></ListItemMeta>-->
            <!--</ListItem>-->
          <!--</List>-->
        <!--</i-col>-->
      </Row>
    </Card>
  </Row>
</template>

<script>
  import {mapGetters} from "vuex";

  export default {
    data() {
      return {

        askStr: "",
        answerList1: [
          {
            id: "1",
            avatar: "https://dev-file.iviewui.com/userinfoPDvn9gKWYihR24SpgC319vXY8qniCqj4/avatar",
            title: "机器人说：",
            answer: "你好，请说出你的问题"
          }
        ],
        answerList2: [
          {
            id: "",
            avatar: "",
            title: " ",
            answer: " "
          }
        ]
      };
    },
    computed: {},
    methods: {
      getAnswer() {
        this.answerList1.push({
          id: this.askStr,
          avatar: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577269289587&di=9ba30317cc323656876e57482ad67339&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201608%2F21%2F20160821194924_UCvFZ.jpeg",
          title: "用户说：",
          answer: this.askStr
        });
        this.answerList2.push({
          id: this.askStr,
          avatar: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577269289587&di=9ba30317cc323656876e57482ad67339&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201608%2F21%2F20160821194924_UCvFZ.jpeg",
          title: "用户说：",
          answer: this.askStr
        });
        // console.log("ask====="+this.answerList1.id);
        this.$http.request({
          url: "/reply/random",
          params: {ask: this.askStr},
          method: "get"
        }).then(res => {
          console.log("res====="+JSON.stringify(res));
          this.answerList1.push(res);
          // this.answerList2.push(res);
          this.$Notice.success({
            title: "询问成功",
            duration: 1
          });
        });
      }
    },
    created() {

    },
    mounted() {

    }
  };
</script>


<style lang="less" scoped>
  .demo-split{
    height: 200px;
    border: 1px solid #dcdee2;
  }
  .demo-split-pane{
    padding: 10px;
  }
</style>
