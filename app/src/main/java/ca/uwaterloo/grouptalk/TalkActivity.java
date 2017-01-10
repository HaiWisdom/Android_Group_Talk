package ca.uwaterloo.grouptalk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class TalkActivity extends FragmentActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private FragmentPagerAdapter fAdapter;
    private List<Fragment> mFragments;
    private Button button_group_game, button_group_music, button_group_color, button_group_run;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_talk);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        initView();
        initEvent();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int currentItem = mViewPager.getCurrentItem();
                setSelect(currentItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    public String getUserId(){
        return userId;
    }

    private void initView(){
        Intent myIntent = getIntent();
        userId = myIntent.getStringExtra("userId");

        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        button_group_game = (Button) findViewById(R.id.button_group_game);
        button_group_music = (Button) findViewById(R.id.button_group_music);
        button_group_color = (Button) findViewById(R.id.button_group_color);
        button_group_run = (Button) findViewById(R.id.button_group_run);

        mFragments = new ArrayList<>();
        Fragment gameFragment = new GameFragment();
        Fragment musicFragment = new MusicFragment();
        Fragment colorFragment = new ColorFragment();
        Fragment runFragment = new RunFragment();
        mFragments.add(gameFragment);
        mFragments.add(musicFragment);
        mFragments.add(colorFragment);
        mFragments.add(runFragment);

        fAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        String groupName = myIntent.getStringExtra("groupName");
        int p = 0;
        switch (groupName){
            case "game":
                p = 0;
                break;
            case "music":
                p = 1;
                break;
            case "color":
                p = 2;
                break;
            case "run":
                p = 3;
                break;
        }
        setSelect(p);
        mViewPager.setAdapter(fAdapter);
        mViewPager.setCurrentItem(p);
    }

    private void resetTabBtn(){
        button_group_color.setTextColor(Color.parseColor("#ffffff"));
        button_group_game.setTextColor(Color.parseColor("#ffffff"));
        button_group_music.setTextColor(Color.parseColor("#ffffff"));
        button_group_run.setTextColor(Color.parseColor("#ffffff"));
    }

    private void initEvent(){
        button_group_game.setOnClickListener(this);
        button_group_music.setOnClickListener(this);
        button_group_color.setOnClickListener(this);
        button_group_run.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = 0;
        switch (v.getId()) {
            case R.id.button_group_game:
                i = 0;
                break;
            case R.id.button_group_music:
                i = 1;
                break;
            case R.id.button_group_color:
                i = 2;
                break;
            case R.id.button_group_run:
                i = 3;
                break;
        }
        setSelect(i);
        mViewPager.setCurrentItem(i);
    }

    private void setSelect(int position){
        resetTabBtn();
        switch (position) {
            case 0:
                button_group_game.setTextColor(Color.parseColor("#FFC107"));
                break;
            case 1:
                button_group_music.setTextColor(Color.parseColor("#FFC107"));
                break;
            case 2:
                button_group_color.setTextColor(Color.parseColor("#FFC107"));
                break;
            case 3:
                button_group_run.setTextColor(Color.parseColor("#FFC107"));
                break;
        }
    }
}
