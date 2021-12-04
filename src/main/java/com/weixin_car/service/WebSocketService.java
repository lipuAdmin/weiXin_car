package com.weixin_car.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
@ServerEndpoint(value = "/sendMsg/{carNum}")
@Component
public class WebSocketService {
    /** 记录当前在线连接数 */
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    /** 存放所有在线的客户端 */
    private static Map<String, Session> clients = new ConcurrentHashMap<>();
    private static Map<String,String> clientCar=new HashMap<>();
    public static IUserCarService iUserCarService;
    @Autowired
    public void setiUserCarService(IUserCarService iUserCarService) {
        WebSocketService.iUserCarService = iUserCarService;
    }
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("carNum") String carNum) {
        onlineCount.incrementAndGet(); // 在线数加1
        clientCar.put(session.getId(),carNum);
        clients.put(carNum, session);
        log.info("有新连接加入：{}，当前在线人数为：{}", carNum, onlineCount.get());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        clients.remove(clientCar.get(session.getId()));
        iUserCarService.closeMsg(clientCar.get(session.getId()));
        log.info("有一连接关闭：{}，当前在线人数为：{}", clientCar.get(session.getId()), onlineCount.get());
    }
    @OnError
    public boolean onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
        return false;
    }

    /**
     * 服务端发送消息给客户端
     */
    public void sendMessage(String message, Session toSession) {
        try {
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
    /**
     * 服务端发送消息给客户端
     */
    public Map<String,Object> sendMessage(String message, String carNum) {
        Map<String,Object> resultMap=new HashMap<>();
        try {
            WebSocketService.clients.get(carNum).getBasicRemote().sendText(message);
            resultMap.put("msg","发送成功");
        } catch (Exception e) {
            resultMap.put("msg","发送失败，对方不在线");
        }
        return resultMap;
    }

}
