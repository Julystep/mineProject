<template>
  <div style="text-align: center">
    <codemirror
      v-model="item"
      :options="cmOption"
      class="code-mirror"
      ref="myCmGenerate"
    ></codemirror>
    <el-input
      type="textarea"
      :rows="2"
      placeholder="输入参数（如果没有就不用输入）"
      style="margin-top: 20px"
      v-model="param"
    >
    </el-input>
    <el-row style="margin-top: 20px">
      <el-button type="success" round @click="submitInfo">点击运行</el-button>
      <el-button type="primary" round @click="downloadcode"
        >点击下载源码</el-button
      >
    </el-row>
    <div class="outputDiv">
      <template v-if="type === 'success'">
        <span v-html="runResult"></span>
      </template>
      <template v-if="type === 'warn'">
        <span style="color: yellow" v-html="runResult"></span>
      </template>
      <template v-if="type === 'error'">
        <span style="color: red" v-html="runResult"></span>
      </template>
    </div>
  </div>
</template>
<script>
import "codemirror/mode/javascript/javascript.js";
import "./setting.js";

export default {
  props: ["code"],
  data() {
    return {
      item: "",
      runResult: "",
      type: "",
      param: "",
      cmOption: {
        mode: "javascript", //编辑器语言
        theme: "monokai", //编辑器主题
        extraKeys: {
          Ctrl: "autocomplete" //ctrl可以弹出选择项
        },
        lineNumbers: true, //显示行号
        tabSize: 6, //tab空格的行号
        Autofocus: true,
        styleActiveLine: true
      },
      methods: {}
    };
  },
  methods: {
    submitInfo() {
      var _this = this;
      this.postRequest("/submit/code", {
        code: _this.item,
        param: _this.param
      }).then(resp => {
        _this.runResult = resp.data.runResult;
        _this.type = resp.data.type;
      });
    },
    showItem() {
      this.$emit("saveCodeInfo", this.item);
    },
    showItem1() {
      this.$emit("saveCodeInfoS", this.item);
    },
    getcode(code) {
      this.item = code;
    },
    downloadcode() {
      var _this = this;
      this.downloadRequest("/downloadcode?", {
        code: _this.item
      }).then(res => {
        console.log(res);
        let blob = new Blob([res.data], { type: "java/java" });
        let link = document.createElement("a");
        link.href = window.URL.createObjectURL(blob);
        link.download = "java文件";
        link.click();
        link.remove();
      });
    }
  },
  mounted() {
    this.$emit("savecode");
  }
};
</script>
<style scoped>
.code-mirror {
  font-size: 13px;
  line-height: 150%;
  text-align: left;
}
.outputDiv {
  border: 1px solid #dcdfe6;
  background-color: #e6e6fa;
  width: 100%;
  margin-top: 20px;
  padding: 20px;
  box-sizing: border-box;
  text-align: left;
}
</style>
