package com.cctvjiatao.customviewgroup.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cctvjiatao.customviewgroup.R;

/**
 * 需求：我们定义一个ViewGroup，内部可以传入0到4个childView，分别依次显示在左上角，右上角，左下角，右下角
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_main2);
        setContentView(R.layout.activity_main3);
    }
}
