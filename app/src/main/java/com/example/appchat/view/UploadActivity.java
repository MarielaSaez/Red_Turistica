package com.example.appchat.view;
import com.example.appchat.R;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.appchat.providers.PostProvider;
import com.example.appchat.util.FileUtils;
import com.example.appchat.viewmodel.ImageViewModel;
import com.parse.ParseException;
import com.parse.ParseFile;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import android.Manifest;
import com.example.appchat.databinding.ActivityUploadBinding;
import com.example.appchat.model.Post;
import com.example.appchat.util.Validaciones;
import com.example.appchat.viewmodel.UploadViewModel;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;

import java.util.List;



public class UploadActivity extends AppCompatActivity {

    private ActivityUploadBinding binding;
    private UploadViewModel uploadViewModel;
    private static final int REQUEST_IMAGE = 1;

    private List<String> imagenesUrls = new ArrayList<>();

    private Uri uri;
    private String id_user;
    private String categoria;
    private int cant = 0;
    private ImageAdapter adapter;
    private ImageViewModel imageViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the view model
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);

        // Observe image URLs list to update the RecyclerView when it changes
        imageViewModel.getImagenesUrls().observe(this, urls -> {
            adapter.updateImages(urls);  // Assure the adapter has a method to update images
        });

        id_user = getIntent().getStringExtra("user_id");
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerView.setAdapter(new ImageAdapter(imagenesUrls, this));
        binding.recyclerView.setVisibility(View.GONE);

        setupCategorySpinner();
        setupViewModel();

        binding.uploadImage.setOnClickListener(v -> requestPermission());
        binding.btnPublicar.setOnClickListener(v ->  publicarPost());
        adapter = new ImageAdapter(imagenesUrls, this);
        binding.recyclerView.setAdapter(adapter);


    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, getResources().getStringArray(R.array.categorias_array));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoria.setAdapter(adapter);
        binding.spinnerCategoria.setSelection(0);
        binding.spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                categoria = (String) parentView.getItemAtPosition(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void setupViewModel() {
        uploadViewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        uploadViewModel.getPostSuccess().observe(this, success -> {

            Toast.makeText(this, success ? "Publicación exitosa" : "Error al publicar", Toast.LENGTH_SHORT).show();
            Log.d("TAG#", "Post upload success: " + success);
            if (success) {
                Toast.makeText(this, "Post published successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error while publishing", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleImageUpload() {
        if (uri != null) {
            try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                byte[] imageBytes = getBytesFromInputStream(inputStream);
                ParseFile parseFile = new ParseFile("image.jpg", imageBytes);
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            String imageUrl = parseFile.getUrl();
                            imageViewModel.agregarImagenUrl(imageUrl);
                            imagenesUrls.add(imageUrl);
                            Log.e("TAG# 2", imageUrl);
                        } else {
                            Log.e("TAG#", "Error uploading image to Parse", e);
                            Toast.makeText(UploadActivity.this, "Error uploading the image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Seleccione una imagen primero", Toast.LENGTH_SHORT).show();
        }
    }



    private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void requestPermission() {
        String permission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
                Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_IMAGE);
        }
        handleImageUpload();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    uri = result.getData().getData();
                    cant++;
                    if (cant == 4) {

                        mostrarRecyclerViewEnLugarDeImagen();
                    }

                    onImageSelected(uri.toString());
                } else {
                    Toast.makeText(this, "Imagen no seleccionada", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void publicarPost() {

        String titulo = binding.itTitulo.getText().toString().trim();
        String descripcion = binding.etDescripcion.getText().toString().trim();
        String duracion = binding.etDuracion.getText().toString().trim();
        String presupuestoStr = binding.etPresupuesto.getText().toString().trim();
        Log.d("TAG# 1","En POST - contenido "+titulo+duracion+categoria+presupuestoStr+imagenesUrls);
        if (!Validaciones.validarTexto(titulo)) {
            binding.itTitulo.setError("Error en el título");
            return;
        }
        if (descripcion.isEmpty()) {
            binding.etDescripcion.setError("La descripción no puede estar vacía");
            return;
        }
        if (Validaciones.validarNumero(duracion) == -1) {
            binding.etDuracion.setError("Error en duración");
            return;
        }
        if (Validaciones.validarNumero(presupuestoStr) == -1) {
            binding.etPresupuesto.setError("El presupuesto no puede estar vacío");
            return;
        }

        double presupuesto;
        try {
            presupuesto = Double.parseDouble(presupuestoStr);
        } catch (NumberFormatException e) {
            binding.etPresupuesto.setError("Presupuesto inválido");
            return;
        }
        Log.d("TAG#", imagenesUrls.toString());
        Post post = new Post(titulo, descripcion, Integer.parseInt(duracion), categoria, presupuesto, imagenesUrls, id_user);
        uploadViewModel.publicar(post);
    }
    private void mostrarRecyclerViewEnLugarDeImagen() {
        // Oculta la imagen
        binding.uploadImage.setVisibility(View.GONE);

        // Ajusta el tamaño del RecyclerView
        ViewGroup.LayoutParams layoutParams = binding.recyclerView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        binding.recyclerView.setLayoutParams(layoutParams);

        // Asegúrate de mostrar el RecyclerView si estaba oculto
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    public void onImageSelected(String imagePath) {

        updateRecyclerViewVisibility();
        adapter.notifyDataSetChanged();

    }


    private void updateRecyclerViewVisibility() {
        binding.recyclerView.setVisibility(imagenesUrls.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
