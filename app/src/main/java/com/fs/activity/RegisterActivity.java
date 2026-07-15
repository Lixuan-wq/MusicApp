package com.fs.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fs.LoginActivity;
import com.fs.R;
import com.fs.api.RetrofitClient;
import com.fs.api.UserApiSerivice;
import com.fs.model.Result;
import com.fs.model.User;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private EditText etUsername,etPassword,etNickname,etEmail,etPhone,etCity,etBirth;
    private RadioGroup rgSex;
    private MaterialButton btnRegister;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        setupListener();
    }
    private void init() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etNickname = findViewById(R.id.etNickname);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etCity = findViewById(R.id.etCity);
        etBirth = findViewById(R.id.etBirth);
        rgSex = findViewById(R.id.rgSex);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
    }
    private void setupListener() {
        btnRegister.setOnClickListener(v ->register());
        tvLogin.setOnClickListener(v ->{
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
        etBirth.setOnClickListener(v ->showDatePicker());
    }
    //显示日期选择器
    private void showDatePicker() {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH);
        int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    etBirth.setText(date);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
    //注册方法（需要看懂）
    private void register() {
        //<1> 获取用户输入的数据
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String nickname = etNickname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String birth = etBirth.getText().toString().trim();

        //<2> 如果输入的值为空，则提示用户输入
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        // 正则表达式：1开头，第二位3-9，后面9位任意数字
        String regex = "^1[3-9]\\d{9}$";
        if(!phone.matches(regex)){
            Toast.makeText(this, "手机号码格式错误", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedSexId = rgSex.getCheckedRadioButtonId();
        RadioButton selectedSex = findViewById(selectedSexId);
        String sex = selectedSex != null ? selectedSex.getText().toString() : "男";

        //<3> 创建User对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPhone(phone);
        user.setCity(city);
        user.setSex("男".equals(sex) ? 1:0);
        user.setBirth(birth);
        // <4> 获取网络请求接口
        UserApiSerivice apiService = RetrofitClient.getClient().create(UserApiSerivice.class);
        // 发起网络请求
        Call<Result> call = apiService.register(user);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Result result = response.body();
                    Toast.makeText(RegisterActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                    if (result.isSuccess()) {
                        // 注册成功，跳转到登录界面
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(RegisterActivity.this, "网络请求失败~!", Toast.LENGTH_SHORT).show();
            }

        });
    }
}