package com.example.mapphigros;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //constantes
    private static final int  pas_win = 5;
    private static final int  pas_loose = 5;
    private static final int max_misses = 10;
    private static final int goal = 1000;
    private static final int nb_buttons = 4 ;

    private static final int GreenInitSpeed = 8;
    private static final int BlackInitSpeed = 5;
    private static final int ScreenOffsetY = 400;
    private  int CurrantNbreButton;

    // sound tracks
    private SoundPlayer soundPlayer;

    // view elements
    private TextView score_label, log;
    private TextView tap_to_start;
    private FrameLayout frame;
    private Button line;
    private ImageView blackButton, greenButton;
    private ImageView[] black_buttons = new ImageView[nb_buttons];
    private ImageView[] green_buttons = new ImageView[nb_buttons];


    // score
    private int score = 500;
    private int perfect, good, bad, miss;

    // sizes
    private int frameHeight;
    private int screenWidth, screenHeight;
    private int boxSize;
    private int black_btn_width, green_btn_width;

    // position
    private float lineX, lineY;
    private float blackKeyX, blackKeyY;
    private float greenkeysX, greenkeysY;
    private float[][] blackkeys_xy = new float[nb_buttons][2];
    private float[][] greenkeys_xy = new float[nb_buttons][2];


    //initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //status check
    //private boolean action_fig = false;
    private boolean start_flag = false;

    // touch mode
    private float xx, yy;

    //timer
    private int start=0;
    private float init_X;

    void initAll(){
        //get screen size
        WindowManager we = getWindowManager();
        Display disp = we.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;

        score_label = findViewById(R.id.score_label);
        log = findViewById(R.id.log);
        tap_to_start = findViewById(R.id.tap_to_start);
        frame = findViewById(R.id.frame);
        line = findViewById(R.id.limit);
        // blackButton = findViewById(R.id.btn_1);
        // greenButton = findViewById(R.id.green_1);

        for(int i =0; i<nb_buttons ; i++){
            String black_id = "btn_" +(i+1);
            String green_id = "green_" +(i+1);

            int resID = getResources().getIdentifier(black_id, "id", getPackageName());
            ImageView btn = findViewById(resID);
            black_buttons[i] = btn;

            resID = getResources().getIdentifier(green_id, "id", getPackageName());
            btn = findViewById(resID);
            green_buttons[i] = btn;
        }

        CurrantNbreButton = 1;

        black_btn_width = black_buttons[0].getWidth();
        green_btn_width = green_buttons[0].getWidth();

        init_X = screenWidth+ black_btn_width + green_btn_width;

        score_label.setText("Score: " + score);

    }

    void ini_black_XY(){
        for(int i=0; i<nb_buttons; i++){
            blackkeys_xy[i][0] = init_X;
             float val = (float) (Math.random()) * (frameHeight - black_btn_width - ScreenOffsetY);
             blackkeys_xy[i][1] = val;

        }
        // blackKeyX = init_X ;
        // blackKeyY = (float) (Math.random()) * (frameHeight - blackButton.getWidth());
    }

    void ini_green_XY(){
        // greenkeysX = init_X ;
        // greenkeysY = (float) (Math.random()) * (frameHeight - greenButton.getWidth());
        for(int i=0; i<nb_buttons; i++){
            greenkeys_xy[i][0] = init_X;
            float val = (float) (Math.random()) * (frameHeight - green_btn_width - ScreenOffsetY);
            greenkeys_xy[i][1] = val;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //soundPlayer = new SoundPlayer(this);

        initAll();

        // move out of screen
        /*blackButton.setX(blackKeyX);
        greenButton.setX(greenkeysX);
        blackButton.setY((float) Math.random() * (500 - blackButton.getWidth()));
        greenButton.setY((float) Math.random() * (500 - greenButton.getWidth()));*/

        for(int i=0; i<nb_buttons; i++){
            blackkeys_xy[i][0] = init_X;
            blackkeys_xy[i][0] = init_X;

            black_buttons[i].setX(blackkeys_xy[i][0]);
            green_buttons[i].setX(greenkeys_xy[i][0]);

            black_buttons[i].setY((float) (Math.random()) * (frameHeight - black_btn_width));
            green_buttons[i].setY((float) (Math.random()) * (frameHeight - green_btn_width));
        }

        // get line position
        lineX = line.getX()+line.getWidth();
        lineY = line.getY()+line.getHeight();


        frame.setOnTouchListener((view, motionEvent) -> {
            if(start_flag) {
                xx = motionEvent.getX();
                yy = motionEvent.getY();
                hitcheck();
            }
            return true;
        });
    }


    private void changePos() {
        //normal keys
        start++;

        // Init All buttons
        if ( start == 1)
                for(int i=0; i<nb_buttons; i++) {
               blackkeys_xy[i][0] = init_X;
               blackkeys_xy[i][1] = (float) (Math.random()) * (frameHeight - black_btn_width - ScreenOffsetY);
               greenkeys_xy[i][0] = init_X;
               greenkeys_xy[i][1] = (float) (Math.random()) * (frameHeight - green_btn_width - ScreenOffsetY);
                }

        //Increase nbre of generated buttons
        if( start < 500)
            CurrantNbreButton = 1;
        else if( start < 1000)
            CurrantNbreButton = 2;
        else if( start < 1500)
            CurrantNbreButton =3;
        else
            CurrantNbreButton = nb_buttons;
        for(int i=0; i<CurrantNbreButton; i++) {
            black_buttons[i].setVisibility(View.VISIBLE);
            green_buttons[i].setVisibility(View.VISIBLE);
        }
        // End modif


        for(int i=0; i<CurrantNbreButton; i++) {
            blackkeys_xy[i][0] -= (BlackInitSpeed+i) *( 1 + (float) start/5000);
            if( blackkeys_xy[i][0] + black_btn_width < -200 ){
                score -=pas_loose;
                //soundPlayer.playOverSound();
                check_win();
                //ini_black_XY();
                blackkeys_xy[i][0] = init_X;
                float val = (float) (Math.random()) * (frameHeight - black_btn_width - ScreenOffsetY);
                blackkeys_xy[i][1] = val;
            }
            if(win_or_loose()== 2){
                /*if(start == 1) {
                    blackkeys_xy[i][0] = init_X;
                    blackkeys_xy[i][1] = (float) (Math.random()) * (frameHeight - black_btn_width - ScreenOffsetY);
                }*/
                black_buttons[i].setX(blackkeys_xy[i][0]);
                black_buttons[i].setY(blackkeys_xy[i][1]);
            }

            //green keys
            greenkeys_xy[i][0] -= GreenInitSpeed + i;
            if( greenkeys_xy[i][0] + green_btn_width < -200 ){

                score -=pas_loose;
                //soundPlayer.playOverSound();
                check_win();
                //ini_green_XY();
                greenkeys_xy[i][0] = init_X;
                float val = (float) (Math.random()) * (frameHeight - black_btn_width - ScreenOffsetY);
                greenkeys_xy[i][1] = val;
            }
            if(win_or_loose()== 2){
                /*if(start == 1) {
                    greenkeys_xy[i][0] = init_X;
                    greenkeys_xy[i][1] = (float) (Math.random()) * (frameHeight - green_btn_width - ScreenOffsetY);
                }*/
                green_buttons[i].setX(greenkeys_xy[i][0]);
                green_buttons[i].setY(greenkeys_xy[i][1]);
            }
        }


        /*blackKeyX -= 10 *( 1 + start/5000) ;

        if( blackKeyX + blackButton.getWidth() < 0 ){
            ini_black_XY();
            score -=pas_loose;
            soundPlayer.playOverSound();
            check_win();
        }
        if(win_or_loose()== 2){
            blackButton.setX(blackKeyX);
            blackButton.setY(blackKeyY);
        }

        //green keys
        greenkeysX -= 15 ;
        if( greenkeysX  + greenButton.getWidth()< 0 ){
            ini_green_XY();
            score -=pas_loose;
            soundPlayer.playOverSound();
            check_win();
        }
        if(win_or_loose()== 2) {
            greenButton.setX(greenkeysX);
            greenButton.setY(greenkeysY);
        }*/
    }

    public void hitcheck(){
        for(int i=0; i<CurrantNbreButton; i++) {
            float blackKeyCenterX = blackkeys_xy[i][0] + (float) black_buttons[i].getWidth() / 2;
            float blackKeyCenterY = blackkeys_xy[i][1] + (float) black_buttons[i].getHeight() / 2;
            float w_error_pos1 = (float)(1+ black_buttons[i].getWidth() ) / 2;
            float h_error_pos1 = (float)(1+ black_buttons[i].getHeight() ) / 2;

            float greenKeyCenterX = greenkeys_xy[i][0] + (float) green_buttons[i].getWidth() / 2;
            float greenKeyCenterY = greenkeys_xy[i][1] + (float) green_buttons[i].getHeight() / 2;
            float w_error_pos2 = (float)(1+ green_buttons[i].getWidth() ) / 2;
            float h_error_pos2 = (float)(1+ green_buttons[i].getHeight() ) / 2;


            if (lineX + line.getWidth() <= blackKeyCenterX
                    && (blackKeyCenterX - w_error_pos1 <= xx && xx <= blackKeyCenterX + w_error_pos1)
                    && (blackKeyCenterY - h_error_pos1 <= yy && yy <= blackKeyCenterY + h_error_pos1)) {

                //ini_black_XY();
                blackkeys_xy[i][0] = init_X;
                float val = (float) (Math.random()) * (frameHeight - black_btn_width - ScreenOffsetY);
                blackkeys_xy[i][1] = val;

                if (win_or_loose() == 2) {
                    score += pas_win;
                    //soundPlayer.playhitSound();
                }
                check_win();
            } else if (lineX + line.getWidth() <= greenKeyCenterX
                    && (greenKeyCenterX - w_error_pos2 <= xx && xx <= greenKeyCenterX + w_error_pos2)
                    && (greenKeyCenterY - h_error_pos2 <= yy && yy <= greenKeyCenterY + h_error_pos2)) {

                //ini_green_XY();
                greenkeys_xy[i][0] = init_X;
                float val = (float) (Math.random()) * (frameHeight - green_btn_width - ScreenOffsetY);
                greenkeys_xy[i][1] = val;

                if (win_or_loose() == 2) {
                    score += pas_win * 2;
                    //soundPlayer.playhitSound();
                }
                check_win();
            }
        }

    }

    public int win_or_loose(){
        if(score >= goal)
            return 1;
        else if(score <= 0)
            return 0;
        return 2;
    }

    public void check_win(){
        score_label.setText("Score: " + score);
       /* if(win_or_loose()!=2){
            start_flag = false;
            tap_to_start.setVisibility(View.VISIBLE);
            tap_to_start.setText("Congratulations");

        }
        else if(win_or_loose()==0){
            start_flag = false;
            tap_to_start.setVisibility(View.VISIBLE);
            tap_to_start.setText("You loose");
        }*/

        if(win_or_loose()!=2){
            start_flag = false;
            timer.cancel();
            timer = null;
           //soundPlayer.playhitSound();

            Intent intent = new Intent(getApplicationContext(), Result.class);
            intent.putExtra("score", score);
            startActivity(intent);
        }
    }

    public boolean onTouchEvent(MotionEvent ev){
        if(!start_flag){
            start_flag = true;
            frameHeight = frame.getHeight();
            tap_to_start.setVisibility(View.GONE);
            for(int i =0; i<CurrantNbreButton ; i++) {
                black_buttons[i].setVisibility(View.VISIBLE);
                green_buttons[i].setVisibility(View.VISIBLE);
            }

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(win_or_loose() == 2)
                                changePos();
                        }
                    });
                }
            }, 0, 16);

        }
        return true;
    }


    // disable return button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            return event.getKeyCode() == KeyEvent.KEYCODE_BACK;
        }
        return super.dispatchKeyEvent(event);
    }

}