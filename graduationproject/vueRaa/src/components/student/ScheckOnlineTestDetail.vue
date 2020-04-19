<template>
  <div class="onlineTest">
    <el-row>
      <el-col :xs="20" :sm="20" :md="20" :lg="16" :xl="16" :offset="2">
        <el-collapse accordion>
          <el-table :data="examination" border style="width: 100%" size="mini">
            <el-table-column
              fixed
              prop="examination_title"
              label="标题"
              width="150"
            >
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
            <el-table-column
              fixed="right"
              type="expand"
              label="详细评分"
              width="200px"
            >
              <template slot-scope="props">
                <template v-if="props.row.answer !== null">
                  <el-tag type="success">{{
                    props.row.answer.question.title
                  }}</el-tag>
                  <el-table
                    border
                    style="width: 100%"
                    :data="props.row.answer.judge"
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

            <el-table-column label="操作" width="160" fixed="right">
              <template slot-scope="scope">
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="参加考试"
                  placement="top"
                >
                  <template
                    v-if="
                      !canUpload(
                        scope.row.sdate,
                        scope.row.edate,
                        scope.row.state
                      )
                    "
                    ><el-button size="small" type="primary" disabled>{{
                      countDown(
                        scope.row.sdate,
                        scope.row.edate,
                        scope.row.state
                      )
                    }}</el-button>
                  </template>
                  <template
                    v-if="
                      canUpload(
                        scope.row.sdate,
                        scope.row.edate,
                        scope.row.state
                      )
                    "
                    ><el-button
                      icon="el-icon-edit"
                      circle
                      size="small"
                      type="primary"
                      @click="showMessage(scope.row)"
                    ></el-button>
                  </template>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </el-collapse>
      </el-col>
    </el-row>
    <el-dialog title="警告" :visible.sync="confirmDialogVisible" width="30%">
      <el-tag type="warning" class="el-icon-info">确定进入考试？</el-tag>
      <span slot="footer" class="dialog-footer">
        <el-button @click="confirmDialogVisible = false" size="mini"
          >取 消</el-button
        >
        <el-button
          type="primary"
          :loading="isLoading"
          @click="answerQuestion"
          size="mini"
          >{{ confirmDialogButtonContent }}</el-button
        >
      </span>
    </el-dialog>
    <el-drawer
      title="我是标题"
      :visible.sync="codeDrawerVisible"
      :with-header="false"
      :before-close="beforeDrawerClose"
      :close-on-press-escape="true"
      size="100%"
    >
      <el-row
        :gutter="10"
        style="margin-top: 30px; margin-left: 20px; text-align: left"
      >
        <el-col :span="8">
          <el-link
            type="success"
            :underline="false"
            style="font-size: 20px; font-weight: bold"
            >{{ answerStudent.question.title }}</el-link
          >
          <el-link
            type="success"
            :underline="false"
            style="margin-top: 20px; font-size: 16px; text-align: left"
            >{{ answerStudent.question.main }}</el-link
          >
          <el-tag
            type="warning"
            style="margin-top: 50px; font-size: 20px; font-weight: bold;"
            >考试结束时间{{
              examinationStudent.edate | formatDateAndTime
            }}</el-tag
          >
          <el-row style="margin-top: 20px;" :gutter="0">
            <el-col :span="3"><el-tag type="info">倒计时:</el-tag></el-col>
            <el-col :span="15"
              ><el-tag type="info"
                >{{ hourNum }}h - {{ minuteNum }}m - {{ secondNum }}s
              </el-tag></el-col
            >
          </el-row>
        </el-col>
        <el-col :span="14" :offset="1">
          <el-row>
            <el-col :offset="19" :span="6">
              <el-button
                type="success"
                @click="saveInfo"
                size="small"
                style="margin-bottom: 4px;"
                >点击提交</el-button
              >
            </el-col>
          </el-row>
          <codemirror
            ref="codemirror"
            @saveCodeInfo="saveCodeInfo"
            @saveCodeInfoS="saveCodeInfoS"
          ></codemirror>
        </el-col>
      </el-row>
    </el-drawer>
  </div>
