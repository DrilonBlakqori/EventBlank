package com.zagapps.eventblank.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zagapps.eventblank.R;
import com.zagapps.eventblank.models.ModelManager;


public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_fragment);

        Thread welcomeThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    super.run();
                    sleep(500);
                    ModelManager.get(getApplicationContext());
                } catch (Exception e)
                {
                    e.printStackTrace();
                } finally {

                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);

                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            runFadeOutAnimation();
                        }
                    });

                    finish();
                }
            }
        };
        welcomeThread.start();
    }

    private void runFadeOutAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        a.reset();
        ImageView imageView=(ImageView)findViewById(R.id.splash_icon);
        imageView.clearAnimation();
        imageView.startAnimation(a);
    }
}
