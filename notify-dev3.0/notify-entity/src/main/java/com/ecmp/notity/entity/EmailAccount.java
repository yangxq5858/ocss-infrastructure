package com.ecmp.notity.entity;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：EMAIL用户账号
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-04-14 20:05      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class EmailAccount {
    //姓名
    private String name;
    //邮箱地址
    private String address;

    /**
     * 默认构造函数
     */
    public EmailAccount(){
        name = "";
        address = "";
    }

    /**
     * 构造函数
     * @param name 姓名
     * @param address 邮箱地址
     */
    public EmailAccount(String name,String address){
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
