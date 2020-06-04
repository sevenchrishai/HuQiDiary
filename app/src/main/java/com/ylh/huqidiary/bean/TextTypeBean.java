package com.ylh.huqidiary.bean;

import java.io.Serializable;

/**
 * @Author: yinlinhai
 * @Date: 2019/6/14
 */
public class TextTypeBean implements Serializable {
    int text_color;
    float text_size;

    public TextTypeBean() {
    }

    public TextTypeBean(int text_color, float text_size) {
        this.text_color = text_color;
        this.text_size = text_size;
    }

    public int getText_color() {
        return text_color;
    }

    public void setText_color(int text_color) {
        this.text_color = text_color;
    }

    public float getText_size() {
        return text_size;
    }

    public void setText_size(float text_size) {
        this.text_size = text_size;
    }
}
