<!--修改用户的角色及用户组-->
<template>
  <!--<div>-->
    <Row>
      <i-col span="12">
        <Card :bordered="false" :dis-hover="true">
          <Form :label-width="100" inline>
            <FormItem label="登陆账号" class="edit-form-item">
              <Input v-model="editData.userName"/>
            </FormItem>
            <FormItem label="注册日期" class="edit-form-item">
              <DatePicker type="date" v-model="editData.regDate" readonly>
              </DatePicker>
            </FormItem>
            <FormItem label="出生日期" class="edit-form-item">
              <DatePicker type="date" v-model="editData.birthday">
              </DatePicker>
            </FormItem>
            <FormItem label="性别" class="edit-form-item">
              <RadioGroup v-model="editData.gender">
                <Radio label="男"></Radio>
                <Radio label="女"></Radio>
              </RadioGroup>
            </FormItem>
            <FormItem label="是否有效" class="edit-form-item">
              <i-switch v-model="editData.valid" size="large">
                <span slot="open">有效</span>
                <span slot="close">无效</span>
              </i-switch>
            </FormItem>
            <FormItem label="个人简介" class="edit-form-item">
              <Input v-model="editData.profile" type="textarea" :autosize="{minRows: 4}"
                     placeholder="请写下个人简介"/>
            </FormItem>
          </Form>
        </Card>
      </i-col>
      <i-col span="10">
        <div class="margin-top-10">
          <Card :bordered="false" :dis-hover="true">
            <Button type="primary" @click="freshEditor">刷新</Button>
            <Button type="success" @click="handlePublish">提交修改</Button>
            <Button type="dashed" @click="closeEditor">关闭</Button>
          </Card>
        </div>
      </i-col>
    </Row>
  </div>
</template>
<script>
  import {mapGetters} from "vuex";
  import {formatDate, formatFullDate} from "@/libs/filters";
  import ISwitch from "iview/src/components/switch/switch";

  export default {
    name: "user-update-editor",
    components: {ISwitch},
    props: ["srcdata", "closemodal", "editablee"],
    data() {
      return {
        fullSrc: {},
        lastUserEditId: "",
        userGroups: "",
        userRoles: ""
      };
    },
    computed: {
      editData() {
        if (this.srcdata && this.srcdata.id && this.srcdata.id !== this.lastUserEditId) {
          this.$http.request({
            url: "/user/edit/get",
            params: {id: this.srcdata.id},
            method: "get"
          }).then(res => {
            this.fullSrc = res;
            console.log("getuser===="+JSON.stringify(this.fullSrc));
            this.fullSrc.regDate = formatFullDate(this.fullSrc.regDate);
            this.fullSrc.birthday = this.fullSrc.birthday ? formatDate(this.fullSrc.birthday) : "";
            this.lastUserEditId = this.srcdata.id;
          }).catch(err => this.$Notice.error({
            title: "获取用户信息失败",
            desc: err.msg
          }));
        }
        return this.fullSrc;
      },
      ...mapGetters(["allroles", "allmodules", "allgroups"])
    },
    methods: {
      freshEditor() {
        this.lastUserEditId = "";
        this.editData;
      },
      closeEditor() {
        this.$emit("update:closemodal", false);
      },
      handlePublish() {
        this.$http.request({
          url: "/user/edit/update",
          data: this.fullSrc,
          method: "post"
        }).then(() => {
          this.$Notice.success({
            title: "修改用户信息成功",
            duration: 1
          });
          this.endPost();
          this.$emit("update:closemodal", false);
        }).catch(err => {
          this.$Notice.error({
            title: "修改用户信息失败",
            desc: err.msg
          });
        });
      },
      endPost() {
        //  更新用户组信息到store
        this.$store.dispatch("init_user_groups");
      }
    },
    created() {
    },
    mounted() {
    }
  };
</script>
<style lang="less" scoped>
  .edit-form-item {
    width: 350px;
  }

  .edit-selector {
    max-height: 600px;
    overflow: auto;
  }

  .edit-scroll {
    height: 500px;
  }

  .edit-selector p label {
    padding-top: 10px;
    font-weight: 500;
    font-size: 13px;
  }
</style>
