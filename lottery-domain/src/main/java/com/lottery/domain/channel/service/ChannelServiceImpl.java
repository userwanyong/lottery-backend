package com.lottery.domain.channel.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 永
 */
@Slf4j
@Component
@ServerEndpoint("/ws/{sid}")
public class ChannelServiceImpl implements ChannelService {

    //根据条件区分所属组
    private static Map<String, Set<Session>> groupMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        String group = sid.split("-")[1];
        groupMap.putIfAbsent(group, ConcurrentHashMap.newKeySet());
        groupMap.get(group).add(session);
        log.info("客户端：{}建立连接 所属组：{} 当前组内会话数量：{}", sid, group, groupMap.get(group).size());
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来自客户端：{}的信息:{} 所属组：{}", sid, message, sid.split("-")[1]);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("sid") String sid) {
        String group = sid.split("-")[1];
        Set<Session> sessionSet = groupMap.get(group);
        if (sessionSet != null) {
            sessionSet.remove(session);
            log.info("客户端：{}断开连接 所属组：{} 当前组内会话数量：{}", sid, group, sessionSet.size());
            if (sessionSet.isEmpty()) {
                groupMap.remove(group);
            }
        }
    }

    @Override
    public void sendToAllClient(String group, String message) {
        Set<Session> sessionSet = groupMap.get(group);
        if (sessionSet == null) {
            return;
        }
        for (Session session : sessionSet) {
            try {
                //服务器向客户端发送消息
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("广播消息失败：{}", e.getMessage());
            }
        }
    }
}
