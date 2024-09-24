package com.example.expensetracky;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.expensetracky.databinding.ActivityTransactionsBinding;

public class Transactions extends AppCompatActivity {

    private ActivityTransactionsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTransactionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // BottomNavigationView setup
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of top-level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        // Setting up NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_transactions);

        // Remove the setupActionBarWithNavController line (it’s only needed if you are using ActionBar)
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // Link BottomNavigationView with NavController
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
