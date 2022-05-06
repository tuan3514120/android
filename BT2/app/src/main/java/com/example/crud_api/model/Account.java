package com.example.crud_api.model;

import com.google.gson.annotations.SerializedName;

public class Account {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    @SerializedName("id")
    private int id;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("password")
    private String 	password;
    @SerializedName("name")
    private String 	name;
    @SerializedName("status")
    private String 	status;
    @SerializedName("result_code")
    private String 	result_code;
}
