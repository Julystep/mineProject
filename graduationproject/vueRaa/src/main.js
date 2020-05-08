// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import VueCodemirror from 'vue-codemirror'
import store from './store'
import pdf from 'vue-pdf'
import 'element-ui/lib/theme-chalk/index.css'
import './utils/filter_utils'
import {
  downloadRequest
} from './utils/api'
import {
  getRequest
} from './utils/api'
import {
  postRequest
} from './utils/api'
import {
  deleteRequest
} from './utils/api'
import {
  uploadFileRequest
} from './utils/api'
import {
  putRequest
} from './utils/api'
import {
  initMenu
} from './utils/utils'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueCodemirror)
Vue.use(pdf)

Vue.prototype.getRequest = getRequest;
Vue.prototype.postRequest = postRequest;
Vue.prototype.downloadRequest = downloadRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.uploadFileRequest = uploadFileRequest;


router.beforeEach((to, from, next) => {
  if (to.name == 'Login') {
    next();
    return;
  }
  var name = store.state.user.name;
  if (name == "未登录") {
    if (to.meta.requireAuth || to.name == null) {
      next({
        path: '/',
        query: {
          redirect: to.path
        }
      })
    } else {
      next();
    }
  } else {
    initMenu(router, store);
    next();
  }
})


new Vue({
  el: '#app',
  router,
  store,
  pdf,
  components: {
    App
  },
  template: '<App/>'
})
