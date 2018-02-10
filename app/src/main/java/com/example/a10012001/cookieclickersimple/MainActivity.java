package com.example.a10012001.cookieclickersimple;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    int grandmaCount,mineCount,factoryCount;

    AnimationSet cookieAnims = new AnimationSet(true);
    AnimationSet clickAnims = new AnimationSet(true);
    AtomicInteger score;
    Button reset,confirm;
    ConstraintLayout constraintLayout;
    ImageView cookie,grandma,mine,factory;
    ScaleAnimation appearAnim;
    TranslateAnimation slideIntoDMs;
    SharedPreferences sharedPreferences;
    TextView count;

    String SCORE_KEY = "scoreKeyThatYouWillNeverGuess";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        reset = (Button)findViewById(R.id.id_reset);
        confirm = (Button)findViewById(R.id.id_confirm);
            confirm.setVisibility(View.INVISIBLE);
        constraintLayout = (ConstraintLayout)findViewById(R.id.id_layout);
        cookie = (ImageView)findViewById(R.id.id_cookieView);
        grandma = (ImageView)findViewById(R.id.id_grandma);
        mine = (ImageView)findViewById(R.id.id_mine);
        factory = (ImageView)findViewById(R.id.id_factory);
        score = new AtomicInteger(sharedPreferences.getInt(SCORE_KEY,0));
        count = (TextView)findViewById(R.id.id_count);

        appearAnim = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        appearAnim.setDuration(300);

        setScore(0);
        PassiveThread passiveThread = new PassiveThread();
        passiveThread.start();

        final ScaleAnimation firstAnim = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation secondAnim = new ScaleAnimation(1.0f,0.909f,1.0f,0.909f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        firstAnim.setDuration(150);
        secondAnim.setDuration(150);
        secondAnim.setStartOffset(150);
        cookieAnims.addAnimation(firstAnim);
        cookieAnims.addAnimation(secondAnim);
        cookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            v.startAnimation(cookieAnims);
            plusOne();
            setScore(1);
            }
        });
        final ScaleAnimation clickAnim1 = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        final ScaleAnimation clickAnim2 = new ScaleAnimation(1.0f,0.909f,1.0f,0.909f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        firstAnim.setDuration(150);
        secondAnim.setDuration(150);
        secondAnim.setStartOffset(150);
        clickAnims.addAnimation(firstAnim);
        clickAnims.addAnimation(secondAnim);
        grandma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setScore(-50);
                addUpgrade(R.drawable.grandma);
                grandmaCount++;
            }
        });
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setScore(-500);
                addUpgrade(R.drawable.minelarge);
                mineCount++;
            }
        });
        factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setScore(-1000);
                addUpgrade(R.drawable.factorylarge);
                factoryCount++;
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirm.getVisibility()==View.VISIBLE){
                    confirm.setVisibility(View.INVISIBLE);
                }else{
                    confirm.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Click CONFIRM to Reset", Toast.LENGTH_SHORT).show();
                }
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = new AtomicInteger(0);
                setScore(0);
                grandmaCount=0;
            }
        });
    }

    public void setScore(int add){
        score.getAndAdd(add);
        if(score.get()==1)
            count.setText(score+" cookie");
        else
            count.setText(score+" cookies");
        if(score.get()>=100) {
            if(grandma.getVisibility()==View.INVISIBLE) {
                grandma.setVisibility(View.VISIBLE);
                grandma.startAnimation(appearAnim);
            }
        }else {
            grandma.setVisibility(View.INVISIBLE);
        }
    }

    public void addUpgrade(int id){
        ImageView imageInCode = new ImageView(this);
        imageInCode.setId(View.generateViewId());
        imageInCode.setImageResource(id);
        imageInCode.setScaleX(0.4f);
        imageInCode.setScaleY(0.4f);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT); //This can be used on ANY View
        imageInCode.setLayoutParams(params);

        constraintLayout.addView(imageInCode);

        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(constraintLayout);

        constraints.connect(imageInCode.getId(), ConstraintSet.TOP, cookie.getId(), ConstraintSet.BOTTOM);
        constraints.connect(imageInCode.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);
        constraints.connect(imageInCode.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
        constraints.connect(imageInCode.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

        constraints.setVerticalBias(imageInCode.getId(), 100 );
        constraints.setHorizontalBias(imageInCode.getId(), (float)(Math.random()) );

        constraints.applyTo(constraintLayout);

        slideIntoDMs = new TranslateAnimation(300,0,0,0);
        slideIntoDMs.setDuration(200);
        imageInCode.startAnimation(slideIntoDMs);
    }

    public void plusOne(){
        TextView textViewInCode;
        textViewInCode = new TextView(this);
        textViewInCode.setId(View.generateViewId()); //Requires minimum SDK version of 17
        textViewInCode.setText("+1");
        textViewInCode.setTextColor(Color.WHITE);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT); //This can be used on ANY View
        textViewInCode.setLayoutParams(params);

        constraintLayout.addView(textViewInCode);

        ConstraintSet constraints = new ConstraintSet();
        constraints.clone(constraintLayout);

        constraints.connect(textViewInCode.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
        constraints.connect(textViewInCode.getId(), ConstraintSet.BOTTOM, cookie.getId(), ConstraintSet.TOP);
        constraints.connect(textViewInCode.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
        constraints.connect(textViewInCode.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);

        constraints.setVerticalBias(textViewInCode.getId(), 75);
        constraints.setHorizontalBias(textViewInCode.getId(), (float)((0.5+2*(Math.random()/10) -.1)));

        constraints.applyTo(constraintLayout);

        final TranslateAnimation translateAnimation = new TranslateAnimation(0,0,50,-200);
        translateAnimation.setDuration(1000);
        textViewInCode.startAnimation(translateAnimation);
        textViewInCode.setVisibility(View.INVISIBLE);
    }

    public class PassiveThread extends Thread{
        @Override
        public void run() {
            while(true) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setScore(grandmaCount * 5);
                        setScore(mineCount*10);
                        setScore(factoryCount*20);
                    }
                });
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SCORE_KEY,score.get());
        editor.commit();
        Log.d("shiv","Score Committed");
        super.onStop();
    }
}
