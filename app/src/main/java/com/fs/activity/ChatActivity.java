package com.fs.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fs.R;
import com.fs.api.ApiClient;
import com.fs.api.ZhiPuApiService;
import com.fs.api.ZhiPuRetrofitClient;
import com.fs.model.ChatRequest;
import com.fs.model.ChatResponse;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";
    private static final String API_KEY = "e56eb2680fbd47ebb31f4bc0f340f6cd.fihihVNVUyaYpCdW";

    private EditText etInput;
    private TextView tvResponse;
    private ProgressBar progressBar;
    private MaterialCardView cardResponse;
    private FloatingActionButton fabSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etInput = findViewById(R.id.etInput);
        tvResponse = findViewById(R.id.tvResponse);
        progressBar = findViewById(R.id.progressBar);
        cardResponse = findViewById(R.id.cardResponse);
        fabSend = findViewById(R.id.fabSend);
    }

    private void setupListeners() {
        fabSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String message = etInput.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, "请输入消息", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        callZhiPuAI(message);
    }

    private void callZhiPuAI(String userMessage) {
        List<ChatRequest.Message> messages = new ArrayList<>();
        messages.add(new ChatRequest.Message("user", userMessage));

        ChatRequest request = new ChatRequest(
                "glm-4-plus",
                messages,
                4096,
                0.7
        );
        request.setThinking(new ChatRequest.Thinking("enable"));

        ZhiPuApiService apiService = ZhiPuRetrofitClient.getClient().create(ZhiPuApiService.class);
        Call<ChatResponse> call = apiService.createChatCompletion("Bearer " + API_KEY, request);

        call.enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ChatResponse chatResponse = response.body();
                    if (chatResponse.getChoices() != null && !chatResponse.getChoices().isEmpty()) {
                        String content = chatResponse.getChoices().get(0).getMessage().getContent();
                        Log.d(TAG, "AI回复: " + content);
                        showResponse(content);
                        etInput.setText("");
                    } else {
                        showError("未获取到回复内容");
                    }
                } else {
                    Log.e(TAG, "请求失败: " + response.code());
                    showError("请求失败: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                showLoading(false);
                Log.e(TAG, "网络错误: " + t.getMessage());
                showError("网络错误: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        fabSend.setEnabled(!isLoading);
        etInput.setEnabled(!isLoading);
    }

    private void showResponse(String response) {
        cardResponse.setVisibility(View.VISIBLE);
        tvResponse.setText(response);
    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}