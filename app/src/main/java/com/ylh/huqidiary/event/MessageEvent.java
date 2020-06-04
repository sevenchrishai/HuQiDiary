package com.ylh.huqidiary.event;

/**
 * @Author: yinlinhai
 * @Date: 2019/6/14
 */
public class MessageEvent {
    private String message;

    public MessageEvent(){
    }

    public  MessageEvent(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
