import Vue from 'vue'
Vue.filter("formatDate", formatDate);
Vue.filter("formatDateAndTime", formatDateAndTime)
Vue.prototype.formatDate = formatDate;
Vue.prototype.formatDateAndTime = formatDateAndTime
Vue.prototype.debounce = debounce

function formatDate(value) {
  var date = new Date(value);
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  if (month < 10) {
    month = "0" + month;
  }
  if (day < 10) {
    day = "0" + day;
  }
  return year + "-" + month + "-" + day;
}

function formatDateAndTime(value) {
  var date = new Date(value);
  var year = date.getFullYear();
  var month = date.getMonth() + 1;
  var day = date.getDate();
  var hour = date.getHours();
  var minute = date.getMinutes();
  var second = date.getSeconds();
  if (month < 10) {
    month = "0" + month;
  }
  if (day < 10) {
    day = "0" + day;
  }
  if (hour < 10) {
    hour = "0" + hour;
  }
  if (minute < 10) {
    minute = "0" + minute;
  }
  if (second < 10) {
    second = "0" + second;
  }
  return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
}

/* 函数防抖,并不好用 */
function debounce(func, wait) {
  var _this = this
  return function (...args) {
    if (_this.timer) clearTimeout(_this.timer)
    _this.timer = setTimeout(() => {
      func.apply(_this, args)
    }, wait)
  }
}
