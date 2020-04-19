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
              <el-dropdown-item command="changePassword"
                >修改密码</el-dropdown-item
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
  </div>
</template>
<script>
export default {
  data() {
    return {};
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
