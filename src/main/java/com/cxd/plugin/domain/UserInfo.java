package com.cxd.plugin.domain;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {

    private String uid;

    private String name;

    private int age;

    private String address;

    private String phone;

    private Date birtydate;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirtydate() {
        return birtydate;
    }

    public void setBirtydate(Date birtydate) {
        this.birtydate = birtydate;
    }

    @Override
    public String toString() {
        return "UserInfo [uid=" + uid + ", name=" + name + ", age=" + age + ", address=" + address + ", phone=" + phone + ", birtydate=" + birtydate + "]";
    }
}
