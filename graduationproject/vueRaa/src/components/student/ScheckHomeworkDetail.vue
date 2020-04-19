<template>
  <div class="scheckhomework">
    <el-row>
      <el-col :xs="20" :sm="20" :md="20" :lg="16" :xl="16" :offset="2">
        <el-collapse accordion>
          <el-table :data="homework" border style="width: 100%" size="mini">
            <el-table-column fixed prop="title" label="标题" width="150">
            </el-table-column>
            <el-table-column label="内容" width="370">
              <template slot-scope="scope">
                <el-button
                  type="primary"
                  @click="showContentDialog(scope.row.main)"
                  size="mini"
                  >查看内容</el-button
                >
              </template>
            </el-table-column>
            <el-table-column label="开始时间" width="160">
              <template slot-scope="scope">{{
                scope.row.sdate | formatDateAndTime
              }}</template>
            </el-table-column>
            <el-table-column label="截止时间" width="160">
              <template slot-scope="scope">{{
                scope.row.edate | formatDateAndTime
              }}</template>
            </el-table-column>
            <el-table-column width="120">
              <template slot-scope="scope">
                <div v-if="scope.row.state === 0">
                  <el-tag type="warning" :closable="false">未提交 </el-tag>
                </div>
                <div v-if="scope.row.state === 1">
                  <el-tag type="success" :closable="false">已提交 </el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="评语" width="100px">
              <template slot-scope="scope">
                <template v-if="scope.row.iscomment">
                  <el-button
                    type="success"
                    size="mini"
                    @click="showCommentDialog(scope.row.comment)"
                    >显示评语</el-button
                  >
                </template>
                <template v-if="!scope.row.iscomment">
                  <el-button
                    type="success"
                    size="mini"
                    @click="showCommentDialog(scope.row.comment)"
                    disabled
                    >显示评语</el-button
                  >
                </template>
              </template>
            </el-table-column>

            <el-table-column
              fixed="right"
              type="expand"
              label="详细评分"
              width="100px"
            >
              <template slot-scope="props">
                <template v-if="props.row.judge !== null">
                  <el-table
                    border
                    style="width: 100%"
                    :data="props.row.judge"
                    size="mini"
                    show-summary
                  >
                    <el-table-column
                      prop="name"
                      label="评分标准"
                    ></el-table-column>
                    <el-table-column
                      prop="value"
                      label="评分"
                    ></el-table-column>
                  </el-table>
                </template>
              </template>
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="100">
              <template slot-scope="scope">
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="提交作业"
                  placement="top"
                >
                  <template v-if="!canUpload(scope.row.sdate, scope.row.edate)"
                    ><el-button
                      icon="el-icon-upload"
                      circle
                      size="small"
                      type="primary"
                      disabled
                    ></el-button>
                  </template>
                  <template v-if="canUpload(scope.row.sdate, scope.row.edate)"
                    ><el-button
                      icon="el-icon-upload"
                      circle
                      size="small"
                      type="primary"
                      @click="showdialogMethod(scope.row)"
                    ></el-button>
                  </template>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse>
      </el-col>
    </el-row>
    <el-dialog title="提示" :visible.sync="showdialog">
      <el-upload
        class="upload-demo"
        ref="upload"
        action="/student/uploadhomework"
        :auto-upload="false"
        :limit="1"
        :before-upload="beforeHomeworkUpload"
        :on-error="uploadError"
        :on-success="uploadSuccess"
        :on-progress="onProgress"
        :data="formFileData"
        accept=".docx,.doc,.java"
      >
        <el-button slot="trigger" size="small" type="primary"
          >选取文件</el-button
        >
        <el-button
          style="margin-left: 10px;"
          size="small"
          type="success"
          :loading="loading"
          @click="submitUpload"
          >上传到服务器</el-button
        >
        <div slot="tip" class="el-upload__tip">
          只能上传doc/docx/java文件,文件名为学号加姓名 例如 160404040张三
        </div>
      </el-upload>
    </el-dialog>
    <el-dialog :visible.sync="commentDialog">
      <el-alert :title="commentDialogContent" type="info" :closable="false">
      </el-alert>
    </el-dialog>
    <el-dialog :visible.sync="contentDialog">
      <el-alert :title="contentDialogContent" type="info" :closable="false">
      </el-alert>
    </el-dialog>
  </div>
