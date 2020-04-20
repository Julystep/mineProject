<template>
  <div>
    <el-row style="margin-bottom: 20px">
      <el-col :span="8">
        <el-upload
          accept="application/vnd.ms-excel"
          action="/admin/importteachers"
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
            导入教师
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
                  @click="getTeacherByMajor"
                  >查询</el-button
                >
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="6">
            <el-button
              size="mini"
              class="el-icon-plus"
              @click="insertTeacherInfo()"
              type="success"
              style="width:100%"
              >添加单个教师</el-button
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
          <el-table-column prop="user_id" label="教师id" fixed width="100px">
          </el-table-column>
          <el-table-column prop="username" label="教师姓名" fixed width="100px">
          </el-table-column>
          <el-table-column label="教师专业" prop="major_name" width="250px">
          </el-table-column>
          <el-table-column prop="email" label="教师邮箱" fixed width="100px">
          </el-table-column>
          <el-table-column prop="phone" label="教师电话" fixed width="100px">
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-tooltip
                class="item"
                effect="dark"
                content="修改教师信息"
                placement="top"
              >
                <el-button
                  type="primary"
                  size="small"
                  circle
                  @click="changeTeacherInfo(scope.row)"
                  class="el-icon-info"
                ></el-button>
              </el-tooltip>

              <el-tooltip
                class="item"
                effect="dark"
                content="删除教师"
                placement="top"
              >
                <el-button
                  type="danger"
                  size="small"
                  circle
                  @click="deleteTeacherInfo(scope.row)"
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
      :visible.sync="insertTeacherDialog"
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
        <el-form-item label="选择专业" prop="majorValue">
          <el-select
            v-model="form.majorValue"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option
              v-for="item in options"
              :key="item.major_id"
              :label="item.major_name"
              :value="item.major_id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="showChangeAdminDialogVisible = false"
          >取 消</el-button
        >
        <el-button type="primary" @click="insertTeacherInfoDetail()"
          >确 定</el-button
        >
      </span>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="changeTeacherDialog"
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
        <el-form-item label="选择班级" prop="majorValue">
          <el-select
            v-model="form.majorValue"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option
              v-for="item in options"
              :key="item.major_id"
              :label="item.major_name"
              :value="item.major_id"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="changeTeacherDialog = false">取 消</el-button>
        <el-button type="primary" @click="changeTeacherInfoDetail()"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  inject: ['reload'],
  data () {
    return {
      form1: {
        majorValue: [],
        userInfo: ''
      },
      currentPage: 1,
      teacherInfo: '',
      majorList: '',
      activeName: '',
      insertTeacherDialog: false,
      changeTeacherDialog: false,
      options: [],
      form: {
        user_id: '',
        username: '',
        email: '',
        phone: '',
        majorValue: ''
      },
      rules: {
        user_id: [
          { required: true, message: '请输入账号', trigger: 'blur' },
          {
            pattern: /^[0-9]{5}$/,
            message: '请输入5位教师账户',
            trigger: 'blur'
          }
        ],
        username: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        email: [
          {
            pattern: /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/,
            message: '请输入正确的邮箱',
            trigger: 'blur'
          }
        ],
        phone: [
          {
            pattern: /^1[3456789]\d{9}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ],
        majorValue: [
          {
            required: true,
            message: '班级选项不为空'
          }
        ]
      }
    }
  },
  methods: {
    fileUploadSuccess () {
      this.$message({
        type: 'success',
        message: '导入文件成功'
      })
    },
    fileUploadError () {
      this.$message({
        type: 'error',
        message:
          '导入文件失败, 可能原因为数据库不存在该教师填写的教师号和专业不规范'
      })
    },
    /* 根据专业来获取教师 */
    getTeacherByMajor () {
      var _this = this
      var form = JSON.stringify(this.form1)
      this.postRequest('/admin/getteacherbymajor', {
        majorList: JSON.stringify(_this.$store.state.admin.majorList),
        currentPage: _this.currentPage,
        size: 20,
        form: form
      }).then(resp => {
        _this.teacherInfo = resp.data
        _this.majorList = _this.$store.state.admin.majorList
      })
    },
    getChange (activeName) {
      /* 获取在当前专业下的班级信息 */
      if (activeName === null || activeName === '') {
        return
      }
      this.options = this.$store.state.admin.majorList
      /* var _this = this;
      this.postRequest("/admin/getallmajorbymajorlist", {
        majorList: JSON.stringify(_this.$store.state.admin.majorList)
      }).then(resp => {
        _this.options = resp.data;
      }); */
    },
    insertTeacherInfo () {
      this.insertTeacherDialog = true
    },
    getInfo (index) {
      /* 获取学生信息 */
      var _this = this
      return this.teacherInfo[_this.majorList[index].major_name]
    },
    getInfoCount (index) {
      var _this = this
      return this.teacherInfo[_this.majorList[index].major_name + 'count']
    },
    currentChange (currentPage) {
      this.currentPage = currentPage
      this.getTeacherByMajor()
    },
    changeTeacherInfo (row) {
      this.form.user_id = row.user_id
      this.form.username = row.username
      this.form.email = row.email
      this.form.phone = row.phone
      this.form.majorValue = row.major_id
      this.changeTeacherDialog = true
    },
    changeTeacherInfoDetail () {
      var _this = this
      this.$refs.formName.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form)
          console.log(form)
          this.postRequest('/admin/changeteacherinfo', {
            form: form
          }).then(() => {
            _this.changeTeacherDialog = false
            _this.getTeacherByMajor()
            _this.reload()
          })
        } else {
          this.$message({
            type: 'warning',
            message: '未按规则填写表单，请重新填写'
          })
        }
      })
    },
    deleteTeacherInfo (row) {
      var _this = this
      this.$confirm('确定要删除该教师的信息吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.postRequest('/admin/deleteTeacherInfo', {
          user_id: row.user_id
        }).then(() => {
          _this.getTeacherByMajor()
          _this.reload()
        })
      })
    },
    insertTeacherInfoDetail () {
      var _this = this
      this.$refs.formName.validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form)
          this.postRequest('/admin/insertteacherinfo', {
            form: form
          }).then(() => {
            _this.insertTeacherDialog = false
            _this.getTeacherByMajor()
            _this.reload()
          })
        } else {
          this.$message({
            type: 'warning',
            message: '未按规则填写表单，请重新填写'
          })
        }
      })
    }
  }
}
</script>
