package com.example.chat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chat.R;
import com.google.firebase.auth.FirebaseAuth;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        EditText etChatId = findViewById(R.id.etChatId);
        Button btnJoin = findViewById(R.id.btnJoin);
        Button btnLogout = findViewById(R.id.btnLogout);

        btnJoin.setOnClickListener(v -> {
            String chatId = etChatId.getText().toString().trim();
            if (!chatId.isEmpty()) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("chatId", chatId);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
