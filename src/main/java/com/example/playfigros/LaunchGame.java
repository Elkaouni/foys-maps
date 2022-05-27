package com.example.playfigros;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class LaunchGame extends AppCompatActivity {

    private CardView foys, logos;

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
        setContentView(R.layout.activity_launch_game);

        foys = findViewById(R.id.game_name);
        logos = findViewById(R.id.logos);

        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);


        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
                                            public void onAnimationStart(Animation animation) {}
                                            public void onAnimationRepeat(Animation animation) {}
                                            public void onAnimationEnd(Animation animation) {
                                            }
                                        });

        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) { }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationEnd(Animation animation) {
                logos.setVisibility(View.GONE);
        }});

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    logos.setVisibility(View.VISIBLE);
                    sleep(2000);
                    logos.startAnimation(animFadeOut);
                }
                catch (Exception e){
                    Log.e("error", e.toString());
                }
            }
        });
        thread.start();
        foys.setVisibility(View.VISIBLE);

        foys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        try {
            thread.destroy();
        }
        catch (Exception e){
            Log.e("error", e.toString());
        }
    }
}