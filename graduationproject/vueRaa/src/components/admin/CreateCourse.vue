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
          <el-button @click="addCourse">新增年级名</el-button>
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
  </div>
</template>
<script>
export default {
  inject: ["reload"],
  data() {
    return {
      options: [],
      defaultProps: {
        children: "childrencourse",
        label: "label"
      },
      changeType: "",
      deleteType: "",
      show: true,
      data: {},
      confirmText: "等待操作",
      deleteText: "等待操作",
      changeCourseDialog: false,
      insertCourseDialog: false,
      form: {
        id: "",
        label: [
          {
            value: ""
          }
        ]
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
      ]
    };
  },
  methods: {
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
      console.log(typeof data.childrencourse !== undefined);
      if (data.major_name !== undefined) {
        this.changeType = "";
        this.deleteType = "";
        this.show = true;
        this.confirmText = "等待操作";
        this.deleteText = "等待操作";
      } else {
        this.changeType = "success";
        this.deleteType = "danger";
        this.confirmText = "修改课程名称";
        this.deleteText = "删除课程";
        this.show = false;
        this.data = data;
      }
    },
    changeCourse() {
      this.changeCourseDialog = true;
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
