package i.speedtyping;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class main extends AppCompatActivity {
    TextView text, starHint, progress, start;
    EditText listener;
    ImageView status, starImage;
    int counter;
    long timeStart, timeEnd;
    String randText;
    FrameLayout leftBar, rightBar;
    SharedPreferences bae;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 20){
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorStatus));
            getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorNav));

        }
        if (Build.VERSION.SDK_INT > 22) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }
        setContentView(R.layout.activity_main);
        bae = getSharedPreferences(data.sharedPreferences, MODE_PRIVATE);
        int star = bae.getInt(data.star, R.drawable.star_novice);
        int bar = bae.getInt(data.bar, 0);
        String ability = bae.getString(data.ability, "Новичок - 0 слов в минуту"); //ability is the measurement rate

        text = findViewById(R.id.text);
        listener = findViewById(R.id.listener);
        status = findViewById(R.id.status);
        start = findViewById(R.id.start);
        progress = findViewById(R.id.progress);
        leftBar = findViewById(R.id.leftBar);
        rightBar = findViewById(R.id.rightBar);
        TextView reset = findViewById(R.id.reset);
        TextView exit = findViewById(R.id.exit);

        starImage = findViewById(R.id.starImage);
        starHint = findViewById(R.id.starHint);

        starImage.setImageResource(star);
        starHint.setText(ability);

        LinearLayout.LayoutParams leftB = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, bar);
        LinearLayout.LayoutParams rightB = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (8 - bar));
        leftBar.setLayoutParams(leftB);
        rightBar.setLayoutParams(rightB);

        counter = 0;

        listener.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence letter, int start, int before, int count) {
                Animation notify = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.notify);



                if ((counter + 1) == randText.length()){


                    timeEnd = System.currentTimeMillis();
                    Double time = (double)(timeEnd - timeStart) / 1000 / 60;
                    int wpm = (int) (20 / time);

                    setRank(wpm);

                    counter = 0;
                    randText = getWords();
                    text.setText(randText);
                    listener.getText().clear();
                }
                else {
                    try {
                        String word = listener.getText().toString();
                        String enteredChar = String.valueOf(word.charAt(word.length() - 1));
                        String expectedChar = String.valueOf(randText.charAt(counter));

                        if (enteredChar.toLowerCase().equals(expectedChar.toLowerCase())) {

                            counter++;

                            String partA = "<font color='#157A0D'>" + randText.substring(0, counter) + "</font>";
                            String partB = "<font color='#444444'>" + randText.substring(counter) + "</font>";
                            text.setText(Html.fromHtml(partA + partB));

                            status.setImageResource(R.drawable.green);
                            status.startAnimation(notify);
                        } else {
                            status.setImageResource(R.drawable.red);
                        }
                    } catch (StringIndexOutOfBoundsException e){

                        try {
                            listener.getText().clear();
                            counter = 0;
                            text.setText(R.string.ready);
                            status.setImageResource(0);
                            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); assert im != null;
                            im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

                        } catch (NullPointerException ignore) {}
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listener.setFocusableInTouchMode(true);
                listener.requestFocus();
                randText = getWords();
                text.setText(randText);
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert im != null;
                im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                timeStart = System.currentTimeMillis();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listener.getText().clear();
                    counter = 0;
                    text.setText(R.string.ready);
                    status.setImageResource(0);
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); assert im != null;
                    im.hideSoftInputFromWindow(v.getWindowToken(), 0);

                }catch (NullPointerException ignored){

                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aboutME();
    }


    private void setRank(int wpm){
        String level = getResources().getString(R.string.novice);
        int star = R.drawable.star_novice;
        int bar = 0;
        int acceleration  = bae.getInt(data.speed, 0);

        if (wpm >= 0 && wpm <= 15){

            level = getResources().getString(R.string.novice);
            star = R.drawable.star_novice;
        }
        if (wpm >= 16 && wpm <= 20){

            bar = 1;
            level = getResources().getString(R.string.amateur);
            star = R.drawable.star_amateur;
        }
        if (wpm >= 21 && wpm <= 24){

            bar = 2;
            level = getResources().getString(R.string.senior);
            star = R.drawable.star_senior;
        }
        if (wpm >= 25 && wpm <= 30){

            bar = 3;
            level = getResources().getString(R.string.enthusiast);
            star = R.drawable.star_enthusiast;
        }
        if (wpm >= 31 && wpm <= 33){

            bar = 4;
            level = getResources().getString(R.string.professional);
            star = R.drawable.star_professional;
        }
        if (wpm >= 34 && wpm <= 36){

            bar = 5;
            level = getResources().getString(R.string.expert);
            star = R.drawable.star_expert;
        }
        if (wpm >= 37 && wpm <= 39){

            bar = 6;
            level = getResources().getString(R.string.veteran);
            star = R.drawable.star_veteran;
        }
        if (wpm >= 40 && wpm <= 44){

            bar = 7;
            level = getResources().getString(R.string.master);
            star = R.drawable.star_master;
        }
        if (wpm > 45){

            bar = 8;
            level = getResources().getString(R.string.ultimate);
            star = R.drawable.star_ultimate;
        }

        if (wpm > acceleration){
            SharedPreferences.Editor makeupBox = bae.edit();
            progress.setText(R.string.new_speed);
            starImage.setImageResource(star);
            starHint.setText(getProgress(level, wpm));

            LinearLayout.LayoutParams leftB = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, bar);
            LinearLayout.LayoutParams rightB = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, (8 - bar));
            leftBar.setLayoutParams(leftB);
            rightBar.setLayoutParams(rightB);

            makeupBox.putInt(data.bar, bar);
            makeupBox.putInt(data.speed, wpm);
            makeupBox.putInt(data.star, star);
            makeupBox.putString(data.ability, getProgress(level, wpm));
            makeupBox.apply();
        }
        else {
            progress.setText(getProgress(level, wpm));
        }
    }

    private String getProgress(String level, int wpm){
        String word = wpm + " слов в минуту";
        SharedPreferences.Editor makeupBox = bae.edit();
        makeupBox.putInt(data.speed, wpm);
        makeupBox.apply();
        return level + " " + word;
    }


    private String getWords(){
        StringBuilder words = new StringBuilder();
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.library));
        ArrayList<String> array = new ArrayList<>();
        while (scan.hasNextLine()){
            array.add(scan.nextLine());
        }
        scan.close();
        Random rand = new Random();
        for (int i= 0; i < 20; i++){
            //get 20 random words
            int line = rand.nextInt(array.size());
            words.append(array.get(line)).append(" ");
        }
        return String.valueOf(words).trim();
    }

    private void aboutME(){
        TextView aboutButton = findViewById(R.id.about);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog about = new Dialog(main.this);
                about.requestWindowFeature(Window.FEATURE_NO_TITLE);
                about.setContentView(R.layout.about);
                about.show();
            }
        });
    }
    private static class data{
        static String sharedPreferences = "dat";
        static String speed = "speed";
        static String star = "star";
        static String ability = "ability";
        static String bar = "bar";
    }
}
