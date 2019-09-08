package com.MithraAandS.projectsa2;

import java.util.Date;

public class BlogPost extends BlogPostId{


    public String user_id, image_url, image_thumb,desc,price,
            product_name,quantity,cartprice,cusweightstring,orderid,weight,category;
    public Date timestamp;

    public BlogPost(String user_id, String image_url, String image_thumb, String desc, String price, String product_name, String quantity,
                    Date timestamp,String cartprice,String cusweightstring,String orderid,String weight,String category) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.image_thumb = image_thumb;
        this.desc = desc;
        this.price = price;
        this.product_name = product_name;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.cartprice=cartprice;
        this.cusweightstring=cusweightstring;
        this.orderid=orderid;
        this.weight=weight;
        this.category=category;

    }

    public BlogPost(){

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOrderid() {
        return orderid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCusweightstring() {
        return cusweightstring;
    }

    public void setCusweightstring(String cusweightstring) {
        this.cusweightstring = cusweightstring;
    }

    public String getCartprice() {
        return cartprice;
    }

    public void setCartprice(String cartprice) {
        this.cartprice = cartprice;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
