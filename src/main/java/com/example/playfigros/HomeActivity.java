package com.example.playfigros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private ImageView open_about, account;
    private ConstraintLayout about_menu;
    private Button close_about;
    private Button playGame;
    private Button settings;
    private Button quit, quit2, stay;
    private LinearLayout leave_popup;

    public void initAll(){
        account = findViewById(R.id.account);
        open_about = findViewById(R.id.about_button);
        close_about = findViewById(R.id.ok_aboutUS);
        playGame = findViewById(R.id.play);
        settings = findViewById(R.id.settings);
        quit2 = findViewById(R.id.leave_game);
        stay = findViewById(R.id.stay_in_game);
        quit = findViewById(R.id.leave);
        about_menu = findViewById(R.id.about_menu);
        leave_popup = findViewById(R.id.leave_popup);

        playGame.setBackgroundColor(Color.rgb(0,150,136));
        settings.setBackgroundColor(Color.rgb(0,150,136));
        quit.setBackgroundColor(Color.rgb(94,2,16));

        stay.setBackgroundColor(Color.rgb(0,150,136));
        quit2.setBackgroundColor(Color.rgb(94,2,16));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        View decorView = getWindow().getDecorView();
        int uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION ) ; // || View.SYSTEM_UI_FLAG_FULLSCREEN);
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_home);

        initAll();

        open_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                about_menu.setVisibility(View.VISIBLE);
            }
        });
        close_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                about_menu.setVisibility(View.GONE);
            }
        });
        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MySongList.class);
                startActivity(intent);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                //Toast.makeText(HomeActivity.this, "Open settings", Toast.LENGTH_SHORT).show();
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave_popup.setVisibility(View.VISIBLE);
            }
        });

        stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave_popup.setVisibility(View.GONE);
            }
        });

        quit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                System.exit(1);
            }
        });

    }



}