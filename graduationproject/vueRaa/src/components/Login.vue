<template>
  <div>
    <el-form
      :rules="rules"
      class="login-container"
      label-position="left"
      :model="loginForm"
      label-width="0px"
      v-loading="loading"
      element-loading-text="登录中"
    >
      <h3 class="login_title">学生在线评阅系统</h3>
      <el-form-item prop="username">
        <el-input
          type="text"
          v-model="loginForm.username"
          auto-complete="off"
          placeholder="账号"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
          v-model="loginForm.password"
          auto-complete="off"
          placeholder="密码"
        ></el-input>
      </el-form-item>
      <el-form-item style="width: 100%">
        <el-button type="success" style="width: 100%" @click="submitClick"
          >登录</el-button
        >
        <el-link type="primary" @click="showForgetDialog" :underline="false"
          >忘记密码</el-link
        >
      </el-form-item>
    </el-form>
    <el-dialog
      title="邮件发送"
      :visible.sync="showForgetDialogDetail"
      :close-on-click-modal="false"
    >
      <template v-if="writeEmail">
        <el-input placeholder="输入账号" size="mini" v-model="form1.user_id">
          <el-button type="success" size="mini" slot="append" @click="sendMail">
            点击发送邮件
          </el-button>
        </el-input>
      </template>
      <template v-if="writeVeritycode">
        <el-input
          placeholder="输入验证码"
          size="mini"
          v-model="veritycodebyuser"
        >
          <el-button
            type="success"
            size="mini"
            slot="append"
            @click="checkveritycode"
          >
            点击提交验证码
          </el-button>
        </el-input>
      </template>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="changePasswordDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form1"
        :rules="rules1"
        ref="formName1"
      >
        <el-form-item label="输入账号" prop="user_id">
          <el-input v-model="form1.user_id" disabled></el-input>
        </el-form-item>
        <el-form-item label="输入密码" prop="password1">
          <el-input v-model="form1.password1" show-password></el-input>
        </el-form-item>
        <el-form-item label="再次输入" prop="password2">
          <el-input v-model="form1.password2" show-password></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="changePasswordDialog = false">取 消</el-button>
        <el-button type="primary" @click="changePasswordDialogDetail()"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      },
      checked: true,
      loginForm: {
        username: "",
        password: ""
      },
      loading: false,
      showForgetDialogDetail: false,
      veritycode: "",
      veritycodebyuser: "",
      writeEmail: false,
      writeVeritycode: false,
      changePasswordDialog: false,
      form1: {
        user_id: this.user_id,
        password1: "",
        password2: ""
      },
      rules1: {
        user_id: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            pattern: /^[0-9]{0,19}$/,
            message: "请输入9-19位账号",
            trigger: "blur"
          }
        ],
        password1: [
          {
            required: true,
            message: "密码不能为空"
          },
          {
            pattern: /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\W_!@#$%^&*`~()-+=]+$)(?![0-9\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\W_!@#$%^&*`~()-+=]{8,30}$/,
            message: "密码格式不正确，由8-20个字母加数字加特殊符号构成"
          }
        ],
        password2: [
          {
            required: true,
            message: "密码不能为空"
          },
          {
            pattern: /^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\W_!@#$%^&*`~()-+=]+$)(?![a-z0-9]+$)(?![a-z\W_!@#$%^&*`~()-+=]+$)(?![0-9\W_!@#$%^&*`~()-+=]+$)[a-zA-Z0-9\W_!@#$%^&*`~()-+=]{8,30}$/,
            message: "密码格式不正确，由8-20个字母加数字加特殊符号构成"
          }
        ]
      }
    };
  },
  methods: {
    submitClick: function() {
      var _this = this;
      this.loading = true;
      this.postRequest("/login", {
        username: this.loginForm.username,
        password: this.loginForm.password
      }).then(resp => {
        console.log(resp);
        _this.loading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          _this.$store.commit("login", data.obj);
          var path = _this.$route.query.redirect;
          _this.$router.replace({
            path: path == "/" || path == undefined ? "/home" : path
          });
        }
      });
    },
    showForgetDialog() {
      this.writeEmail = true;
      this.showForgetDialogDetail = true;
    },
    sendMail() {
      var _this = this;
      var email;
      this.getRequest("/getemailbyuser_id?user_id=" + _this.form1.user_id).then(
        resp => {
          email = resp.data;
          this.getRequest("/mail/getveritycode?receiver=" + email).then(
            resp => {
              if (resp.data == "发送失败") {
                this.$message({
                  type: "error",
                  message: "发送失败，请检查邮箱是否符合规范或者联系管理员"
                });
              } else {
                _this.veritycode = resp.data;
                _this.writeEmail = false;
                _this.writeVeritycode = true;
              }
            }
          );
        }
      );
    },
    checkveritycode() {
      if (this.veritycode == this.veritycodebyuser) {
        this.writeVeritycode = false;
        this.showForgetDialogDetail = false;
        this.changePasswordDialog = true;
      } else {
        this.$message({
          type: "error",
          message: "验证码输入错误"
        });
      }
    },
    changePasswordDialogDetail() {
      var _this = this;
      this.$refs.formName1.validate(valid => {
        if (valid) {
          if (_this.form1.password1 !== _this.form1.password2) {
            this.$message({
              type: "warning",
              message: "两次输入密码不正确，请重新输入"
            });
            return;
          }
          var form = JSON.stringify(_this.form1);
          this.postRequest("/admin/changepassword", {
            form: form
          }).then(() => {
            _this.changePasswordDialog = false;
            _this.$router.push("/");
          });
        } else {
          this.$message({
            type: "warning",
            message: "未按规则填写表单，请重新填写"
          });
        }
      });
    }
  }
};
</script>
<style scoped>
.login-container {
  border-radius: 20px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}
.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}
.login_remember {
  margin: 0px 0px 35px 0px;
  text-align: left;
}
</style>
