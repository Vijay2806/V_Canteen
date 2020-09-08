package com.vijay.v_canteen.pojo;

public class History_getter {
    String i_count,i_name,o_total,o_status;

    public History_getter(String i_count, String i_name, String o_total, String o_status) {
        this.i_count = i_count;
        this.i_name = i_name;
        this.o_total = o_total;
        this.o_status = o_status;
    }

    public String getI_count() {
        return i_count;
    }

    public void setI_count(String i_count) {
        this.i_count = i_count;
    }

    public String getI_name() {
        return i_name;
    }

    public void setI_name(String i_name) {
        this.i_name = i_name;
    }

    public String getO_total() {
        return o_total;
    }

    public void setO_total(String o_total) {
        this.o_total = o_total;
    }

    public String getO_status() {
        return o_status;
    }

    public void setO_status(String o_status) {
        this.o_status = o_status;
    }
}
