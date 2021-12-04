package com.weixin_car.service;

import com.weixin_car.pojo.UserCar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liHeWei
 * @since 2021-11-28
 */
public interface IUserCarService extends IService<UserCar> {
    Map<String,Object> bindCar(UserCar userCar);
    Map<String,Object> isSendMsg(UserCar userCar);
    int closeMsg(String carNum);
    Map<String,Object> endMsg(UserCar userCar);
    Map<String,Object> addUser(String ownName,String userName,String carNum);
    Map<String,Object> unBind(String ownName,String userName,String carNum);
    Map<String,Object> queryCarNum(String userName);
    Map<String,Object> queryUserName(String carNum,String userName);
    boolean isOwner(String ownName,String carNum);
}
