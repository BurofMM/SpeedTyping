package i.speedtyping;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class intro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 20){
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatus));
            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorNav));
            //this two immediate lines performs two different function.
            //the first one sets color of status bar(the one containing battery status, notification icons, network signals e.t.c)
            //the second one sets color for navigation(the one with home button, back button and recent apps button)
        }
        if (Build.VERSION.SDK_INT > 22) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //now this is to tell android the status bar color we are using is light, and should therefore set icons as black
        }
        setContentView(R.layout.activity_intro);
        TextView introText = findViewById(R.id.introText);
        Animation fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        introText.startAnimation(fade);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    startActivity(new Intent(getApplicationContext(), main.class));
                    finish();
            }
        }, 2200);
        //this is just a simple splash screen, nothing too fancy
    }
}
