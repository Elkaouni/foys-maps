package com.example.playfigros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Start extends AppCompatActivity {
    private String game_mode="easy";
    private String map_title = "Disorder ft. Hyun";
    private TextView score_label ;
    private TextView title_label;
    private CardView easy_btn, hard_btn;

    private void initAll(){
        score_label = findViewById(R.id.last_score);
        title_label = findViewById(R.id.title);
        easy_btn= findViewById(R.id.easy_mode);
        hard_btn=findViewById(R.id.hard_mode);

        title_label.setText(map_title);
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH-SCORE", 0);
        score_label.setText("Highest Score: "+highScore);
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

        setContentView(R.layout.activity_start);
        initAll();

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("game_mode", game_mode);
                startActivity(intent);
            }
        });

        easy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                game_mode = "easy";
                easy_btn.setCardBackgroundColor(Color.rgb(3,218,197));
                hard_btn.setCardBackgroundColor(Color.BLACK);
            }
        });
        hard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                game_mode = "hard";
                hard_btn.setCardBackgroundColor(Color.rgb(3,218,197));
                easy_btn.setCardBackgroundColor(Color.BLACK);
            }
        });
    }


}