</template>
<script>
import codemirror from "../codemirror/codemirror";
import moment from "../../../node_modules/moment";
export default {
  components: {
    codemirror: codemirror
  },
  data() {
    return {
      now: moment(),
      examination: [],
      formFileData: {
        homeworkID: "",
        major: "",
        grade: "",
        sclass: "",
        classID: ""
      },
      confirmDialogVisible: false,
      confirmDialogButtonContent: "点击进入",
      isLoading: true,
      /* 特定的学生的考试内容存储 */
      examinationStudent: {},
      /* 特定的answer */
      answerStudent: {
        question: {},
        code: "",
        judge: []
      },
      codeDrawerVisible: false,
      examinationID: 0,
      hours: 0,
      minutes: 0,
      seconds: 0,
      days: 0,
      answer: {}
    };
  },
  mounted() {
    this.getMajorGradeClassByStudentId();
    this.getExaminationInfoAndState();
    /* 定时更新当前时间 */
  },
  methods: {
    timeDown() {
      console.log(this.examinationStudent.edate);
      var timeSetInterval = window.setInterval(() => {
        if (moment(this.examinationStudent.edate).isBefore(moment())) {
          this.$message({
            type: "info",
            message: "考试时间结束，你的考试内容已被提交"
          });
          this.saveInfo1();
          window.clearInterval(timeSetInterval);
        }
        let dur = moment.duration(
          moment(this.examinationStudent.edate) - moment(),
          "ms"
        );
        this.days = dur.get("days");
        this.hours = dur.get("hours");
        this.minutes = dur.get("minutes");
        this.seconds = dur.get("seconds");
      }, 1000);
    },
    countDown(sdate, edate, state) {
      if (state === 1) {
        return "已提交";
      }
      let m2 = moment(sdate);
      let m3 = moment(edate);

      let m1 = this.now;
      var du = moment.duration(m2 - m1, "ms"),
        hours = du.get("hours"),
        mins = du.get("minutes"),
        ss = du.get("seconds");
      if (moment(m3).isBefore(m1)) {
        return "考试已结束";
      }
      if (hours <= 0 && mins <= 0 && ss <= 0) {
        return "";
      } else {
        return "考试未开始";
      }
    },
    saveCodeInfo(item) {
      var _this = this;
      this.answerStudent.code = item;
      this.$confirm("确认提交吗？提交后将不可再次修改，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancleButtonText: "取消",
        type: "warning",
        center: "true"
      })
        .then(() => {
          var answerStudent = JSON.stringify(this.answerStudent);
          this.postRequest("/student/submitCode", {
            answerStudent: answerStudent,
            studentID: _this.$store.state.user.user_id,
            examinationID: _this.examinationID
          }).then(resp => {
            this.codeDrawerVisible = false;
            this.countDown(
              this.examinationStudent.sdate,
              this.examinationStudent.edate
            );
          });
        })
        .catch(() => {
          this.$message({ type: "info", message: "已取消删除" });
        });
    },
    saveCodeInfoS(item) {
      var _this = this;
      this.answerStudent.code = item;
      var answerStudent = JSON.stringify(this.answerStudent);
      this.postRequest("/student/submitCode", {
        answerStudent: answerStudent,
        studentID: _this.$store.state.user.user_id,
        examinationID: _this.examinationID
      }).then(resp => {
        this.codeDrawerVisible = false;
        this.countDown(
          this.examinationStudent.sdate,
          this.examinationStudent.edate
        );
      });
    },

    //触发子组件的函数
    saveInfo() {
      this.$refs.codemirror.showItem();
    },
    saveInfo1() {
      this.$refs.codemirror.showItem1();
    },

    beforeDrawerClose() {
      var _this = this;
      this.$confirm(
        "你还没有提交代码，如果此时关闭，代码将直接提交并且无法再次进行参考，确认关闭吗？",
        "提示",
        {
          confirmButtonText: "确定",
          cancleButtonText: "取消",
          type: "warning",
          center: "true"
        }
      )
        .then(() => {
          _this.saveInfo1();
          this.codeDrawerVisible = false;
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    answerQuestion() {
      var examination_main = JSON.parse(
        this.examinationStudent.examination_main
      );
      var i = parseInt(examination_main.length * Math.random());
      if (this.answer != null) {
        this.answerStudent.question = this.answer.question;
      } else {
        this.answerStudent.question = examination_main[i];
        var answerStudent = JSON.stringify(this.answerStudent);
        var _this = this;
        this.postRequest("/student/submitCode", {
          answerStudent: answerStudent,
          studentID: _this.$store.state.user.user_id,
          examinationID: _this.examinationID
        }).then(() => {});
      }
      this.confirmDialogVisible = false;
      this.codeDrawerVisible = true;
      this.timeDown();
    },
    showMessage(examination) {
      //展示内容
      this.examinationID = examination.id;
      this.examinationStudent = examination;
      this.answer = examination.answer;
      console.log(this.answer);
      this.confirmDialogVisible = true;
      this.isLoading = true;
      /* 设定倒计时的时间 */
      var total = 0;
      let clock = window.setInterval(() => {
        total--;
        this.confirmDialogButtonContent = total + "s后准备就绪";
        if (total < 0) {
          window.clearInterval(clock);
          this.confirmDialogButtonContent = "点击进入";
          this.isLoading = false;
        }
      }, 1000);
    },
    getMajorGradeClassByStudentId() {
      /* 获取学生的年级和班级 */
      var _this = this;
      this.getRequest(
        "/student/getmgcbystudentid?studentID=" + this.$store.state.user.user_id
      ).then(resp => {
        if (resp.status == 200) {
          console.log(resp.data);
          _this.formFileData.major = resp.data.studentMajor.label;
          _this.formFileData.grade = resp.data.studentMajor.children[0].label;
          _this.formFileData.sclass =
            resp.data.studentMajor.children[0].children[0].label;
          _this.formFileData.classID =
            resp.data.studentMajor.children[0].children[0].value;
        }
      });
    },
    getExaminationInfoAndState() {
      var _this = this;
      this.getRequest(
        "/student/getexaminationinfoandstate?studentID=" +
          this.$store.state.user.user_id +
          "&courseID=" +
          this.$route.query.courseID
      ).then(resp => {
        var data = resp.data;
        _this.examination = data;
        for (var i = 0; i < _this.examination.length; i++) {
          _this.examination[i].answer = JSON.parse(_this.examination[i].answer);
        }
        console.log(_this.examination);
      });
    },
    canUpload(sdate, edate, state) {
      if (state === 1) {
        return false;
      }
      var d = new Date();
      d = this.formatDateAndTime(d);
      sdate = this.formatDateAndTime(sdate);
      edate = this.formatDateAndTime(edate);
      if (d > sdate && d < edate) {
        return true;
      }
      return false;
    },
    PrefixInteger(num, n) {
      return (Array(n).join(0) + num).slice(-n);
    },
    ifHaveUpload(state) {
      if (state === 1) {
        return false;
      } else {
        return true;
      }
    }
  },
  computed: {
    dayNum() {
      if (this.days < 10) return "0" + this.days;
      return this.days;
    },

    hourNum() {
      if (this.hours < 10) return "0" + this.hours;
      return this.hours;
    },
    minuteNum() {
      if (this.minutes < 10) return "0" + this.minutes;
      return this.minutes;
    },
    secondNum() {
      if (this.seconds < 10) return "0" + this.seconds;
      return this.seconds;
    }
  }
};
</script>
<style>
.onlineTest {
  margin-top: 40px;
}
</style>
