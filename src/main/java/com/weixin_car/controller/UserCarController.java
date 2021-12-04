package com.weixin_car.controller;


import com.alibaba.fastjson.JSONObject;
import com.weixin_car.pojo.UserCar;
import com.weixin_car.service.IUserCarService;
import com.weixin_car.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liHeWei
 * @since 2021-11-28
 */
@RestController
@RequestMapping("/car")
public class UserCarController {
    private static WebSocketService webSocketService;
    @Autowired
    public void setWebSocketService(WebSocketService webSocketService) {
        UserCarController.webSocketService = webSocketService;
    }
    @Autowired
    private IUserCarService iUserCarService;
    @RequestMapping("/bindCar")
    public Map<String,Object> bindCar(@RequestBody UserCar  userCar) {
        return iUserCarService.bindCar(userCar);
    }
    @RequestMapping("/addUser")
    public Map<String,Object> addUser(@RequestBody JSONObject jsonParam) {
        return iUserCarService.addUser(jsonParam.getString("ownName"),
                jsonParam.getString("userName"), jsonParam.getString("carNum"));
    }
    @RequestMapping("/unBind")
    public Map<String,Object> unBind(@RequestBody JSONObject jsonParam) {
        return iUserCarService.unBind(jsonParam.getString("ownName"),
                jsonParam.getString("userName"), jsonParam.getString("carNum"));
    }
    @RequestMapping("/queryCarNum")
    public Map<String,Object> queryCarNum(@RequestBody JSONObject jsonParam){
        return iUserCarService.queryCarNum(jsonParam.getString("userName"));
    }
    @RequestMapping("/queryUserName")
    public Map<String,Object> queryUserName(@RequestBody JSONObject jsonParam){
        return iUserCarService.queryUserName(jsonParam.getString("carNum"),jsonParam.getString("userName"));
    }
    @RequestMapping("/isOwner")
    public Map<String,Object> isOwner(@RequestBody JSONObject jsonParam){
        Map<String,Object> resultMap=new HashMap<>();
        if(iUserCarService.isOwner(jsonParam.getString("ownName"),jsonParam.getString("carNum"))){
            resultMap.put("code", "200");
        }else{
            resultMap.put("code", 500);
            resultMap.put("msg", "您不是车主,无权访问");
        }
        return resultMap;
    }
    @RequestMapping("/isSendMsg")
    public Map<String,Object> isSendMsg(@RequestBody UserCar  userCar){
        return iUserCarService.isSendMsg(userCar);
    }
    @RequestMapping("/endMsg")
    public Map<String,Object> endMsg(@RequestBody UserCar  userCar){
        return iUserCarService.endMsg(userCar);
    }
    @RequestMapping("/sendMsg")
    public Map<String,Object> sendMsg(@RequestBody JSONObject jsonParam){
        double limitHeight=Double.parseDouble(jsonParam.getString("limitHeight"));
        double carHeight=Double.parseDouble(jsonParam.getString("carHeight"));
        String msg="";
        if(carHeight>limitHeight) msg="前方限高杆高度为"+limitHeight+"您的车高为"+carHeight+"无法通过请绕行";
        else msg="前方限高杆高度为"+limitHeight+"您的车高为"+carHeight+"可以安全通过";
        return webSocketService.sendMessage(msg, jsonParam.getString("carNum"));
    }
    @RequestMapping("/closeMsg")
    public Map<String,Object> closeMsg(@RequestBody JSONObject jsonParam){
        Map<String,Object> resultMap=new HashMap<>();
        iUserCarService.closeMsg(jsonParam.getString("carNum"));
        resultMap.put("msg", "成功");
        return resultMap;
    }
}

