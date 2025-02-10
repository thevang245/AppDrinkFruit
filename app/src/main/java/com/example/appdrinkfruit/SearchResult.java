package com.example.appdrinkfruit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;

import com.example.appdrinkfruit.Fragment.CartFragment;
import com.example.appdrinkfruit.Fragment.SearchFragment;

public class SearchResult extends AppCompatActivity {

    SearchFragment searchFragment;
    TextView tvsearch;
    ImageView btnCart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView back = findViewById(R.id.result_back);
        tvsearch = findViewById(R.id.hienthisearch);
        btnCart = findViewById(R.id.btn_cart);

        SearchView searchQuery = findViewById(R.id.searchView);
        searchQuery.setIconifiedByDefault(false); // Mở rộng SearchView ngay từ đầu
        searchQuery.requestFocus();
        // Khởi tạo fragment tìm kiếm
        if (savedInstanceState == null) {
            searchFragment = new SearchFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_searchresult, searchFragment)
                    .commit();
        } else {
            searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.frame_searchresult);
        }

        // Nút quay lại
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResult.this, MainActivity.class);
                finish();
            }
        });
        // Lấy query từ Intent
        String searchQueryIntent = getIntent().getStringExtra("tukhoasearch");
        if (searchQueryIntent != null) {
            searchQuery.setQuery(searchQueryIntent, true); // Đặt từ khóa vào thanh search
            tvsearch.setText("Kết quả tìm kiếm: " + searchQueryIntent);

        }

        // Lắng nghe sự thay đổi của thanh SearchView
        searchQuery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchFragment != null) {
                    searchFragment.search(newText); // Tìm kiếm theo nội dung mới nhập

                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (searchFragment != null) {
                    searchFragment.search(query); // Tìm kiếm khi nhấn Enter
                }
                return true;
            }
        });
    }
}
