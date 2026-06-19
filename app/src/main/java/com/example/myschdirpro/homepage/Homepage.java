package com.example.myschdirpro.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myschdirpro.R;
import com.example.myschdirpro.timetable.Timetable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Homepage extends AppCompatActivity {

    private void setQuickAccess(){
        View mapView = findViewById(R.id.quickMap);
        ImageView mapIcon = mapView.findViewById(R.id.iconQuickAccess);
        TextView mapText = mapView.findViewById(R.id.txtQuickAccess);

        mapIcon.setImageResource(R.drawable.map);
        mapText.setText("Map");


    }


    private void setBottomBar(){
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_home) {
                startActivity(new Intent(this, Homepage.class));
                return true;
            }

            if (id == R.id.nav_timetable) {
                startActivity(new Intent(this, Timetable.class));
                return true;
            }

            /*if (id == R.id.nav_map) {
                startActivity(new Intent(this, CampusMap.class));
                return true;
            }*/

            /*if (id == R.id.nav_messages) {
                startActivity(new Intent(this, Messages.class));
                return true;
            }*/

            return false;
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setBottomBar();

        setQuickAccess();
    }
}