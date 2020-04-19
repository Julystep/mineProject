<template>
  <div>
    <el-tabs type="card" class="tabs">
      <el-tab-pane label="专业教师管理"
        ><majorteachermanagement ref="teacher"></majorteachermanagement
      ></el-tab-pane>
      <el-tab-pane label="专业学生管理"
        ><majorstudentmanagement ref="student"></majorstudentmanagement
      ></el-tab-pane>
      <el-tab-pane label="专业班级管理"
        ><createclass ref="class"></createclass
      ></el-tab-pane>
      <el-tab-pane label="专业课程管理">
        <createcourse ref="course"></createcourse>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import majorstudentmanagement from "./MajorStudentManagement";
import majorteachermanagement from "./MajorTeacherManagement";
import onlineusermanagement from "./OnlineUserManageMent";
import createclass from "./CreateClass";
import createcourse from "./CreateCourse";
export default {
  components: {
    majorstudentmanagement,
    majorteachermanagement,
    onlineusermanagement,
    createclass,
    createcourse
  },
  data() {
    return {};
  },
  mounted() {
    this.getMajorID();
  },
  methods: {
    getMajorID() {
      /* 获取专业的id */
      var _this = this;
      this.getRequest(
        "/admin/getmajorid?userID=" + this.$store.state.user.user_id
      ).then(resp => {
        _this.$store.commit("insertAdmin", resp.data);
        this.$refs.student.getStudentByMajor();
        this.$refs.class.getAllGrade();
        this.$refs.teacher.getTeacherByMajor();
        this.$refs.course.getAllCourse();
      });
    }
  }
};
</script>
<style>
.tabs {
  margin: auto;
  margin-top: 30px;
  width: 90%;
}
</style>
