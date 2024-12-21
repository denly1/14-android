package com.example.tictac;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    SharedPreferences stats;
    SharedPreferences themeSettings;
    SharedPreferences.Editor editorSettings;
    ImageButton imageTheme;
    TextView tvStats;
    Button btnChangeTheme, btnStartGame, btnStartGameWithBot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stats = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        themeSettings=getSharedPreferences("SETTINGS", MODE_PRIVATE);
        EdgeToEdge.enable(this);

        tvStats = findViewById(R.id.tvStats);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnStartGameWithBot = findViewById(R.id.btnStartGameWithBot);
        imageTheme=findViewById(R.id.imageBtn);

        updateStats();

        updateImageButton();
        imageTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (themeSettings.getBoolean("MODE_NIGHT_ON", false)) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editorSettings = themeSettings.edit();
                    editorSettings.putBoolean("MODE_NIGHT_ON", false);
                    editorSettings.apply();
                    Toast.makeText(MainActivity.this, "Темная тема отключена", Toast.LENGTH_SHORT).show();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editorSettings = themeSettings.edit();
                    editorSettings.putBoolean("MODE_NIGHT_ON", true);
                    editorSettings.apply();
                    Toast.makeText(MainActivity.this, "Темная тема включена", Toast.LENGTH_SHORT).show();
                }
                updateImageButton();
                recreate();
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(false);
            }
        });

        btnStartGameWithBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(true);
            }
        });
    }

    private void updateImageButton() {
        if(themeSettings.getBoolean("MODE_NIGHT_ON", false)){
            imageTheme.setImageResource(R.drawable.day_night);
        }
        else{
            imageTheme.setImageResource(R.drawable.day_night);
        }
    }

    private void setCurrentTheme() {
        if(themeSettings.getBoolean("MODE_NIGHT_ON", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private void startGame(boolean withBot) {
        Intent intent = new Intent(this, TicTacActivity.class);
        intent.putExtra("withBot", withBot);
        startActivity(intent);
    }

    private void updateStats() {
        int wins = stats.getInt("wins", 0);
        int losses = stats.getInt("losses", 0);
        int draws = stats.getInt("draws", 0);

        tvStats.setText("X Победы: " + wins + " | O Победы: " + losses + " | Ничьи: " + draws);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStats();
    }


}