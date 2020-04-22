<template>
  <div class="form-div" id="homeworkDetailPage">
    <el-row :gutter="20">
      <el-col :xs="20" :sm="23" :md="20" :lg="16" :xl="12" :offset="1"
        ><el-form ref="form" :model="form" :rules="rules" label-width="100px">
          <el-form-item label="考试名称" prop="title">
            <el-input v-model="form.title" size="mini"></el-input>
          </el-form-item>
          <el-form-item label="选择班级" prop="classValue">
            <el-cascader
              v-model="form.classValue"
              :props="props"
              :options="options"
              clearable
              size="mini"
              class="cascader1"
            ></el-cascader>
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

          <el-form-item
            v-for="(m, index) in form.main"
            :label="'题目' + index"
            :key="m.key"
            :rules="{
              required: true,
              message: '发布考试主体不能为空',
              trigger: 'blur'
            }"
          >
            <el-input
              v-model="m.title"
              size="mini"
              placeholder="请输入题目名称"
            ></el-input>
            <el-input
              v-model="m.main"
              size="mini"
              type="textarea"
              placeholder="请输入题目详述"
            >
            </el-input>
            <el-form-item
              label="总分"
              prop="totalScores"
              style="width: 80%, margin-left: 200px; margin-top:20px"
              size="small"
            >
              <el-input
                v-model="m.totalScores"
                placeholder="输入总分(下列评分标准的分数不可超过总分)"
                size="mini"
              ></el-input>
            </el-form-item>

            <el-form-item
              v-for="(judgeRule, index) in m.judgeRules"
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
                style="width: 30%"
                placeholder="请输入评分项名"
              ></el-input>
              <el-input
                v-model="judgeRule.value"
                size="mini"
                style="width: 30%; margin-left: 3px"
                placeholder="请输入评分项分数"
              >
              </el-input>
              <el-button @click="addJudgeRule(m)" size="small"
                >新增标准</el-button
              >
              <el-button
                @click.prevent="removeJudgementRule(judgeRule, m)"
                size="mini"
                >删除</el-button
              >
            </el-form-item>

            <el-button @click="addExamination()" size="small"
              >添加考试题目</el-button
            >
            <el-button @click.prevent="removeExamination(m)" size="mini"
              >删除考试题目</el-button
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
export default {
  data() {
    return {
      form: {
        title: "",
        teacherID: this.$store.state.user.user_id,
        courseID: this.$route.query.courseID,
        classValue: "",
        main: [
          {
            title: "",
            main: "",
            totalScores: "",
            judgeRules: [
              {
                name: "",
                value: ""
              }
            ]
          }
        ]
      },
      rules: {
        classValue: [
          {
            required: true,
            message: "需要填写班级名称",
            trigger: blur
          }
        ],
        title: [
          {
            required: true,
            message: "需要填写考试题目",
            trigger: blur
          }
        ],
        dateValue: [
          {
            required: true,
            message: "需要填写考试日期",
            trigger: blur
          }
        ]
      },
      fullscreenLoading: false,
      props: {
        multiple: true
      },
      options: []
    };
  },
  mounted() {
    this.getClassesByCourseId();
  },

  methods: {
    onSubmit(form) {
      var _this = this;
      var totalScores = 0;
      var map = new Map();
      this.$refs[form].validate(valid => {
        if (!valid) {
          return false;
        } else {
          form = JSON.stringify(_this.form);
          _this.fullscreenLoading = true;
          _this
            .postRequest("/teacher/subexamination", { form: form })
            .then(resp => {
              if (resp.status == 200) {
                _this.fullscreenLoading = false;
                this.$router.push({
                  name: "查看考试中心",
                  query: {
                    userID: _this.$store.state.user.user_id,
                    courseID: _this.$route.query.courseID
                  }
                });
              }
            });
        }
      });
    },
    removeJudgementRule(item, m) {
      var index = m.judgeRules.indexOf(item);
      if (index === 0) {
        this.$message({ type: "error", message: "最后一个标准不可被删除" });
        return;
      }
      if (index !== -1) {
        m.judgeRules.splice(index, 1);
      }
    },
    addJudgeRule(m) {
      if (m.judgeRules.length === 5) {
        this.$message({
          type: "warning",
          message: "添加的标准最多为5个"
        });
        return;
      }
      m.judgeRules.push({ name: "", value: "", key: Date.now() });
    },

    addExamination() {
      this.form.main.push({
        title: "",
        main: "",
        judgeRules: [{ name: "", value: "" }],
        key: Date.now()
      });
    },
    removeExamination(item) {
      var index = this.form.main.indexOf(item);
      if (index === 0) {
        this.$message({ type: "error", message: "最后一个题目不可被删除" });
        return;
      }
      if (index !== -1) {
        this.form.main.splice(index, 1);
      }
    },
    getClassesByCourseId() {
      var _this = this;
      this.getRequest(
        "/teacher/getclassesbycourseid2?courseID=" + this.$route.query.courseID
      ).then(resp => {
        if (resp.status == 200) {
          _this.options = resp.data.list;
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
