package com.fs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fs.activity.MusicListActivity;
import com.fs.activity.RegisterActivity;
import com.fs.api.RetrofitClient;
import com.fs.api.UserApiSerivice;
import com.fs.model.Result;
import com.fs.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText et_username;
    private EditText et_password;
    private Button btn_login;
    private TextView goto_register;

    // 新增：标记请求状态，防止重复点击
    private boolean isRequesting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        addListener();
    }
    private void init() {
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        goto_register = findViewById(R.id.goto_register);
    }
    private void addListener() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 正在请求则直接返回，防止多次点击
                if (isRequesting) {
                    return;
                }
                //获取用户输入的用户名和密码
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 标记开始请求
                isRequesting = true;
                btn_login.setEnabled(false); // 按钮置灰，禁止再次点击

                //调用登录接口
                UserApiSerivice apiService = RetrofitClient.getClient().create(UserApiSerivice.class);
                Call<Result<User>> call = apiService.login(username, password);
                //发送异步请求
                call.enqueue(new Callback<Result<User>>() {
                    @Override
                    public void onResponse(Call<Result<User>> call, Response<Result<User>> response) {
                        // 请求结束，恢复状态
                        isRequesting = false;
                        btn_login.setEnabled(true);

                        if (response.isSuccessful() && response.body() != null) {
                            Result<User> result = response.body();
                            Log.i(TAG, "onResponse: " + result);
                            //跳转到音乐列表页面
                            if (result.isSuccess()){
                                Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MusicListActivity.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(LoginActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Result<User>> call, Throwable t) {
                        // 请求结束，恢复状态
                        isRequesting = false;
                        btn_login.setEnabled(true);

                        Log.e(TAG, "onFailure: ", t);
                        Toast.makeText(LoginActivity.this, "网络请求失败~!", Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
        goto_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}
