package com.vijay.v_canteen.pojo;

public class Item_getter {
    Integer i_id;
    String i_name,i_type,i_price,i_quantity,i_image,c_id;

    public Item_getter(Integer i_id, String i_name, String i_type, String i_price, String i_quantity,String i_image,String c_id) {
        this.i_id = i_id;
        this.i_name = i_name;
        this.i_type = i_type;
        this.i_price = i_price;
        this.i_quantity = i_quantity;
        this.i_image=i_image;
        this.c_id=c_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getI_image() {
        return i_image;
    }

    public void setI_image(String i_image) {
        this.i_image = i_image;
    }

    public Integer getI_id() {
        return i_id;
    }

    public void setI_id(Integer i_id) {
        this.i_id = i_id;
    }

    public String getI_name() {
        return i_name;
    }

    public void setI_name(String i_name) {
        this.i_name = i_name;
    }

    public String getI_type() {
        return i_type;
    }

    public void setI_type(String i_type) {
        this.i_type = i_type;
    }

    public String getI_price() {
        return i_price;
    }

    public void setI_price(String i_price) {
        this.i_price = i_price;
    }

    public String getI_quantity() {
        return i_quantity;
    }

    public void setI_quantity(String i_quantity) {
        this.i_quantity = i_quantity;
    }
}
