<template>
  <div>
    <el-row style="margin-bottom: 10px">
      <el-col :span="6">
        <el-button
          type="success"
          @click="insertMajor"
          style="width: 100%"
          size="mini"
          >点击添加</el-button
        >
      </el-col>
    </el-row>

    <el-tree
      :data="options"
      :props="defaultProps"
      :expand-on-click-node="false"
      node-key="id"
      :highlight-current="true"
      :check-on-click-node="true"
      @node-click="handleNodeClick"
      ref="tree"
    ></el-tree>
    <el-row style="margin-top: 10px">
      <el-col :span="4">
        <el-button
          :type="changeType"
          @click="changeMajor"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >点击修改</el-button
        >
      </el-col>
      <el-col :span="4">
        <el-button
          :type="deleteType"
          @click="deleteMajor"
          style="width: 100%"
          size="mini"
          :disabled="show"
          >点击删除</el-button
        >
      </el-col>
    </el-row>
    <el-dialog
      title="提示"
      :visible.sync="changeMajorDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="data"
        ref="data"
        :rules="rules"
      >
        <el-form-item label="专业id">
          <el-input v-model="data.value" disabled></el-input>
        </el-form-item>
        <el-form-item label="专业名称" prop="label">
          <el-input v-model="data.label"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="showChangeAcademyDialogVisible = false"
            >取 消</el-button
          >
          <el-button type="primary" @click="changeMajorDialogDetail()"
            >确 定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="insertMajorDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form"
        ref="form"
        :rules="rules"
      >
        <el-form-item label="选择学院" prop="academy_id">
          <el-select
            v-model="form.academy_id"
            placeholder="请选择"
            style="width: 100%"
          >
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
          label="专业名称"
          v-for="(label, index) in form.label"
          :key="label.key"
          :prop="'label.' + index + '.value'"
          :rules="{
            required: true,
            message: '专业名不能为空',
            trigger: 'blur'
          }"
        >
          <el-input v-model="label.value" style="width: 65%"></el-input>
          <el-button @click="addMajor">新增专业名</el-button>
          <el-button @click.prevent="removeMajor(label)">删除</el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="showChangeAcademyDialogVisible = false"
            >取 消</el-button
          >
          <el-button type="primary" @click="insertMajorDialogDetail()"
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
      changeType: "",
      deleteType: "",
      show: true,
      data: {},
      form: {
        academy_id: "",
        label: [
          {
            value: ""
          }
        ]
      },
      rules: {
        academy_id: [
          {
            required: true,
            message: "请选择学院",
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
      changeMajorDialog: false,
      insertMajorDialog: false,
      defaultProps: {
        children: "children",
        label: "label"
      }
    };
  },
  methods: {
    removeMajor(item) {
      var index = this.form.label.indexOf(item);
      if (index !== -1) {
        this.form.label.splice(index, 1);
      }
    },
    addMajor() {
      this.form.label.push({ value: "", key: Date.now() });
    },
    handleNodeClick(data) {
      if (data.children == null) {
        this.changeType = "success";
        this.deleteType = "danger";
        this.show = false;
        this.data = data;
      } else {
        this.changeType = "";
        this.deleteType = "";
        this.show = true;
      }
    },
    getAllMajor() {
      var _this = this;
      this.getRequest("/root/getallmajor").then(resp => {
        _this.options = resp.data;
      });
    },
    insertMajor() {
      this.insertMajorDialog = true;
    },
    insertMajorDialogDetail() {
      var _this = this;
      this.$refs.form.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form);
          this.postRequest("/root/insertmajorname", {
            form: form
          }).then(() => {
            _this.changeMajorDialog = false;
            _this.getAllMajor();
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
    changeMajor() {
      this.changeMajorDialog = true;
    },
    changeMajorDialogDetail(formName) {
      var _this = this;
      this.$refs.data.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.data);
          this.postRequest("/root/updatemajorname", {
            form: form
          }).then(() => {
            _this.changeMajorDialog = false;
            _this.getAllMajor();
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
    deleteMajor() {
      var _this = this;
      this.$confirm(
        "确定要删除这个专业吗，删除之后关于该专业的信息将完全消失",
        "提示",
        { confirmButtonText: "确定", cancelButtonText: "取消", type: "warning" }
      ).then(() => {
        _this
          .postRequest("/root/deletemajor", {
            major_id: _this.data.value
          })
          .then(() => {
            _this.getAllMajor();
            _this.reload();
          });
      });
    }
  }
};
</script>
