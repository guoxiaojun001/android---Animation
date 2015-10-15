package com.example.anumbrella.appwelcome;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anumbrella.appwelcome.view.ParallaxContainer;

public class MainActivity extends Activity {

    private ImageView man_run;

    private ParallaxContainer parallaxContainer;


    private ImageView iv_weibo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        man_run = (ImageView) findViewById(R.id.man_run);

        parallaxContainer = (ParallaxContainer) findViewById(R.id.parallax_container);

        if (parallaxContainer != null) {
            parallaxContainer.setImage(man_run);
            parallaxContainer.setLooping(false);
            man_run.setVisibility(View.VISIBLE);

            parallaxContainer.setupChildren(getLayoutInflater(),
                    R.layout.view_intro_1, R.layout.view_intro_2,
                    R.layout.view_intro_3, R.layout.view_intro_4,
                    R.layout.view_intro_5, R.layout.view_login);
        }

        iv_weibo = (ImageView) findViewById(R.id.rl_weibo);
        iv_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
