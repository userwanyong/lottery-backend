package com.lottery.domain.channel.service;


/**
 * @author 永
 * 通信领域
 */
@Deprecated
public interface ChannelService {

    void sendToAllClient(String group,String message);

}
