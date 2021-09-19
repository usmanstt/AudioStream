package com.example.audiostream.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.audiostream.R;
import com.example.audiostream.profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AllUserActivity extends AppCompatActivity {
    ImageView backhome;
    RecyclerView alluserRecycler;
    LinearLayoutManager layoutManager;
    UserAdapter userAdapter;
    EditText search;
    ArrayList<UserModel> list=new ArrayList<>();
    DatabaseReference reference,databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);
        backhome=(ImageView)findViewById(R.id.backhome);
        alluserRecycler=(RecyclerView)findViewById(R.id.alluserrecyclerview);
        layoutManager=new LinearLayoutManager(AllUserActivity.this);
        userAdapter=new UserAdapter(list,AllUserActivity.this);
        alluserRecycler.setLayoutManager(layoutManager);
        alluserRecycler.setAdapter(userAdapter);
        search=findViewById(R.id.search);
        databaseReference=FirebaseDatabase.getInstance().getReference();
        reference= FirebaseDatabase.getInstance().getReference();
        fetchAllUser();

     search.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {

         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {

         }

         @Override
         public void afterTextChanged(Editable s) {
             filter(s.toString());

         }
     });
        //Back to profile
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllUserActivity.this, profile.class));
                finish();
            }
        });
    }

    //Fetch All users-------------------------------------------------------------------------------
  public void fetchAllUser()
  {
      try {
          reference.child("users").child("profile").addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                  list.clear();
                  if(snapshot.exists())
                  {
                      for(DataSnapshot dataSnapshot:snapshot.getChildren())
                      {
                          UserModel model=dataSnapshot.getValue(UserModel.class);
                          list.add(model);


                      }
                      userAdapter.notifyDataSetChanged();
                  }
              }

              @Override
              public void onCancelled(@NonNull @NotNull DatabaseError error) {

              }
          });

      }
      catch (NullPointerException e)
      {
          e.printStackTrace();
      }
  }
  // Search users------------------------------------------------------------------------------------------------------------------------------------
    private void  filter (String text)
    {
        ArrayList<UserModel> filtereddata=new ArrayList<>();
        UserAdapter adapter= new UserAdapter(list,AllUserActivity.this);
        alluserRecycler.setAdapter(adapter);
        databaseReference.child("users").child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    UserModel helper=dataSnapshot.getValue(UserModel.class);
                    if(helper.getUsername().toLowerCase().contains(text.toLowerCase()))
                    {
                        filtereddata.add(helper);
                    }
                    adapter.filterlist(filtereddata);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }



}