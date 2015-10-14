package com.example.anumbrella.appwelcome;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.anumbrella.appwelcome.view.ParallaxContainer;

public class MainActivity extends AppCompatActivity {

    private ImageView man_run;

    private ParallaxContainer parallaxContainer;


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

         //   parallaxContainer.setupChildren();


        }


    }
}
