<template>
  <div>
    <el-container>
      <el-header> </el-header>
      <el-main
        ><el-row :gutter="20">
          <template v-for="item in subjects">
            <el-col
              :span="4"
              :xs="15"
              :sm="8"
              :md="4"
              :lg="4"
              :xl="4"
              :offset="3"
              class="el-col"
              ><el-card :body-style="{ padding: '0px' }">
                <img :src="item.course_picture" class="image" />
                <div style="padding: 14px;">
                  <span>{{ item.course_name }}</span>
                  <div class="bottom clearfix">
                    <el-button
                      type="text"
                      class="button"
                      @click="getDetailSubject(item.course_id)"
                      >操作按钮</el-button
                    >
                  </div>
                </div>
              </el-card>
            </el-col>
          </template>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>
<script>
export default {
  data() {
    return {
      subjects: null
    };
  },
  mounted() {
    this.$message({
      message: "选择课程",
      type: "info"
    });
    this.getSubjectsByTeacherId();
  },
  methods: {
    /**
  获得学生所学科目
   */
    getSubjectsByTeacherId() {
      this.getRequest(
        "/student/getsubjectsbystudentid?studentID=" +
          this.$store.state.user.user_id
      ).then(resp => {
        if (resp.status == 200) {
          this.subjects = resp.data.subject;
        }
      });
    },
    /**
    获得学科作业的详细信息
     */
    getDetailSubject(courseID) {
      var _this = this;
      this.$router.push({
        name: "提交作业中心",
        query: {
          userID: _this.$store.state.user.user_id,
          courseID: courseID
        }
      });
    }
  }
};
</script>
<style>
.SubHome-header {
  font-size: 25px;
  color: blue;
  font-weight: bold;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB",
    "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
}
.grid-content {
  background: green;
}
.time {
  font-size: 13px;
  color: #999;
}
.bottom {
  margin-top: 13px;
  line-height: 12px;
}
.button {
  padding: 0;
  float: right;
}
.image {
  width: 100%;
  display: block;
}
.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
.el-col {
  margin-top: 5px;
}
</style>
