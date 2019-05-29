package com.example.myfirebasesir;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String name;
    private String uri_img;

    public User() {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri_img() {
        return uri_img;
    }

    public void setUri_img(String uri_img) {
        this.uri_img = uri_img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    private String address;
    private String img;
    public User( String line1, String line2,String name1) {
        this.uri_img = name1;
        this.name = line1;
        this.address = line2;

    }


   /* public String getUri_img() {
        return uri_img;
    }

    public void setUri_img(String uri_img) {
        this.uri_img = uri_img;
    }

    public User() {

    }

    public User(String name1, String line1, String line2) {
        this.uri_img = name1;
        this.name = line1;
        this.address = line2;

    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getName() {
        return name;
    }

    public String setName(String name) {
        this.name = name;
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String setAddress(String address) {
        this.address = address;
        return address;
    }




    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("address", address);
        result.put("uri_img", uri_img);

        return result;
    }

*/
   @Exclude
   public Map<String, Object> toMap() {
       HashMap<String, Object> result = new HashMap<>();
       result.put("name", name);
       result.put("address", address);
       result.put("uri_img", uri_img);

       return result;
   }

}
