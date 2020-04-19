import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import CodeMirror from '@/components/codemirror/codemirror'
import SubHomeworkDetail from '@/components/teacher/SubHomeworkDetail'
import HomeworkDetail2 from '@/components/student/ScheckHomeworkDetail'
import CheckHomeworkDetail from '@/components/teacher/CheckHomeworkDetail'
import SubExaminationDetail from '@/components/teacher/SubExaminationDetail'
import ScheckOnlineTestDetail from '@/components/student/ScheckOnlineTestDetail'
import CheckOnlineTestDetail from '@/components/teacher/CheckOnlineTestDetail'

Vue.use(Router)

export default new Router({
  routes: [{
      path: '/',
      name: 'Login',
      component: Login,
    },
    {
      path: '/home',
      name: '主页',
      component: Home,
      children: [{
          path: '/getteacherdetails',
          name: '发布作业中心',
          component: SubHomeworkDetail,

        },
        {
          path: '/check/teacher/detail',
          name: '查看作业中心',
          component: CheckHomeworkDetail,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/getstudentdetails',
          name: '提交作业中心',
          component: HomeworkDetail2,
          meta: {
            keepAlive: true
          }
        },
        {
          path: '/subexaminationdetail',
          name: "发布考试中心",
          component: SubExaminationDetail,

        }, {
          path: '/onlinetestdetail',
          name: "在线考试中心",
          component: ScheckOnlineTestDetail,
          meta: {
            keepAlive: true //懵逼的点是如果这样就不会出问题
          }
        },
        {
          path: '/check/teacher/onlinetestdetail',
          name: '查看考试中心',
          component: CheckOnlineTestDetail,
          meta: {
            keepAlive: true
          }
        },
      ]
    }
  ]
})
