package com.example.mapphigros;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

    private AudioAttributes audioAtt;
    final int SOUND_POOL_MAX = 20;

    private static SoundPool soundPool;
    private static int hitSound;
    private static int overSound;
    private static int music;

    public SoundPlayer(Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            audioAtt =  new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();

            soundPool =  new SoundPool.Builder()
                    .setAudioAttributes(audioAtt).setMaxStreams(SOUND_POOL_MAX).build();
        }
        else{
            // cuz deprecated
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }


        hitSound = soundPool.load(context, R.raw.win_click,1);
        overSound = soundPool.load(context, R.raw.miss_click,1);
    }

    public void playhitSound(){
        soundPool.play(hitSound, 10.0f, 10.0f, 1, 0, 1);
    }
    public void playOverSound(){
        soundPool.play(overSound, 10.0f, 10.0f, 1, 0, 1);
    }

}
