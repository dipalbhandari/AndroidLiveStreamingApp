package com.example.dipalbhandari.nebraskacapitollive;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Welcome extends AppCompatActivity {

    private ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        imageView = (ImageView) findViewById(R.id.imageView);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_animation);
        imageView.startAnimation(anim);
        final Intent i = new Intent(this, MainActivity.class);
        Thread timer = new Thread() {

            public void run(){
                try

            {
                sleep(1500);
            } catch(InterruptedException e1)

            {
                e1.printStackTrace();
            }


            finally

            {
                startActivity(i);
                finish();
            }
        }

    };
        timer.start();


    }
}
