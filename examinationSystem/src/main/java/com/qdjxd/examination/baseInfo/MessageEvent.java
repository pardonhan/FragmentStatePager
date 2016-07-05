package com.qdjxd.examination.baseInfo;

/**
 * Created by Just on 16/7/5.
 * EventBus MessageEvent
 */
public class MessageEvent {

    private int Code;
    private String message;

    public MessageEvent(int code,String message){
        this.Code = code;
        this.message = message;
    }
}
