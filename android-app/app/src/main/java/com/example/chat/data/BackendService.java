package com.example.chat.data;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BackendService {
    @POST("notify/message")
    Call<Void> syncAndNotify(@Body MessageSyncRequest request);

    class MessageSyncRequest {
        public String chatId;
        public String messageId;
        public String senderId;
        public String senderName;
        public String text;
        public List<String> participantUids;
        public List<String> targetTokens;

        public MessageSyncRequest(String chatId, String messageId, String senderId, String senderName, String text,
                List<String> participantUids, List<String> targetTokens) {
            this.chatId = chatId;
            this.messageId = messageId;
            this.senderId = senderId;
            this.senderName = senderName;
            this.text = text;
            this.participantUids = participantUids;
            this.targetTokens = targetTokens;
        }
    }
}
