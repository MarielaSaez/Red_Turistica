package com.example.appchat.view;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.appchat.databinding.ActivityMainBinding;
import com.example.appchat.providers.AuthProvider;
import com.example.appchat.util.Validaciones;
import com.example.appchat.viewmodel.MainViewModel;
import com.example.appchat.viewmodel.MainViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private AuthProvider authProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(this)).get(MainViewModel.class);
        authProvider=new AuthProvider(this);
        manejarEventos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica si el Intent contiene el extra "fromLogout"
       /*
        boolean fromLogout = getIntent().getBooleanExtra("fromLogout", false);

        if (!fromLogout && authProvider.getCurrentUserID() != null) {
            Log.d("Main#", "Usuario activo detectado. Redirigiendo a HomeActivity.");
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        } else if (fromLogout) {
            Log.d("Main#", "Actividad iniciada después de un logout.");
        } else {
            Log.d("Main#", "No hay usuario activo. Permaneciendo en MainActivity.");
        }*/
    }


    private void manejarEventos() {
        binding.tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.itUsuario.getText().toString().trim();
                String pass = binding.itPassword.getText().toString().trim();

                if (!Validaciones.validarMail(email)) {
                    showToast("Email incorrecto");
                    return;
                }
                if (!Validaciones.controlarPasword(pass)) {
                    showToast("Password incorrecto");
                    return;
                }

        viewModel.login(email, pass).observe(MainActivity.this, user_id -> {
               if (user_id!=null) {

                   Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                //   intent.putExtra("user_id",user_id);
                   startActivity(intent);
               } else {
                   showToast("Login fallido");
               }
           });
        }
    });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
   @Override
   protected void onResume() {
        super.onResume();
        limpiarCampos();
   }
   private void limpiarCampos() {
        binding.itUsuario.setText("");
        binding.itPassword.setText("");

    }


}
