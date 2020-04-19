<template>
  <div>
    <el-row style="margin-bottom: 20px">
      <el-col :span="8">
        <el-upload
          accept="application/vnd.ms-excel"
          action="/admin/importstudents"
          :on-success="fileUploadSuccess"
          :on-error="fileUploadError"
          style="position: absolute; top: 0px;"
        >
          <el-button
            size="mini"
            type="success"
            icon="el-icon-sort-down"
            style="width: 100%"
          >
            导入学生
          </el-button>
        </el-upload>
      </el-col>
    </el-row>
    <el-collapse
      v-model="activeName"
      accordion
      @change="getChange(activeName)"
      style="margin-top: 40px"
    >
      <el-collapse-item
        v-for="(item, index) in majorList"
        :key="index"
        :title="item.major_name"
        :name="index"
      >
        <el-row style="margin-bottom:4px">
          <el-col :span="18">
            <el-form
              label-position="left"
              label-width="80px"
              :model="form1"
              ref="form1"
              :inline="true"
            >
              <el-form-item label="班级">
                <el-cascader
                  v-model="form1.classValue"
                  :options="options"
                  :props="{ multiple: true }"
                  style="width: 100%"
                  size="mini"
                  clearable
                ></el-cascader>
              </el-form-item>
              <el-form-item label="姓名">
                <el-input
                  placeholder="请输入管理员的账号或者姓名"
                  v-model="form1.userInfo"
                  class="input-with-select"
                  size="mini"
                >
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="mini"
                  @click="getStudentByMajor"
                  >查询</el-button
                >
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-button
              size="mini"
              class="el-icon-plus"
              @click="insertStudentInfo()"
              type="success"
              style="width:100%"
              >添加单个学生</el-button
            >
          </el-col>
        </el-row>
        <el-table
          border
          style="width: 100%"
          :data="getInfo(index)"
          size="mini"
          type="index"
        >
          <el-table-column prop="user_id" label="学生id" fixed width="100px">
          </el-table-column>
          <el-table-column prop="username" label="学生姓名" fixed width="100px">
          </el-table-column>
          <el-table-column label="班级" fixed width="250px">
            <template slot-scope="scope">{{
              scope.row.major_name + scope.row.grade_name + scope.row.class_name
            }}</template>
          </el-table-column>
          <el-table-column prop="email" label="学生邮箱" fixed width="100px">
          </el-table-column>
          <el-table-column prop="phone" label="学生电话" fixed width="100px">
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-tooltip
                class="item"
                effect="dark"
                content="修改学生信息"
                placement="top"
              >
                <el-button
                  type="primary"
                  size="small"
                  circle
                  @click="changeStudentInfo(scope.row)"
                  class="el-icon-info"
                ></el-button>
              </el-tooltip>

              <el-tooltip
                class="item"
                effect="dark"
                content="删除学生"
                placement="top"
              >
                <el-button
                  type="danger"
                  size="small"
                  circle
                  @click="deleteStudentInfo(scope.row)"
                  class="el-icon-delete"
                ></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top: 25px;">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="getInfoCount(index)"
            :currentPage="currentPage"
            @current-change="currentChange"
          >
          </el-pagination>
        </div>
      </el-collapse-item>
    </el-collapse>
    <el-dialog
      title="提示"
      :visible.sync="insertStudentDialog"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form"
        :rules="rules"
        ref="formName"
      >
        <el-form-item label="账号" prop="user_id">
          <el-input v-model="form.user_id"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="username">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="选择班级" prop="classValue">
          <el-cascader
            v-model="form.classValue"
            :options="options"
            :props="{ expandTrigger: 'hover' }"
            style="width: 100%"
          ></el-cascader>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showChangeAdminDialogVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" @click="insertStudentInfoDetail()"
          >确 定</el-button
        >
      </span>
    </el-dialog>

    <el-dialog
      title="提示"
      :visible.sync="changeStudentDialog"
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
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone"></el-input>
        </el-form-item>
        <el-form-item label="选择班级" prop="classValue">
          <el-cascader
            v-model="form.classValue"
            :options="options"
            :props="{ expandTrigger: 'hover' }"
            style="width: 100%"
          ></el-cascader>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="changeStudentDialog = false">取 消</el-button>
        <el-button type="primary" @click="changeStudentInfoDetail()"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
