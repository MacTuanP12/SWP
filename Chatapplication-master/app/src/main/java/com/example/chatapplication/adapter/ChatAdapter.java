package com.example.chatapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import com.example.chatapplication.model.Chat;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private List<Chat> chatList;
    private OnChatClickListener listener;

    public ChatAdapter(Context context, List<Chat> chatList, OnChatClickListener listener) {
        this.context = context;
        this.chatList = chatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        holder.chatName.setText(chat.getChatName());
        holder.lastMessage.setText(chat.getLastMessage());

        // Lấy URL của ảnh từ Firebase Storage
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference().child("profileImages/" + chat.getProfileImageUrl());

        // Sử dụng Glide để tải ảnh từ Firebase Storage
        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(context)
                    .load(uri)
                    .placeholder(R.drawable.profile_placeholder)
                    .into(holder.profileImage);
        }).addOnFailureListener(e -> {

            holder.profileImage.setImageResource(R.drawable.profile_placeholder);
        });

        holder.itemView.setOnClickListener(v -> listener.onChatClick(chat));
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView chatName, lastMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            chatName = itemView.findViewById(R.id.chatName);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }
}
