<template>
  <div>
    <el-row style="margin-bottom: 10px">
      <el-col :span="6">
        <el-button
          type="success"
          @click="insertCourse"
          style="width: 100%"
          size="mini"
          >点击添加课程</el-button
        >
      </el-col>
    </el-row>
    <el-tree
      :data="options"
      :props="defaultProps"
      :expand-on-click-node="false"
      :highlight-current="true"
      :check-on-click-node="true"
      @node-click="handleNodeClick"
      ref="tree"
    ></el-tree>
    <el-row style="margin-top: 10px">
      <el-col :span="4">
        <el-button
          :type="submitType"
          @click="submitclassandteacher"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >{{ submitText }}</el-button
        >
      </el-col>
      <el-col :span="4">
        <el-button
          :type="checkType"
          @click="showcheckdialog"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >{{ checkText }}</el-button
        >
      </el-col>
      <el-col :span="4">
        <el-button
          :type="changeType"
          @click="changeCourse"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >{{ confirmText }}</el-button
        >
      </el-col>
      <el-col :span="4">
        <el-button
          :type="uploadPictureType"
          @click="uploadPicture"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >{{ uploadPictureText }}</el-button
        >
      </el-col>

      <el-col :span="4">
        <el-button
          :type="deleteType"
          @click="deleteCourse"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >{{ deleteText }}</el-button
        >
      </el-col>
    </el-row>
    <el-dialog
      title="提示"
      :visible.sync="changeCourseDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="data"
        ref="data"
        :rules="rules"
      >
        <el-form-item label="id">
          <el-input v-model="data.value" disabled></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="label">
          <el-input v-model="data.label"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="changeCourseDialog = false">取 消</el-button>
          <el-button type="primary" @click="changeCourseDetail()"
            >确 定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="uploadPictureDialog"
      :close-on-click-modal="false"
    >
      <el-upload
        class="avatar-uploader"
        action="/admin/uploadPicture"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
        :on-error="handleAvatarError"
        :data="fileData"
      >
        <img v-if="imageUrl" :src="imageUrl" class="avatar" />
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="insertCourseDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form"
        ref="form"
        :rules="rules"
      >
        <el-form-item label="选择专业" prop="id">
          <el-select v-model="form.id" placeholder="请选择" style="width: 100%">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          label="课程名称"
          v-for="(label, index) in form.label"
          :key="label.key"
          :prop="'label.' + index + '.value'"
          :rules="rules1"
        >
          <el-input v-model="label.value" style="width: 65%"></el-input>
          <el-button @click="addCourse">新增课程名</el-button>
          <el-button @click.prevent="removeCourse(label)">删除</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="insertCourseDialog = false">取 消</el-button>
          <el-button type="primary" @click="insertCourseDetail()"
            >确 定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="submitclassandteacherDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form1"
        ref="form1"
        :rules="rules2"
      >
        <el-form-item label="名称">
          <el-input v-model="form1.course_name" disabled />
        </el-form-item>
        <el-form-item label="班级名称" prop="classValue">
          <el-cascader
            v-model="form1.classValue"
            :options="majors"
            :props="{ expandTrigger: 'hover', multiple: true }"
            style="width: 100%"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="教师名称" prop="classValue">
          <el-cascader
            v-model="form1.teacher_id"
            :options="teachers"
            :props="{ expandTrigger: 'hover' }"
            style="width: 100%"
          ></el-cascader>
        </el-form-item>

        <el-form-item>
          <el-button @click="submitclassandteacherDialog = false"
            >取 消</el-button
          >
          <el-button type="primary" @click="submitclassandteacherDetail()"
            >确 定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="checkclassandteacherDialog"
      :close-on-click-modal="false"
    >
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>绑定的班级</span>
        </div>
        <div v-for="(o, index) in mgcBeans" :key="index" class="text item">
          {{ o.major_name + o.grade_name + o.class_name }}
        </div>
      </el-card>
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>绑定的教师</span>
        </div>
        <template v-if="teacher !== null">
          <div class="text item">编号: {{ teacher.user_id }}</div>
          <div class="text item">姓名: {{ teacher.username }}</div>
        </template>
        <template v-if="teacher === null">
          <div class="text item">暂无教师</div>
        </template>
      </el-card>
      <el-button
        type="danger"
        style="margin-top: 5px; width: 50%"
        @click="deleteCourseAndTeacherAndClassConnect()"
        >删除关联</el-button
      >
    </el-dialog>
  </div>
