package com.weixin_car.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liHeWei
 * @since 2021-11-28
 */
@TableName("user_car")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCar implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @JSONField(name = "userName")
    private String userName;

    /**
     * 车牌号
     */
    @JSONField(name = "carNum")
    private String carNum;

    /**
     * 1:拥有 0:不拥有
     */
    @JSONField(name = "isOwner")
    private Integer isOwner;

    /**
     * 1:开启 0:未开启
     */
    @JSONField(name = "isMsg")
    private Integer isMsg;

}
