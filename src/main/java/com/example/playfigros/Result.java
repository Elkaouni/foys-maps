package com.example.playfigros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Result extends AppCompatActivity {

    private TextView gameover, score_label, highest_score_label, miss_label, good_label, difficulty, song_title;
    private ImageButton try_again_btn;
    private ImageView continue_btn;

    // Song Data
    private String game_mode;
    private String map_title;
    private String author;
    private int song_cover;
    private int song_wallpaper;
    private int song_mp3;
    private void getIntenExtras(){
        game_mode = getIntent().getStringExtra("game_mode");
        map_title = getIntent().getStringExtra("song_title") ;
        author = getIntent().getStringExtra("song_author");
        song_cover = getIntent().getIntExtra("song_cover",0);
        song_wallpaper = getIntent().getIntExtra("song_map_bg",0);
        song_mp3 = getIntent().getIntExtra("song_mp3",0);
    }
    public void passSongExtras(Intent intent){
        intent.putExtra("song_title", map_title);
        intent.putExtra("song_author", author);
        intent.putExtra("song_cover", song_cover);
        intent.putExtra("song_map_bg", song_wallpaper);
        intent.putExtra("song_mp3", song_mp3);
    }



    void initAll(){
        gameover = findViewById(R.id.game_over);
        highest_score_label = findViewById(R.id.high_score);
        score_label = findViewById(R.id.final_score);
        good_label = findViewById(R.id.good);
        miss_label = findViewById(R.id.miss);
        try_again_btn = findViewById(R.id.try_again);
        continue_btn = findViewById(R.id.back);
        difficulty= findViewById(R.id.difficulty);
        song_title= findViewById(R.id.song_title);

        getIntenExtras();

        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToStart(view);
            }
        });
        try_again_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tryAgain(view);
            }
        });

        difficulty.setText(game_mode);
        song_title.setText(map_title + "-" + author);
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
        setContentView(R.layout.activity_result);

        initAll();

        findViewById(R.id.result_background).setBackground(getResources().getDrawable(song_wallpaper));

        int score = getIntent().getIntExtra("score",0);
        int miss = getIntent().getIntExtra("miss",0);
        int good = getIntent().getIntExtra("good",0);
        String scr = ""+score;
        String miss_l = ""+miss;
        String good_l = ""+good;
        score_label.setText(scr);
        miss_label.setText(miss_l);
        good_label.setText(good_l);

        String song_high_score = "HIGH-SCORE-"+map_title+'-'+game_mode;
        Log.e("tag", "songhigscore ---" +song_high_score);
        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt(song_high_score, 0);

        if (score > highScore){
            highest_score_label.setText(scr);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(song_high_score, score);
            editor.commit();
        }
        else
            highest_score_label.setText(" "+highScore);
    }

    public void tryAgain(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        String game_mode = getIntent().getStringExtra("game_mode");
        intent.putExtra("game_mode",game_mode);
        passSongExtras(intent);
        startActivity(intent);
        finish();
    }

    public void backToStart(View view){
        Intent intent = new Intent(getApplicationContext(), com.example.playfigros.Start.class);
        passSongExtras(intent);
        startActivity(intent);
        finish();
    }

    // disable return button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }


}













