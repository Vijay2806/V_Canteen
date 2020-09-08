package com.vijay.v_canteen.pojo;

import android.content.Intent;

public class User_getter {
    Integer u_serial;
    String u_firstname,u_lastname,u_email,u_phone,u_password,u_id,u_gender,u_photo,u_dob,u_block;

    public User_getter(Integer u_serial, String u_firstname, String u_lastname, String u_email, String u_phone, String u_password, String u_id, String u_gender, String u_photo, String u_dob, String u_block) {
        this.u_serial = u_serial;
        this.u_firstname = u_firstname;
        this.u_lastname = u_lastname;
        this.u_email = u_email;
        this.u_phone = u_phone;
        this.u_password = u_password;
        this.u_id = u_id;
        this.u_gender = u_gender;
        this.u_photo = u_photo;
        this.u_dob = u_dob;
        this.u_block = u_block;
    }

    public Integer getU_serial() {
        return u_serial;
    }

    public void setU_serial(Integer u_serial) {
        this.u_serial = u_serial;
    }

    public String getU_firstname() {
        return u_firstname;
    }

    public void setU_firstname(String u_firstname) {
        this.u_firstname = u_firstname;
    }

    public String getU_lastname() {
        return u_lastname;
    }

    public void setU_lastname(String u_lastname) {
        this.u_lastname = u_lastname;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_gender() {
        return u_gender;
    }

    public void setU_gender(String u_gender) {
        this.u_gender = u_gender;
    }

    public String getU_photo() {
        return u_photo;
    }

    public void setU_photo(String u_photo) {
        this.u_photo = u_photo;
    }

    public String getU_dob() {
        return u_dob;
    }

    public void setU_dob(String u_dob) {
        this.u_dob = u_dob;
    }

    public String getU_block() {
        return u_block;
    }

    public void setU_block(String u_block) {
        this.u_block = u_block;
    }
}
