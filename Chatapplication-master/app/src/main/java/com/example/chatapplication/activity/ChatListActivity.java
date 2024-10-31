package com.example.chatapplication.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.adapter.ChatAdapter;
import com.example.chatapplication.model.Chat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChatList;
    private ChatAdapter chatAdapter;
    private List<Chat> chatList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        recyclerViewChatList = findViewById(R.id.recyclerViewChatList);
        recyclerViewChatList.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>(); // Khởi tạo danh sách cuộc trò chuyện
        chatAdapter = new ChatAdapter(this, chatList, chat -> {
            // Xử lý sự kiện khi người dùng bấm vào cuộc trò chuyện
            Intent intent = new Intent(ChatListActivity.this, ChatActivity.class);
            intent.putExtra("chatId", chat.getChatId());
            startActivity(intent);
        });

        recyclerViewChatList.setAdapter(chatAdapter);

        loadChats(); // Tải dữ liệu các cuộc trò chuyện từ Firebase
    }

    private void loadChats() {
        // Lấy danh sách cuộc trò chuyện từ Firebase và cập nhật vào chatList
        DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    chatList.add(chat);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
            }
        });
    }
}
