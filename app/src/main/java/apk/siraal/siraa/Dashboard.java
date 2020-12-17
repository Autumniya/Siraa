package apk.siraal.siraa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


 public class Dashboard extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button letsGetStarted;
    int currentPos;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        button = (Button) findViewById(R.id.get_started_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_started();
            }
        });


        //Hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);
        letsGetStarted = findViewById(R.id.get_started_btn);

        //Call adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);




        addDots(  0 );
        viewPager.addOnPageChangeListener(changeListener);
    }


    public void get_started() {
        Intent intent = new Intent(this,Userxui.class);
        startActivity(intent);
        finish();

    }

    public void get_started(View view){
        finish();
    }

    public void next(View view){
        viewPager.setCurrentItem(currentPos + 1);

    }

    private void addDots(int position){

        dots = new TextView[2];
        dotsLayout.removeAllViews();

        for(int i=0; i<dots.length; i ++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;


           if(position ==0){
               letsGetStarted.setVisibility(View.INVISIBLE);
           }
           else {
               letsGetStarted.setVisibility(View.VISIBLE);
           }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

 }