import Vue from 'vue'
import Vuex from 'vuex'
import '../lib/sockjs'
import '../lib/stomp'

Vue.use(Vuex)

export default new Vuex.Store({
  /**
   * state是vue中的状态，存储着vue的各种需要的信息
   */
  state: {
    user: {
      id: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).id,
      user_id: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).user_id,
      avatar: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).avatar,
      username: window.localStorage.getItem('user' || '[]') == null ? '未登录' : JSON.parse(window.localStorage.getItem('user' || '[]')).username,
      email: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).email,
      phone: window.localStorage.getItem('user' || '[]') == null ? '' : JSON.parse(window.localStorage.getItem('user' || '[]')).phone,
    },
    routes: [],
    stomp: null,
    url: "http://127.0.0.1:8087",
    admin: ""
  },
  /**
   * mutations是唯一的特定改变state的内容
   */
  mutations: {
    /**
     *
     * @param {用户信息} state
     * @param {获取的用户信息的存储} user
     */
    login(state, user) {
      state.user = user;
      window.localStorage.setItem('user', JSON.stringify(user));
    },
    logout(state) {
      window.localStorage.removeItem('user');
      state.routes = [];
    },
    /**
     *
     * @param {目录状态} state
     * @param {获取的目录存储} menus
     */
    initMenu(state, menus) {
      state.routes = menus;
    },
    insertAdmin(state, admin) {
      state.admin = admin;
    }
  },
  actions: {

  }
});
