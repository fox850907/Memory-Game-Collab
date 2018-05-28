package edu.tw.week13_hw;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    ImageView iv1, iv2, iv3, iv4, iv5, iv6;  //六個圖片
    int[] card = {
            R.drawable.front1,R.drawable.front1,R.drawable.front2,R.drawable.front2,R.drawable.front3,R.drawable.front3
    };  //card陣列   六個兩兩一組圖片
    boolean[] bool = {false,false,false,false,false,false};  //六個布林值 配對

    int flop1, flop2; //點到的 int
    int btncount = 0;
    int time = 0;  //紀錄時間
    int end = 0;

    ImageView fiv1, fiv2; //點到的
    MediaPlayer mp = new MediaPlayer();

    void findView(){
        iv1 = findViewById(R.id.imageView);
        iv2 = findViewById(R.id.imageView2);
        iv3 = findViewById(R.id.imageView3);
        iv4 = findViewById(R.id.imageView4);
        iv5 = findViewById(R.id.imageView5);
        iv6 = findViewById(R.id.imageView6);
    }

    //動畫
    private void animation(final ImageView img, final int ivValue){  //iv 要翻轉的值
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.back);  //翻面動畫
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(!bool[ivValue]){
                    img.setImageResource(card[ivValue]);
                    bool[ivValue] = true;
                }
                img.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.front));

                btncount++;
                if(btncount == 1){
                    fiv1 = img;
                    flop1 = ivValue;
                    img.setEnabled(false);
                }else if(btncount ==2){
                    fiv2 = img;
                    flop2 = ivValue;
                    iv1.setEnabled(false);
                    iv2.setEnabled(false);
                    iv3.setEnabled(false);
                    iv4.setEnabled(false);
                    iv5.setEnabled(false);
                    iv6.setEnabled(false);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        img.startAnimation(animation);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                setTitle("遊戲時間：" + (time++) / 5 + " s ");
                if (btncount == 3) {
                    if (card[flop1] == card[flop2]) {
                        fiv1.setVisibility(View.INVISIBLE);
                        fiv2.setVisibility(View.INVISIBLE);
                        end++;
                        //                mp = MediaPlayer.create(MainActivity.this, R.drawable.o);
                        if (card.length / 2 == end) {
                            timer.cancel();
                            setTitle("遊戲結束!! 紀錄:" + time / 5 + " s ");
                        }
                    } else {
                        fiv1.setEnabled(true);
                        fiv1.setImageResource(R.drawable.back);
                        fiv2.setImageResource(R.drawable.back);
                        bool[flop1] = false;
                        bool[flop2] = false;
//                        mp = MediaPlayer.create(MainActivity.this, R.drawable);
                    }
                    //歸零
                    btncount = 0;
                    iv1.setEnabled(true);
                    iv2.setEnabled(true);
                    iv3.setEnabled(true);
                    iv4.setEnabled(true);
                    iv5.setEnabled(true);
                    iv6.setEnabled(true);
                } else if (btncount >= 2)
                    btncount++;
            }
        }
    };

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        for(int i = 0; i<10000; i++){
            int shuffle = (int)(Math.random()*6);
            int c = card[shuffle];
            card[shuffle] = card[0];
            card[0] = c;
        }

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(iv1, 0);
            }
        });
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(iv2, 1);
            }
        });
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(iv3, 2);
            }
        });
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(iv4, 3);
            }
        });
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(iv5, 4);
            }
        });
        iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation(iv6, 5);
            }
        });

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        }, 0, 200);
    }
}
