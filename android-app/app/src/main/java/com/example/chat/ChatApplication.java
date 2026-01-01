package main.java.com.example.chat;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class ChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Enable offline persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
