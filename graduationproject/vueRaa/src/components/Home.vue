<template>
  <div>
    <el-container class="home-container">
      <el-header class="home-header">
        <span class="home_title">学生在线评阅系统</span>
        <div style="display: flex;align-items: center;margin-right: 7px">
          <el-dropdown @command="handleCommand">
            <span
              class="el-dropdown-link home_userinfo"
              style="display: flex;align-items: center"
            >
              {{ user.username }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="changeInfo">修改信息</el-dropdown-item>
              <el-dropdown-item command="changePassword"
                >修改密码</el-dropdown-item
              >
              <el-dropdown-item command="forgetPassword"
                >忘记密码</el-dropdown-item
              >
              <el-dropdown-item command="logout" divided>注销</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="180px" class="home-aside">
          <div
            style="display: flex;justify-content: flex-start;width: 180px;text-align: left;"
          >
            <el-menu
              style="background: #ececec;width: 180px; height: 1000px"
              unique-opened
              router
            >
              <template
                v-for="(item, index) in this.routes"
                v-if="!item.hidden"
              >
                <el-submenu :key="index" :index="index + ''">
                  <template slot="title">
                    <i
                      :class="item.iconCls"
                      style="color: #20a0ff;width: 14px;"
                    ></i>
                    <span slot="title">{{ item.name }}</span>
                  </template>
                  <el-menu-item
                    width="180px"
                    style="padding-left: 30px;padding-right:0px;margin-left: 0px;width: 170px;text-align: left"
                    v-for="child in item.children"
                    :index="child.path"
                    :key="child.path"
                    >{{ child.name }}
                  </el-menu-item>
                </el-submenu>
              </template>
            </el-menu>
          </div>
        </el-aside>
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }"
              >首页</el-breadcrumb-item
            >
            <el-breadcrumb-item
              v-text="this.$router.currentRoute.name"
            ></el-breadcrumb-item>
          </el-breadcrumb>
          <keep-alive>
            <router-view v-if="!this.$route.meta.keepAlive"></router-view>
          </keep-alive>
          <router-view v-if="this.$route.meta.keepAlive"></router-view>
        </el-main>
      </el-container>
    </el-container>
    <el-dialog
      title="提示"
      :visible.sync="changeInfoDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form"
        :rules="rules"
        ref="formName"
      >
        <el-form-item label="账号">
          <el-input v-model="form.user_id" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="username">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="changeInfoDialog = false">取 消</el-button>
        <el-button type="primary" @click="changeInfoDialogDetail()"
          >确 定</el-button
        >
      </span>
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
  inject: ["reload"],
  data() {
    return {
      changeInfoDialog: false,
      changePasswordDialog: false,
      form: {
        user_id: "",
        usernam: "",
        email: "",
        phone: ""
      },
      rules: {
        email: [
          {
            pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
            message: "请输入正确的邮箱",
            trigger: "blur"
          }
        ],
        phone: [
          {
            pattern: /^1[3456789]\d{9}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      },
      form1: {
        user_id: this.$store.state.user.user_id,
        password1: "",
        password2: ""
      },
      rules1: {
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
  mounted() {},
  methods: {
    handleCommand(cmd) {
      var _this = this;
      if (cmd == "logout") {
        this.$confirm("注销登录, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            _this.getRequest("/logout");
            _this.$store.commit("logout");
            _this.$router.replace({ path: "/" });
          })
          .catch(() => {
            _this.$message({
              type: "info",
              message: "取消"
            });
          });
      }
      if (cmd == "changeInfo") {
        this.changeInfoDialog = true;
        this.form.user_id = this.$store.state.user.user_id;
        this.form.username = this.$store.state.user.username;
        this.form.email = this.$store.state.user.email;
        this.form.phone = this.$store.state.user.phone;
      }
      if (cmd == "changePassword") {
        this.changePasswordDialog = true;
      }
    },
    changeInfoDialogDetail() {
      var _this = this;
      this.$refs.formName.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form);
          console.log(form);
          this.postRequest("/admin/changestudentinfo", {
            form: form
          }).then(() => {
            _this.changeInfoDialog = false;
            _this.reload();
          });
        } else {
          this.$message({
            type: "warning",
            message: "未按规则填写表单，请重新填写"
          });
        }
      });
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
          console.log(form);
          this.postRequest("/admin/changepassword", {
            form: form
          }).then(() => {
            _this.changeInfoDialog = false;
            _this.reload();
          });
        } else {
          this.$message({
            type: "warning",
            message: "未按规则填写表单，请重新填写"
          });
        }
      });
    }
  },
  computed: {
    user() {
      return this.$store.state.user;
    },
    routes() {
      return this.$store.state.routes;
    }
  }
};
</script>
<style>
.home-container {
  height: 80px;
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100%;
}
.home-header {
  background-color: #20a0ff;
  color: #333;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-sizing: content-box;
  padding: 0px;
}
.home-aside {
  background-color: #ececec;
}
.home-main {
  background-color: #fff;
  color: #000;
  text-align: center;
  margin: 0px;
  padding: 0px;
}
.home_title {
  color: #fff;
  font-size: 22px;
  display: inline;
  margin-left: 8px;
}
.home_userinfo {
  color: #fff;
  cursor: pointer;
}
.home_userinfoContainer {
  display: inline;
  margin-right: 20px;
}
.el-submenu .el-menu-item {
  width: 180px;
  min-width: 175px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
