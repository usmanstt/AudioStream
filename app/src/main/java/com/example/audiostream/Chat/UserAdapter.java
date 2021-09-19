package com.example.audiostream.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.audiostream.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    ArrayList<UserModel> list;
    Context context;
    FirebaseUser user;
    RecyclerView recyclerView;
    public UserAdapter(ArrayList<UserModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.ViewHolder holder, int position) {
        try {
            user= FirebaseAuth.getInstance().getCurrentUser();
            final UserModel users = list.get(position);
            assert user != null;
            if(user.getUid().equals(users.getUid()))
            {
                    holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));

            }
            holder.UserName.setText(users.getUsername());
            holder.email.setText(users.getEmail());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ChatDetailActivity.class);
                    intent.putExtra("id", users.getUid());
                    intent.putExtra("username", users.getUsername());
                    intent.putExtra("devicetoken", users.getDevicetoken());
                    context.startActivity(intent);
                }
            });
        }
        catch (NullPointerException e )
        {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView UserName,email;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.profilepic);
            UserName=itemView.findViewById(R.id.username);
            email=itemView.findViewById(R.id.tvemail);
        }
    }
    public void filterlist(ArrayList<UserModel> filtereddata) {
        list=filtereddata;
        notifyDataSetChanged();
    }
}
