package com.bram41.utspm2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bram41.utspm2.util.Adapter;
import com.bram41.utspm2.util.ApiClient;
import com.bram41.utspm2.util.ApiInterface;
import com.bram41.utspm2.util.Dosen;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class daftar_dosen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Dosen> dosenList;
    ApiInterface apiInterface;
    Adapter.RecyclerViewClickListener listener;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_dosen);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Daftar Dosen");

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        listener = new Adapter.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(daftar_dosen.this, editor.class);
                intent.putExtra("kode", dosenList.get(position).getKode());
                intent.putExtra("nama", dosenList.get(position).getNama());
                intent.putExtra("alamat", dosenList.get(position).getAlamat());
                intent.putExtra("kota", dosenList.get(position).getKota());
                intent.putExtra("gambar", dosenList.get(position).getGambar());
                intent.putExtra("status", dosenList.get(position).getStatus());
                intent.putExtra("kelamin", dosenList.get(position).getKelamin());
                intent.putExtra("tanggal", dosenList.get(position).getTanggal());
                intent.putExtra("agama", dosenList.get(position).getAgama());
                startActivity(intent);
            }

        };

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(daftar_dosen.this, editor.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        //code it to launch an intent to the activity you want
        finish();
        return true;
    }

    public void getDosen() {
        Call<List<Dosen>> call = apiInterface.getDosen();
        call.enqueue(new Callback<List<Dosen>>() {
            @Override
            public void onResponse(@NonNull Call<List<Dosen>> call, @NonNull Response<List<Dosen>> response) {
                progressBar.setVisibility(View.GONE);
                dosenList = response.body();
                assert response.body() != null;
                Log.i(daftar_dosen.class.getSimpleName(), response.body().toString());
                adapter = new Adapter(dosenList, daftar_dosen.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<List<Dosen>> call, @NonNull Throwable t) {
                Toast.makeText(daftar_dosen.this, "rp :" +
                                t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDosen();
    }
}
