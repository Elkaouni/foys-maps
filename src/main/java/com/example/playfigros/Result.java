package com.example.playfigros;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private TextView gameover, score_label, highest_score_label, miss_label, good_label, perfect_label;
    private ImageButton try_again_btn;
    private ImageView continue_btn;

    void initAll(){
        gameover = findViewById(R.id.game_over);
        highest_score_label = findViewById(R.id.high_score);
        score_label = findViewById(R.id.final_score);
        good_label = findViewById(R.id.good);
        miss_label = findViewById(R.id.miss);
        try_again_btn = findViewById(R.id.try_again);
        continue_btn = findViewById(R.id.back);

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

        int score = getIntent().getIntExtra("score",0);
        int miss = getIntent().getIntExtra("miss",0);
        int good = getIntent().getIntExtra("good",0);
        String scr = ""+score;
        String miss_l = ""+miss;
        String good_l = ""+good;
        score_label.setText(scr);
        miss_label.setText(miss_l);
        good_label.setText(good_l);


        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH-SCORE", 0);

        if (score > highScore){
            highest_score_label.setText(scr);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH-SCORE", score);
            editor.commit();
        }
        else
            highest_score_label.setText(" "+highScore);
    }

    public void tryAgain(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        String game_mode = getIntent().getStringExtra("game_mode");
        intent.putExtra("game_mode",game_mode);
        startActivity(intent);
        finish();
    }

    public void backToStart(View view){
        startActivity(new Intent(getApplicationContext(), com.example.playfigros.Start.class));
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













