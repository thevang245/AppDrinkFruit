package com.example.appdrinkfruit;// SearchActivity.java
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.appdrinkfruit.Fragment.HomeFragment;
import com.example.appdrinkfruit.Fragment.SearchFragment;

public class SearchActivity extends AppCompatActivity {

    SearchView searchView;
    HomeFragment homeFragment;
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.btn_search);
        back = findViewById(R.id.search_back);
        homeFragment = new HomeFragment();

        searchView.setIconifiedByDefault(false);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Khi nhấn Enter, chuyển sang SearchResultActivity
                Intent intent = new Intent(SearchActivity.this, SearchResult.class);
                intent.putExtra("tukhoasearch", query); // Truyền query đến SearchResultActivity
                startActivity(intent);


                return true; // Trả về true để chỉ ra rằng sự kiện đã được xử lý
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xử lý thay đổi văn bản nếu cần
                return false; // Trả về false nếu không xử lý
            }
        });
    }
}
