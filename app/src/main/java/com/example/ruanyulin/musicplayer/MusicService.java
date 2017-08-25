package com.example.ruanyulin.musicplayer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;

import java.io.IOException;

import music.MusicResource;

import static java.lang.Thread.sleep;

public class MusicService extends Service {
    private int newmusic;
    private MusicResource musicResource;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private int statue = 1;
    private int nowtime;
    private int totaltime;
    private Handler playing;
    private Runnable runnable;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //注册广播
        MyBroadcastReceiver receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter("com.ruanyulin.service");
        registerReceiver(receiver,filter);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //nowtime=0;
                //totaltime=0;
                playing.getLooper().quit();
                Intent intent = new Intent("com.ruanyulin.main");
                intent.putExtra("over",1);
                sendBroadcast(intent);
            }
        });

    }


    public class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            newmusic = intent.getIntExtra("newmusic",-1);
            int flag = intent.getIntExtra("isplay",-1);
            int first = intent.getIntExtra("isfirst",-1);
            int isover = intent.getIntExtra("isover",-1);
            if (isover == 1){
                playing.getLooper().quit();
            }
            /*int over = intent.getIntExtra("over",-1);
            if (over != -1) {
                playing.removeCallbacks(runnable);
            }*/
            if (first == 1){
                statue = 0;
            }

             if (newmusic == 1) {
                musicResource = (MusicResource) intent.getSerializableExtra("musicresource");
                if (musicResource != null) {
                    playmusic(musicResource);
                    statue = 1;
                }
            }
            if (flag == 1) {
                switch (statue){
                    case 0:
                        musicResource = (MusicResource) intent.getSerializableExtra("musicresource");
                        playmusic(musicResource);
                        statue=1;
                        break;
                    case 1:
                        mediaPlayer.pause();
                        statue=2;
                        break;
                    case 2:
                        mediaPlayer.start();
                        statue=1;
                        break;
                    default:
                        break;

                }
            }
            int progress = intent.getIntExtra("progress",-1);
            if (progress != -1) {
                nowtime = (int) (((progress*1.0)/100)*totaltime);
                mediaPlayer.seekTo(nowtime);
            }
            Intent intent1 = new Intent("com.ruanyulin.main");
            intent1.putExtra("statue",statue);
            sendBroadcast(intent1);
        }
    }

    public void playmusic(MusicResource resource){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(resource.getPath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                totaltime = mediaPlayer.getDuration();

                HandlerThread thread = new HandlerThread("handler");
                thread.start();
                playing = new Handler(thread.getLooper());

                runnable = new Runnable() {
                    @Override
                    public void run() {
                        while (nowtime<totaltime){
                            try {
                                sleep(1000);
                                nowtime = mediaPlayer.getCurrentPosition();
                                Intent intent = new Intent("com.ruanyulin.main");
                                intent.putExtra("nowtime",nowtime);
                                intent.putExtra("totaltime",totaltime);
                                sendBroadcast(intent);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                };
                playing.post(runnable);

                /*playing = new Handler(new Runnable() {
                    @Override
                    public void run() {
                        while (nowtime<totaltime){
                            try {
                                sleep(1000);
                                nowtime = mediaPlayer.getCurrentPosition();
                                Intent intent = new Intent("com.ruanyulin.main");
                                intent.putExtra("nowtime",nowtime);
                                intent.putExtra("totaltime",totaltime);
                                sendBroadcast(intent);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                //playing.start();
                /*{
                    public void run(){
                        while (nowtime<totaltime){
                            try {
                                sleep(1000);
                                nowtime = mediaPlayer.getCurrentPosition();
                                Intent intent = new Intent("com.ruanyulin.main");
                                intent.putExtra("nowtime",nowtime);
                                intent.putExtra("totaltime",totaltime);
                                sendBroadcast(intent);

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }.start();*/
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
