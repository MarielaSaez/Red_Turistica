package com.example.appchat.view;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import com.example.appchat.databinding.ActivityPostBinding;
import com.example.appchat.model.Post;
import com.example.appchat.viewmodel.PostViewModel;
import com.google.android.material.chip.Chip;
import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {

    private ActivityPostBinding binding;
    private PostViewModel postViewModel;

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
    }

    private void publishPost() {
        String titulo = binding.itPassword.getText().toString();
        String descripcion = binding.etDescripcion.getText().toString();
        String ubicacion = binding.etUbicacion.getText().toString();
        String duracion = binding.etDuracion.getText().toString();
        double presupuesto = Double.parseDouble(binding.etPresupuesto.getText().toString());

        // Obtener la categoría seleccionada
        int selectedChipId = binding.chipGroupCategoria.getCheckedChipId();
        Chip selectedChip = findViewById(selectedChipId);
        String categoria = (selectedChip != null) ? selectedChip.getText().toString() : "";

        // Crear el objeto Post
        Post post = new Post(titulo, descripcion, ubicacion, categoria, duracion, presupuesto, new ArrayList<>());

        // Llamar al método de publicación en el ViewModel
        postViewModel.publishPost(post);
    }
}

