package com.kukuh.studio;

public class Employee {
    public String nama;
    public String Posisi;
    public String email;
    public String phone;
    public String Alamat;
    public String Checkin;
    public String Checkout;
    public String urlFoto;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPosisi() {
        return Posisi;
    }

    public void setPosisi(String posisi) {
        Posisi = posisi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlamat() {
        return Alamat;
    }

    public void setAlamat(String alamat) {
        Alamat = alamat;
    }

    public String getCheckin() {
        return Checkin;
    }

    public void setCheckin(String checkin) {
        Checkin = checkin;
    }

    public String getCheckout() {
        return Checkout;
    }

    public void setCheckout(String checkout) {
        Checkout = checkout;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Employee(String nama, String posisi, String email, String phone, String alamat, String checkin, String checkout, String urlFoto) {
        this.nama = nama;
        Posisi = posisi;
        this.email = email;
        this.phone = phone;
        Alamat = alamat;
        Checkin = checkin;
        Checkout = checkout;
        this.urlFoto = urlFoto;
    }

    public Employee(String nama, String posisi, String email, String phone, String alamat, String urlFoto) {
        this.nama = nama;
        Posisi = posisi;
        this.email = email;
        this.phone = phone;
        Alamat = alamat;
        this.urlFoto = urlFoto;
    }

    public Employee(String nama, String email, String checkin, String checkout) {
        this.nama = nama;
        this.email = email;
        Checkin = checkin;
        Checkout = checkout;
    }
}
