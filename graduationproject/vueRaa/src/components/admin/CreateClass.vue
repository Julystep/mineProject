<template>
  <div>
    <el-row style="margin-bottom: 10px">
      <el-col :span="6">
        <el-button
          type="success"
          @click="insertGrade"
          style="width: 100%"
          size="mini"
          >点击添加年级</el-button
        >
      </el-col>
      <el-col :span="6">
        <el-button
          type="success"
          @click="insertClass"
          style="width: 100%"
          size="mini"
          >点击添加班级</el-button
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
          @click="changeGradeOrClass"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >{{ confirmText }}</el-button
        >
      </el-col>
      <el-col :span="4">
        <el-button
          :type="deleteType"
          @click="deleteGradeOrClass"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >{{ deleteText }}</el-button
        >
      </el-col>
    </el-row>
    <el-dialog
      title="提示"
      :visible.sync="changeGradeOrClassDialog"
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
          <el-button @click="showChangeAcademyDialogVisible = false"
            >取 消</el-button
          >
          <el-button type="primary" @click="changegradeorclassDetail()"
            >确 定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="insertGradeDialog"
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
          label="年级名称"
          v-for="(label, index) in form.label"
          :key="label.key"
          :prop="'label.' + index + '.value'"
          :rules="rules1"
        >
          <el-input v-model="label.value" style="width: 65%"></el-input>
          <el-button @click="addGrade">新增年级名</el-button>
          <el-button @click.prevent="removeGrade(label)">删除</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="insertGradeDialog = false">取 消</el-button>
          <el-button type="primary" @click="insertGradeDialogDetail()"
            >确 定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="insertClassDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form1"
        ref="form1"
        :rules="rules"
      >
        <el-form-item label="选择班级" prop="classValue">
          <el-cascader
            v-model="form1.classValue"
            :options="majorandgrade"
            style="width: 100%"
            clearable
          ></el-cascader>
        </el-form-item>
        <el-form-item
          label="年级名称"
          v-for="(label, index) in form1.label"
          :key="label.key"
          :prop="'label.' + index + '.value'"
          :rules="rules2"
        >
          <el-input v-model="label.value" style="width: 65%"></el-input>
          <el-button @click="addClass">新增班级名</el-button>
          <el-button @click.prevent="removeClass(label)">删除</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="insertClassDialog = false">取 消</el-button>
          <el-button type="primary" @click="insertClassDialogDetail()"
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
      insertGradeDialog: false,
      insertClassDialog: false,
      options: [],
      majorandgrade: [],
      changeType: "",
      deleteType: "",
      type: "",
      show: true,
      data: {},
      confirmText: "等待操作",
      deleteText: "等待操作",
      changeGradeOrClassDialog: false,
      form: {
        id: "",
        label: [
          {
            value: ""
          }
        ]
      },
      form1: {
        classValue: "",
        label: [
          {
            value: ""
          }
        ]
      },
      rules1: [
        {
          required: true,
          message: "年级名不能为空",
          trigger: "blur"
        },
        {
          pattern: /^[0-9]{4}级$/,
          message: "输入不正确，正确命名为 2016级",
          trigger: "blur"
        }
      ],
      rules2: [
        {
          required: true,
          message: "年级名不能为空",
          trigger: "blur"
        },
        {
          pattern: /^[1-9]{1}[0-9]?班$/,
          message: "输入不正确，正确命名为 1班",
          trigger: "blur"
        }
      ],
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
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  methods: {
    insertGradeDialogDetail() {
      var _this = this;
      this.$refs.form.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form);
          this.postRequest("/admin/insertgrade", {
            form: form
          }).then(() => {
            _this.insertGradeDialog = false;
            _this.getAllGrade();
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
    insertClassDialogDetail() {
      var _this = this;
      this.$refs.form1.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form1);
          this.postRequest("/admin/insertclass", {
            form: form
          }).then(() => {
            _this.insertClassDialog = false;
            _this.getAllGrade();
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
    removeGrade(item) {
      var index = this.form.label.indexOf(item);
      if (index !== -1) {
        this.form.label.splice(index, 1);
      }
    },
    removeClass(item) {
      var index = this.form1.label.indexOf(item);
      if (index !== -1) {
        this.form1.label.splice(index, 1);
      }
    },
    addGrade() {
      this.form.label.push({ value: "", key: Date.now() });
    },
    addClass() {
      this.form1.label.push({ value: "", key: Date.now() });
    },
    insertGrade() {
      this.insertGradeDialog = true;
    },
    insertClass() {
      this.insertClassDialog = true;
      var majorList = JSON.stringify(this.$store.state.admin.majorList);
      this.postRequest("/admin/getmajorandgrade", {
        majorList: majorList
      }).then(resp => {
        this.majorandgrade = resp.data;
      });
    },
    handleNodeClick(data) {
      console.log(data);
      /* typeof(reValue) == undefined */
      console.log(typeof data.major_id !== undefined);
      if (data.major_name !== undefined) {
        this.changeType = "";
        this.deleteType = "";
        this.show = true;
        this.confirmText = "等待操作";
        this.deleteText = "等待操作";
      } else if (!data.children) {
        this.changeType = "success";
        this.deleteType = "danger";
        this.confirmText = "修改班级名称";
        this.deleteText = "删除班级";
        this.show = false;
        this.type = "class";
        this.data = data;
      } else {
        this.changeType = "success";
        this.deleteType = "danger";
        this.confirmText = "修改年级名称";
        this.deleteText = "删除年级";
        this.show = false;
        this.type = "grade";
        this.data = data;
      }
    },
    changeGradeOrClass() {
      this.changeGradeOrClassDialog = true;
    },
    changegradeorclassDetail() {
      var _this = this;
      if (this.type === "grade") {
        this.$refs.data.validate(valid => {
          if (valid) {
            var form = JSON.stringify(_this.data);
            this.postRequest("/admin/updategrade", {
              form: form
            }).then(() => {
              _this.changeGradeOrClassDialog = false;
              _this.getAllGrade();
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
      if (this.type === "class") {
        this.$refs.data.validate(valid => {
          if (valid) {
            var form = JSON.stringify(_this.data);
            this.postRequest("/admin/updateclass", {
              form: form
            }).then(() => {
              _this.changeGradeOrClassDialog = false;
              _this.getAllGrade();
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
    deleteGradeOrClass() {
      var _this = this;
      if (this.type === "grade") {
        this.$confirm(
          "确定要删除这个年级吗，删除之后关于该年级的信息将完全消失",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).then(() => {
          _this
            .postRequest("/admin/deletegrade", {
              grade_id: _this.data.value
            })
            .then(() => {
              _this.getAllGrade();
              _this.reload();
            });
        });
      }
      if (this.type === "class") {
        this.$confirm(
          "确定要删除这个班级吗，删除之后关于该班级的信息将完全消失",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
        ).then(() => {
          _this
            .postRequest("/admin/deleteclass", {
              class_id: _this.data.value
            })
            .then(() => {
              _this.getAllGrade();
              _this.reload();
            });
        });
      }
    },
    getAllGrade() {
      var _this = this;
      this.postRequest("/admin/getallgrade", {
        majorList: JSON.stringify(_this.$store.state.admin.majorList)
      }).then(resp => {
        _this.options = resp.data;
        console.log(resp.data);
      });
    }
  }
};
</script>
