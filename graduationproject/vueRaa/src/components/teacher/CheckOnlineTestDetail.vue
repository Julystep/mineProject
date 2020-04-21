<template>
  <div class="CheckOnlineTestDetail">
    <el-collapse accordion v-model="activeName" @change="getChange(activeName)">
      <el-collapse-item
        v-for="(item, index1) in onlinetest"
        :key="index1"
        :title="item.examination_title"
        :name="index1"
      >
        <el-row>
          <el-col :span="5">
            <el-select
              v-model="classValue"
              multiple
              collapse-tags
              clearable
              placeholder="请选择班级"
              style="width: 100%"
              @focus="getSelectFocus(item.children1)"
              size="mini"
            >
              <el-option
                v-for="item1 in options"
                :key="item1.value"
                :label="item1.major_name + item1.grade_name + item1.class_name"
                :value="item1.class_id"
              >
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="3">
            <el-input
              v-model="studentInfo"
              placeholder="输入姓名/学号"
              size="mini"
            ></el-input>
          </el-col>
          <el-col :span="7">
            <el-checkbox v-model="stateChecked">是否提交</el-checkbox>
            <el-checkbox v-model="commentChecked">是否评阅</el-checkbox>
            <el-checkbox v-model="allChecked">全选</el-checkbox>
          </el-col>
          <el-col :span="1">
            <el-button
              type="success"
              size="mini"
              @click="getonlinetestStudentInfo"
              class="el-icon-search"
            ></el-button>
          </el-col>
          <el-col :span="8">
            <el-tag :type="getType(item.sdate, item.edate)" style="height: 50%">
              {{ item.sdate | formatDateAndTime }} /
              {{ item.edate | formatDateAndTime }}
            </el-tag>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px">
          <el-col :span="3">
            <el-tag type="success" style="width: 100%" size="small">
              已提交人数: {{ submitCount }}
            </el-tag>
          </el-col>
          <el-col :span="3">
            <el-tag type="warning" style="width: 100%" size="small">
              未评阅人数: {{ noCheckedCount }}
            </el-tag>
          </el-col>
          <el-col :span="3">
            <el-tag type="danger" style="width: 100%" size="small">
              未提交人数: {{ noSubmitCount }}
            </el-tag>
          </el-col>
          <el-col :span="8" :offset="7">
            <el-button-group>
              <el-button
                class="el-icon-sort-up"
                type="primary"
                size="mini"
                @click="exportInfo(item.children1)"
                >导出考试结果</el-button
              >
              <el-button
                class="el-icon-mobile"
                type="warning"
                size="mini"
                @click="changeOnlineTestTime()"
                :loading="isloading"
                >修改考试时间</el-button
              >
              <el-button
                class="el-icon-delete"
                type="danger"
                size="mini"
                @click="deleteOnlineTest()"
                :loading="isloading"
                >删除考试信息</el-button
              >
            </el-button-group>
          </el-col>
        </el-row>
        <div class="CheckHomeDetail-table">
          <el-table
            border
            style="width: 100%"
            :data="onlinetestStudentInfo"
            size="mini"
            type="index"
          >
            <el-table-column label="班级" fixed width="250px">
              <template slot-scope="scope">{{
                scope.row.major_name +
                  scope.row.grade_name +
                  scope.row.class_name
              }}</template>
            </el-table-column>
            <el-table-column
              prop="student_id"
              label="学生id"
              fixed
              width="100px"
            >
            </el-table-column>
            <el-table-column
              prop="student_name"
              label="学生姓名"
              fixed
              width="100px"
            >
            </el-table-column>
            <el-table-column type="expand" label="详细评分" width="100px">
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
            <el-table-column label="评语" width="100px">
              <template slot-scope="scope">
                <template v-if="scope.row.iscomment">
                  <el-button
                    type="success"
                    size="mini"
                    @click="
                      showCommentDialog(scope.row.comment, scope.row.student_id)
                    "
                    >显示评语</el-button
                  >
                </template>
                <template v-if="!scope.row.iscomment">
                  <el-button type="success" size="mini" disabled
                    >显示评语</el-button
                  >
                </template>
              </template>
            </el-table-column>
            <el-table-column label="状态">
              <template slot-scope="scope">
                <template
                  v-if="
                    scope.row.state === true && scope.row.iscomment === true
                  "
                >
                  <el-tag type="success" disable-transitions
                    >已提交已评阅</el-tag
                  >
                </template>
                <template
                  v-if="
                    scope.row.state === true && scope.row.iscomment === false
                  "
                >
                  <el-tag type="warning" disable-transitions
                    >已提交未评阅</el-tag
                  >
                </template>
                <template v-if="scope.row.state === false">
                  <el-tag type="danger" disable-transitions>未提交</el-tag>
                </template>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template slot-scope="scope">
                <el-tooltip
                  class="item"
                  effect="dark"
                  content="检查"
                  placement="top"
                >
                  <template v-if="scope.row.state === true">
                    <el-button
                      icon="el-icon-folder-opened"
                      circle
                      size="mini"
                      type="primary"
                      @click="
                        showFileOnDrawer(
                          scope.row.answer,
                          scope.row.student_id,
                          scope.$index
                        )
                      "
                    ></el-button>
                  </template>
                  <template v-if="scope.row.state === false">
                    <el-button
                      icon="el-icon-folder-opened"
                      circle
                      size="mini"
                      disabled
                      type="primary"
                    ></el-button>
                  </template>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 25px;">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="studentsCount"
              :currentPage="currentPage"
              @current-change="currentChange"
            >
            </el-pagination>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>
    <!--导出考试成绩-->
    <el-dialog :visible.sync="exportDialog" :close-on-click-modal="false">
      <el-select
        v-model="classValue"
        multiple
        clearable
        placeholder="请选择班级"
        style="width: 100%"
        @focus="getSelectFocus(item.children1)"
        size="mini"
      >
        <el-option
          v-for="item1 in options"
          :key="item1.value"
          :label="item1.major_name + item1.grade_name + item1.class_name"
          :value="item1.class_id"
        >
        </el-option>
      </el-select>
      <el-button
        size="mini"
        @click="exportExcel"
        type="success"
        style="margin-top: 10px"
        :loading="isloading"
        >导出信息</el-button
      >
    </el-dialog>
    <el-dialog
      :visible.sync="changeOnlineTestTimeDialog"
      :close-on-click-modal="false"
    >
      <div class="block">
        <el-date-picker
          v-model="dateValue"
          type="datetimerange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['12:00:00', '12:00:00']"
        >
        </el-date-picker>
      </div>
      <el-button
        size="mini"
        @click="sumbitDate"
        type="success"
        style="margin-top: 10px"
        >修改日期</el-button
      >
    </el-dialog>
    <el-drawer
      title="在线浏览"
      :visible.sync="drawer"
      direction="ttb"
      :wrapperClosable="false"
      size="100%"
    >
      <template v-if="onlinetestStudentInfo.length !== 0">
        <div style="margin-bottom: 10px; margin-right: 400px">
          <el-tag type="info"
            >班级：{{
              onlinetestStudentInfo[studentIndex].major_name +
                onlinetestStudentInfo[studentIndex].grade_name +
                onlinetestStudentInfo[studentIndex].class_name
            }}</el-tag
          >
          <el-tag type="warning"
            >学号： {{ onlinetestStudentInfo[studentIndex].student_id }}</el-tag
          >
          <el-tag type="success"
            >姓名：
            {{ onlinetestStudentInfo[studentIndex].student_name }}</el-tag
          >
        </div>
      </template>
      <el-form
        :ref="scoresForm"
        :model="scoresForm"
        label-width="100px"
        size="small"
        style="width: 20%; position: fixed; margin-left: 75%"
      >
        <el-button-group style="margin-bottom: 10px">
          <el-button
            type="primary"
            icon="el-icon-arrow-left"
            size="mini"
            @click="getPrevious"
            >上一页</el-button
          >
          <el-button type="primary" size="mini" @click="getNext"
            >下一页<i class="el-icon-arrow-right el-icon--right"></i
          ></el-button>
        </el-button-group>
        <template>
          <el-form-item
            v-for="(item5, index) in scoresForm.judgeStudent"
            :label="item5.name"
            :key="item5.key"
          >
            <el-slider
              v-model="item5.value"
              show-input
              :max="parseInt(returnMax(index))"
              @change="getAllSocres()"
            ></el-slider>
          </el-form-item>
        </template>
        <el-tag type="info" style="width: 100%">总成绩: {{ allScores }}</el-tag>
        <el-select
          v-model="commentValue"
          filterable
          allow-create
          clearable
          default-first-option
          placeholder="请选择或填写教师评语"
          style="width: 100%; margin-top: 7px; "
        >
          <el-option
            v-for="item1 in commentOptions"
            :key="item1.value"
            :label="item1.label"
            :value="item1.value"
          >
          </el-option>
        </el-select>

        <el-form-item size="mini">
          <el-button
            type="primary"
            @click="onSubmitSocres"
            style="margin-top: 10px; margin-right:80px"
            >立即提交</el-button
          >
        </el-form-item>
      </el-form>
      <codemirror
        class="code"
        ref="codemirror"
        @savecode="sendCode(answer)"
      ></codemirror>
    </el-drawer>
    <el-dialog :visible.sync="commentDialog">
      <template v-if="this.commentFlag === false">
        <el-tag type="info" :closable="false" style="margin-bottom:10px">
          {{ commentDialogContent }}
        </el-tag>
      </template>
      <template v-if="this.commentFlag === true">
        <el-input
          size="mini"
          v-model="commentDialogContent"
          type="textarea"
          style="margin-bottom:10px"
        />
      </template>
      <template v-if="this.commentFlag === false">
        <el-button
          type="success"
          size="mini"
          style="display: block; margin: auto"
          @click="commentFlag = true"
          >修改评语</el-button
        >
      </template>
      <template v-if="this.commentFlag === true">
        <el-button
          type="success"
          size="mini"
          style="display: block; margin: auto"
          @click="changeComment"
          >提交</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>
