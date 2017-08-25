package com.example.ruanyulin.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import adapter.MusicAdapter;
import music.MusicResource;
import music.MusicUtil;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private TextView time;
    private TextView time1;
    private SeekBar seekBar;
    private ImageButton pre;
    private ImageButton play;
    private ImageButton next;
    private ImageView playing;
    private Button button;
    private TextView musicname;
    private MusicAdapter musicAdapter;
    private List<MusicResource> list = null;
    private Context context;
    private MusicResource musicResource;
    private int index = 0;
    private int index1;
    private View view1 = null;
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initview();
        //play.setImageDrawable(getDrawable(R.drawable.play));
        play.setBackground(getDrawable(R.drawable.play));

        list = MusicUtil.getmusic(context);
        if (list == null) {
            Toast.makeText(context,"listnull",Toast.LENGTH_SHORT).show();
        }
        musicAdapter = new MusicAdapter(list,context);
        listView.setAdapter(musicAdapter);
        listView.setOnItemClickListener(itemclicklisten);
        musicAdapter.setFlag(list.size());
        index1 = list.size();
        seekbarchange();
        //time.setText("3:50");
        button.setText("列表循环");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button.getText().equals("列表循环")){
                    button.setText("单曲循环");
                } else if (button.getText().equals("单曲循环")) {
                    button.setText("随机播放");
                } else if (button.getText().equals("随机播放")) {
                    button.setText("列表循环");
                }
            }
        });
        moveTaskToBack(false);

    }

    public class MyBroadcastMain extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {


            int statue = intent.getIntExtra("statue",-1);
            switch (statue) {
                case 0:
                    play.setBackground(getDrawable(R.drawable.pause));
                    break;
                case 1://播放
                    play.setBackground(getDrawable(R.drawable.pause));
                    //play.setImageDrawable(getDrawable(R.drawable.play));
                    break;
                case 2://暂停

                    play.setBackground(getDrawable(R.drawable.play));
                    break;
                default:
                    break;

            }
            int totaltime = intent.getIntExtra("totaltime",-1);
            int nowtime = intent.getIntExtra("nowtime",-1);
            if (nowtime != -1) {
                seekBar.setProgress((int) (((nowtime * 1.0)/totaltime) * 100));
                time.setText(inittime(nowtime));
                //time.setText(String.valueOf(nowtime));
                time1.setText(inittime(totaltime));
                //Toast.makeText(context,String.valueOf(((nowtime * 1.0)/totaltime) * 100),Toast.LENGTH_SHORT).show();
                /*if (nowtime == totaltime) {

                    //Toast.makeText(context,"next",Toast.LENGTH_SHORT).show();



                    Intent intent1 = new Intent("com.ruanyulin.service");
                    musicResource = list.get(index);
                    intent1.putExtra("newmusic",1);
                    intent1.putExtra("musicresource",musicResource);
                    intent1.putExtra("over",1);
                    sendBroadcast(intent1);
                }*/
            }
            if (intent.getIntExtra("over",-1) != -1) {
                if (button.getText().equals("列表循环")){
                    if (index == list.size()-1){
                        index = 0;
                        //Toast.makeText(context,String.valueOf(index),Toast.LENGTH_SHORT).show();
                    } else {
                        index++;
                        //Toast.makeText(context,String.valueOf(index) + "a",Toast.LENGTH_SHORT).show();
                    }
                } else if (button.getText().equals("单曲循环")) {

                } else if (button.getText().equals("随机播放")) {

                    index = random.nextInt(list.size());
                }
                //Toast.makeText(context,"next",Toast.LENGTH_SHORT).show();


                Intent intent1 = new Intent("com.ruanyulin.service");
                musicResource = list.get(index);
                intent1.putExtra("newmusic",1);
                intent1.putExtra("musicresource",musicResource);
                //intent1.putExtra("over",1);
                sendBroadcast(intent1);
            }


        }
    }

    private AdapterView.OnItemClickListener itemclicklisten = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            musicAdapter.setFlag(i);
            musicAdapter.getView(i,view,listView);
            //playing = view.findViewById(R.id.playing);
            //playing.setVisibility(View.VISIBLE);
            if (index1<list.size() && view1 != null) {
                musicAdapter.getView(index1,view1,listView);
            }
            Intent intent = new Intent("com.ruanyulin.service");
            index = i;
            if (musicResource != null) {
                intent.putExtra("isover",1);
            }
            musicResource = list.get(i);
            musicname.setText(musicResource.getName());
            index1 = i;
            view1 = view;
            intent.putExtra("musicresource",musicResource);
            intent.putExtra("newmusic",1);
            sendBroadcast(intent);
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent("com.ruanyulin.service");
            switch (view.getId()){
                case R.id.pre:
                    if ( index == 0){
                        index = list.size()-1;
                    } else {
                        index--;
                    }
                    if (musicResource != null){
                        musicResource = list.get(index);
                        intent.putExtra("newmusic",1);
                        intent.putExtra("isover",1);
                        intent.putExtra("musicresource",musicResource);
                    }

                    break;
                case R.id.play:
                    if (musicResource == null) {
                        musicResource = list.get(index);
                        intent.putExtra("musicresource",musicResource);
                        intent.putExtra("isfirst",1);
                        Toast.makeText(context,"first",Toast.LENGTH_SHORT).show();
                    }
                    intent.putExtra("isplay",1);
                    break;
                case R.id.next:
                    if (musicResource != null && index == list.size()-1){
                        index = 0;
                    } else {
                        index++;
                    }
                    if (musicResource != null){
                        musicResource = list.get(index);
                        intent.putExtra("newmusic",1);
                        intent.putExtra("isover",1);
                        intent.putExtra("musicresource",musicResource);
                    }
                    break;
                default:
                    break;
            }
            sendBroadcast(intent);
        }
    };

    private void seekbarchange(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //正在拖动
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            //开始拖动进度条调用
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //停止拖动
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent = new Intent("com.ruanyulin.service");
                intent.putExtra("progress",seekBar.getProgress());
                Toast.makeText(context,String.valueOf(seekBar.getProgress()),Toast.LENGTH_SHORT).show();
                sendBroadcast(intent);
            }
        });
    }

    private String inittime(int time){
        int minu = time/1000/60;
        int sec = time/1000%60;
        return String.format("%02d",minu) + ":" + String.format("%02d",sec);
        //return muni + ":" + sec;
    }
    private void initview() {
        listView = (ListView) findViewById(R.id.listview);
        time = (TextView) findViewById(R.id.nowtime);
        time1 = (TextView) findViewById(R.id.totaltime);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        pre = (ImageButton) findViewById(R.id.pre);
        play = (ImageButton) findViewById(R.id.play);
        next = (ImageButton) findViewById(R.id.next);
        button = (Button) findViewById(R.id.button);
        musicname = (TextView) findViewById(R.id.musicname);
        context = MainActivity.this;


        MyBroadcastMain myBroadcastMain = new MyBroadcastMain();
        IntentFilter filter = new IntentFilter("com.ruanyulin.main");
        registerReceiver(myBroadcastMain,filter);
        Intent intent = new Intent(context,MusicService.class);
        startService(intent);
        //seekBar.setMax(1);
        pre.setOnClickListener(onClickListener);
        play.setOnClickListener(onClickListener);
        next.setOnClickListener(onClickListener);
    }

    /*@Override
    protected void onStop() {
        super.onStop();
        /*try {
            unregisterReceiver(myBroadcastMain);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
