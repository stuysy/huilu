package com.example.ysy.myapplication1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FuncFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.frame_fun, container);
    }
    @Override
    public void onActivityCreated(Bundle savedinstanceState) {
        super.onActivityCreated(savedinstanceState);
        TextView tv = (TextView)getView().findViewById(R.id.funcTextView1);
        tv.setText("这是功能页面");
    }





    }
