package com.ecmp.basic.entity.vo;

import java.io.Serializable;

/**
 * <p/>
 * 实现功能：货币Vo
 * <p/>
 *
 * @author 豆
 * @version 1.0.00
 */
public class CurrencyVo implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String PATH = "10.4.68.77:9084/fim-service/currency/findByPage";

    /**
     * 货币代码
     */
    private String code;

    /**
     * 货币名称
     */
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
