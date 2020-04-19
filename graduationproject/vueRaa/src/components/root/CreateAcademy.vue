<template>
  <div>
    <el-row style="margin-bottom: 10px">
      <el-col :span="8">
        <el-button
          type="success"
          style="width: 100%"
          @click="insertAcademy"
          size="mini"
        >
          添加学院
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="academyInfo" border size="mini">
      <el-table-column prop="academy_name" label="学院名称"> </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-tooltip
            class="item"
            effect="dark"
            content="删除学院信息（会连带删除掉所有的育学园有关的人的信息）"
            placement="top"
          >
            <el-button
              type="danger"
              size="small"
              circle
              @click="deleteAcademy(scope.row)"
              class="el-icon-delete"
            ></el-button>
          </el-tooltip>
          <el-tooltip
            class="item"
            effect="dark"
            content="修改学院名称"
            placement="top"
          >
            <el-button
              type="primary"
              size="small"
              circle
              @click="showChangeAcademyDialog(scope.row)"
              class="el-icon-eleme"
            ></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="提示"
      :visible.sync="insertAcademyDialog"
      :close-on-click-modal="false"
    >
      <el-form
        :model="academyForm"
        ref="academyForm"
        label-width="100px"
        class="demo-dynamic"
      >
        <el-form-item
          v-for="(academy, index) in academyForm.academy"
          label="学院名称"
          :key="academy.key"
          :prop="'academy.' + index + '.value'"
          :rules="{
            required: true,
            message: '学院名不能为空',
            trigger: 'blur'
          }"
        >
          <el-input v-model="academy.value" style="width: 80%"></el-input
          ><el-button @click.prevent="removeAcademy(academy)">删除</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm('academyForm')"
            >提交</el-button
          >
          <el-button @click="addAcademy">新增学院名</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      title="提示"
      :visible.sync="showChangeAcademyDialogVisible"
      :close-on-click-modal="false"
    >
      <el-form
        label-position="left"
        label-width="80px"
        :model="form"
        ref="form"
        :rules="rules"
      >
        <el-form-item label="学院id">
          <el-input v-model="form.academy_id" disabled></el-input>
        </el-form-item>
        <el-form-item label="学院名称" prop="academy_name">
          <el-input v-model="form.academy_name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="showChangeAcademyDialogVisible = false"
            >取 消</el-button
          >
          <el-button type="primary" @click="updateAcademyName('form')"
            >确 定</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
export default {
  inject: ['reload'],
  data () {
    return {
      academyInfo: [],
      academyForm: {
        academy: [
          {
            value: ''
          }
        ]
      },
      showChangeAcademyDialogVisible: false,
      row: '',
      form: {
        academy_id: 0,
        academy_name: ''
      },
      insertAcademyDialog: false,
      rules: {
        academy_name: [
          {
            required: true,
            message: '学院名称不能为空',
            trigger: blur
          }
        ]
      }
    }
  },
  methods: {
    insertAcademy () {
      this.insertAcademyDialog = true
    },
    deleteAcademy (row) {
      var _this = this
      console.log(row)
      this.$confirm(
        '确定要删除这个学院吗，删除之后关于该学院的信息将完全消失',
        '提示',
        { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
      ).then(() => {
        _this
          .postRequest('/root/deleteacademy', {
            academy_id: row.academy_id
          })
          .then(() => {
            _this.getAllAcademy()
            _this.reload()
          })
      })
    },
    showChangeAcademyDialog (row) {
      this.showChangeAcademyDialogVisible = true
      this.row = row
      this.form.academy_id = row.academy_id
      this.form.academy_name = row.academy_name
    },
    updateAcademyName (formName) {
      var _this = this
      this.$refs[formName].validate(valid => {
        if (valid) {
          var form = JSON.stringify(_this.form)
          this.postRequest('/root/updateacademyname', {
            form: form
          }).then(() => {
            _this.showChangeAcademyDialogVisible = false
            _this.getAllAcademy()
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
    submitForm (formName) {
      var _this = this
      this.$refs[formName].validate(valid => {
        if (valid) {
          console.log(JSON.stringify(_this.academyForm))
          _this
            .postRequest('/root/createacamedy', {
              form: JSON.stringify(_this.academyForm)
            })
            .then(() => {
              _this.getAllAcademy()
              _this.reload()
            })
        } else {
          this.$message({
            type: 'error',
            message: '添加学院失败'
          })
        }
      })
    },
    removeAcademy (item) {
      var index = this.academyForm.academy.indexOf(item)
      if (index !== -1) {
        this.academyForm.academy.splice(index, 1)
      }
    },
    addAcademy () {
      this.academyForm.academy.push({
        value: '',
        key: Date.now()
      })
    },
    getAllAcademy () {
      var _this = this
      this.getRequest('/root/getallacademy').then(resp => {
        console.log('========================================')
        console.log(resp.data)
        _this.academyInfo = resp.data
      })
    }
  }
}
</script>
