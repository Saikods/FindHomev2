package com.joseluis.findhomev2.pojo;

public class User {
    private String dni;
    private String first_name;
    private String last_name;
    private String date_birth;
    private String phone;
    private int cp;
    private String province;
    private String town;
    private String nickname;
    private String password;
    private String email;


    public User(String dni, String first_name, String last_name, String date_birth, String phone, int cp, String province, String town, String nickname, String password, String email) {
        this.dni = dni;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_birth = date_birth;
        this.phone = phone;
        this.cp = cp;
        this.province = province;
        this.town = town;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
    }

    public User() {
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(String date_birth) {
        this.date_birth = date_birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
