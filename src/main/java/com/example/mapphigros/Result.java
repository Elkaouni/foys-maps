package com.example.mapphigros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    private TextView gameover, score_label, highest_score_label;
    private Button btn_try_again;

    void initAll(){
        gameover = findViewById(R.id.game_over);
        highest_score_label = findViewById(R.id.high_score);
        score_label = findViewById(R.id.final_score);
        btn_try_again = findViewById(R.id.try_agin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        initAll();

        int score = getIntent().getIntExtra("score",0);
        score_label.setText("Score: "+ score);

        if(score >=100)
            gameover.setText("Congratz. You won!");
        else
            gameover.setText("Too bad. You lost!");

        SharedPreferences settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH-SCORE", 0);

        if (score > highScore){
            highest_score_label.setText("High Score: " + score);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH-SCORE", score);
            editor.commit();
        }
        else
            highest_score_label.setText("High Score: " + highScore);
    }

    public void tryAgain(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void backToStart(View view){
        startActivity(new Intent(getApplicationContext(), Start.class));
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