</template>
<script>
export default {
  inject: ["reload"],
  data() {
    return {
      homework: [],
      showdialog: false,
      fileList: [],
      formFileData: {
        homeworkID: "",
        major: "",
        grade: "",
        sclass: "",
        classID: ""
      },
      contentDialog: false,
      commentDialog: false,
      commentDialogContent: "",
      contentDialogContent: "",
      loading: false
    };
  },
  mounted() {
    this.getHomworkInfoAndState();
    this.getMajorGradeClassByStudentId();
  },
  methods: {
    onProgress() {
      this.loading = true;
    },
    showCommentDialog(comment) {
      this.commentDialog = true;
      this.commentDialogContent = comment;
    },
    showContentDialog(content) {
      this.contentDialog = true;
      this.contentDialogContent = content;
    },

    canUpload(sdate, edate) {
      var d = new Date();
      d = this.formatDateAndTime(d);
      sdate = this.formatDateAndTime(sdate);
      edate = this.formatDateAndTime(edate);

      if (d > sdate && d < edate) {
        return true;
      }
      return false;
    },
    getHomworkInfoAndState() {
      var _this = this;
      this.getRequest(
        "/student/gethomworkinfoandState?studentID=" +
          this.$store.state.user.user_id +
          "&courseID=" +
          this.$route.query.courseID
      ).then(resp => {
        if (resp.status == 200) {
          if (resp.data.length == 0) {
            return;
          }

          _this.homework = resp.data;
          for (var i = 0; i < _this.homework.length; i++) {
            if (_this.homework[i].judge === null) {
              continue;
            } else {
              _this.homework[i].judge = JSON.parse(_this.homework[i].judge);
            }
          }
        }
      });
    },
    /*
    获得该学生所在专业年级班级情况
     */
    getMajorGradeClassByStudentId() {
      var _this = this;
      this.getRequest(
        "/student/getmgcbystudentid?studentID=" + this.$store.state.user.user_id
      ).then(resp => {
        if (resp.status == 200) {
          _this.formFileData.major = resp.data.studentMajor.label;
          _this.formFileData.grade = resp.data.studentMajor.children[0].label;
          _this.formFileData.sclass =
            resp.data.studentMajor.children[0].children[0].label;
          _this.formFileData.classID =
            resp.data.studentMajor.children[0].children[0].value;
        }
      });
    },
    submitUpload() {
      this.$refs.upload.submit();
    },
    beforeHomeworkUpload(file) {
      /*
      好像没啥用，不过做一个bug防御也不错
       */
      var parrten = /^[0-9]{9}[\u4E00-\u9FA5\uf900-\ufa2d·s]{2,20}$/;
      var name = file.name.substring(0, file.name.indexOf("."));
      if (!parrten.test(name)) {
        this.$message({
          type: "warning",
          message: "文件名称不正确"
        });
        return false;
      }
    },
    uploadError(err) {
      this.$message({
        showClose: true,
        type: "error",
        message: "出现错误"
      });
    },
    uploadSuccess() {
      this.$message({
        showClose: true,
        type: "success",
        message: "作业上传成功"
      });
      this.loading = false;
      this.showdialog = false;
      this.getHomworkInfoAndState();
      this.reload();
    },
    showdialogMethod(row) {
      this.showdialog = true;
      console.log(row.id);
      this.formFileData.homeworkID = row.id;
    }
  }
};
</script>
<style>
.scheckhomework {
  margin-top: 40px;
}
</style>
