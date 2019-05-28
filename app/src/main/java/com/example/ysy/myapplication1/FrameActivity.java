package com.example.ysy.myapplication1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FrameActivity extends FragmentActivity {
    private Fragment mFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton rbtHome,rbtFunc,rbtSetting;
    private RadioButton rtbHome;
    private RadioButton rtbFunc;
    private RadioButton rtbSetting;

    @Override
    protected void onCreate(Bundle savedinstanceState){
        super.onCreate(savedinstanceState);

        setContentView(R.layout.activity_fragment);
        mFragments = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_main);
        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_func);
        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_setting);
        fragmentTransaction =
                fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        fragmentTransaction.show(mFragments[0]).commit();

        rtbHome = (RadioButton)findViewById(R.id.radioHome);
        rtbFunc = (RadioButton)findViewById(R.id.radioFunc);
        rtbSetting = (RadioButton)findViewById(R.id.radioSetting);
        rtbHome.setBackgroundResource(R.drawable.shape3);

        radioGroup = (RadioGroup)findViewById(R.id.bottomGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId){
                Log.i("radioGroup", "checkedId=" + checkedId);
                fragmentTransaction =
                        fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
                rtbHome.setBackgroundResource(R.drawable.shape2);
                rtbFunc.setBackgroundResource(R.drawable.shape2);
                rtbSetting.setBackgroundResource(R.drawable.shape2);
                switch(checkedId){
                    case R.id.radioHome:
                        fragmentTransaction.show(mFragments[0]).commit();
                        rbtHome.setBackgroundResource(R.drawable.shape3);
                        break;
                    case R.id.radioFunc:
                        fragmentTransaction.show(mFragments[1]).commit();
                        rtbFunc.setBackgroundResource(R.drawable.shape3);
                        break;
                    case R.id.radioSetting:
                        fragmentTransaction.show(mFragments[2]).commit();
                        rtbSetting.setBackgroundResource(R.drawable.shape3);
                        break;
                    default:
                        break;
                }

            }
        });


        }

}
