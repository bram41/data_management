package com.bram41.utspm2.util;

import com.google.gson.annotations.SerializedName;

public class Dosen {

    @SerializedName("kode")
    private String kode;
    @SerializedName("nama")
    private String nama;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("kelamin")
    private String kelamin;
    @SerializedName("agama")
    private String agama;
    @SerializedName("alamat")
    private String alamat;
    @SerializedName("status")
    private String status;
    @SerializedName("kota")
    private String kota;
    @SerializedName("gambar")
    private String gambar;
    @SerializedName("value")
    private String value;
    @SerializedName("message")
    private String massage;

    public Dosen(String kode, String nama, String tanggal, String kelamin, String agama, String alamat, String status, String kota, String gambar, String value, String massage) {
        this.kode = kode;
        this.nama = nama;
        this.tanggal = tanggal;
        this.kelamin = kelamin;
        this.agama = agama;
        this.alamat = alamat;
        this.status = status;
        this.kota = kota;
        this.gambar = gambar;
        this.value = value;
        this.massage = massage;
    }

    public String getKode() {
        return kode;
    }

    public String getNama() {
        return nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKelamin() {
        return kelamin;
    }

    public String getAgama() {
        return agama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getStatus() {
        return status;
    }

    public String getKota() {
        return kota;
    }

    public String getGambar() {
        return gambar;
    }

    public String getValue() {
        return value;
    }

    public String getMassage() {
        return massage;
    }
}
