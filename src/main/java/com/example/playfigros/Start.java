package com.example.playfigros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class Start extends AppCompatActivity {
    private String game_mode="easy";

    private TextView score_label ;
    private TextView title_label;
    private ImageView cover;
    private ImageView back;
    private CardView easy_btn;
    private CardView hard_btn;

    private String map_title;
    private String author;
    private int song_cover;
    private int song_wallpaper;
    private int song_mp3;
    private void getIntenExtras(){
        map_title = getIntent().getStringExtra("song_title") ;
        author = getIntent().getStringExtra("song_author");
        song_cover = getIntent().getIntExtra("song_cover",0);
        song_wallpaper = getIntent().getIntExtra("song_map_bg",0);
        song_mp3 = getIntent().getIntExtra("song_mp3",0);
    }

    public void passExtras(Intent intent){
        intent.putExtra("song_title", map_title);
        intent.putExtra("song_author", author);
        intent.putExtra("song_cover", song_cover);
        intent.putExtra("song_map_bg", song_wallpaper);
        intent.putExtra("song_mp3", song_mp3);
    }

    public void set_highscore(){
        String song_high_score = "HIGH-SCORE-"+map_title+'-'+game_mode;
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt(song_high_score, 0);
        score_label.setText("Highest Score: "+highScore);
    }

    private void initAll(){
        score_label = findViewById(R.id.last_score);
        title_label = findViewById(R.id.title);
        cover = findViewById(R.id.song_cover);
        easy_btn= findViewById(R.id.easy_mode);
        hard_btn=findViewById(R.id.hard_mode);
        back = findViewById(R.id.back_to_song_list);
        getIntenExtras();

        set_highscore();


        Log.e("song_wallpaper", String.valueOf(song_wallpaper));
        Log.e("song_cover", String.valueOf(song_cover));
        Log.e("song_mp3", String.valueOf(song_mp3));
        title_label.setText(map_title + "-"+author);
        cover.setImageResource(song_cover);
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

        // init high scores

        if(false){
         String song_high_score = "HIGH-SCORE-"+map_title+'-'+game_mode;
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(song_high_score, 0);
        editor.commit();
        }


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("game_mode", game_mode);
                passExtras(intent);
                startActivity(intent);
            }
        });

        easy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                game_mode = "easy";
                easy_btn.setCardBackgroundColor(Color.rgb(3,218,197));
                hard_btn.setCardBackgroundColor(Color.BLACK);
                set_highscore();
            }
        });
        hard_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                game_mode = "hard";
                hard_btn.setCardBackgroundColor(Color.rgb(3,218,197));
                easy_btn.setCardBackgroundColor(Color.BLACK);
                set_highscore();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(getApplicationContext(), com.example.playfigros.MySongList.class));
                finish();
            }
        });
    }


}