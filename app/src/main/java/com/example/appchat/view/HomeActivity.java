package com.example.appchat.view;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.appchat.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.appchat.databinding.ActivityHomeBinding;
import com.example.appchat.view.fragments.ChatsFragment;
import com.example.appchat.view.fragments.FiltrosFragment;
import com.example.appchat.view.fragments.HomeFragment;
import com.example.appchat.view.fragments.PerfilFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    final int HOME = R.id.itemHome;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getStringExtra("user_id");

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itemHome) {
                    openFragment(HomeFragment.newInstance(userId)); // Pasamos userId aquí
                } else if (item.getItemId() == R.id.itemChats) {
                    openFragment(new ChatsFragment());
                } else if (item.getItemId() == R.id.itemPerfil) {
                    openFragment(new PerfilFragment());
                } else if (item.getItemId() == R.id.itemFiltros) {
                    openFragment(new FiltrosFragment());
                }
                return true;
            }
        });

        openFragment(HomeFragment.newInstance(userId)); // Pasa userId aquí también
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}

