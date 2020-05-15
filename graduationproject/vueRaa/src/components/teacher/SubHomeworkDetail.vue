<template>
  <div class="form-div" id="homeworkDetailPage">
    <el-row :gutter="20">
      <el-col :xs="20" :sm="23" :md="20" :lg="16" :xl="12" :offset="1"
        ><el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="作业名称" prop="title">
            <el-input v-model="form.title"></el-input>
          </el-form-item>
          <el-form-item label="作业内容" prop="main">
            <quill-editor
              ref="text"
              v-model="form.main"
              class="myQuillEditor"
              :options="editorOption"
            />
          </el-form-item>
          <el-form-item label="选择时间" prop="dateValue">
            <el-date-picker
              class="datepicker"
              v-model="form.dateValue"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :default-time="['12:00:00', '12:00:00']"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="选择班级" prop="classValue">
            <el-cascader
              v-model="form.classValue"
              :props="props"
              :options="options"
              clearable
              class="cascader1"
            ></el-cascader>
          </el-form-item>
          <el-form-item
            label="作业总成绩"
            prop="totalScores"
            style="width: 80%, margin-left: 200px;"
            size="small"
          >
            <el-input v-model="form.totalScores"></el-input>
          </el-form-item>
          <el-form-item
            v-for="(judgeRule, index) in form.judgeRules"
            :label="'评分标准' + index"
            :key="judgeRule.key"
            :rules="{
              required: true,
              message: '评分标准不能为空',
              trigger: 'blur'
            }"
          >
            <el-input
              v-model="judgeRule.name"
              size="mini"
              style="width: 30%;"
              placeholder="请输入评分项名"
            ></el-input>
            <el-input
              v-model="judgeRule.value"
              size="mini"
              style="width: 30%; margin-left: 3px;"
              placeholder="请输入评分项分数"
            >
            </el-input>
            <el-button @click="addJudgeRule()" size="small">新增标准</el-button>
            <el-button
              @click.prevent="removeJudgementRule(judgeRule)"
              size="mini"
              >删除</el-button
            >
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              @click="onSubmit('form')"
              v-loading.fullscreen.lock="fullscreenLoading"
              >立即创建</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
export default {
  components: {
    quillEditor
  },
  data() {
    return {
      editorOption: {
        modules: {
          toolbar: ["bold", "italic", "underline", "strike"]
        }
      },
      form: {
        title: "",
        main: "",
        dateValue: "",
        classValue: "",
        teacherID: this.$store.state.user.user_id,
        courseID: this.$route.query.courseID,
        judgeRules: [
          {
            name: "",
            value: ""
          }
        ],
        totalScores: ""
      },
      rules: {
        title: [
          {
            required: true,
            message: "请输入作业题目",
            trigger: "blur"
          }
        ],
        main: [
          {
            required: true,
            message: "请输入作业内容",
            trigger: "blur"
          }
        ],
        dateValue: [
          {
            required: true,
            message: "请选择时间",
            trigger: "blur"
          }
        ],

        classValue: [
          {
            required: true,
            message: "请选择班级",
            trigger: "blur"
          }
        ],
        totalScores: [
          {
            required: true,
            message: "请输入总分",
            trigger: "blur"
          }
        ]
      },
      data: [],
      options: [],
      props: {
        multiple: true
      },
      fullscreenLoading: false
    };
  },
  mounted() {
    this.getClassesByCourseId();
  },
  methods: {
    removeJudgementRule(item) {
      var index = this.form.judgeRules.indexOf(item);
      if (index === 0) {
        this.$message({
          type: "error",
          message: "最后一个标准不可被删除"
        });
        return;
      }
      if (index !== -1) {
        this.form.judgeRules.splice(index, 1);
      }
    },

    addJudgeRule() {
      this.form.judgeRules.push({ name: "", value: "", key: Date.now() });
    },
    /**
    获取学习这门课程的班级信息
     */
    getClassesByCourseId() {
      var _this = this;
      this.getRequest(
        "/teacher/getclassesbycourseid2?courseID=" + this.$route.query.courseID
      ).then(resp => {
        if (resp.status == 200) {
          _this.options = resp.data.list;
        }
      });
    },
    /**
    提交表单中的作业的内容
     */
    onSubmit(form) {
      var _this = this;
      var totalScores = 0;
      var map = new Map();
      this.$refs[form].validate(valid => {
        if (!valid) {
          return false;
        } else {
          /**
          将评分标准打包成json
           */
          for (var i = 0; i < _this.form.judgeRules.length; i++) {
            if (parseInt(_this.form.judgeRules[i].value) <= 0) {
              _this.$message({ type: "error", message: "评分项标准不能为0" });
              return;
            }
            console.log(typeof map.get(_this.form.judgeRules[i].name));
            if (typeof map.get(_this.form.judgeRules[i].name) == "undefined") {
              map.set(
                _this.form.judgeRules[i].name,
                _this.form.judgeRules[i].value
              );
            } else {
              _this.$message({
                type: "error",
                message: "评分项不能有相同的名称"
              });
              return;
            }
            totalScores += parseInt(_this.form.judgeRules[i].value);
            _this.form.judgeRules[i].value = parseInt(
              _this.form.judgeRules[i].value
            );
          }

          if (totalScores !== parseInt(_this.form.totalScores)) {
            _this.$message({
              type: "error",
              message: "分项成绩之和与总成绩不相等"
            });
            return;
          }

          form = JSON.stringify(_this.form);
          console.log(form);
          _this.fullscreenLoading = true;
          _this
            .postRequest("/teacher/subhomework", { form: form })
            .then(resp => {
              if (resp.status == 200) {
                _this.fullscreenLoading = false;
                this.$router.push({
                  name: "查看作业中心",
                  query: {
                    userID: _this.$store.state.user.user_id,
                    courseID: _this.$route.query.courseID
                  }
                });
              }
            });
        }
      });
    }
  }
};
</script>
<style>
.form-div {
  margin-top: 80px;
}

.el-transfer__buttons {
  padding: 0 10px;
}
.el-transfer__button {
  padding: 10px;
  border-radius: 20px;
}
.el-transfer-panel {
  width: 40%;
}
.cascader1 {
  width: 100%;
}
.el-tag__close.el-icon-close {
  display: none;
}
.el-range-editor.el-input__inner {
  width: 100%;
}
</style>
