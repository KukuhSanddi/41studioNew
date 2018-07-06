package com.kukuh.studio;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable{
    public String nama;
    public String posisi;
    public String email;
    public String phone;
    public String alamat;
    public String checkin;
    public String checkout;
    public String urlFoto;
    public String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Employee(String nama, String posisi, String email, String phone, String alamat, String urlFoto, String status) {
        this.nama = nama;
        this.posisi = posisi;
        this.email = email;
        this.phone = phone;
        this.alamat = alamat;
        this.urlFoto = urlFoto;
        this.status = status;
    }

    public Employee(String nama, String email, String checkin, String checkout) {
        this.nama = nama;
        this.email = email;
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public Employee(){

    }

    public Employee(Parcel parcel){
        nama = parcel.readString();
        posisi = parcel.readString();
        email = parcel.readString();
        phone = parcel.readString();
        alamat = parcel.readString();
        urlFoto = parcel.readString();
        status = parcel.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nama);
        parcel.writeString(posisi);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(alamat);
        parcel.writeString(urlFoto);
        parcel.writeString(status);
    }

    public static final Parcelable.Creator<Employee> CREATOR = new Parcelable.Creator<Employee>(){

        @Override
        public Employee createFromParcel(Parcel parcel) {
            return new Employee(parcel);
        }

        @Override
        public Employee[] newArray(int i) {
            return new Employee[i];
        }
    };
}
