package com.example.appdrinkfruit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.appdrinkfruit.Fragment.CartFragment;
import com.example.appdrinkfruit.Fragment.DiscountFragment;
import com.example.appdrinkfruit.Fragment.HomeFragment;
import com.example.appdrinkfruit.Fragment.SettingFragment;
import com.example.appdrinkfruit.Model.USER_MODEL;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    public static USER_MODEL USER;
    TextView tendangnhap;


    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    SearchView searchView;
    NavigationView navigationView;
    FloatingActionButton floatingActionButton;

    FragmentTransaction fragmentTransaction;

    CartFragment cartFragment;
    HomeFragment homeFragment;
    DiscountFragment discountFragment;
    SettingFragment settingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        anhxa();
        bottomNavigationView.setBackground(null);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();



        XulyBottomNavigationView();
        XulyNavigationView();

        //Khoi tao fragment
        homeFragment = new HomeFragment();
        cartFragment = new CartFragment();
        discountFragment = new DiscountFragment();
        settingFragment = new SettingFragment();


        loadFragment(homeFragment);

        searchView.setIconifiedByDefault(true);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(homeFragment);
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchResult.class);
                startActivity(intent);
            }
        });


//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                if (homeFragment != null) {
//                    homeFragment.search(query);  // Gọi hàm search của HomeFragment khi nhấn submit
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (homeFragment != null) {
//                    homeFragment.search(newText);  // Gọi hàm search của HomeFragment khi thay đổi văn bản
//                }
//                return false;
//            }
//        });


    }

    public void logout() {
        // Điều hướng người dùng đến màn hình đăng nhập
        Intent intent = new Intent(MainActivity.this, LoginActivity.class); // Giả sử bạn có một Activity cho màn hình đăng nhập
        startActivity(intent);
    }



    private void XulyNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                switch (id) {
                    case R.id.mnuHome:
                        loadFragment(homeFragment);
                        break;
                    case R.id.mnuCart:
                        loadFragment(cartFragment);
                        break;
                    case R.id.mnuAccount:
                        loadFragment(settingFragment);
                        break;
                    case R.id.logout:
                        logout();
                        Toast.makeText(MainActivity.this,"Đăng xuất thành công",Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });
    }

    private void XulyBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch (id) {
                    case R.id.btmnuHome:
                        loadFragment(homeFragment);
                        break;
                    case R.id.btmnuCart:
                        loadFragment(cartFragment);
                        break;
                    case R.id.btmnuDiscount:
                        loadFragment(discountFragment);
                        break;
                    case R.id.btnSetting:
                        loadFragment(settingFragment);
                        break;
                }
                return true;
            }
        });
    }



    private void anhxa() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchview);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navigationView = findViewById(R.id.navigationview);
        floatingActionButton = findViewById(R.id.fabhome);

        View view = navigationView.getHeaderView(0);

        tendangnhap = view.findViewById(R.id.textView2);
        if(USER != null) {
            tendangnhap.setText(USER.tendangnhap);
        }

    }

    public void loadFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayoutmain,fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setIconified(true);  // Đảm bảo SearchView luôn thu nhỏ khi quay lại
    }


}