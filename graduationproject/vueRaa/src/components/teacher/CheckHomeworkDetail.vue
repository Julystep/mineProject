<template>
  <div class="CheckHomeworkDetail">
    <el-collapse accordion v-model="activeName" @change="getChange(activeName)">
      <el-collapse-item
        v-for="(item, index1) in homework"
        :key="index1"
        :title="item.title"
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
              @click="gethomeworkStudentInfo"
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
                class="el-icon-zoom-in"
                type="success"
                size="mini"
                @click="showMain(item.main)"
                >显示作业内容</el-button
              >
              <el-button
                class="el-icon-sort-up"
                type="primary"
                size="mini"
                @click="exportInfo(item.children1)"
                >导出作业内容</el-button
              >
              <el-button
                class="el-icon-mobile"
                type="warning"
                size="mini"
                @click="changeHomeworkTime()"
                :loading="isloading"
                >修改作业时间</el-button
              >
              <el-button
                class="el-icon-delete"
                type="danger"
                size="mini"
                @click="deleteHomework()"
                :loading="isloading"
                >删除作业信息</el-button
              >
            </el-button-group>
          </el-col>
        </el-row>
        <div class="CheckHomeDetail-table">
          <el-table
            border
            style="width: 100%"
            :data="homeworkStudentInfo"
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
                  <el-button
                    size="mini"
                    style="width:100%"
                    type="success"
                    @click="
                      showSorcesDialog(props.row.judge, props.row.student_id)
                    "
                    >修改评分</el-button
                  >
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
                          scope.row.url,
                          scope.row.judge,
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
    <el-dialog
      :visible.sync="mainDialog"
      :close-on-click-modal="false"
      @close="closemainDialog"
    >
      <template v-if="this.mainFlag === false">
        <main>
          <span style="text-align:left" v-html="mainContent"></span>
        </main>
      </template>
      <template v-if="this.mainFlag === true">
        <quill-editor
          ref="text"
          v-model="mainContent"
          class="myQuillEditor"
          :options="editorOption"
        />
      </template>
      <template v-if="this.mainFlag === false">
        <el-button
          type="success"
          size="mini"
          style="display: block; margin: auto"
          @click="mainFlag = true"
          >修改内容</el-button
        >
      </template>
      <template v-if="this.mainFlag === true">
        <el-button
          type="success"
          size="mini"
          style="display: block; margin: auto"
          @click="changeMainContent"
          >提交</el-button
        >
      </template>
    </el-dialog>
    <el-dialog :visible.sync="scoresDialog" :close-on-click-modal="false">
      <el-form
        :ref="scoresForm"
        :model="scoresForm"
        label-width="100px"
        size="small"
      >
        <template>
          <el-form-item
            v-for="(item1, index) in scoresForm.judgeStudent"
            :label="item1.name"
            :key="item1.key"
          >
            <el-slider
              v-model="item1.value"
              show-input
              :max="judgehomework[index].value"
              @change="getAllSocres()"
            ></el-slider>
          </el-form-item>
        </template>
        <el-tag type="info" style="width: 100%">总成绩: {{ allScores }}</el-tag>
        <el-form-item size="mini">
          <el-button
            type="primary"
            @click="onSubmitSocres"
            style="margin-top: 10px; margin-right:80px"
            >立即修改</el-button
          >
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
      :visible.sync="changeHomeworkTimeDialog"
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
    <el-drawer
      title="在线浏览"
      :visible.sync="drawer"
      direction="ttb"
      :wrapperClosable="false"
      size="100%"
    >
      <template v-if="homeworkStudentInfo.length !== 0">
        <div style="margin-bottom: 10px; margin-right: 400px">
          <el-tag type="info"
            >班级：{{
              homeworkStudentInfo[studentIndex].major_name +
                homeworkStudentInfo[studentIndex].grade_name +
                homeworkStudentInfo[studentIndex].class_name
            }}</el-tag
          >
          <el-tag type="warning"
            >学号： {{ homeworkStudentInfo[studentIndex].student_id }}</el-tag
          >
          <el-tag type="success"
            >姓名： {{ homeworkStudentInfo[studentIndex].student_name }}</el-tag
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
          <template v-if="scoresForm.judgeStudent.length === 0">
            <el-form-item
              v-for="(item1, index) in judgehomework"
              :label="item1.name"
              :key="item1.key"
            >
              <el-slider
                v-model="scoresForm.judgeStudent[index].value"
                show-input
                :max="item1.value"
                @change="getAllSocres()"
              ></el-slider>
            </el-form-item>
          </template>
          <template v-if="scoresForm.judgeStudent.length !== 0">
            <el-form-item
              v-for="(item1, index) in scoresForm.judgeStudent"
              :label="item1.name"
              :key="item1.key"
            >
              <el-slider
                v-model="item1.value"
                show-input
                :max="judgehomework[index].value"
                @change="getAllSocres()"
              ></el-slider>
            </el-form-item>
          </template>
          <el-tag type="info" style="width: 100%"
            >总成绩: {{ allScores }}</el-tag
          >
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
        </template>
      </el-form>

      <iframe class="showpdf" :src="src"> </iframe>
    </el-drawer>
  </div>
