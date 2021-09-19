package com.example.audiostream.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.audiostream.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

public class ChatDetailActivity extends AppCompatActivity {
    CircleImageView cahtpic;
    TextView chatname;
    ImageView backarrow,attachfile,SendSms;
    RecyclerView chatrecyclerview;
    EditText sms_editText;
    String id,devicetoken,username,senderRoom,receiverRoom;
    FirebaseUser user;
    String SenderName;
    String Message;
    ChatAdapter adapter;
    DatabaseReference reference,reference1,reference2;
    LinearLayoutManager layoutManager;
    final int RESULT_LOAD_IMAGE = 0;
    ArrayList <ChatModel> list=new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == RESULT_OK) {
                assert data != null;



            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        Initialization();
        showsendmessage();
;

        attachfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatDetailActivity.this,AllUserActivity.class));
                finish();
            }
        });
          reference2.child("users").child("profile").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                  if(snapshot.exists())
                  {
                      UserModel model=snapshot.getValue(UserModel.class);
                      SenderName=model.getUsername();
                  }
              }

              @Override
              public void onCancelled(@NonNull @NotNull DatabaseError error) {

              }
          });

        SendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmessage();
                sendFCMPush();
            }
        });

    }






    private void sendmessage()
    {

        Message=sms_editText.getText().toString();
        Date date=new Date();
        ChatModel chatModel=new ChatModel(Message,user.getUid(),id,date.getTime());
        reference.child("users").child("Chat").child(receiverRoom).child("Message").push().setValue(chatModel);
        reference1.child("users").child("Chat").child(senderRoom).child("Message").push().setValue(chatModel);
        sms_editText.setText(null);
        adapter.notifyDataSetChanged();
    }
    private void showsendmessage()
    {
        reference.child("users").child("Chat").child(senderRoom).child("Message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                if(snapshot.exists()) {
                    for (DataSnapshot d : snapshot.getChildren()) {
                        ChatModel chatModel = d.getValue(ChatModel.class);
                        assert chatModel != null;

                        {
                            list.add(chatModel);

                        }
                        adapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }




    public void Initialization()
    {
        cahtpic=(CircleImageView)findViewById(R.id.cahtpic);
        chatname=(TextView)findViewById(R.id.chatname);
        backarrow=(ImageView)findViewById(R.id.backarrow);
        chatrecyclerview=(RecyclerView)findViewById(R.id.chatrecyclerview);
        sms_editText=(EditText)findViewById(R.id.sms_editText);
        attachfile=(ImageView)findViewById(R.id.IVattach);
        layoutManager=new LinearLayoutManager(ChatDetailActivity.this);
        layoutManager.setStackFromEnd(true);
        adapter=new ChatAdapter(list,ChatDetailActivity.this);
        SendSms=(ImageView)findViewById(R.id.ivsend);
        chatrecyclerview.setAdapter(adapter);
        user= FirebaseAuth.getInstance().getCurrentUser();
        chatrecyclerview.setLayoutManager(layoutManager);
        reference= FirebaseDatabase.getInstance().getReference();
        reference1= FirebaseDatabase.getInstance().getReference();
        id=getIntent().getStringExtra("id");
        senderRoom=user.getUid()+id;
        receiverRoom=id+user.getUid();
        devicetoken=getIntent().getStringExtra("devicetoken");
        username=getIntent().getStringExtra("username");
        chatname.setText(username);
        reference2=FirebaseDatabase.getInstance().getReference();

    }


    private void sendFCMPush() {

        String Legacy_SERVER_KEY = "AAAAUd2BJwk:APA91bGbH_srmQM_xVdUAgFY-TySpekXa5_cTUzvh3C4GExqFtHJra-_BE8Wa4hOv0ghGV1c2qjeAjWA8EkpJUtoEA2gCC0uHvrUCTBzRnR_9hxsvn4XWLXTuw2f2vM4cz8dk7TMrzlp";
        String msg = Message;
        String title = SenderName;
        String token = devicetoken;

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name image must be there in drawable
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                (com.android.volley.Response.Listener<JSONObject>) response -> Log.e("!_@@_SUCESS", response + ""),
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

}