</template>
<script>
export default {
  inject: ["reload"],
  data() {
    return {
      imageUrl: "",
      options: [],
      defaultProps: {
        children: "childrencourse",
        label: "label"
      },
      changeType: "",
      deleteType: "",
      submitType: "",
      checkType: "",
      uploadPictureType: "",
      show: true,
      data: {},
      confirmText: "等待操作",
      deleteText: "等待操作",
      submitText: "等待操作",
      checkText: "等待操作",
      uploadPictureText: "等待操作",
      changeCourseDialog: false,
      insertCourseDialog: false,
      submitclassandteacherDialog: false,
      checkclassandteacherDialog: false,
      uploadPictureDialog: false,
      mgcBeans: [],
      teacher: "",
      form: {
        id: "",
        label: [
          {
            value: ""
          }
        ]
      },
      form1: {
        course_id: "",
        course_name: "",
        classValue: "",
        teacher_name: ""
      },
      rules: {
        id: [
          {
            required: true,
            message: "需要填写专业名称",
            trigger: blur
          }
        ],
        classValue: [
          {
            required: true,
            message: "需要填写班级名称",
            trigger: blur
          }
        ],
        label: [
          {
            required: true,
            message: "专业名称不能为空",
            trigger: blur
          }
        ]
      },
      rules1: [
        {
          required: true,
          message: "课程名不能为空",
          trigger: "blur"
        }
      ],
      rules2: {
        classValue: [
          {
            required: true,
            message: "需要填写班级名称",
            trigger: blur
          }
        ],
        teacher_name: [
          {
            required: true,
            message: "需要填写教师名称",
            trigger: blur
          }
        ]
      },
      majors: [],
      teachers: [],
      fileData: { id: "", name: "" }
    };
  },
  methods: {
    handleAvatarError() {
      this.$message({
        message: "图片上传失败",
        type: error
      });
    },
    handleAvatarSuccess(res, file) {
      this.imageUrl = URL.createObjectURL(file.raw);
      this.$message({
        message: "图片上传成功",
        type: success
      });
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      return isJPG && isLt2M;
    },
    showcheckdialog() {
      this.checkclassandteacherDialog = true;
      this.getRequest(
        "/admin/getclassesandteacher?course_id=" + this.data.value
      ).then(resp => {
        console.log(resp.data);
        this.mgcBeans = resp.data.mgcBeans;
        this.teacher = resp.data.teacher;
      });
    },
    addCourse() {
      this.form.label.push({ value: "", key: Date.now() });
    },
    removeCourse(item) {
      var index = this.form.label.indexOf(item);
      if (index !== -1) {
        this.form.label.splice(index, 1);
      }
    },
    getAllCourse() {
      var _this = this;
      this.postRequest("/admin/getallcourse", {
        majorList: JSON.stringify(_this.$store.state.admin.majorList)
      }).then(resp => {
        _this.options = resp.data;
      });
    },
    handleNodeClick(data) {
      console.log(data);
      console.log(typeof data.childrencourse !== undefined);
      if (data.major_name !== undefined) {
        this.changeType = "";
        this.deleteType = "";
        this.submitType = "";
        this.checkType = "";
        this.show = true;
        this.confirmText = "等待操作";
        this.deleteText = "等待操作";
        this.submitText = "等待操作";
        this.checkText = "等待操作";
        this.uploadPictureText = "等待操作";
      } else {
        this.changeType = "success";
        this.deleteType = "danger";
        this.submitType = "primary";
        this.checkType = "warning";
        this.uploadPictureType = "info";
        this.confirmText = "修改课程名称";
        this.submitText = "添加关联班级和教师";
        this.checkText = "查看关联班级和教师";
        this.deleteText = "删除课程";
        this.uploadPictureText = "上传课程图片";
        this.show = false;
        this.data = data;
        console.log(this.data);
      }
    },
    changeCourse() {
      this.changeCourseDialog = true;
    },
    uploadPicture() {
      this.fileData.id = this.data.value;
      this.fileData.name = this.data.label;
      this.uploadPictureDialog = true;
    },
    insertCourse() {
      this.insertCourseDialog = true;
    },
    changeCourseDetail() {
      var _this = this;
      this.$refs.data.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.data);
          this.postRequest("/admin/updatecourse", {
            form: form
          }).then(() => {
            _this.changeCourseDialog = false;
            _this.getAllCourse();
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
    submitclassandteacher() {
      this.form1.course_id = this.data.value;
      this.form1.course_name = this.data.label;
      this.submitclassandteacherDialog = true;
      var _this = this;
      this.postRequest("/admin/getclassandteacherbymajorlist", {
        majorList: JSON.stringify(_this.$store.state.admin.majorList)
      }).then(resp => {
        console.log(resp.data);
        _this.majors = resp.data.majors;
        _this.teachers = resp.data.teachers;
      });
    },
    insertCourseDetail() {
      var _this = this;
      this.$refs.form.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form);
          this.postRequest("/admin/insertcourse", {
            form: form
          }).then(() => {
            _this.insertCourseDialog = false;
            _this.getAllCourse();
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
    submitclassandteacherDetail() {
      var _this = this;
      this.$refs.form1.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form1);
          this.postRequest("/admin/submitclassandteacher", {
            form: form
          }).then(() => {
            _this.insertCourseDialog = false;
            _this.getAllCourse();
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
    deleteCourseAndTeacherAndClassConnect() {
      var _this = this;
      this.$confirm(
        "确定要删除这个关联吗，删除之后关于该关联方式将完全消失",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        _this
          .postRequest("/admin/deletecourseandteacherandclassconnect", {
            course_id: _this.data.value
          })
          .then(() => {
            _this.getAllCourse();
            _this.reload();
          });
      });
    },
    deleteCourse() {
      var _this = this;
      this.$confirm(
        "确定要删除这个课程吗，删除之后关于该课程的信息将完全消失",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        _this
          .postRequest("/admin/deletecourse", {
            course_id: _this.data.value
          })
          .then(() => {
            _this.getAllCourse();
            _this.reload();
          });
      });
    }
  }
};
</script>
<style>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}
</style>
