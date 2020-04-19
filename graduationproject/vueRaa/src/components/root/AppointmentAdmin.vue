<template>
  <div>
    <el-row>
      <el-col :offset="2" :span="18">
        <el-row style="margin-bottom: 5px;">
          <el-col :span="8">
            <el-input
              placeholder="请输入管理员的账号或者姓名"
              v-model="userInfo"
              class="input-with-select"
              size="mini"
            >
              <el-button
                slot="append"
                icon="el-icon-search"
                @click="getAdminBuUserInfo"
              ></el-button>
            </el-input>
          </el-col>
          <el-col :offset="12" :span="4">
            <el-button
              type="success"
              size="mini"
              style="width: 100%;"
              @click="insertDialog = true"
              >添加管理员</el-button
            >
          </el-col>
        </el-row>
        <el-table :data="tableData" border size="mini">
          <el-table-column fixed prop="user_id" label="管理员账号" width="150">
          </el-table-column>
          <el-table-column fixed prop="username" label="姓名" width="120">
          </el-table-column>
          <el-table-column prop="email" label="邮箱" width="240">
          </el-table-column>
          <el-table-column prop="phone" label="电话" width="240">
          </el-table-column>
          <el-table-column prop="major_name" label="专业名称" width="300">
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template slot-scope="scope">
              <!--<el-tooltip
                class="item"
                effect="dark"
                content="封停账号"
                placement="top"
              >
                <el-button
                  type="warning"
                  size="small"
                  circle
                  @click="stoporstartAdmin(scope.row)"
                  class="el-icon-warning"
                ></el-button>
              </el-tooltip>-->

              <el-tooltip
                class="item"
                effect="dark"
                content="删除账号"
                placement="top"
              >
                <el-button
                  type="danger"
                  size="small"
                  circle
                  @click="deleteAdmin(scope.row)"
                  class="el-icon-delete"
                ></el-button>
              </el-tooltip>
              <el-tooltip
                class="item"
                effect="dark"
                content="修改管理专业"
                placement="top"
              >
                <el-button
                  type="primary"
                  size="small"
                  circle
                  @click="showChangeAdminDialog(scope.row)"
                  class="el-icon-eleme"
                ></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-dialog
      title="提示"
      :visible.sync="insertDialog"
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
        <el-form-item label="选择专业" prop="major_id">
          <el-cascader
            v-model="form.major_id"
            :props="{ multiple: true }"
            :options="major"
            clearable
            class="cascader1"
            style="width: 100%"
          ></el-cascader>
        </el-form-item>
        <el-form-item>
          <el-button @click="insertDialog = false">取 消</el-button>
          <el-button type="primary" @click="insertAdmin">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="showChangeAdminDialogVisible"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form"
        ref="form2"
        :rules="rules"
      >
        <el-form-item label="选择专业" prop="major_id">
          <el-cascader
            v-model="form.major_id"
            :props="{ multiple: true }"
            :options="major"
            clearable
            class="cascader1"
            style="width: 100%"
          ></el-cascader>
        </el-form-item>
        <el-form-item>
          <el-button @click="showChangeAdminDialogVisible = false"
            >取 消</el-button
          >
          <el-button type="primary" @click="changAdminMajor()">确 定</el-button>
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
      form: {
        user_id: "",
        username: "",
        email: "",
        phone: "",
        major_id: []
      },
      rules: {
        user_id: [
          { required: true, message: "请输入账号", trigger: "blur" },
          {
            pattern: /^[0-9]{5}$/,
            message: "请输入五位用户账户",
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
        major_id: [
          { required: true, message: "请选择至少一个专业", trigger: "blur" }
        ]
      },
      tableData: [],
      currentPage: 1,
      userInfo: "",
      major: [],
      insertDialog: false,
      showChangeAdminDialogVisible: false,
      row: ""
    };
  },
  mounted() {
    this.getSecondaryAdministrator();
    this.getAllMajor();
  },
  methods: {
    getSecondaryAdministrator() {
      /* 获取所有的管理员 */
      var _this = this;
      this.getRequest(
        "/root/getsecondaryadministrator?currentPage=" +
          _this.currentPage +
          "&size=10&userInfo=" +
          _this.userInfo
      ).then(resp => {
        this.tableData = resp.data;
        for (var i = 0; i < this.tableData.length; i++) {
          var x = "";
          for (var j = 0; j < this.tableData[i].majorList.length; j++) {
            x = x + this.tableData[i].majorList[j].major_name + ", ";
          }
          this.tableData[i].major_name = x;
        }
      });
    },
    getAdminBuUserInfo() {
      this.getSecondaryAdministrator();
    },
    getAllMajor() {
      /* 获得所有的专业 */
      var _this = this;
      this.getRequest("/root/getallmajor").then(resp => {
        _this.major = resp.data;
      });
    },
    insertAdmin() {
      var _this = this;
      this.$refs.formName.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form);
          this.postRequest("/root/createadmin", {
            form: form
          }).then(() => {
            _this.insertDialog = false;
            _this.getSecondaryAdministrator();
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

    deleteAdmin(row) {
      /* 删除管理员信息 */
      var _this = this;
      this.$confirm("确定要进行删除吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.postRequest("/root/deleteAdmin", {
          user_id: row.user_id
        }).then(() => {
          _this.getSecondaryAdministrator();
          _this.reload();
        });
      });
    },
    showChangeAdminDialog(row) {
      this.row = row;
      this.showChangeAdminDialogVisible = true;
    },
    changAdminMajor() {
      var _this = this;
      this.$refs.form2.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form);
          this.postRequest("/root/changadminmajor", {
            user_id: _this.row.user_id,
            form: form
          }).then(() => {
            _this.getSecondaryAdministrator();
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
  }
};
</script>
<style></style>
