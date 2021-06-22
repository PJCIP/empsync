package com.srinidhi.lgb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.srinidhi.lgb.Prevalent.Prevalent;
import com.srinidhi.lgb.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private CircleImageView profileimageview;
    private EditText fullnametext,addresstext;
    private TextView profilechangetext,closetext,savetext,userphonetext;
    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageprofilepictureref;
    private String checker="";
    private Button SecurityQuesbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        storageprofilepictureref = FirebaseStorage.getInstance().getReference().child("profilepictures");

//        profileimageview=(CircleImageView) findViewById(R.id.setting_profile_image);
        fullnametext=(EditText) findViewById(R.id.settings_full_name);
        userphonetext= (TextView) findViewById(R.id.settings_phone_number);
        addresstext=(EditText) findViewById(R.id.settings_address);
//        profilechangetext=(TextView) findViewById(R.id.profile_image_change_text);
        closetext=(TextView) findViewById(R.id.close_settings);
        savetext=(TextView) findViewById(R.id.update_account_settings);
        SecurityQuesbtn=(Button) findViewById(R.id.security_question_btn);
        Intent intent1 = getIntent();
        String Mobile = intent1.getStringExtra("phone");
        userphonetext.setText(Mobile);
//        Toast.makeText(SettingsActivity.this,"Mobile no. is "+Mobile,Toast.LENGTH_SHORT).show();
        userinfodisplay(profileimageview,fullnametext,addresstext);

        closetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SecurityQuesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this,ResetPasswordActivity.class);
                intent.putExtra("check","settings");
                startActivity(intent);

            }
        });

        savetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("Clicked"))
                {
                    userinfosaved();
                }
                else{
                    updateonlyuserinfo();

                }
            }
        });

//        profilechangetext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checker="clicked";
//                CropImage.activity(imageUri)
//                        .setAspectRatio(1,1)
//                        .start(SettingsActivity.this);
//
//
//            }
//        });

    }

    private void updateonlyuserinfo() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
        HashMap<String,Object> userMap=new HashMap<>();
        userMap.put("name",fullnametext.getText().toString());
        userMap.put("address",addresstext.getText().toString());
//        userMap.put("phone",userphonetext.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);



        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
        Toast.makeText(SettingsActivity.this,"Profile Info Updated",Toast.LENGTH_SHORT).show();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageUri =result.getUri();
            profileimageview.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this,"Error Try Again",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void userinfosaved() {
        if(TextUtils.isEmpty(fullnametext.getText().toString()))
        {
            Toast.makeText(this,"Name Is Mandatory",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addresstext.getText().toString()))
        {
            Toast.makeText(this,"Address Is Mandatory",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(userphonetext.getText().toString()))
        {
            Toast.makeText(this,"Phone  Is Mandatory",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadimage();
        }
    }

    private void uploadimage() {
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please Wait,While We Are Updating Your Account Information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri!=null)
        {
            final StorageReference fileRef=storageprofilepictureref.child(Prevalent.currentOnlineUser.getPhone()+ ".jpg");

            uploadTask=fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            })
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                Uri downloadUri=task.getResult();
                                myUrl=downloadUri.toString();

                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Users");
                                HashMap<String,Object> userMap=new HashMap<>();
                                userMap.put("name",fullnametext.getText().toString());
                                userMap.put("address",addresstext.getText().toString());
                                userMap.put("phone",userphonetext.getText().toString());
                                userMap.put("image",myUrl);

                                ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);

                                progressDialog.dismiss();
                                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
                                Toast.makeText(SettingsActivity.this,"Profile Info Updated",Toast.LENGTH_SHORT).show();
                                finish();

                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this,"Error In Updating Info",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
        else
        {
            Toast.makeText(SettingsActivity.this,"Imagr Not Selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void userinfodisplay(final CircleImageView profileimageview, final EditText fullnametext, EditText addresstext) {
        DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.child("image").exists())
                    {
                        String image=snapshot.child("image").getValue().toString();
                        String name=snapshot.child("name").getValue().toString();
//                        String phone=snapshot.child("phone").getValue().toString();
                        String address=snapshot.child("address").getValue().toString();

                        Picasso.get().load(image).into(profileimageview);
                        fullnametext.setText(name);
//                        userphonetext.setText(phone);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
