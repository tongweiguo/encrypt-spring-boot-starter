package com.twg.encrypt.model;

/**
 * @author :twg
 * @date :2022/6/20
 * @description : 相应模型
 */
public class RespBean {

    /**
     * 相应码
     */
    private int status;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 数据内容
     */
    private Object data;

    public static RespBean build(){
        return new RespBean();
    }

    public static RespBean ok(String msg){
        return new RespBean(200, msg, null);
    }

    public static RespBean ok(String msg, Object data){
        return new RespBean(200, msg, data);
    }

    public static RespBean error(String msg){
        return new RespBean(500, msg, null);
    }

    public static RespBean error(String msg, Object data){
        return new RespBean(500, msg, data);
    }

    private RespBean() {
    }

    private RespBean(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public RespBean setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RespBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RespBean setData(Object data) {
        this.data = data;
        return this;
    }
}