<script>
import codemirror from "../codemirror/codemirror";

export default {
  components: { codemirror: codemirror },
  data() {
    return {
      item: "",
      allScores: "",
      commentValue: "",
      onlinetest: "",
      onlinetestStudentInfo: [],
      studentsCount: 0,
      activeName: "",
      allChecked: true,
      stateChecked: true,
      commentChecked: true,
      classValue: [],
      options: [],
      dateValue: "",
      studentInfo: "",
      currentPage: 1,
      submitCount: 0,
      noCheckedCount: 0,
      noSubmitCount: 0,
      exportDialog: false,
      changeOnlineTestTimeDialog: false,
      confirmButtonLoading: false,
      isloading: false,
      studentIndex: 0,
      commentFlag: false,
      commentDialog: false,
      commentDialogContent: "",
      drawer: false,
      studentID: "",
      scoresForm: { judgeStudent: [] },
      commentOptions: [
        { value: "考试不错，继续努力", label: "考试不错，继续努力" },
        {
          value: "考试完成度不高，需要修改",
          label: "考试完成度不高，需要修改"
        },
        {
          value: "考试不合格，重写！！！！",
          label: "考试不合格，重写！！！！"
        }
      ],
      answerRules: [],
      answer: {}
    };
  },
  mounted() {
    this.getOnlineTestInfoByTeacherIdAndCourseId();
  },
  methods: {
    returnMax(index) {
      console.log(this.answerRules[index].value);
      return this.answerRules[index].value;
    },
    getOnlineTestInfoByTeacherIdAndCourseId() {
      /* 获取该教师在本门课程中发布的内容 */
      var _this = this;
      this.getRequest(
        "/teacher/getonlinetestinfobyteacheridandcourseid?teacherID=" +
          this.$store.state.user.user_id +
          "&courseID=" +
          this.$route.query.courseID
      ).then(resp => {
        _this.onlinetest = resp.data;
        console.log(resp.data);
      });
    },
    getChange(activeName) {
      /* 切换一次折叠框就会改变一次 */

      if (activeName === null || activeName === "") {
        return;
      }
      this.currentPage = 1;
      this.allChecked = true;
      this.stateChecked = false;
      this.commentChecked = false;
      this.item = this.onlinetest[activeName];
      this.getonlinetestStudentInfo();
    },
    getonlinetestStudentInfo() {
      var _this = this;
      this.getOnlineTestDetailCount(this.item.id, this.classValue);
      var item = JSON.stringify(this.item);
      this.postRequest("/teacher/getSubmitonlinetestStudent", {
        onlinetest: item,
        classValue: _this.classValue,
        studentInfo: _this.studentInfo,
        stateChecked: _this.stateChecked,
        commentChecked: _this.commentChecked,
        allChecked: this.allChecked,
        page: _this.currentPage,
        size: 20
      }).then(resp => {
        _this.onlinetestStudentInfo = resp.data.classStudents;
        _this.studentsCount = resp.data.studentsCount;
        for (var i = 0; i < _this.onlinetestStudentInfo.length; i++) {
          _this.onlinetestStudentInfo[i].answer = JSON.parse(
            _this.onlinetestStudentInfo[i].answer
          );
        }
        console.log(_this.onlinetestStudentInfo);
      });
    },
    getOnlineTestDetailCount(onlinetestID, classID) {
      var _this = this;
      this.postRequest("/teacher/getonlinetestdetailcount", {
        onlinetestID: onlinetestID,
        classValue: classID
      }).then(resp => {
        _this.submitCount = resp.data.submitCount;
        _this.noCheckedCount = resp.data.noCheckedCount;
        _this.noSubmitCount = resp.data.noSubmitCount;
      });
    },
    getSelectFocus(children) {
      /* 获取班级选项 */
      this.options = [];
      for (var i = 0; i < children.length; i++) {
        this.options.push(children[i]);
      }
    },
    getType(sdate, edate) {
      /* 获取考试到期日期 */
      var d = new Date();
      d = this.formatDateAndTime(d);
      sdate = this.formatDateAndTime(sdate);
      edate = this.formatDateAndTime(edate);
      if (d > sdate && d < edate) {
        return "success";
      }
      return "danger";
    },
    exportInfo(children) {
      /* 显示导出学生信息的页面 */
      this.exportDialog = true;
      this.getSelectFocus(children);
    },
    exportExcel() {
      /* 导出学生的考试信息 */
      this.isloading = true;
      window.open(
        "/teacher/exportStudentTest?onlinetestID=" +
          this.item.id +
          "&classValue=" +
          this.classValue,
        "_parent"
      );
      this.isloading = false;
      this.exportDialog = false;
    },
    changeOnlineTestTime() {
      /* 修改考试时间 */
      this.changeOnlineTestTimeDialog = true;
    },
    sumbitDate() {
      /* 提交修改的日期 */

      var _this = this;
      this.postRequest("/teacher/changeonlinetestdate", {
        onlinetestID: _this.item.id,
        dateValue: _this.dateValue
      }).then(resp => {
        _this.getOnlineTestStudentInfo();
        _this.changeOnlineTestTimeDialog = false;
      });
    },
    deleteOnlineTest() {
      /* 删除作业所有的信息 */

      var _this = this;
      this.$confirm("此操作将会永久删除作业，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        beforeClose: (action, instance, done) => {
          if (action === "confirm") {
            instance.confirmButtonLoading = true;
            instance.confirmButtonText = "执行中...";
            _this
              .postRequest("/teacher/deleteonlineTest", {
                onlinetestID: _this.item.id
              })
              .then(resp => {
                instance.confirmButtonLoading = false;
                done();
              });
          } else {
            done();
          }
        }
      });
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.getonlinetestStudentInfo();
    },
    /* 这里出现了问题 */
    showFileOnDrawer(answer, studentID, index) {
      this.answer = answer;
      if (answer == null || answer == "") {
        {
          this.$message({ type: "warn", message: "该生未参与考试" });
          this.studentIndex = index;
          return;
        }
      }
      this.drawer = true;
      this.studentID = studentID;
      if (answer.judge == !answer.judge) {
        for (var i = 0; i < answer.question.judgeRules.length; i++) {
          answer.question.judgeRules[i].value = parseInt(
            answer.question.judgeRules[i].value
          );
        }

        this.scoresForm.judgeStudent = JSON.parse(
          JSON.stringify(answer.question.judgeRules)
        );
      } else {
        for (var i = 0; i < answer.judge.length; i++) {
          answer.judge[i].value = parseInt(answer.judge[i].value);
          answer.question.judgeRules[i].value = parseInt(
            answer.question.judgeRules[i].value
          );
        }
        this.scoresForm.judgeStudent = JSON.parse(JSON.stringify(answer.judge));
      }
      this.answerRules = JSON.parse(JSON.stringify(answer.question.judgeRules));
      this.studentIndex = index;
    },
    getPrevious() {
      if (this.studentIndex === 0) {
        this.$message({ type: "warn", message: "已经是本页第一名" });
        return;
      }
      if (this.studentIndex > 0) {
        /* 操作学生成绩时上一名学生 */
        this.studentIndex -= 1;
        if (this.onlinetestStudentInfo[this.studentIndex].answer != null) {
          this.$refs.codemirror.getcode(
            this.onlinetestStudentInfo[this.studentIndex].answer.code
          );
        } else {
          this.$refs.codemirror.getcode("");
        }
        this.showFileOnDrawer(
          this.onlinetestStudentInfo[this.studentIndex].answer,
          this.onlinetestStudentInfo[this.studentIndex].student_id,
          this.studentIndex
        );
      }
    },
    getNext() {
      /* 操作学生成绩时下一名学生 */
      if (this.studentIndex === this.onlinetestStudentInfo.length - 1) {
        this.$message({ type: "warn", message: "已经是本页最后一名， 请换页" });
        return;
      }
      if (this.studentIndex < this.onlinetestStudentInfo.length) {
        this.studentIndex += 1;
        if (this.onlinetestStudentInfo[this.studentIndex].answer != null) {
          this.$refs.codemirror.getcode(
            this.onlinetestStudentInfo[this.studentIndex].answer.code
          );
        } else {
          this.$refs.codemirror.getcode("");
        }

        this.showFileOnDrawer(
          this.onlinetestStudentInfo[this.studentIndex].answer,
          this.onlinetestStudentInfo[this.studentIndex].student_id,
          this.studentIndex
        );
      }
    },
    onSubmitSocres() {
      /* 提交学生修改后的信息 */
      var _this = this;
      this.answer.judge = this.scoresForm.judgeStudent;
      var answer = JSON.stringify(this.answer);
      console.log(answer);
      /* this.postRequest("/teacher/changestudentsexaminationcores", {
        answer: answer,
        onlinetestID: _this.item.id,
        studentID: _this.studentID,
        commentValue: _this.commentValue
      }).then(resp => {
        _this.getonlinetestStudentInfo();
        _this.scoresDialog = false;
      }); */
    },
    getAllSocres() {
      /* 计算某个学生的总成绩 */

      this.allScores = 0;
      for (var i = 0; i < this.scoresForm.judgeStudent.length; i++) {
        this.allScores += this.scoresForm.judgeStudent[i].value;
      }
    },
    sendCode(answer) {
      this.$refs.codemirror.getcode(answer.code);
    },
    showCommentDialog(comment, studentID) {
      /* 显示评阅的内容 */
      this.commentDialog = true;
      this.commentDialogContent = comment;
      this.studentID = studentID;
    },
    changeComment() {
      /* 修改教师相对于学生的评语 */
      var _this = this;
      this.postRequest("/teacher/changeexaminationcomment", {
        studentID: _this.studentID,
        onlinetestID: _this.item.id,
        main: _this.commentDialogContent
      }).then(resp => {
        _this.getonlinetestStudentInfo();
        _this.commentDialog = false;
      });
    }
  }
};
</script>
<style>
.CheckOnlineTestDetail {
  width: 90%;
  margin: auto;
  margin-top: 20px;
}
.el-cascader.el-cascader--medium {
  width: 100%;
}
.el-tag__close.el-icon-close {
  display: none;
}
.el-swicth2 {
  margin-top: 4px;
}
.CheckHomeDetail-table {
  margin-top: 10px;
}
.cascader1 {
  width: 100%;
}
.search_button {
  margin-top: 7px;
}
.showpdf {
  width: 75%;
  height: 100%;
  margin-right: 25%;
}
.code {
  width: 70%;
  margin-left: 10px;
}
</style>
