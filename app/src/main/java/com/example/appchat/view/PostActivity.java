package com.example.appchat.view;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.appchat.databinding.ActivityPostBinding;
import com.example.appchat.model.Post;
import com.example.appchat.providers.CloudinaryResponse;
import com.example.appchat.providers.CloudinaryService;
import com.example.appchat.viewmodel.PostViewModel;
import com.google.android.material.chip.Chip;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private PostViewModel postViewModel;
    private String YOUR_API_KEY = "525875494321911";
    private String YOUR_API_SECRET = "YOUR_API_SECRET"; // Añade tu API secret de Cloudinary
    private String YOUR_CLOUD_NAME = "YOUR_CLOUD_NAME"; // Añade tu Cloudinary Cloud Name
    private String YOUR_UPLOAD_PRESET = "YOUR_UPLOAD_PRESET"; // Añade tu upload preset de Cloudinary
    private int REQUEST_IMAGE = 1;
    private Uri imagePath;
    private List<String> imagenesUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ViewModelProvider para obtener el PostViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Observar el estado de éxito de publicación
        postViewModel.getPostSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Publicación exitosa", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al publicar", Toast.LENGTH_SHORT).show();
            }
        });

        // Configuración de los chips de categoría
        binding.chipGroupCategoria.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = findViewById(checkedId);
            if (chip != null) {
                String categoria = chip.getText().toString();
                binding.etUbicacion.setText(categoria);
            }
        });

        // Configurar el botón de publicación
        binding.btnPublicar.setOnClickListener(v -> publishPost());

        // Botón para abrir la galería y seleccionar una imagen
        binding.btnAgregarImagen.setOnClickListener(v -> openGallery());
    }

    // Método para abrir la galería y seleccionar una imagen
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            uploadImageToCloudinary(imageUri);
        }
    }

    // Método para cargar la imagen en Cloudinary
    private void uploadImageToCloudinary(Uri imageUri) {
        File file = new File(getRealPathFromUri(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // Generar timestamp y signature aquí
        long timestamp = System.currentTimeMillis() / 1000L;
        String signature = generateSignature(timestamp);

        CloudinaryService service = RetrofitClientInstance.getRetrofitInstance().create(CloudinaryService.class);
        Call<CloudinaryResponse> call = service.uploadImage(body, YOUR_API_KEY, timestamp, signature, YOUR_UPLOAD_PRESET);
        call.enqueue(new Callback<CloudinaryResponse>() {
            @Override
            public void onResponse(Call<CloudinaryResponse> call, Response<CloudinaryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageUrl = response.body().getUrl();
                    // Guarda el enlace de la imagen en la lista de URLs
                    imagenesUrls.add(imageUrl);
                    updateRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<CloudinaryResponse> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Error al subir imagen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para obtener el path real de la Uri de la imagen
    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        return null;
    }

    // Método para generar la firma para Cloudinary
    private String generateSignature(long timestamp) {
        String stringToSign = "timestamp=" + timestamp + YOUR_API_SECRET;
        return md5(stringToSign);
    }

    private String md5(String s) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String h = Integer.toHexString(0xFF & b);
                while (h.length() < 2) h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // Método para publicar un post
    private void publishPost() {
        String titulo = binding.itTitulo.getText().toString();
        String descripcion = binding.etDescripcion.getText().toString();
        String ubicacion = binding.etUbicacion.getText().toString();
        String duracion = binding.etDuracion.getText().toString();
        double presupuesto = Double.parseDouble(binding.etPresupuesto.getText().toString());

        int selectedChipId = binding.chipGroupCategoria.getCheckedChipId();
        Chip selectedChip = findViewById(selectedChipId);
        String categoria = (selectedChip != null) ? selectedChip.getText().toString() : "";

        Post post = new Post(titulo, descripcion, ubicacion, categoria, duracion, presupuesto, imagenesUrls);

        postViewModel.publishPost(post);
    }

    private void updateRecyclerView() {
        // Configurar el RecyclerView si no está configurado
        if (binding.rvImagenes.getAdapter() == null) {
            // Inicializar el adaptador con la lista de URLs de imágenes
            ImageAdapter imageAdapter = new ImageAdapter(imagenesUrls);
            binding.rvImagenes.setAdapter(imageAdapter);
            binding.rvImagenes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        } else {
            // Notificar al adaptador que los datos han cambiado
            binding.rvImagenes.getAdapter().notifyDataSetChanged();
        }
    }

}


