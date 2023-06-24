package com.si6a.minemenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.si6a.minemenu.databinding.ActivityUpdateUnggahBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUnggahActivity extends AppCompatActivity {
    private ActivityUpdateUnggahBinding binding;
    private Unggah unggah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUpdateUnggahBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_update_unggah);

        unggah = getIntent().getParcelableExtra("EXTRA_DATA");
        String id = unggah.getId();

        binding.etNamamenu.setText(unggah.getNamamenu());
        binding.etHarga.setText(unggah.getHarga());
        binding.etDeskripsi.setText(unggah.getDeskripsi());

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namamenu = binding.etNamamenu.getText().toString();
                String harga = binding.etHarga.getText().toString();
                String deskripsi = binding.etDeskripsi.getText().toString();

                boolean bolehUpdate = true;
                if (TextUtils.isEmpty(namamenu)){
                    bolehUpdate =false;
                    binding.etNamamenu.setError("Please fill the Name of Menu");
                }
                if (TextUtils.isEmpty(harga)){
                    bolehUpdate =false;
                    binding.etNamamenu.setError("Price cannot empty, please fill first");
                }
                if (TextUtils.isEmpty(deskripsi)){
                    bolehUpdate =false;
                    binding.etNamamenu.setError("Description cannot empty, please fill first");
                }
                if (bolehUpdate){
                    updateUnggah(id,namamenu,harga,deskripsi);
                }
            }
        });

    }

    private void updateUnggah(String id, String namamenu, String harga, String deskripsi) {
        binding.progressBar.setVisibility(View.VISIBLE);
        APIService api = Utilities.getRetrofit().create(APIService.class);
        Call<ValueNoData> call = api.updateUnggah(id, namamenu, harga, deskripsi);
        call.enqueue(new Callback<ValueNoData>() {
            @Override
            public void onResponse(Call<ValueNoData> call, Response<ValueNoData> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();

                    if (success == 1) {
                        Toast.makeText(UpdateUnggahActivity.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpdateUnggahActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(UpdateUnggahActivity.this, "Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ValueNoData> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                System.out.println("Retrofit Error :" + t.getMessage());
                Toast.makeText(UpdateUnggahActivity.this, "Retrofit Error :" + t.getMessage(), Toast.LENGTH_SHORT).show();
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