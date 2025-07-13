package com.lottery.domain.channel.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 永
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{sid}")
public class ChannelServiceImpl implements ChannelService {

//    //存放会话对象
//    private static Map<String, Session> sessionMap = new HashMap<>();
    //根据条件区分所属组
    private static Map<String, Map<String, Session>> groupMap = new HashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        String group = sid.split("-")[1];
        Map<String, Session> map = new HashMap<>();
        map.put(sid, session);
        groupMap.put(group, map);
        Map<String, Session> sessionMap = groupMap.getOrDefault(group, new HashMap<>());
        log.info("客户端：{}建立连接 所属组：{} 当前组内会话数量：{}", sid,group,sessionMap.size());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来自客户端：{}的信息:{} 所属组：{}", sid, message,sid.split("-")[1]);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        String group = sid.split("-")[1];
        Map<String, Session> sessionMap = groupMap.get(group);
        if (sessionMap == null){
            return;
        }
        sessionMap.remove(sid);
        log.info("客户端：{}断开连接 所属组：{} 当前组内会话数量：{}", sid,group,sessionMap.size());
        // 如果该组下没有会话对象，则删除组
        if (sessionMap.isEmpty()){
            groupMap.remove(group);
        }
    }

    @Override
    public void sendToAllClient(String group,String message) {
        Map<String, Session> sessionMap = groupMap.get(group);
        if (sessionMap==null){
            return;
        }
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
