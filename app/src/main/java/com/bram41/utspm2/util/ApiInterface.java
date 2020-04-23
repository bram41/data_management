package com.bram41.utspm2.util;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("read.php")
    Call<List<Dosen>> getDosen();

    @FormUrlEncoded
    @POST("add.php")
    Call<Dosen> insert(
            @Field("key") String key,
            @Field("nama") String nama,
            @Field("kota") String kota,
            @Field("alamat") String alamat,
            @Field("kode") String kode,
            @Field("agama") String agama,
            @Field("tanggal") String tanggal,
            @Field("status") String status,
            @Field("kelamin") String kelamin,
            @Field("gambar") String gambar);

    @FormUrlEncoded
    @POST("update.php")
    Call<Dosen> update(
            @Field("key") String key,
            @Field("nama") String nama,
            @Field("kota") String kota,
            @Field("alamat") String alamat,
            @Field("kode") String kode,
            @Field("agama") String agama,
            @Field("tanggal") String tanggal,
            @Field("status") String status,
            @Field("kelamin") String kelamin,
            @Field("gambar") String gambar);

    @FormUrlEncoded
    @POST("delete.php")
    Call<Dosen> delete(
            @Field("key") String key,
            @Field("kode") String kode,
            @Field("gambar") String gambar);
}

