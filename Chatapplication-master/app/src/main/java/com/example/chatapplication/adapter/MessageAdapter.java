package com.example.chatapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;
import com.example.chatapplication.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private String currentUserId; // ID của người dùng hiện tại để xác định tin nhắn gửi đi hoặc nhận

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (message.getSenderId().equals(currentUserId)) {
            // Tin nhắn gửi đi
            holder.textMessageSent.setVisibility(View.VISIBLE);
            holder.textMessageSent.setText(message.getText());
            holder.textMessageReceived.setVisibility(View.GONE);
        } else {
            // Tin nhắn nhận được
            holder.textMessageReceived.setVisibility(View.VISIBLE);
            holder.textMessageReceived.setText(message.getText());
            holder.textMessageSent.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessageSent, textMessageReceived;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessageSent = itemView.findViewById(R.id.text_message_sent);
            textMessageReceived = itemView.findViewById(R.id.text_message_received);
        }
    }
}
