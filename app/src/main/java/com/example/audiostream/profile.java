package com.example.audiostream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

public class profile extends AppCompatActivity {
    Button logout,inbox,musicupload,podcastupload,articlesupload, backbutton;
    ImageView pfp;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase, firebaseDatabase1, firebaseDatabase2;
    DatabaseReference databaseReference, databaseReference1;
    TextView name;
    String uid;
    EditText title;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fauth = FirebaseAuth.getInstance();
        fuser =fauth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase2 = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        storageReference = FirebaseStorage.getInstance().getReference();

        logout = findViewById(R.id.btnlogout);
        pfp = findViewById(R.id.pfp);
        name = findViewById(R.id.usersnameview);
        inbox = findViewById(R.id.btncollab);
        musicupload = findViewById(R.id.musicupload);
        podcastupload = findViewById(R.id.podcastupload);
        articlesupload = findViewById(R.id.articleupload);
        backbutton = findViewById(R.id.backbtn);
        progressBar = findViewById(R.id.progress);


        //Retrieving user's info
        Query query = databaseReference.orderByChild("email").equalTo(fuser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                  for(DataSnapshot ds: snapshot.getChildren()){

                      //get data
                      String username = ds.child("username").getValue(String.class);
                      String image = ds.child("image").getValue(String.class);

                      //set data
                      name.setText(username);
                      try{
                          Picasso.get().load(image).into(pfp);
                      }
                      catch (Exception x){
                          Toast.makeText(profile.this, x.toString() , Toast.LENGTH_LONG).show();
                      }
                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "Error fetching data", Toast.LENGTH_LONG).show();
            }
        });

        //uploadingmusic
        musicupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickaudio = new Intent();
                pickaudio.setType("audio/*");
                pickaudio.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(pickaudio, 1200);

            }
        });

        //podcast upload
        podcastupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickpodcastaudio = new Intent();
                pickpodcastaudio.setType("audio/*");
                pickpodcastaudio.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(pickpodcastaudio, 1300);
            }
        });
        //article upload
        articlesupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickarticlefile = new Intent();
                pickarticlefile.setType("application/pdf");
                pickarticlefile.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(pickarticlefile, 1400);
            }
        });



        //logout functionality
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), loginpage.class));
                Toast.makeText(getApplicationContext(), "Logged out Successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), homepage.class));
            }
        });

    }

    public void changepfp(View view) {

        pfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //checking for the right activity for image
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                pfp.setImageURI(imageUri);
                
                uploadImage(imageUri);

                
            }
        }
        //checking for the right activity for music audio.
        if(requestCode == 1200){

            if (resultCode == Activity.RESULT_OK){
                Log.i("success", "Statement reach 2");
                Uri audiouri = data.getData();
                uploadaudio(audiouri);
            }
        }
        //checking for the right activity for podcast audio.
        if(requestCode == 1300){

            if (resultCode == Activity.RESULT_OK){
                Log.i("success", "Statement reach 2");
                Uri audiouripodcast = data.getData();
                uploadpodcastaudio(audiouripodcast);
            }
        }

        //checking for the right activity for articles
        if (requestCode == 1400){
            if(resultCode == Activity.RESULT_OK){
                Uri articleuri = data.getData();
                uploadarticle(articleuri);
            }
        }
    }

    private void uploadarticle(Uri articleuri) {
        title = findViewById(R.id.filetitle);
        String filename = title.getText().toString();
        StorageReference articleFileReference = storageReference.child("users/" + "articles" + "/" + filename + ".pdf");
        articleFileReference.putFile(articleuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                articleFileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        Toast.makeText(profile.this, "Article Uploaded", Toast.LENGTH_SHORT).show();
                        Task<Uri> uriforarticle = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriforarticle.isComplete());
                        Uri articleurl = uriforarticle.getResult();

                        uploadArticles uploadArticle = new uploadArticles(filename, articleurl.toString());
                        String uid = fauth.getCurrentUser().getUid().toString();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("users").child(uid).child("articles");
//                        databaseReference.child("article1").setValue(uploadArticle);
                        databaseReference1 = firebaseDatabase2.getReference("users").child("articles");

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue() != null){
                                    int totalarticles = (int) snapshot.getChildrenCount();
                                    totalarticles = totalarticles + 1;
                                    String articleadd = "article" + String.valueOf(totalarticles);
                                    databaseReference.child(articleadd).setValue(uploadArticle);

                                }
                                else if(snapshot.getValue() == null){
                                    databaseReference.child("article1").setValue(uploadArticle);
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Error Occured: " + error.toString());
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                        //uploading to different node for retrieving
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue() != null){
                                    int totalarticles = (int) snapshot.getChildrenCount();
                                    totalarticles = totalarticles + 1;
                                    String articleadd = "article" + String.valueOf(totalarticles);
                                    databaseReference1.child(articleadd).setValue(uploadArticle);

                                }
                                else if(snapshot.getValue() == null){
                                    databaseReference1.child("article1").setValue(uploadArticle);
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Error Occured: " + error.toString());
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this, "Article Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }



    //uploading podcast
    private void uploadpodcastaudio(Uri audiouripodcast) {
        title = findViewById(R.id.filetitle);
        String filename = title.getText().toString();

        StorageReference audioFileReference = storageReference.child("users/"+"podcast"+"/"+filename);
        audioFileReference.putFile(audiouripodcast).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                audioFileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Task<Uri> uriformusic = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriformusic.isComplete());
                        Uri podcasturl = uriformusic.getResult();

                        uploadPodcasts uploadPodcasts = new uploadPodcasts(filename, podcasturl.toString());
                        String uid = fauth.getCurrentUser().getUid().toString();
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("users").child(uid).child("podcasts");
                        databaseReference1 = firebaseDatabase2.getReference("users").child("podcasts");

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue() != null){
                                    int totalpodcasts = (int) snapshot.getChildrenCount();
                                    totalpodcasts = totalpodcasts + 1;
                                    String podcastadd = "podcast" + String.valueOf(totalpodcasts);
                                    databaseReference.child(podcastadd).setValue(uploadPodcasts);

                                }
                                else if(snapshot.getValue() == null){
                                    databaseReference.child("podcast1").setValue(uploadPodcasts);
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Error Occured: " + error.toString());
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });

                        //for retrieving
                        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.getValue() != null){
                                    int totalpodcasts = (int) snapshot.getChildrenCount();
                                    totalpodcasts = totalpodcasts + 1;
                                    String podcastadd = "podcast" + String.valueOf(totalpodcasts);
                                    databaseReference1.child(podcastadd).setValue(uploadPodcasts);

                                }
                                else if(snapshot.getValue() == null){
                                    databaseReference1.child("podcast1").setValue(uploadPodcasts);
                                }
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                alertDialog.setTitle("Error");
                                alertDialog.setMessage("Error Occured: " + error.toString());
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this, "Podcast Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }


    //uploading music
    private void uploadaudio(Uri audiouri) {
        title = findViewById(R.id.filetitle);
        String filename = title.getText().toString();


        StorageReference audioFileReference = storageReference.child("users/"+"music"+"/"+filename);
        audioFileReference.putFile(audiouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    audioFileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Task<Uri> uriformusic = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriformusic.isComplete());
                            Uri musicurl = uriformusic.getResult();

                            uploadMusic uploadMusic = new uploadMusic(filename, musicurl.toString());
                            String uid = fauth.getCurrentUser().getUid().toString();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference("users").child(uid).child("music");
                            databaseReference1 = firebaseDatabase2.getReference("users").child("music");

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue() != null){
                                        int totalmusictracks = (int) snapshot.getChildrenCount();
                                        totalmusictracks = totalmusictracks + 1;
                                        String musictracksadd = "musictrack" + String.valueOf(totalmusictracks);
                                        databaseReference.child(musictracksadd).setValue(uploadMusic);

                                    }
                                    else if(snapshot.getValue() == null){
                                        databaseReference.child("musictrack1").setValue(uploadMusic);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage("Error Occured: " + error.toString());
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });

                            //for retrieving
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.getValue() != null){
                                        int totalmusictracks = (int) snapshot.getChildrenCount();
                                        totalmusictracks = totalmusictracks + 1;
                                        String musictracksadd = "musictrack" + String.valueOf(totalmusictracks);
                                        databaseReference1.child(musictracksadd).setValue(uploadMusic);

                                    }
                                    else if(snapshot.getValue() == null){
                                        databaseReference1.child("musictrack1").setValue(uploadMusic);
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                                    alertDialog.setTitle("Error");
                                    alertDialog.setMessage("Error Occured: " + error.toString());
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    alertDialog.show();
                                }
                            });

                        }
                    });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this, "Music Could Not Be Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    //image upload
    private void uploadImage(Uri imageUri) {
        //upload image to firebase storage
        StorageReference imageFileReference = storageReference.child("users/"+fuser.getUid()+"/profile.jpg");
        imageFileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pfp);
                        firebaseDatabase = FirebaseDatabase.getInstance();
                        databaseReference = firebaseDatabase.getReference("users");
                        uid = fauth.getCurrentUser().getUid();
                        databaseReference.child(uid).child("image").setValue(uri.toString());
                        progressBar.setVisibility(View.GONE);

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile.this, "Image Could Not Be Updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

}