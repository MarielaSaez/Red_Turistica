package com.example.appchat.view;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appchat.databinding.ActivityUserBinding;
import com.example.appchat.model.User;
import com.example.appchat.viewmodel.UserViewModel;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    private UserViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        setupObservers();
        setupListeners();
    }

    private void setupObservers() {
        // Observamos los cambios en el estado de la operación para mostrar mensajes de éxito o error
        viewModel.getOperationStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                Toast.makeText(UserActivity.this, status, Toast.LENGTH_SHORT).show();
                limpiar();
            }
        });

        // Observamos el usuario actual para mostrarlo en la interfaz
        viewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {

                    mostrarUsuarioEnUI(user);
                }
            }
        });
    }

    private void setupListeners() {
        // Listener para botón de crear usuario
        binding.btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User usuario = obtenerDatosDeUsuario();
                viewModel.createUser(usuario, UserActivity.this);
            }
        });

        // Listener para botón de actualizar usuario
        binding.btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User usuario = obtenerDatosDeUsuario();
                viewModel.updateUser(usuario, UserActivity.this);
            }
        });

        // Listener para botón de eliminar usuario
        binding.btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = binding.itId.getText().toString().trim();
                viewModel.deleteUser(id, UserActivity.this);
            }
        });

        // Listener para botón de búsqueda de usuario
        binding.btnReadUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.itEmail.getText().toString().trim();

                viewModel.getUser(email, UserActivity.this);

            }
        });

        binding.circleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private User obtenerDatosDeUsuario() {
        String username = binding.itUsuario.getText().toString();
        String email = binding.itEmail.getText().toString().trim();
        String id = binding.itId.getText().toString().trim();
        String password = binding.itPassword.getText().toString().trim();
        return new User(id, username, email, password);
    }

    private void mostrarUsuarioEnUI(User user) {
        binding.itUsuario.setText(user.getUsername());
        binding.itEmail.setText(user.getEmail());
        binding.itId.setText(user.getId());
        binding.itPassword.setText(user.getPassword());
        Log.d("mostrar", user.getId()+"-"+user.getUsername());
    }

    private void limpiar() {
        binding.itUsuario.setText("");
        binding.itEmail.setText("");
        binding.itId.setText("");
        binding.itPassword.setText("");

    }
}