</template>
<script>
import pdf from "vue-pdf";
import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
export default {
  components: { pdf, quillEditor },
  inject: ["reload"],
  data() {
    return {
      editorOption: {
        modules: {
          toolbar: ["bold", "italic", "underline", "strike"]
        }
      },
      homework: "",
      props: { multiple: true },
      homeworkStudentInfo: [],
      activeName: "",
      classValue: [],
      options: [],
      item: "",
      count: 0,
      drawer: false,
      src: "",
      commentDialog: false,
      commentDialogContent: "",
      commentFlag: false,
      studentInfo: "",
      submitCount: 0,
      noCheckedCount: 0,
      noSubmitCount: 0,
      stateChecked: true,
      commentChecked: true,
      allChecked: true,
      currentPage: 1,
      studentsCount: 0,
      isloading: false,
      mainDialog: false,
      mainContent: "",
      mainFlag: false,
      studentID: "",
      scoresDialog: false,
      scoresForm: {
        judgeStudent: []
      },
      judgehomework: [],
      scoreAlert: [],
      allScores: 0,
      changeHomeworkTimeDialog: false,
      dateValue: "",
      commentValue: "",
      studentIndex: 0,
      commentOptions: [
        {
          value: "作业不错，继续努力",
          label: "作业不错，继续努力"
        },
        {
          value: "作业完成度不高，需要修改",
          label: "作业完成度不高，需要修改"
        },
        {
          value: "作业不合格，重写！！！！",
          label: "作业不合格，重写！！！！"
        }
      ],
      exportDialog: false
      /* isIndeterminate: true,
      checkAll: false */
    };
  },
  mounted() {
    this.getHomeworkInfoByTeacherIdAndCourseId();
  },
  methods: {
    closemainDialog() {
      this.mainFlag = false;
    },
    exportInfo(children) {
      /* 显示导出学生信息的页面 */
      this.exportDialog = true;
      this.getSelectFocus(children);
    },
    exportExcel() {
      /* 导出学生的作业信息 */

      this.isloading = true;
      window.open(
        "/teacher/exportStudent?homeworkID=" +
          this.item.id +
          "&classValue=" +
          this.classValue,
        "_parent"
      );
      this.isloading = false;
      this.exportDialog = false;
    },
    getPrevious() {
      if (this.studentIndex === 0) {
        this.$message({ type: "warn", message: "已经是本页第一名" });
        return;
      }

      if (this.studentIndex > 0) {
        /* 操作学生成绩时上一名学生 */
        this.studentIndex -= 1;
        this.showFileOnDrawer(
          this.homeworkStudentInfo[this.studentIndex].url,
          this.homeworkStudentInfo[this.studentIndex].judge,
          this.homeworkStudentInfo[this.studentIndex].student_id,
          this.studentIndex
        );
      }
    },
    getNext() {
      /* 操作学生成绩时下一名学生 */
      if (this.studentIndex === this.homeworkStudentInfo.length - 1) {
        this.$message({ type: "warn", message: "已经是本页最后一名， 请换页" });
        return;
      }
      if (this.studentIndex < this.homeworkStudentInfo.length) {
        this.studentIndex += 1;
        this.showFileOnDrawer(
          this.homeworkStudentInfo[this.studentIndex].url,
          this.homeworkStudentInfo[this.studentIndex].judge,
          this.homeworkStudentInfo[this.studentIndex].student_id,
          this.studentIndex
        );
      }
    },

    deleteHomework() {
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
              .postRequest("/teacher/deletehomework", {
                homeworkID: _this.item.id
              })
              .then(resp => {
                _this.reload();
                instance.confirmButtonLoading = false;
                done();
              });
          } else {
            done();
          }
        }
      });
    },
    sumbitDate() {
      /* 提交修改的日期 */
      var _this = this;
      this.postRequest("/teacher/changehomeworkdate", {
        homeworkID: _this.item.id,
        dateValue: _this.dateValue
      }).then(resp => {
        _this.gethomeworkStudentInfo();
        _this.changeHomeworkTimeDialog = false;
      });
    },
    getAllSocres() {
      /* 计算某个学生的总成绩 */
      this.allScores = 0;
      for (var i = 0; i < this.scoresForm.judgeStudent.length; i++) {
        this.allScores += this.scoresForm.judgeStudent[i].value;
      }
    },
    onSubmitSocres() {
      /* 提交学生修改后的信息 */
      var _this = this;
      var j = JSON.stringify(this.scoresForm.judgeStudent);
      this.postRequest("/teacher/changestudentscores", {
        judgeStudent: j,
        homeworkID: _this.item.id,
        studentID: _this.studentID,
        commentValue: _this.commentValue
      }).then(resp => {
        _this.gethomeworkStudentInfo();
        _this.scoresDialog = false;
      });
    },
    showSorcesDialog(judge, studentID) {
      /* 展示修改分数的模态框 */
      this.scoresDialog = true;
      for (var i = 0; i < judge.length; i++) {
        judge[i].value = parseInt(judge[i].value);
      }
      this.scoresForm.judgeStudent = judge;
      this.studentID = studentID;
      this.getAllSocres();
    },
    changeMainContent() {
      /* 改变作业的内容 */
      var _this = this;
      this.postRequest("/teacher/changehoeworkmain", {
        homeworkID: _this.item.id,
        main: _this.mainContent
      }).then(resp => {
        _this.gethomeworkStudentInfo();
        _this.mainDialog = false;
      });
    },
    changeComment() {
      /* 修改教师相对于学生的评语 */
      var _this = this;
      this.postRequest("/teacher/changecomment", {
        studentID: _this.studentID,
        homeworkID: _this.item.id,
        main: _this.commentDialogContent
      }).then(resp => {
        _this.gethomeworkStudentInfo();
        _this.commentDialog = false;
      });
    },
    showCommentDialog(comment, studentID) {
      /* 显示评阅的内容 */
      this.commentDialog = true;
      this.commentDialogContent = comment;
      this.studentID = studentID;
    },
    showFileOnDrawer(url, judge, studentID, index) {
      if (url === null) {
        {
          this.$message({
            type: "warn",
            message: "该生未提交作业"
          });
          this.src = url;
          this.studentIndex = index;
          return;
        }
      }

      url = url.substring(0, url.indexOf(".")) + ".pdf";

      this.src = url;
      this.drawer = true;
      this.studentID = studentID;
      if (judge == null) {
      } else {
        for (var i = 0; i < judge.length; i++) {
          judge[i].value = parseInt(judge[i].value);
        }
        this.scoresForm.judgeStudent = judge;
      }
      this.studentIndex = index;
    },

    getSelectFocus(children) {
      this.options = [];
      for (var i = 0; i < children.length; i++) {
        this.options.push(children[i]);
      }
    },
    /* 获得教师在本门作业的信息 */
    getHomeworkInfoByTeacherIdAndCourseId() {
      var _this = this;
      this.getRequest(
        "/teacher/gethomeworkinfobyteacheridandcourseid?teacherID=" +
          this.$store.state.user.user_id +
          "&courseID=" +
          this.$route.query.courseID
      ).then(resp => {
        _this.homework = resp.data;
      });
    },
    getType(sdate, edate) {
      var d = new Date();
      d = this.formatDateAndTime(d);
      sdate = this.formatDateAndTime(sdate);
      edate = this.formatDateAndTime(edate);
      if (d > sdate && d < edate) {
        return "success";
      }
      return "danger";
    },
    gethomeworkStudentInfo() {
      var _this = this;
      this.getHomeworkDetailCount(this.item.id, this.classValue);
      var item = JSON.stringify(this.item);
      this.postRequest("/teacher/getSubmitStudent", {
        homework: item,
        classValue: _this.classValue,
        studentInfo: _this.studentInfo,
        stateChecked: _this.stateChecked,
        commentChecked: _this.commentChecked,
        allChecked: _this.allChecked,
        page: _this.currentPage,
        size: 20
      }).then(resp => {
        console.log(resp.data);
        if (resp.status == 200) {
          _this.homeworkStudentInfo = resp.data.classStudents;
          console.log(_this.homeworkStudentInfo);
          _this.studentsCount = resp.data.studentsCount;
          for (var i = 0; i < _this.homeworkStudentInfo.length; i++) {
            if (_this.homeworkStudentInfo[i].judge === null) {
              continue;
            } else {
              _this.homeworkStudentInfo[i].judge = JSON.parse(
                _this.homeworkStudentInfo[i].judge
              );
            }
          }
        }
      });
    },
    getChange(activeName) {
      /* 切换一次折叠框就会改变一次 */
      if (activeName === null || activeName === "") {
        return;
      }

      this.allChecked = true;
      this.stateChecked = false;
      this.commentChecked = false;

      this.currentPage = 1;
      this.item = this.homework[activeName];
      this.judgehomework = JSON.parse(this.item.judge);
      this.scoresForm.judgeStudent = JSON.parse(this.item.judge);
      this.gethomeworkStudentInfo();
    },
    getValue() {
      return this.value;
    },
    /* 一整组返回提交信息的内容
      所需要的参数是classID和homeworkID
     */
    getHomeworkDetailCount(homeworkID, classID) {
      var _this = this;
      this.postRequest("/teacher/gethomeworkdetailcount", {
        homeworkID: homeworkID,
        classValue: classID
      }).then(resp => {
        _this.submitCount = resp.data.submitCount;
        _this.noCheckedCount = resp.data.noCheckedCount;
        _this.noSubmitCount = resp.data.noSubmitCount;
      });
    },
    currentChange(currentPage) {
      this.currentPage = currentPage;
      this.gethomeworkStudentInfo();
    },
    showMain(main) {
      /* 显示作业的内容,提供修改的选项 */
      this.mainDialog = true;
      this.mainContent = main;
    },
    changeHomeworkTime() {
      this.changeHomeworkTimeDialog = true;
    }
  }
};
</script>
<style>
.CheckHomeworkDetail {
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
</style>
