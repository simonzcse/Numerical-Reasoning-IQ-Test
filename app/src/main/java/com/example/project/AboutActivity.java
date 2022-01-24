package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.textView3);
        tv.clearAnimation();
        tv.startAnimation(a);
    }
}
