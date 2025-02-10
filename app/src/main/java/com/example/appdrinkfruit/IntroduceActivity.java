package com.example.appdrinkfruit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appdrinkfruit.R;

public class IntroduceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        // Ánh xạ logo và tên ứng dụng
        ImageView logo = findViewById(R.id.logo);
        TextView appName = findViewById(R.id.app_name);

        // Tải animation từ tệp anim
        Animation fadeInLogo = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeInAppName = AnimationUtils.loadAnimation(this, R.anim.fade_in_name);

        // Áp dụng animation cho logo và tên ứng dụng
        logo.startAnimation(fadeInLogo);
        appName.startAnimation(fadeInAppName);

        // Chuyển sang màn hình chính sau khi giới thiệu kết thúc (3 giây)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(IntroduceActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Đóng màn hình giới thiệu sau khi chuyển màn hình
            }
        }, 3000); // Độ trễ 3 giây (3000ms)
    }
}
