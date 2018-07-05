package com.kukuh.studio;

public class Employee {
    public String nama;
    public String posisi;
    public String email;
    public String phone;
    public String alamat;
    public String checkin;
    public String checkout;
    public String urlFoto;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
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
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public Employee(String nama, String posisi, String email, String phone, String alamat, String checkin, String checkout, String urlFoto) {
        this.nama = nama;
        this.posisi = posisi;
        this.email = email;
        this.phone = phone;
        this.alamat = alamat;
        this.checkin = checkin;
        this.checkout = checkout;
        this.urlFoto = urlFoto;
    }

    public Employee(String nama, String posisi, String email, String phone, String alamat, String urlFoto) {
        this.nama = nama;
        this.posisi = posisi;
        this.email = email;
        this.phone = phone;
        this.alamat = alamat;
        this.urlFoto = urlFoto;
    }

    public Employee(String nama, String email, String checkin, String checkout) {
        this.nama = nama;
        this.email = email;
        this.checkin = checkin;
        this.checkout = checkout;
    }
}
