package com.kukuh.studio;

public class Visitor {

    public String nama;
    public String email;
    public String phone;
    public String checkin;
    public String checkout;
    public String keperluan;
    public String urlFoto;
    public String jdlFoto;


    public Visitor(String nama, String email, String phone, String checkin, String checkout, String keperluan) {
        this.nama = nama;
        this.email = email;
        this.phone = phone;
        this.checkin = checkin;
        this.checkout = checkout;
        this.keperluan = keperluan;
    }

    public Visitor(String nama, String email, String phone, String checkin, String keperluan) {
        this.nama = nama;
        this.email = email;
        this.phone = phone;
        this.checkin = checkin;
        this.keperluan = keperluan;
    }

    public Visitor(String nama, String email, String phone, String checkin, String checkout, String keperluan, String urlFoto) {
        this.nama = nama;
        this.email = email;
        this.phone = phone;
        this.checkin = checkin;
        this.checkout = checkout;
        this.keperluan = keperluan;
        this.urlFoto = urlFoto;

    }

    public Visitor() {
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    public String getKeperluan() {
        return keperluan;
    }

    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getJdlFoto() {
        return jdlFoto;
    }

    public void setJdlFoto(String jdlFoto) {
        this.jdlFoto = jdlFoto;
    }

}
//    <!--Code By M. Adli Rachman-->