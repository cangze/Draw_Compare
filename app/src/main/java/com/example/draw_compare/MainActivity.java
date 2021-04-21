package com.example.draw_compare;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    MSurfaceView mSurfaceView;
    Button save,reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ttttt_layout);
        checkPermission();
//        drawPicture=findViewById(R.id.drawPicture3);
        mSurfaceView=findViewById(R.id.MSurfaceView2);
        reset=findViewById(R.id.button3_reset);
        save=findViewById(R.id.button3_check);
//        next=findViewById(R.id.button5);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSurfaceView.reset();
            }
        });
//
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mSurfaceView.save();
            }
        });

    }
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //未获取到读取短信权限
            //向系统申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Toast.makeText(MainActivity.this,"Permission access ",Toast.LENGTH_SHORT).show();
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //未获取到读取短信权限
            //向系统申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Toast.makeText(MainActivity.this,"Permission access ",Toast.LENGTH_SHORT).show();
        }
    }
}
