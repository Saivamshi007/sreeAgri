package com.MithraAandS.projectsa2;

import java.util.Date;

public class OrdersSetter extends BlogPost {
    public String cityname,customername,phone_number,housename,houseno,colonyname,streetname,landmark,pincode,nickname,
            product_name,price,desc,image_url,image_thumb,orderid,type_of_order,weight,totalpay;
    public Date adresstimestamp;


    public  OrdersSetter(){}

    public OrdersSetter(String cityname, String customername, String phone_number, String housename, String houseno, String colonyname, String streetname, String landmark, String pincode, String nickname, String product_name, String price,
                        String desc, String image_url, String image_thumb,
                        Date adresstimestamp,String orderid,String type_of_order,String weight,String totalpay) {
        this.cityname = cityname;
        this.customername = customername;
        this.phone_number = phone_number;
        this.housename = housename;
        this.houseno = houseno;
        this.colonyname = colonyname;
        this.streetname = streetname;
        this.landmark = landmark;
        this.pincode = pincode;
        this.nickname = nickname;
        this.product_name = product_name;
        this.price = price;
        this.desc = desc;
        this.image_url = image_url;
        this.image_thumb = image_thumb;
        this.adresstimestamp=adresstimestamp;
        this.orderid=orderid;
        this.type_of_order=type_of_order;
        this.weight=weight;
        this.totalpay=totalpay;
    }

    public String getTotalpay() {
        return totalpay;
    }

    public void setTotalpay(String totalpay) {
        this.totalpay = totalpay;
    }

    public String getType_of_order() {
        return type_of_order;
    }

    public void setType_of_order(String type_of_order) {
        this.type_of_order = type_of_order;
    }

    @Override
    public String getWeight() {
        return weight;
    }

    @Override
    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public Date getAdresstimestamp() {
        return adresstimestamp;
    }

    public void setAdresstimestamp(Date adresstimestamp) {
        this.adresstimestamp = adresstimestamp;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHousename() {
        return housename;
    }

    public void setHousename(String housename) {
        this.housename = housename;
    }

    public String getHouseno() {
        return houseno;
    }

    public void setHouseno(String houseno) {
        this.houseno = houseno;
    }

    public String getColonyname() {
        return colonyname;
    }

    public void setColonyname(String colonyname) {
        this.colonyname = colonyname;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String getProduct_name() {
        return product_name;
    }

    @Override
    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getImage_url() {
        return image_url;
    }

    @Override
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public String getImage_thumb() {
        return image_thumb;
    }

    @Override
    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }


    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCityname() {
        return cityname;
    }



}
