package com.example.audiostream.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiostream.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int MSG_TYP_SENT=1;
    public static final int MSG_TYP_RECIEVE=2;
    private List<ChatModel> mChat;
    FirebaseUser fuser;
    Context context;

    public ChatAdapter(List<ChatModel> mChat,Context context) {
        this.mChat = mChat;
        this.context=context;
    }
    public void setList(List<ChatModel> mChat,Context context) {
        this.mChat = mChat;
        this.context=context;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        if(viewType==MSG_TYP_SENT)
        {
            return new SendMessageViewHolder(layoutInflater,parent);
        }
        else {
            return new RecieveMessageViewHolder(layoutInflater,parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull RecyclerView.ViewHolder holder, int position) {
        ChatModel model=mChat.get(position);
        DateFormat df = new SimpleDateFormat("HH:mm");
        String dateString=df.format(new Date(Long.parseLong(String.valueOf(model.getTimeStamp()))));

        if(holder.getClass()==SendMessageViewHolder.class)
        {
            ((SendMessageViewHolder) holder).SendMessageShow.setText(model.getSendermessage());

            ((SendMessageViewHolder) holder).timeSender.setText(dateString);
        }
        else
        {
            ((RecieveMessageViewHolder)holder).ShowMessage.setText((model.getSendermessage()));
            ((RecieveMessageViewHolder)holder).timereceiver.setText(dateString);

        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
    @Override
    public int getItemViewType(int position) {
        fuser=FirebaseAuth.getInstance().getCurrentUser();
        ChatModel model=mChat.get(position);
        if(model.getId().equals(fuser.getUid()))
        {
            return MSG_TYP_RECIEVE;
        }
        else
            return MSG_TYP_SENT;
    }
    public static class SendMessageViewHolder extends RecyclerView.ViewHolder{
        public TextView SendMessageShow,timeSender;

        public SendMessageViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.chatsender,viewGroup,false));
            SendMessageShow=itemView.findViewById(R.id.sendmessage);
            timeSender=itemView.findViewById(R.id.timesender);

        }
    }
    public static class RecieveMessageViewHolder extends RecyclerView.ViewHolder{
        public TextView ShowMessage,timereceiver;

        public RecieveMessageViewHolder(LayoutInflater layoutInflater,ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.chatreciever,viewGroup,false));
            ShowMessage=itemView.findViewById(R.id.recievemessage);
            timereceiver=itemView.findViewById(R.id.timereceiver);

        }
    }

}
