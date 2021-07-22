package com.bjpowernode.crm.commons.domain;
// 结果类，因为结果被所有人使用，放在公共的commons包下
// 主要是方便前端处理，返回统一的对象
public class ReturnObject {
    // 状态码
    private String code;
    // 错误信息
    private String message;
    // 返回数据
    private Object retData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
