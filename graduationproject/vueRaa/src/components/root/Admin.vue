<template>
  <div>
    <el-tabs class="admin" v-model="activeName" type="card">
      <el-tab-pane label="管理专业管理员" name="first"
        ><appointmentadmin></appointmentadmin
      ></el-tab-pane>
      <el-tab-pane label="学院操作" name="second"
        ><createacademy ref="createacademy"></createacademy
      ></el-tab-pane>
      <el-tab-pane label="专业操作" name="third"
        ><createmajor ref="createmajor"></createmajor
      ></el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import appointmentadmin from '../root/AppointmentAdmin'
import createacademy from '../root/CreateAcademy'
import createmajor from '../root/CreateMajor'
export default {
  components: {
    appointmentadmin,
    createacademy,
    createmajor
  },
  data () {
    return { activeName: 'first' }
  },
  methods: {},
  mounted () {
    this.$refs.createacademy.getAllAcademy()
    this.$refs.createmajor.getAllMajor()
  }
}
</script>
<style>
.admin {
  margin-top: 20px;
}
</style>
