package com.example.playfigros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static String game_mode = "easy";
    //easy mode
    private static final int  e_pas_win = 50;
    private static final int  e_pas_loose = 50;
    private static final int e_max_misses = 20;
    private static final int e_goal = 10000;
    private static final int easy1=1000, easy2=2000, easy3=4000;

    //hard mode
    private static final int  h_pas_win = 20;
    private static final int  h_pas_loose = 50;
    private static final int  h_max_misses = 10;
    private static final int h_goal = 100000;
    private static final int hard1=500, hard2=1000, hard3=3000;

    //music
    MediaPlayer ring;
    private boolean music_done_flag = false;
    // sound tracks
    private SoundPlayer soundPlayer;

    private int  pas_win;
    private int  pas_loose;
    private int  max_misses;
    private int  goal;
    private int lvl1,lvl2,lvl3;

    private static final int nb_buttons = 4 ;

    private static final int GreenInitSpeed = 8;
    private static final int BlackInitSpeed = 5;
    private static final int ScreenOffsetY = 400;
    private  int CurrantNbreButton;



    // view elements
    private TextView score_label;
    private TextView tap_to_start, feedback, chances;
    private FrameLayout frame;
    private CardView line;
    private ImageView pause;
    private Button pause_quit, pause_resume;
    private LinearLayout pause_menu;
    private ImageView[] black_buttons = new ImageView[nb_buttons];
    private ImageView[] green_buttons = new ImageView[nb_buttons];


    // score
    private int score = 0;
    private int perfect, good, miss;

    // sizes
    private int frameHeight, frameWidth;
    private int screenWidth, screenHeight;
    private int black_btn_width, green_btn_width;
    private int black_btn_height, green_btn_height;

    // position
    private float lineX, lineY;
    private float[][] blackkeys_xy = new float[nb_buttons][2];
    private float[][] greenkeys_xy = new float[nb_buttons][2];


    //initialize class
    private Handler handler = new Handler();
    private Timer timer ;
    private float timer_feedback =  0;

    //status check
    private boolean start_flag = false, pause_flag=false;

    // touch mode
    private float xx, yy;

    //timer
    private int start=0;
    private float init_X, init_Y;


    void initAll(){
        //get screen size
        WindowManager we = getWindowManager();
        Display disp = we.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;

        score_label = findViewById(R.id.score_label);
        chances = findViewById(R.id.chances);
        tap_to_start = findViewById(R.id.tap_to_start);
        feedback = findViewById(R.id.feedback);
        frame = findViewById(R.id.frame);
        pause = findViewById(R.id.pause);
        pause_menu = findViewById(R.id.pause_menu);
        pause_resume = findViewById(R.id.pause_resume);
        pause_quit = findViewById(R.id.pause_quit);
        line = findViewById(R.id.limit);
        chances.setText("Chances : " + max_misses);

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

        black_btn_width = black_buttons[0].getHeight();
        green_btn_width = green_buttons[0].getHeight();

        init_X = black_btn_width;
        init_Y = - ScreenOffsetY;

        score_label.setText("Score : "+score);


        //init buttons'positions
        for(int i=0; i<nb_buttons; i++){
            blackkeys_xy[i][1] = init_Y;
            greenkeys_xy[i][1] = init_Y;
            black_buttons[i].setY(blackkeys_xy[i][1]);
            green_buttons[i].setY(greenkeys_xy[i][1]);

            float min = frameWidth;
            float max = frameWidth - black_btn_width/2 ;
            float range = max - min +1;
            black_buttons[i].setX(range);
            green_buttons[i].setX(range);
        }

        timer_feedback = 0;
        pause_quit.setBackgroundColor(Color.RED);
        pause_resume.setBackgroundColor(Color.rgb(3,218,197));
        feedback.setVisibility(View.GONE);
    }

    @SuppressLint("ClickableViewAccessibility")
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

        setContentView(R.layout.activity_main);
        game_mode = getIntent().getStringExtra("game_mode");
        if(getIntent() !=null) {
            if(game_mode.equals("easy")){
                pas_win = e_pas_win;
                pas_loose = e_pas_loose;
                max_misses = e_max_misses;
                goal = e_goal;
                lvl1 = easy1;
                lvl2 = easy2;
                lvl3 = easy3;
            }
            else{
                pas_win = h_pas_win;
                pas_loose = h_pas_loose;
                max_misses = h_max_misses;
                goal = h_goal;
                lvl1 = hard1;
                lvl2 = hard2;
                lvl3 = hard3;
            }
        }
        soundPlayer = new SoundPlayer(this);
        ring= MediaPlayer.create(MainActivity.this,R.raw.disorder_hyun);
        try {
            ring.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ring.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                music_done_flag = true;
                check_win();
            }
        });

        initAll();
        line.setX(black_buttons[0].getHeight());

        pause.setOnClickListener((view) -> {
            if(!pause_flag){
                start_flag = false;
                pause_flag = true;
                if(ring.isPlaying())
                    ring.pause();
                timer.cancel();
                //timer =null;
                pause.setImageResource(android.R.drawable.ic_media_play);
                pause_menu.setVisibility(View.VISIBLE);
                //tap_to_start.setVisibility(View.VISIBLE);
                //tap_to_start.setText("Pause");
            }
        });

        pause_resume.setOnClickListener((view) -> {
            if(pause_flag){
                pause_flag = false;
                start_flag = false;
                Log.e("e", "---- resume game");
                pause.setImageResource(R.drawable.pause);
                pause_menu.setVisibility(View.GONE);
                tap_to_start.setVisibility(View.VISIBLE);


            }
        });

        pause_quit.setOnClickListener((view) -> {
            if(pause_flag){
                pause_flag = false;
                start_flag = false;
                Log.e("e", "---- Quit game");
                if(ring.isPlaying()) {
                    ring.stop();
                    ring.release();
                }

                Intent intent = new Intent(getApplicationContext(), Start.class);
                startActivity(intent);

            }
        });



        frame.setOnTouchListener((view, motionEvent) -> {
            if(!start_flag && !pause_flag){
                start_flag = true;
                pause_flag = false;

                if(!ring.isPlaying())
                    ring.start();
                pause.setImageResource(R.drawable.pause);
                frameHeight = frame.getHeight();
                frameWidth = frame.getWidth();
                // get line position
                lineY = line.getY();
                lineX = line.getX();
               tap_to_start.setVisibility(View.GONE);
                for(int i =0; i<CurrantNbreButton ; i++) {
                    black_buttons[i].setVisibility(View.VISIBLE);
                    green_buttons[i].setVisibility(View.VISIBLE);
                }
                ring.start();
                try {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (win_or_loose() == 2)
                                        changePos();
                                }
                            });
                        }
                    }, 0, 16);
                }
                catch(Exception e){
                    Log.e("timer","Error in timer!");
                    System.out.println("Error in timer!");
                }

            }
            else if(start_flag) {
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
        float min = green_buttons[0].getHeight()/2;
        float max = frameWidth - green_buttons[0].getHeight()*3/2 ;
        float range = max - min +1;

        if(timer_feedback > 0){
            timer_feedback--;
            feedback.setVisibility(View.VISIBLE);
        }
        else {
            timer_feedback = 0;
            feedback.setVisibility(View.GONE);
        }
        // Init All buttons
        if ( start == 1)
            for(int i=0; i<nb_buttons; i++) {
                blackkeys_xy[i][1] = init_Y;
                blackkeys_xy[i][0] = (float) ( min + (Math.random()) * range);
                greenkeys_xy[i][1] = init_Y;
                greenkeys_xy[i][0] = (float) ( min + (Math.random()) * range);
            }

        //Increase nbre of generated buttons
        if( start < lvl1)
            CurrantNbreButton = 1;
        else if( start < lvl2)
            CurrantNbreButton = 2;
        else if( start < lvl3)
            CurrantNbreButton =3;
        else
            CurrantNbreButton = nb_buttons;
        for(int i=0; i<CurrantNbreButton; i++) {
            black_buttons[i].setVisibility(View.VISIBLE);
            green_buttons[i].setVisibility(View.VISIBLE);
        }
        // End modif


        for(int i=0; i<CurrantNbreButton; i++) {
            blackkeys_xy[i][1] += (BlackInitSpeed+i) *( 1 + (float) start/10000);
            if( blackkeys_xy[i][1] > lineY){
                miss++;
                chances.setText("Chances: " + (max_misses-miss));
                score -=pas_loose;
                if(score < 0) score = 0;
                soundPlayer.playOverSound();
                check_win();
                blackkeys_xy[i][1] = init_Y;
                blackkeys_xy[i][0] = (float) ( min + (Math.random()) * range);;
            }

            if(win_or_loose()== 2){
                black_buttons[i].setX(blackkeys_xy[i][0]);
                black_buttons[i].setY(blackkeys_xy[i][1]);
            }

            //green keys
            greenkeys_xy[i][1] += GreenInitSpeed + i;
            if( greenkeys_xy[i][1] > lineY ){
                miss++;
                chances.setText("Chances: " + (max_misses-miss));
                score -=pas_loose;
                if(score < 0) score = 0;
                soundPlayer.playOverSound();
                check_win();
                greenkeys_xy[i][1] = init_Y;
                greenkeys_xy[i][0] = (float) ( min + (Math.random()) * range);;
            }
            if(win_or_loose()== 2){
                green_buttons[i].setX(greenkeys_xy[i][0]);
                green_buttons[i].setY(greenkeys_xy[i][1]);
            }
        }
    }



    public void hitcheck(){
        float min = green_buttons[0].getHeight()/2;
        float max = frameWidth - green_buttons[0].getHeight()*3/2 ;
        float range = max - min +1;

        for(int i=0; i<CurrantNbreButton; i++) {
            float blackKeyCenterY = blackkeys_xy[i][1] + (float) black_buttons[i].getWidth() / 2;
            float blackKeyCenterX = blackkeys_xy[i][0] + (float) black_buttons[i].getHeight() / 2;
            float w_error_pos1 = (float)(1+ black_buttons[i].getWidth() ) / 2;
            float h_error_pos1 = (float)(1+ black_buttons[i].getHeight() ) / 2;

            float greenKeyCenterY = greenkeys_xy[i][1] + (float) green_buttons[i].getWidth() / 2;
            float greenKeyCenterX = greenkeys_xy[i][0] + (float) green_buttons[i].getHeight() / 2;
            float w_error_pos2 = (float)(1+ green_buttons[i].getWidth() ) / 2;
            float h_error_pos2 = (float)(1+ green_buttons[i].getHeight() ) / 2;


            float  a = lineY + line.getHeight();
            if (lineY + line.getHeight() >= blackKeyCenterY
                     && (blackKeyCenterX - w_error_pos1 <= xx && xx <= blackKeyCenterX + w_error_pos1)
                    && (blackKeyCenterY - h_error_pos1 <= yy && yy <= blackKeyCenterY + h_error_pos1)) {
                blackkeys_xy[i][1] = init_Y;
                blackkeys_xy[i][0] = (float) ( min + (Math.random()) * range);

                if (win_or_loose() == 2) {
                    score += pas_win;
                    good++;
                    timer_feedback = 20;
                    soundPlayer.playhitSound();
                }
                check_win();
            }
            else if (lineY + line.getHeight() >= greenKeyCenterY
                    && (greenKeyCenterX - w_error_pos2 <= xx && xx <= greenKeyCenterX + w_error_pos2)
                    && (greenKeyCenterY - h_error_pos2 <= yy && yy <= greenKeyCenterY + h_error_pos2)) {
                 greenkeys_xy[i][1] = init_Y;
                 greenkeys_xy[i][0] = (float) ( min + (Math.random()) * range);

                if (win_or_loose() == 2) {
                    score += pas_win * 2;
                    good++;
                    timer_feedback = 20;
                    soundPlayer.playhitSound();
                }
                check_win();
            }
        }
    }

    public int win_or_loose(){
        if(music_done_flag )//score >= goal)
            return 1;
        else if(miss == max_misses )
            return 0;
        return 2;
    }

    public void check_win(){
        score_label.setText("Score: "+score);
       if(win_or_loose()!=2){
            start_flag = false;
            Log.e("e", "Game done");
          //  timer.cancel();
          //  timer = null;
         //  Log.e("e", "timer was canceled !!");
           if(ring.isPlaying()) {
               ring.stop();

               ring.release();
           }
           // ring = null;

            Intent intent = new Intent(getApplicationContext(), Result.class);
            intent.putExtra("score", score);
            intent.putExtra("good", good);
            intent.putExtra("miss", miss);
            intent.putExtra("game_mode", game_mode);

            startActivity(intent);
        }
    }


    // disable return button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            return event.getKeyCode() == KeyEvent.KEYCODE_BACK;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ring.release();
        ring=null;
    }
}