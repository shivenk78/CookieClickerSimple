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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    int grandmaCount,mineCount,factoryCount;
    boolean save;

    AnimationSet cookieAnims = new AnimationSet(true);
    AnimationSet clickAnims = new AnimationSet(true);
    Animation fadeIn,fadeOut;
    AtomicInteger score;
    Button reset,confirm,tester;
    ConstraintLayout constraintLayout;
    ImageView cookie,grandma,mine,factory;
    LinearLayout grandmaLay,mineLay,factoryLay;
    ScaleAnimation appearAnim, disappearAnim;
    TranslateAnimation slideIntoDMs;
    SharedPreferences sharedPreferences;
    Switch saveSwitch;
    TextView count,granPrice,minePrice,facPrice;
    Upgrade Grandma, Mine, Factory;

    /*TODO:
        MAKE A DISSAPPEAR ANIMATION
        MAKE LOOP TO ADD UPGRADES ON RESTART
     */

    String SCORE_KEY = "scoreKeyThatYouWillNeverGuess";
    String GRAN_KEY = "granKeyThatYouWillNeverGuess";
    String MINE_KEY = "mineKeyThatYouWillNeverGuess";
    String FACTORY_KEY = "factoryKeyThatYouWillNeverGuess";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        reset = (Button)findViewById(R.id.id_reset);
            reset.setVisibility(View.INVISIBLE);
        confirm = (Button)findViewById(R.id.id_confirm);
            confirm.setVisibility(View.INVISIBLE);
        tester = (Button)findViewById(R.id.id_tester);
        constraintLayout = (ConstraintLayout)findViewById(R.id.id_layout);
        grandmaLay = (LinearLayout)findViewById(R.id.grandmaLay);
        mineLay = (LinearLayout)findViewById(R.id.mineLay);
        factoryLay = (LinearLayout)findViewById(R.id.factoryLay);
        cookie = (ImageView)findViewById(R.id.id_cookieView);
        grandma = (ImageView)findViewById(R.id.id_grandma);
        mine = (ImageView)findViewById(R.id.id_mine);
        factory = (ImageView)findViewById(R.id.id_factory);
        score = new AtomicInteger(sharedPreferences.getInt(SCORE_KEY,0));
        grandmaCount = sharedPreferences.getInt(GRAN_KEY,0);
        mineCount = sharedPreferences.getInt(MINE_KEY,0);
        factoryCount = sharedPreferences.getInt(FACTORY_KEY,0);
        count = (TextView)findViewById(R.id.id_count);
        saveSwitch = (Switch)findViewById(R.id.id_saveSwitch);
            saveSwitch.setSelected(true);

        granPrice = (TextView)findViewById(R.id.stat_grandma_price);
        minePrice = (TextView)findViewById(R.id.stat_mine_price);
        facPrice = (TextView)findViewById(R.id.stat_fac_price);

        Grandma = new Upgrade(5, 50);
            granPrice.setText(Grandma.getPrice()+"");
        Mine = new Upgrade(10,300);
            minePrice.setText(Mine.getPrice()+"");
        Factory = new Upgrade(15,1000);
            facPrice.setText(Factory.getPrice()+"");

        appearAnim = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        appearAnim.setDuration(300);
        disappearAnim = new ScaleAnimation(1.0f,0.0f,1.0f,0.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        disappearAnim.setDuration(300);

        for(int i=0;i<grandmaCount;i++){
            addUpgrade(R.drawable.grandma);
        }
        for(int i=0;i<mineCount;i++){
            addUpgrade(R.drawable.mine);
        }
        for(int i=0;i<factoryCount;i++){
            addUpgrade(R.drawable.factory);
        }

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
                setScore(-Grandma.getPrice());
                addUpgrade(R.drawable.grandma);
                grandmaCount++;
                Grandma.increasePrice();
                granPrice.setText(Grandma.getPrice()+"");
            }
        });
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(-Mine.getPrice());
                addUpgrade(R.drawable.mine);
                mineCount++;
                Mine.increasePrice();
                minePrice.setText(Mine.getPrice()+"");
            }
        });
        factory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(-Factory.getPrice());
                addUpgrade(R.drawable.factory);
                factoryCount++;
                Factory.increasePrice();
                facPrice.setText(Factory.getPrice()+"");
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
                /*score = new AtomicInteger(0);
                setScore(0);
                grandmaCount=0;
                mineCount=0;
                factoryCount=0;*/
            }
        });
        saveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                save = b;
            }
        });
    }

    public void setScore(int add){
        score.getAndAdd(add);
        if(score.get()==1)
            count.setText(score+" cookie");
        else
            count.setText(score+" cookies");
        if(score.get()>=Grandma.getPrice()) {
            if(grandmaLay.getVisibility()==View.INVISIBLE) {
                grandmaLay.setVisibility(View.VISIBLE);
                grandmaLay.startAnimation(appearAnim);
                granPrice.setText(Grandma.getPrice()+"");
            }
        }else {
            grandmaLay.startAnimation(disappearAnim);
            grandmaLay.setVisibility(View.INVISIBLE);
        }
        if(score.get()>=Mine.getPrice()) {
            if(mineLay.getVisibility()==View.INVISIBLE) {
                mineLay.setVisibility(View.VISIBLE);
                mineLay.startAnimation(appearAnim);
                minePrice.setText(Mine.getPrice()+"");
            }
        }else {
            mineLay.startAnimation(disappearAnim);
            mineLay.setVisibility(View.INVISIBLE);
        }
        if(score.get()>=Factory.getPrice()) {
            if(factoryLay.getVisibility()==View.INVISIBLE) {
                factoryLay.setVisibility(View.VISIBLE);
                factoryLay.startAnimation(appearAnim);
                facPrice.setText(Factory.getPrice()+"");
            }
        }else {
            factoryLay.startAnimation(disappearAnim);
            factoryLay.setVisibility(View.INVISIBLE);
        }
    }

    public void addUpgrade(int id){
        ImageView imageInCode = new ImageView(this);
        imageInCode.setId(View.generateViewId());
        imageInCode.setImageResource(id);
        if(id==R.drawable.grandma) {
            imageInCode.setScaleX(0.3f);
            imageInCode.setScaleY(0.3f);
        }
        if(id==R.drawable.factory) {
            imageInCode.setScaleX(0.45f);
            imageInCode.setScaleY(0.45f);
        }
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

        slideIntoDMs = new TranslateAnimation(700,0,0,0);
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

        AnimationSet plusOneAnim = new AnimationSet(true);
        final TranslateAnimation translateAnimation = new TranslateAnimation(0,0,50,-2000);
        translateAnimation.setDuration(3000);
        fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(500);
        fadeOut = new AlphaAnimation(1,0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1500);
        //plusOneAnim.addAnimation(fadeIn);
        plusOneAnim.addAnimation(translateAnimation);
        plusOneAnim.addAnimation(fadeOut);
        textViewInCode.startAnimation(plusOneAnim);
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

    public class Upgrade{
        int cps;
        int price;
        public Upgrade(int cps, int price){
            this.cps = cps;
            this.price = price;
        }
        public int getPrice(){ return price; }
        public int getCps(){ return cps; }
        public void increasePrice(){ price+=5; }
    }

    @Override
    protected void onStop() {
        if(save) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(SCORE_KEY, score.get());
            editor.putInt(GRAN_KEY, grandmaCount);
            editor.putInt(MINE_KEY, mineCount);
            editor.putInt(FACTORY_KEY, factoryCount);
            editor.commit();
            Log.d("shiv", "Score Committed");
        }
        super.onStop();
    }

    public void add100(View v){
        setScore(500);
    }
}
