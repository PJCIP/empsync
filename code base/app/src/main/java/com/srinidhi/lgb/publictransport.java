package com.srinidhi.lgb;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.srinidhi.lgb.utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

class pGlobal {
    public static String tomail;

}

public class publictransport extends AppCompatActivity
{
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageView img;
    Button browse, upload;
    Uri filepath;
    Bitmap bitmap;
    int ticket_id;
    List<String> bccmail,ccmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);

        img=(ImageView)findViewById(R.id.imageView);
        upload=(Button)findViewById(R.id.upload);
        browse=(Button)findViewById(R.id.browse);
        ticket_id = Prefconfig.loadticketid(this);

        ccmail = new ArrayList<>();
        bccmail = new ArrayList<>();
        DatabaseReference mailfetch = FirebaseDatabase.getInstance().getReference().child("admin");
        mailfetch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    pGlobal.tomail = snapshot.child("mail").getValue(String.class);
//                    Toast.makeText(publictransport.this, "Admin mail id is "+ pGlobal.tomail, Toast.LENGTH_LONG).show();
                    Toast.makeText(publictransport.this, "Fetched mail id", Toast.LENGTH_LONG).show();
                    //Need to convert the testmail to mail this is just for testing purpose
//                    Global.tomail = snapshot.child("testmail").getValue(String.class);
//                    Toast.makeText(owntransport.this, "Admin mail id is "+ Global.tomail+"my mail id is"+Global.frommail, Toast.LENGTH_LONG).show();
//                    Toast.makeText(publictransport.this, "Fetched mailid", Toast.LENGTH_LONG).show();
                    for (DataSnapshot childSnapshot : snapshot.child("cc").getChildren()) {
                        String mailc = String.valueOf(childSnapshot.getValue());
                        ccmail.add(mailc);
                    }
                    Toast.makeText(publictransport.this, "Fetched cc"+ccmail, Toast.LENGTH_LONG).show();
                    for (DataSnapshot childSnapshot : snapshot.child("bcc").getChildren()) {
                        String mailc = String.valueOf(childSnapshot.getValue());
                        bccmail.add(mailc);
                    }
                    Toast.makeText(publictransport.this, "Fetched bcc"+bccmail, Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(publictransport.this, "Unable to fetch mailid", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Dexter.withActivity(publictransport.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response)
                            {
                                Intent intent=new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent,"Please select Image"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadtofirebase();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
            }catch (Exception ex)
            {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadtofirebase()
    {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setTitle("File Uploader");
        dialog.show();


        FirebaseStorage storage= FirebaseStorage.getInstance();

        //Need to update this below for multiple users
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.getDefault());
        final String formatteddatetime = df2.format(c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String formatteddate = df.format(c);
        SimpleDateFormat df1 = new SimpleDateFormat("hh:mm", Locale.getDefault());
        final String formattedtime = df1.format(c);
        Intent intent = getIntent();
        final String username = intent.getStringExtra("username").trim();
        final String usermail = intent.getStringExtra("mailid");
//        final String[] generatedFilePath = new String[1];
//        final String[] generatedFilePath = new String[1];
        StorageReference uploader=storage.getReference().child(usermail).child(formatteddate).child(formattedtime).child("ticket "+formatteddatetime);
//        StorageReference storageRef = storage.getReference();
        uploader.putFile(filepath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"File Uploaded", Toast.LENGTH_LONG).show();
                        ticket_id++;
                        Prefconfig.saveticketid(getApplicationContext(), ticket_id);
//                        storageRef.child("lgbl-c68c1.appspot.com/"+username+formatteddate+formattedtime).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                // Got the download URL for 'users/me/profile.png'
////                                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
//                                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
//                                generatedFilePath[0] = downloadUri.getResult().toString();
////                                generatedFilePath[0] = downloadUri.toString(); /// The string(file link) that you need
//                                Toast.makeText(getApplicationContext(),"Try "+ generatedFilePath[0]+downloadUri, Toast.LENGTH_LONG).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                // Handle any errors
//                            }
//                        });
//                        Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
//                        String downloadurl = taskSnapshot.getMetadata().getReference("gs://lgbl-c68c1.appspot.com/"+username+formatteddate+formattedtime).getDownloadUrl().toString();
//                        Toast.makeText(getApplicationContext(),downloadurl, Toast.LENGTH_LONG).show();
                        final String password = "Frontyard@lgb2021";
                        final String frommail ="projectlgb7@gmail.com";
                        Properties properties = new Properties();
                        properties.put("mail.smtp.auth","true");
                        properties.put("mail.smtp.starttls.enable","true");
                        properties.put("mail.smtp.host","smtp.gmail.com");
                        properties.put("mail.smtp.port","587");
//                        Toast.makeText(getApplicationContext(),"Try "+ generatedFilePath[0], Toast.LENGTH_LONG).show();
//Checking the password
                        Session session = Session.getInstance(properties, new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(frommail,password);
                            }
                        });


                        try {
                            Message message = new MimeMessage(session);
                            message.setFrom(new InternetAddress(frommail));
                            message.setRecipient(Message.RecipientType.TO, new InternetAddress(pGlobal.tomail.trim()));
//                            message.setRecipient(Message.RecipientType.TO, new InternetAddress("pjcip1999@gmail.com"));
                            InternetAddress[] ccAddress = new InternetAddress[ccmail.size()];

                            // To get the array of ccaddresses
                            for( int j = 0; j < ccmail.size(); j++ ) {
                                ccAddress[j] = new InternetAddress(ccmail.get(j));
                            }

                            // Set cc: header field of the header.
                            for( int j = 0; j < ccAddress.length; j++) {
                                message.addRecipient(Message.RecipientType.CC, ccAddress[j]);
                            }

                            InternetAddress[] bccAddress = new InternetAddress[bccmail.size()];

                            // To get the array of bccaddresses
                            for( int j = 0; j < bccmail.size(); j++ ) {
                                bccAddress[j] = new InternetAddress(bccmail.get(j));
                            }

                            // Set bcc: header field of the header.
                            for( int j = 0; j < bccAddress.length; j++) {
                                message.addRecipient(Message.RecipientType.BCC, bccAddress[j]);
                            }
                            String url = "https://console.firebase.google.com/u/3/project/lgbl-c68c1/storage/lgbl-c68c1.appspot.com/files/~2F";
                            String msg = "Our employee "+username+" has finished his travel and the picture of the  ticket is in this url "+url+usermail+"~2F"+formatteddate+"~2F"+formattedtime;
//                            String msg = "Our employee "+username+" has finished his travel and the picture of the  ticket is in this url "+downloadurl;
                            message.setSubject("Our employee "+username+" travel reimbursement");
                            message.setText(msg);
                            new SendMail().execute(message);
                        }catch(MessagingException e){
                            e.printStackTrace();
                        }

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        float percent=(100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        dialog.setMessage("Uploaded :"+(int)percent+" %");
                    }
                });
    }

    private class SendMail extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(publictransport.this,"Please wait","Ending the journey",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            }catch (MessagingException e)
            {
                e.printStackTrace();
                return "Error";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Success")){
                AlertDialog.Builder builder = new AlertDialog.Builder(publictransport.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color ='#509324'>Success</font>"));
                builder.setMessage("Journey is ended");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }else{
                Toast.makeText(getApplicationContext(),"Something went wrong?", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected void onStart()
    {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }
    protected void onStop()
    {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}
