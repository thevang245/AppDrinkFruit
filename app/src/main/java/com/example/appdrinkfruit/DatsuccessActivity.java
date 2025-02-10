package com.example.appdrinkfruit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appdrinkfruit.Fragment.CartFragment;
import com.example.appdrinkfruit.Fragment.HomeFragment;

public class DatsuccessActivity extends AppCompatActivity {

    Button btnTT;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datsuccess);


        btnTT = findViewById(R.id.btnHome);

        LottieAnimationView successAnimation = findViewById(R.id.imgsucces);
        successAnimation.playAnimation();

        btnTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DatsuccessActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }
}