export default {
  inject: ["reload"],
  mounted() {},
  data() {
    return {
      studentInfo: "",
      majorList: "",
      activeName: "",
      form1: {
        classValue: [],
        userInfo: ""
      },
      options: [],
      insertStudentDialog: false,
      changeStudentDialog: false,
      currentPage: 1,
      form: {
        user_id: "",
        username: "",
        email: "",
        phone: "",
        classValue: []
      },
      rules: {
        user_id: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            pattern: /^[0-9]{9}$/,
            message: "请输入九位学生账户",
            trigger: "blur"
          }
        ],
        username: [{ required: true, message: "请输入姓名", trigger: "blur" }],
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
        ],
        classValue: [
          {
            required: true,
            message: "班级选项不为空"
          }
        ]
      }
    };
  },
  methods: {
    fileUploadSuccess() {
      this.$message({
        type: "success",
        message: "导入文件成功"
      });
    },
    fileUploadError() {
      this.$message({
        type: "error",
        message: "导入文件失败, 可能原因为数据库不存在该学生填写的班级或学号不和规范"
      });
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.getStudentByMajor();
    },

    insertStudentInfo() {
      this.insertStudentDialog = true;
    },
    insertStudentInfoDetail() {
      var _this = this;
      this.$refs.formName.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form);
          this.postRequest("/admin/insertstudentinfo", {
            form: form
          }).then(() => {
            _this.insertStudentDialog = false;
            _this.getStudentByMajor();
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
    changeStudentInfo(row) {
      this.form.user_id = row.user_id;
      this.form.username = row.username;
      this.form.email = row.email;
      this.form.phone = row.phone;
      this.form.classValue = [row.major_id, row.grade_id, row.class_id];
      this.changeStudentDialog = true;
    },
    changeStudentInfoDetail() {
      var _this = this;
      this.$refs.formName.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form);
          console.log(form);
          this.postRequest("/admin/changestudentinfo", {
            form: form
          }).then(() => {
            _this.changeStudentDialog = false;
            _this.getStudentByMajor();
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
    deleteStudentInfo(row) {
      this.$confirm("确定要删除该学生的信息吗", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.postRequest("/admin/deleteStudentInfo", {
          user_id: row.user_id
        }).then(() => {
          _this.getStudentByMajor();
          _this.reload();
        });
      });
    },
    getInfo(index) {
      /* 获取学生信息 */
      var _this = this;
      return this.studentInfo[_this.majorList[index].major_name];
    },
    getInfoCount(index) {
      var _this = this;
      return this.studentInfo[_this.majorList[index].major_name + "count"];
    },
    getStudentByMajor() {
      /* 根据专业来获取教师 */
      var _this = this;
      var majorList = JSON.stringify(_this.$store.state.admin.majorList);
      var form = JSON.stringify(this.form1);
      this.postRequest("/admin/getstudentbymajor", {
        majorList: majorList,
        currentPage: _this.currentPage,
        size: 20,
        form: form
      }).then(resp => {
        _this.studentInfo = resp.data;
        _this.majorList = _this.$store.state.admin.majorList;
      });
    },
    getChange(activeName) {
      /* 获取在当前专业下的班级信息 */
      if (activeName === null || activeName === "") {
        return;
      }

      var _this = this;
      this.getRequest(
        "/admin/getclassesbymajorIid?majorID=" +
          _this.majorList[activeName].major_id
      ).then(resp => {
        _this.options = resp.data;
      });
    }
  }
};
</script>
