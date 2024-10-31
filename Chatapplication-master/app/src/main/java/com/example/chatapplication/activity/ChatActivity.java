package com.example.chatapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.chatapplication.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ChildEventListener;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.view.View;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference messagesRef;
    private ListView chatListView;
    private EditText messageField;
    private Button sendButton;
    private List<String> messagesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat); // Liên kết với layout activity_chat.xml

        // Khởi tạo Firebase và các thành phần giao diện
        database = FirebaseDatabase.getInstance();
        messagesRef = database.getReference("messages");
        chatListView = findViewById(R.id.chat_list);
        messageField = findViewById(R.id.message_field);
        sendButton = findViewById(R.id.send_button);

        // Thiết lập adapter cho ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messagesList);
        chatListView.setAdapter(adapter);

        // Lắng nghe các tin nhắn mới
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                String newMessage = dataSnapshot.getValue(String.class);
                if (newMessage != null) {
                    messagesList.add(newMessage);
                    adapter.notifyDataSetChanged();
                    chatListView.setSelection(messagesList.size() - 1); // Cuộn xuống tin nhắn mới nhất
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ChatActivity", "loadPost:onCancelled", databaseError.toException());
            }
        });

        // Logic nút gửi tin nhắn
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageField.getText().toString().trim();
                if (!message.isEmpty()) {
                    messagesRef.push().setValue(message); // Gửi tin nhắn đến Firebase
                    messageField.setText(""); // Xóa nội dung nhập sau khi gửi
                }
            }
        });
    }
}
