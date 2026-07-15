package com.example.chat.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chat.R;
import com.example.chat.data.BackendService;
import com.example.chat.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    private String chatId;
    private DatabaseReference chatRef;
    private MessageAdapter adapter;
    private EditText etMessage;
    private BackendService backendService;

    // Cached auth token for OkHttp interceptor
    private volatile String cachedIdToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get Chat ID from Intent or Deep Link
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction()) && intent.getData() != null) {
            Uri data = intent.getData(); // app://chat/{chatId}
            chatId = data.getLastPathSegment();
        } else {
            chatId = intent.getStringExtra("chatId");
        }

        if (chatId == null || chatId.isEmpty()) {
            Toast.makeText(this, "No Chat ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        RecyclerView rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        Button btnSend = findViewById(R.id.btnSend);

        adapter = new MessageAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(adapter);

        chatRef = FirebaseDatabase.getInstance().getReference("messages").child(chatId);
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                if (msg != null) {
                    adapter.addMessage(msg);
                    rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database listener cancelled", error.toException());
            }
        });

        // Pre-fetch auth token before setting up Retrofit
        refreshAuthToken();
        setupRetrofit();

        btnSend.setOnClickListener(v -> sendMessage());
    }

    /**
     * Fetches (or refreshes) the Firebase ID token and caches it for the OkHttp interceptor.
     */
    private void refreshAuthToken() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.getIdToken(false).addOnSuccessListener(result -> {
                cachedIdToken = result.getToken();
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Failed to fetch ID token", e);
            });
        }
    }

    private void setupRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Auth interceptor that attaches the Firebase ID token to every request
        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder();

            if (cachedIdToken != null) {
                builder.header("Authorization", "Bearer " + cachedIdToken);
            }

            Response response = chain.proceed(builder.build());

            // If we get a 401, try refreshing the token for the next request
            if (response.code() == 401) {
                Log.w(TAG, "Received 401 — token may be expired, refreshing");
                refreshAuthToken();
            }

            return response;
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/") // Emulator localhost
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        backendService = retrofit.create(BackendService.class);
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();
        String messageId = chatRef.push().getKey();
        if (messageId == null) {
            Log.e(TAG, "Failed to generate message ID");
            return;
        }

        Message message = new Message(messageId, uid, text, System.currentTimeMillis());

        chatRef.child(messageId).setValue(message)
                .addOnSuccessListener(aVoid -> {
                    etMessage.setText("");
                    syncWithBackend(message);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to send message", e);
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show();
                });
    }

    private void syncWithBackend(Message message) {
        BackendService.MessageSyncRequest req = new BackendService.MessageSyncRequest(
                chatId, message.messageId, message.senderId, "User", message.text,
                Arrays.asList(message.senderId, "other-uid"), // In production: fetch real participants
                null // Target tokens — let backend resolve or leave empty
        );

        backendService.syncAndNotify(req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Backend sync successful");
                } else {
                    Log.w(TAG, "Backend sync failed: HTTP " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e(TAG, "Backend sync error", t);
            }
        });
    }
}
