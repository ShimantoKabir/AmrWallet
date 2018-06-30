package com.example.maask.amrwallet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    ImageView wel_image;
    TextView wel_head,wel_slogan;
    SharedPreferences sharedPreferences;
    private static final String PREFERENCES_KEY  = "reg_preferences";
    private static final String VISIT_WELCOME = "visit_welcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        wel_image  = findViewById(R.id.wel_img);
        wel_head   = findViewById(R.id.wel_head);
        wel_slogan = findViewById(R.id.wel_slogan);

        Animation wel_animation_uptobottom = AnimationUtils.loadAnimation(this,R.anim.welcome_transition_uptobottom);
        Animation wel_animation_bottomtoup = AnimationUtils.loadAnimation(this,R.anim.welcome_transition_bottomtoup);
        wel_image.setAnimation(wel_animation_uptobottom);
        wel_head.setAnimation(wel_animation_bottomtoup);
        wel_slogan.setAnimation(wel_animation_bottomtoup);

        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(VISIT_WELCOME, "Y");
        editor.commit();

        final Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);

        Thread timer = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    startActivity(intent);
                    finish();
                }
            }
        };

        timer.start();

    }
}
