package com.example.phonecatcher.vo;

public class BlackPhone{
    private String phone;

    public BlackPhone(){
        this.phone="0000";
    }

    public BlackPhone(String phone){
        this.phone=phone;
    }

    public void set(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }
}
