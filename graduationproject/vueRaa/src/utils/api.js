import axios from "axios"
import {
  Message,
  MessageBox
} from "element-ui"
import router from '../router'
axios.interceptors.response.use(data => {
  if (data.status && data.status == 200 && data.data.status == 500) {
    Message.error({
      message: data.data.msg
    });
    return;
  }
  if (data.status && data.status == 200 && data.data.status == 600) {
    MessageBox.alert(data.data.msg, "超时通知", {
      confirmButtonText: '返回登录',
      callback: action => {
        router.replace({
          path: "/"
        });
      }
    })
    return;
  }
  if (data.data.msg) {
    Message.success({
      message: data.data.msg
    });
    console.log(data);
    if (data.data.redirect) {
      router.push("/");
    }
  }
  return data;
}, err => {
  if (err.response.status == 504 || err.response.status == 404) {
    Message.error({
      message: '服务器被吃了⊙﹏⊙∥'
    });
  } else if (err.response.status == 403) {
    Message.error({
      message: '权限不足,请联系管理员!'
    });
  } else if (err.response.status == 401) {
    Message.error({
      message: err.response.data.msg
    });
  } else {
    if (err.response.data.msg) {
      Message.error({
        message: err.response.data.msg
      });
    } else {
      Message.error({
        message: '未知错误!'
      });
    }
  }
})

let base = '';
export const postRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
}
export const uploadFileRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    /* headers: {
      'Content-Type': 'multipart/form-data'
    } */
  });
}
export const putRequest = (url, params) => {
  return axios({
    method: 'put',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
}
export const deleteRequest = (url) => {
  return axios({
    method: 'delete',
    url: `${base}${url}`
  });
}
export const getRequest = (url) => {
  return axios({
    method: 'get',
    url: `${base}${url}`
  });
}
