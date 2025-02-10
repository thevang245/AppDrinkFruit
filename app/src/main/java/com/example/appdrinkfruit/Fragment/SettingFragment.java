package com.example.appdrinkfruit.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.appdrinkfruit.LoginActivity;
import com.example.appdrinkfruit.MainActivity;
import com.example.appdrinkfruit.Model.USER_MODEL;
import com.example.appdrinkfruit.R;
public class SettingFragment extends Fragment {

    public static USER_MODEL USER;

    private static final int REQUEST_IMAGE_PICK = 1;
    ImageView ivAvatar;
    Button btnChangeAvatar;

    AppCompatButton btnlogout;

    TextView tvUser, tvDiachi,tvSDT;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_setting_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivAvatar = view.findViewById(R.id.iv_avatar);  // Ảnh đại diện
        btnChangeAvatar = view.findViewById(R.id.btn_change_avatar);  // Nút đổi ảnh

        tvUser = view.findViewById(R.id.tv_user);
        // Lấy tên đăng nhập từ MainActivity.USER
        if(MainActivity.USER != null) {
            tvUser.setText(MainActivity.USER.tendangnhap);
        } else {
            tvUser.setText("Người dùng chưa đăng nhập");
        }

        tvDiachi = view.findViewById(R.id.tv_diachi);
        // Lấy tên đăng nhập từ MainActivity.USER
        if(MainActivity.USER != null) {
            tvDiachi.setText(MainActivity.USER.diachi);
        } else {
            tvUser.setText("Người dùng chưa đăng nhập");
        }

        if(MainActivity.USER.diachi.length() > 30) {
            String diachi = MainActivity.USER.diachi.substring(0,30);
            tvDiachi.setText(diachi+"...");
        }


        tvSDT = view.findViewById(R.id.tv_sdt);
        // Lấy tên đăng nhập từ MainActivity.USER
        if(MainActivity.USER != null) {
            tvSDT.setText(MainActivity.USER.sodienthoai);
        } else {
            tvUser.setText("Người dùng chưa đăng nhập");
        }




        // Sự kiện khi nhấn nút "Đổi ảnh đại diện"
        btnChangeAvatar.setOnClickListener(v -> openImagePicker());

        btnlogout = view.findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(v -> {
            logout();
            Toast.makeText(getContext(), "Bạn đã đăng xuất", Toast.LENGTH_LONG).show();
        });
    }

    // Phương thức mở thư viện ảnh để chọn
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    // Xử lý kết quả khi người dùng chọn ảnh từ thư viện
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Hiển thị ảnh đã chọn lên ImageView
                ivAvatar.setImageURI(selectedImageUri);

                // Tại đây, bạn có thể thực hiện các bước lưu trữ ảnh đại diện của người dùng (ví dụ: tải lên server)
                // uploadImageToServer(selectedImageUri);
            }
        }
    }

    // Phương thức đăng xuất
    public void logout() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }
}