package com.vijay.v_canteen.pojo;

import android.content.Intent;

public class canteen_getter {
    Integer c_id;
    String c_name,c_image,c_phone,c_email,c_landline_ext;

    public canteen_getter(Integer c_id, String c_name, String c_image, String c_phone, String c_email, String c_landline_ext) {
        this.c_id = c_id;
        this.c_name = c_name;
        this.c_image = c_image;
        this.c_phone = c_phone;
        this.c_email = c_email;
        this.c_landline_ext = c_landline_ext;
    }

    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_image() {
        return c_image;
    }

    public void setC_image(String c_image) {
        this.c_image = c_image;
    }

    public String getC_phone() {
        return c_phone;
    }

    public void setC_phone(String c_phone) {
        this.c_phone = c_phone;
    }

    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    }

    public String getC_landline_ext() {
        return c_landline_ext;
    }

    public void setC_landline_ext(String c_landline_ext) {
        this.c_landline_ext = c_landline_ext;
    }
}
