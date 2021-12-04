package com.weixin_car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weixin_car.pojo.UserCar;
import com.weixin_car.mapper.UserCarMapper;
import com.weixin_car.service.IUserCarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liHeWei
 * @since 2021-11-28
 */
@Service
public class UserCarServiceImpl extends ServiceImpl<UserCarMapper, UserCar> implements IUserCarService {
    @Autowired
    private UserCarMapper userCarMapper;
    /*
    绑定车牌号
    */
    @Override
    public Map<String,Object> bindCar(UserCar userCar) {
        Map<String,Object> resultMap=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("car_num", userCar.getCarNum());
        Map<String,Object> queryMap1=new HashMap<>();
        queryMap1.put("user_name", userCar.getUserName());
        if(!userCarMapper.selectByMap(queryMap).isEmpty()){
            resultMap.put("code", 500);
            resultMap.put("msg", "绑定失败,该车牌号已被绑定");
        }else if(userCarMapper.selectByMap(queryMap1).size()>=3){
            resultMap.put("code", 500);
            resultMap.put("msg", "绑定失败,一个用户最多只能绑定三辆车");
        }else {
            userCar.setIsOwner(1); //赋予车主权利
            if(userCarMapper.insert(userCar)==1) {
                resultMap.put("code", 200);
                resultMap.put("msg", "绑定成功");
            }else {
                resultMap.put("code", 500);
                resultMap.put("msg", "绑定失败");
            }
        }
        return resultMap;
    }
    /*
            查询该车牌号绑定的用户名
            */
    @Override
    public Map<String, Object> queryUserName(String carNum,String userName) {
        Map<String,Object> resultMap=new HashMap<>();
        QueryWrapper<UserCar> wrapper = new QueryWrapper<>();
        wrapper.eq("car_num", carNum);
        wrapper.ne("user_name", userName);
        if(userCarMapper.selectMaps(wrapper).isEmpty()){
            resultMap.put("code", 500);
            resultMap.put("msg", "该车牌号暂未绑定其他用户");
        }else{
            resultMap.put("result", userCarMapper.selectMaps(wrapper));
            resultMap.put("code", 200);
            resultMap.put("msg", "查询成功");
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> endMsg(UserCar userCar) {
        userCar.setIsMsg(0);
        Map<String,Object> resultMap=new HashMap<>();
        QueryWrapper<UserCar> wrapper = new QueryWrapper<>();
        wrapper.eq("car_num", userCar.getCarNum());
        wrapper.eq("user_name", userCar.getUserName());
        if(userCarMapper.update(userCar, wrapper)==1){
            resultMap.put("code", 200);
            resultMap.put("msg", "关闭成功");
            return  resultMap;
        }else {
            resultMap.put("code", 500);
            resultMap.put("msg", "关闭失败");
            return resultMap;
        }
    }

    /*
            查询该用户名绑定的车牌号
            */
    @Override
    public Map<String, Object> queryCarNum(String userName) {
        Map<String,Object> resultMap=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("user_name", userName);
        if(userCarMapper.selectByMap(queryMap).isEmpty()){
            resultMap.put("code", 500);
            resultMap.put("msg", "该账号暂未绑定车牌号");
        }else{
            resultMap.put("result", userCarMapper.selectByMap(queryMap));
            resultMap.put("code", 200);
            resultMap.put("msg", "查询成功");
        }
        return resultMap;
    }

    @Override
    public int closeMsg(String carNum) {
        QueryWrapper<UserCar> wrapper = new QueryWrapper<>();
        wrapper.eq("car_num", carNum);
        userCarMapper.update(new UserCar(null,null,null,0),wrapper);
        return 0;
    }

    @Override
    public Map<String, Object> isSendMsg(UserCar userCar) {
        userCar.setIsMsg(1);
        Map<String,Object> resultMap=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        Map<String,Object> queryMap1=new HashMap<>();
        queryMap1.put("car_num", userCar.getCarNum());
        queryMap.put("user_name", userCar.getUserName());
        QueryWrapper<UserCar> wrapper = new QueryWrapper<>();
        wrapper.eq("car_num", userCar.getCarNum());
        wrapper.eq("user_name", userCar.getUserName());
        List<UserCar> userCars = userCarMapper.selectByMap(queryMap);
        for(UserCar us:userCars){
            if(us.getIsMsg()==1) {
                resultMap.put("code", 500);
                resultMap.put("msg", "开启失败,您已开启其他车辆提醒");
                return  resultMap;
            }
        }
        List<UserCar> userCarList = userCarMapper.selectByMap(queryMap1);
        if(!userCarList.isEmpty()){
            for(UserCar us:userCarList){
                if(us.getIsMsg()==1) {
                    resultMap.put("code", 500);
                    resultMap.put("msg", "开启失败,已有其他用户开启此车辆提醒");
                    return  resultMap;
                }
            }
        }
        if(userCarMapper.update(userCar, wrapper)==1){
            resultMap.put("code", 200);
            resultMap.put("msg", "开启成功");
            return  resultMap;
        }else {
            resultMap.put("code", 500);
            resultMap.put("msg", "开启失败");
            return resultMap;
        }
    }

    /*
            添加成员
            */
    @Override
    public Map<String, Object> addUser(String ownName,String userName,String carNum) {
        Map<String,Object> resultMap=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        Map<String,Object> queryMap1=new HashMap<>();
        queryMap1.put("user_name", userName);
        queryMap1.put("car_num", carNum);
        queryMap.put("car_num", carNum);
        if (!isOwner(ownName, carNum)){
            resultMap.put("code", 500);
            resultMap.put("msg", "绑定失败,您不是车主无权添加成员");
        }else if (userCarMapper.selectByMap(queryMap).size()>=5){
            resultMap.put("code", 500);
            resultMap.put("msg", "绑定失败,一辆车最多只能绑定5个成员");
        }else if (userCarMapper.selectByMap(queryMap1).size()>=1){
            resultMap.put("code", 500);
            resultMap.put("msg", "添加失败,该成员已经添加过");
        }else {
            UserCar userCar=new UserCar(userName,carNum,0,0);
            if(userCarMapper.insert(userCar)==1) {
                resultMap.put("code", 200);
                resultMap.put("msg", "添加成功");
            }else {
                resultMap.put("code", 500);
                resultMap.put("msg", "添加失败");
            }
        }
        return resultMap;
    }
    /*
    解绑
    */
    @Override
    public Map<String, Object> unBind(String ownName,String userName,String carNum) {
        Map<String,Object> resultMap=new HashMap<>();
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("user_name", userName);
        queryMap.put("car_num", carNum);
        if (!isOwner(ownName, carNum)){
            resultMap.put("code", 500);
            resultMap.put("msg", "解绑失败,您不是车主无权解绑成员");
        }else if(userCarMapper.deleteByMap(queryMap)==1){
            resultMap.put("code", 200);
            resultMap.put("msg", "解绑成功");
        }else {
            resultMap.put("code", 500);
            resultMap.put("msg", "解绑失败");
        }
        return resultMap;
    }

    /*
    判断是否为车主
    */
    public boolean isOwner(String ownName,String carNum){
        Map<String,Object> queryMap=new HashMap<>();
        queryMap.put("user_name", ownName);
        queryMap.put("car_num", carNum);
        return userCarMapper.selectByMap(queryMap).get(0).getIsOwner() != 0;
    }
}
