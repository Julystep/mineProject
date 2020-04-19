package com.example.demo.controller.root;

import com.example.demo.bean.Academy;
import com.example.demo.bean.Admin;
import com.example.demo.bean.Major;
import com.example.demo.bean.RespBean;
import com.example.demo.service.root.RootService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/root")
public class RootController {

    @Autowired
    RootService rootService;

    /**
     *
     * @param page  第几页
     * @param size  页面大小
     * @return
     */
    @RequestMapping(value = "/getsecondaryadministrator", method = RequestMethod.GET)
    public List<Admin> getSecondaryAdministrator(@RequestParam("currentPage") int page,
                                                 @RequestParam("size") int size,
                                                 @RequestParam("userInfo") String userInfo){

        return rootService.getSecondaryAdministrator(page, size, userInfo);

    }

    @RequestMapping(value = "/getallmajor", method = RequestMethod.GET)
    public List<Academy> getAllMajor(){

        return rootService.getAllMajor();

    }

    @RequestMapping(value = "/createadmin", method = RequestMethod.POST)
    public RespBean insertAdmin(@RequestParam("form") String form){

        boolean flag = rootService.insertAdmin(form);

        if(flag){
            return RespBean.ok("添加管理员成功");
        }else{
            return RespBean.error("添加管理员失败, 可能原因为账号重复");
        }

    }

    @RequestMapping(value = "/deleteAdmin", method = RequestMethod.POST)
    public RespBean deleteAdmin(@RequestParam("user_id") String user_id){

        boolean flag = rootService.deleteAdmin(user_id);

        if(flag){
            return RespBean.ok("删除管理员成功");
        }else{
            return RespBean.error("删除管理员失败");
        }

    }

    @RequestMapping(value = "/changadminmajor", method = RequestMethod.POST)
    public RespBean changAdminMajor(@RequestParam("user_id") String user_id,
                                    @RequestParam("form") String form){

        boolean flag = rootService.changAdminMajor(user_id, form);

        if(flag){
            return RespBean.ok("修改专业成功");
        }else{
            return RespBean.error("修改专业失败");
        }

    }

    @RequestMapping(value = "/createacamedy", method = RequestMethod.POST)
    public RespBean createAcamedy(@RequestParam("form") String form){

        if(rootService.createAcamedy(form)){
            return RespBean.ok("存储学院成功");
        }else{
            return RespBean.error("存储学院失败，可能原因是相同的名称");
        }

    }

    @RequestMapping(value = "/getallacademy", method = RequestMethod.GET)
    public List<Academy> getAllAcademy(){

        return rootService.getAllAcademy();

    }

    @RequestMapping(value = "deleteacademy", method = RequestMethod.POST)
    public RespBean deleteAcademy(@RequestParam("academy_id") int academyID){

        if(rootService.deleteAcademy(academyID)) {
            return RespBean.ok("删除成功");
        }else{
            return RespBean.error("删除失败");
        }

    }

    @RequestMapping(value = "updateacademyname", method = RequestMethod.POST)
    public RespBean updateAcademyname(@RequestParam("form") String form){

        if(rootService.updateAcademyname(form)) {
            return RespBean.ok("更新成功");
        }else{
            return RespBean.error("更新失败");
        }

    }

    /*分界线*/
    @RequestMapping(value = "/insertmajorname", method = RequestMethod.POST)
    public RespBean createMajor(@RequestParam("form") String form){

        if(rootService.createMajor(form)){
            return RespBean.ok("存储学院成功");
        }else{
            return RespBean.error("存储学院失败，可能原因是相同的名称");
        }

    }

    @RequestMapping(value = "/deletemajor", method = RequestMethod.POST)
    public RespBean deletemajor(@RequestParam("major_id") int majorID){

        if(rootService.deleteMajor(majorID)) {
            return RespBean.ok("删除成功");
        }else{
            return RespBean.error("删除失败");
        }

    }

    @RequestMapping(value = "updatemajorname", method = RequestMethod.POST)
    public RespBean updateMajorname(@RequestParam("form") String form){

        if(rootService.updateMajorname(form)) {
            return RespBean.ok("更新成功");
        }else{
            return RespBean.error("更新失败");
        }

    }



}
