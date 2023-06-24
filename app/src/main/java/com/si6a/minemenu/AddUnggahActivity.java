package com.si6a.minemenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.si6a.minemenu.APIService;
import com.si6a.minemenu.Utilities;
import com.si6a.minemenu.ValueNoData;
import com.si6a.minemenu.databinding.ActivityAddUnggahBinding;

import com.si6a.minemenu.databinding.ActivityAddUnggahBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUnggahActivity extends AppCompatActivity {
    private ActivityAddUnggahBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddUnggahBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaevent = binding.etNamamenu.getText().toString();
                String alamat = binding.etHarga.getText().toString();
                String deskripsi = binding.etDeskripsi.getText().toString();

                boolean bolehUnggah = true;
                if (TextUtils.isEmpty(namaevent)){
                    bolehUnggah =false;
                    binding.etNamamenu.setError("Nama Barang Tidak Boleh Kosong");
                }
                if (TextUtils.isEmpty(alamat)){
                    bolehUnggah =false;
                    binding.etNamamenu.setError("Alamat Tidak Boleh Kosong");
                }
                if (TextUtils.isEmpty(deskripsi)){
                    bolehUnggah =false;
                    binding.etNamamenu.setError("Deskripsi Tidak Boleh Kosong");
                }
                if (bolehUnggah){
                    String userId = Utilities.getValue(AddUnggahActivity.this,"xUserId");
                    addUnggah(userId,namaevent,alamat,deskripsi);
                }
            }
        });
    }

    private void addUnggah(String userId, String namaevent, String alamat, String deskripsi) {
        binding.progressBar.setVisibility(View.VISIBLE);
        APIService api = Utilities.getRetrofit().create(APIService.class);
        Call<ValueNoData> call = api.addUnggah(namaevent, alamat, deskripsi, userId);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if (success == 1) {
                        Toast.makeText(AddUnggahActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddUnggahActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddUnggahActivity.this, "Response " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error :" + t.getMessage());
                Toast.makeText(AddUnggahActivity.this, "Retrofit Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}