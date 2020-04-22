package com.bram41.utspm2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bram41.utspm2.util.ApiClient;
import com.bram41.utspm2.util.ApiInterface;
import com.bram41.utspm2.util.Dosen;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class editor extends AppCompatActivity {


    private Spinner mAgamaSpinner;
    private RadioGroup mStatus, mKel;
    private RadioButton rStatus, rKel, satu, dua, tiga, empat;
    private Button mTgl;
    private EditText mKode, mNama, mAlamat, mKota;
    private CircleImageView mGambar;

    private String mAgama = "0";
    public static final String AGAMA_UNKNOWN = "0";
    public static final String AGAMA_ISLAM = "1";
    public static final String AGAMA_KRISTEN = "2";
    public static final String AGAMA_KATHOLIK = "3";
    public static final String AGAMA_HINDU = "4";
    public static final String AGAMA_BUDHA = "5";
    public static final String AGAMA_KONGHUCU = "6";
    private FloatingActionButton mFabChoosePic;
    private SimpleDateFormat dateFormatter, dateDB;
    private TextView ltgl;

    private String nama, kode, kelamin, gambar, alamat, kota, tgl, status, agama, lahir;

    private Menu action;
    private Bitmap bitmap;

    private ApiInterface apiInterface;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        getWindow().setBackgroundDrawableResource(R.mipmap.editor);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mNama = findViewById(R.id.nama_dosen);
        mKode = findViewById(R.id.kode_dosen);
        mAlamat = findViewById(R.id.alamat);
        mKota = findViewById(R.id.kota);
        mStatus = findViewById(R.id.radio_status);
        mKel = findViewById(R.id.radio_g);
        ltgl = findViewById(R.id.tgl);
        mAgamaSpinner = findViewById(R.id.agama);
        mGambar = findViewById(R.id.gambar);
        mTgl = findViewById(R.id.datepicker);
        mFabChoosePic = findViewById(R.id.fabChoosePic);
        satu = findViewById(R.id.kel_0);
        dua = findViewById(R.id.kel_1);
        tiga = findViewById(R.id.st_0);
        empat = findViewById(R.id.st_1);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateDB = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        mTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        setupSpinner();

        Intent intent = getIntent();
        kode = intent.getStringExtra("kode");
        nama = intent.getStringExtra("nama");
        tgl = intent.getStringExtra("tanggal");
        alamat = intent.getStringExtra("alamat");
        gambar = intent.getStringExtra("gambar");
        kota = intent.getStringExtra("kota");
        kelamin = intent.getStringExtra("kelamin");
        status = intent.getStringExtra("status");
        agama = intent.getStringExtra("agama");

        setDataFromIntentExtra();

    }


    private void showDateDialog(){

        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                ltgl.setText("Tanggal dipilih : " + dateFormatter.format(newDate.getTime()));
                lahir = dateDB.format(newDate.getTime());
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    @SuppressLint({"SetTextI18n", "CheckResult"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setDataFromIntentExtra() {

        if (kode == null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Tambah Dosen");
        } else {

            readMode();
            Objects.requireNonNull(getSupportActionBar()).setTitle(nama);

            mNama.setText(nama);
            mKota.setText(kota);
            mKode.setText(kode);
            mAlamat.setText(alamat);
            lahir = tgl;

            if (kelamin.equals("1")){
                RadioButton pria = findViewById(R.id.kel_0);
                pria.setChecked(true);
            } else {
                RadioButton wanita = findViewById(R.id.kel_1);
                wanita.setChecked(true);
            }

            if (status.equals("1")){
                RadioButton tetap = findViewById(R.id.st_0);
                tetap.setChecked(true);
            } else {
                RadioButton kel = findViewById(R.id.st_1);
                kel.setChecked(true);
            }

            ltgl.setText("Tanggal dipilih : "+tgl);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.icon_dosen);
            requestOptions.error(R.drawable.icon_dosen);

            Glide.with(editor.this)
                    .load(gambar)
                    .apply(requestOptions)
                    .into(mGambar);

            switch (agama) {
                case AGAMA_ISLAM:
                    mAgamaSpinner.setSelection(1);
                    break;
                case AGAMA_KRISTEN:
                    mAgamaSpinner.setSelection(2);
                    break;
                case AGAMA_KATHOLIK:
                    mAgamaSpinner.setSelection(3);
                    break;
                case AGAMA_HINDU:
                    mAgamaSpinner.setSelection(4);
                    break;
                case AGAMA_BUDHA:
                    mAgamaSpinner.setSelection(5);
                    break;
                case AGAMA_KONGHUCU:
                    mAgamaSpinner.setSelection(6);
                    break;
                default:
                    mAgamaSpinner.setSelection(0);
                    break;
            }

        }
    }

    private void setupSpinner(){
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_agama_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mAgamaSpinner.setAdapter(genderSpinnerAdapter);

        mAgamaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.agama_islam))) {
                        mAgama = AGAMA_ISLAM;
                    } else if (selection.equals(getString(R.string.agama_kristen))) {
                        mAgama = AGAMA_KRISTEN;
                    } else if (selection.equals(getString(R.string.agama_katholik))) {
                        mAgama = AGAMA_KATHOLIK;
                    } else if (selection.equals(getString(R.string.agama_hindu))) {
                        mAgama = AGAMA_HINDU;
                    } else if (selection.equals(getString(R.string.agama_budha))) {
                        mAgama = AGAMA_BUDHA;
                    } else if (selection.equals(getString(R.string.agama_konghucu))) {
                        mAgama = AGAMA_KONGHUCU;
                    } else {
                        mAgama = AGAMA_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAgama = "";
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        if (kode == null){

            action.findItem(R.id.menu_edit).setVisible(false);
            action.findItem(R.id.menu_delete).setVisible(false);
            action.findItem(R.id.menu_save).setVisible(true);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                return true;
            case R.id.menu_edit:
                //Edit

                editMode();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert imm != null;
                imm.showSoftInput(mNama, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_delete).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;
            case R.id.menu_save:
                //Save

                if (kode == null) {

                    if (TextUtils.isEmpty(mNama.getText().toString()) ||
                            TextUtils.isEmpty(mKota.getText().toString()) ||
                            TextUtils.isEmpty(mAlamat.getText().toString()) ||
                            lahir == null  || mAgama == null || bitmap == null ||
                            TextUtils.isEmpty(mKode.getText().toString()) ){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setMessage("Tolong lengkapi semua field!");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }

                    else {

                        postData();
                        action.findItem(R.id.menu_edit).setVisible(true);
                        action.findItem(R.id.menu_save).setVisible(false);
                        action.findItem(R.id.menu_delete).setVisible(true);

                        readMode();

                    }

                } else {

                    updateData(kode);
                    action.findItem(R.id.menu_edit).setVisible(true);
                    action.findItem(R.id.menu_save).setVisible(false);
                    action.findItem(R.id.menu_delete).setVisible(true);

                    readMode();
                }

                return true;
            case R.id.menu_delete:

                AlertDialog.Builder dialog = new AlertDialog.Builder(editor.this);
                dialog.setMessage("Hapus dosen ini?");
                dialog.setPositiveButton("Yes" ,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteData(kode, gambar);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                mGambar.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void postData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        readMode();

        int selectedId = mStatus.getCheckedRadioButtonId();
        rStatus = findViewById(selectedId);

        int selectedIdKelamin = mKel.getCheckedRadioButtonId();
        rKel = findViewById(selectedIdKelamin);

        String kode = mKode.getText().toString().trim();
        String nama = mNama.getText().toString().trim();
        String kota = mKota.getText().toString().trim();
        String alamat = mAlamat.getText().toString().trim();
        String agama = mAgama;
        String status = String.valueOf(rStatus.getText());
        String kelamin = String.valueOf(rKel.getText());
        String tanggal = lahir;
        String gambar;
        if (bitmap == null) {
            gambar = "";
        } else {
            gambar = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Dosen> call = apiInterface.insert("insert", nama, kota, alamat, kode, agama, tanggal, status, kelamin, gambar);

        call.enqueue(new Callback<Dosen>() {
            @Override
            public void onResponse(@NonNull Call<Dosen> call, @NonNull Response<Dosen> response) {

                progressDialog.dismiss();

                Log.i(editor.class.getSimpleName(), response.toString());

                assert response.body() != null;
                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    finish();
                } else {
                    Toast.makeText(editor.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Dosen> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(editor.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateData(final String kode) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        int selectedId = mStatus.getCheckedRadioButtonId();
        rStatus = findViewById(selectedId);

        int selectedIdKelamin = mKel.getCheckedRadioButtonId();
        rKel = findViewById(selectedIdKelamin);

        String nama = mNama.getText().toString().trim();
        String kota = mKota.getText().toString().trim();
        String alamat = mAlamat.getText().toString().trim();
        String agama = mAgama;
        String status = String.valueOf(rStatus.getText());
        String kelamin = String.valueOf(rKel.getText());
        String tanggal = lahir;
        String gambar;
        if (bitmap == null) {
            gambar = "";
        } else {
            gambar = getStringImage(bitmap);
        }

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Dosen> call = apiInterface.update("update", nama, kota, alamat, kode, agama, tanggal, status, kelamin, gambar);

        call.enqueue(new Callback<Dosen>() {
            @Override
            public void onResponse(@NonNull Call<Dosen> call, @NonNull Response<Dosen> response) {

                progressDialog.dismiss();

                Log.i(editor.class.getSimpleName(), response.toString());

                assert response.body() != null;
                String value = response.body().getValue();
                String message = response.body().getMassage();

                if (value.equals("1")){
                    Toast.makeText(editor.this, message, Toast.LENGTH_SHORT).show();
                    readMode();
                } else {
                    Toast.makeText(editor.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<Dosen> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(editor.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteData(final String kode, final String pic) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        readMode();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Dosen> call = apiInterface.delete("delete", kode, pic);

        call.enqueue(new Callback<Dosen>() {
            @Override
            public void onResponse(@NonNull Call<Dosen> call, @NonNull Response<Dosen> response) {

                progressDialog.dismiss();

                Log.i(editor.class.getSimpleName(), response.toString());

                assert response.body() != null;
                String value = response.body().getValue();

                if (value.equals("1")){
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Dosen> call, @NonNull Throwable t) {
                progressDialog.dismiss();

            }
        });

    }

    void readMode(){

        mNama.setFocusableInTouchMode(false);
        mKode.setFocusableInTouchMode(false);
        mKota.setFocusableInTouchMode(false);
        mAlamat.setFocusableInTouchMode(false);
        satu.setEnabled(false);
        dua.setEnabled(false);
        tiga.setEnabled(false);
        empat.setEnabled(false);
        mTgl.setEnabled(false);
        mFabChoosePic.setEnabled(false);
        mAgamaSpinner.setEnabled(false);
    }

    private void editMode(){

        mNama.setFocusableInTouchMode(true);
        mKode.setFocusableInTouchMode(false);
        mKota.setFocusableInTouchMode(true);
        mAlamat.setFocusableInTouchMode(true);
        satu.setEnabled(true);
        dua.setEnabled(true);
        tiga.setEnabled(true);
        empat.setEnabled(true);
        mTgl.setEnabled(true);
        mFabChoosePic.setEnabled(true);
        mAgamaSpinner.setEnabled(true);

    }

}
