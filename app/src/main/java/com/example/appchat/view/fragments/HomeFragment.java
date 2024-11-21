package com.example.appchat.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.appchat.R;
import com.example.appchat.adapters.PostAdapter;
import com.example.appchat.databinding.FragmentHomeBinding;
import com.example.appchat.providers.AuthProvider;
import com.example.appchat.view.MainActivity;
import com.example.appchat.view.PostActivity;
import com.example.appchat.viewmodel.PostViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private AuthProvider authProvider;
    public HomeFragment() {
        // Constructor vacío requerido
    }

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authProvider=new AuthProvider(getContext());
        setHasOptionsMenu(true); // menú de opciones
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.tools);

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PostActivity.class);
            startActivity(intent);
        });

        PostViewModel postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postViewModel.getPosts().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null) {
                PostAdapter adapter = new PostAdapter(posts);
                binding.recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu); // Infla el menú de opciones
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itemLogout) {
            authProvider.logout().observe(getViewLifecycleOwner(), logoutResult -> {
                if (logoutResult != null && logoutResult) {
                    Log.d("AuthProvider", "Cierre de sesión exitoso, redirigiendo a MainActivity...");
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("fromLogout", true); // Indicar que viene de un logout
                    startActivity(intent);
                } else {
                    Log.e("AuthProvider", "Error al cerrar sesión.");
                    Toast.makeText(getContext(), "Error al cerrar sesión. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
                }
            });


            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiar el binding para evitar fugas de memoria
        binding = null;
    }
}


