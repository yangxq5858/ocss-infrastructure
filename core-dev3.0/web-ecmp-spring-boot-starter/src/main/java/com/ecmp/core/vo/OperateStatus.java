package com.ecmp.core.vo;

import com.ecmp.context.ContextUtil;

import java.io.Serializable;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：前端返回结果类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017/4/5 16:27      陈飞(fly)                  新建
 * <p/>
 * *************************************************************************************************
 */
public class OperateStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String COMMON_SUCCESS_MSG = ContextUtil.getMessage("ecmp_context_00001");
    public static final String COMMON_FAILURE_MSG = ContextUtil.getMessage("ecmp_context_00002");

    /**
     * 成功标志
     */
    private boolean success;
    /**
     * 消息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;


    public OperateStatus(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public OperateStatus(boolean success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public OperateStatus data(Object data) {
        this.data = data;
        return this;
    }

    public static OperateStatus defaultSuccess() {
        return new OperateStatus(true, COMMON_SUCCESS_MSG);
    }

    public static OperateStatus defaultFailure() {
        return new OperateStatus(false, COMMON_FAILURE_MSG);
    }

    public boolean isSuccess() {
        return success;
    }

    public OperateStatus setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public OperateStatus success() {
        this.success = true;
        return this;
    }

    public OperateStatus failure() {
        this.success = false;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public OperateStatus setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public OperateStatus setData(Object data) {
        this.data = data;
        return this;
    }
}
