package com.osmanlioglu.neweye;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.osmanlioglu.neweye.Models.Users;
import com.osmanlioglu.neweye.databinding.ActivitySettingsBinding;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,25);

            }
        });

        binding.SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!binding.etStatus.getText().toString().equals("") && !binding.txtUsername.getText().toString().equals("")){
                    String status = binding.etStatus.getText().toString();
                    String username = binding.txtUsername.getText().toString();

                    HashMap<String,Object> obj = new HashMap<>();
                    obj.put("userName",username);
                    obj.put("status",status);

                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);
                    Toast.makeText(SettingsActivity.this, "Profile is Updated", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(SettingsActivity.this, "Please Enter Username and Status", Toast.LENGTH_SHORT).show();
                }
            }


        });

        binding.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, PrivacyActivity.class);
                startActivity(intent);
            }
        });

        binding.textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null) {
            Uri sFile = data.getData();
            binding.profileImage.setImageURI(sFile);
            final String uid = FirebaseAuth.getInstance().getUid();
            final String filePath = "profile_pic/" + uid + "/profile.jpg";
            final StorageReference reference = storage.getReference().child(filePath);

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // URI'yi Firebase veritabanına kaydet
                            database.getReference().child("Users").child(uid)
                                    .child("profilePic").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String profilePicUrl = sharedPreferences.getString("profile_pic_url", null);
        if (profilePicUrl != null) {
            Picasso.get()
                    .load(profilePicUrl)
                    .placeholder(R.drawable.avatar)
                    .into(binding.profileImage);
        }



        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);
                        Picasso.get()
                                .load(users.getProfilePic())
                                .placeholder(R.drawable.avatar)
                                .into(binding.profileImage);

                        binding.etStatus.setText(users.getStatus());
                        binding.txtUsername.setText(users.getUserName());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }}






















