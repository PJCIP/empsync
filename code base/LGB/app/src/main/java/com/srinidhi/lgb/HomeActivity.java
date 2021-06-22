package com.srinidhi.lgb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.srinidhi.lgb.Prevalent.Prevalent;
import com.srinidhi.lgb.utility.NetworkChangeListener;

import java.util.Collections;

import io.paperdb.Paper;

class home
{
//    public static int cc;
    public static String phone,model,username,mail,vehicle,cc;
}
public class HomeActivity extends AppCompatActivity {
    CardView mytravel;
    CardView myprofile;
    CardView calender;
    CardView mywork;
    ImageView Logoutbtn,Settingbtn;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private LocationRequest locationRequest;
    public static final int REQUEST_CHECK_SETTING = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(this);
        mytravel=findViewById(R.id.mytravel);
        myprofile=findViewById(R.id.myprofile);
        calender=findViewById(R.id.calender);
        mywork=findViewById(R.id.mywork);
        Logoutbtn=findViewById(R.id.lgout_logo);
        Settingbtn=findViewById(R.id.Settings);
        Intent intent = getIntent();
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        home.phone = intent.getStringExtra("phone");

        if (TextUtils.isEmpty(home.phone))
        {
            final String globalphone = Paper.book().read(Prevalent.UserPhoneKey);
//            Toast.makeText(this, "Please write your phone number..."+globalphone, Toast.LENGTH_SHORT).show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
            Query checkuser = reference.orderByChild("phone").equalTo(globalphone);
            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if (snapshot.child(globalphone).hasChild("VehicleModalName")) {
                            //run some code
                            home.model = snapshot.child(globalphone).child("VehicleModalName").getValue(String.class);
                            home.cc = home.model;
                            home.vehicle = snapshot.child(globalphone).child("vehiclename").getValue(String.class);
                            home.mail = snapshot.child(globalphone).child("email").getValue(String.class);
                            home.username = snapshot.child(globalphone).child("name").getValue(String.class);
                            String phone = Paper.book().read(Prevalent.UserPhoneKey);
                            Toast.makeText(HomeActivity.this, "Welcome "+home.username, Toast.LENGTH_LONG).show();
//                            Toast.makeText(HomeActivity.this, phone, Toast.LENGTH_LONG).show();
                        }else
                        {
//                            Toast.makeText(HomeActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setCancelable(false);
                            builder.setTitle(Html.fromHtml("<font color ='#509324'>Attention Please</font>"));
                            builder.setMessage("Before proceeding further please fill the asset details under my profile");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
            Query checkuser = reference.orderByChild("phone").equalTo(home.phone);
            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if (snapshot.child(home.phone).hasChild("VehicleModalName")) {
                            //run some code
                            home.model = snapshot.child(home.phone).child("VehicleModalName").getValue(String.class);
                            home.cc = snapshot.child(home.phone).child("VehicleCC").getValue(String.class);
                            home.vehicle = snapshot.child(home.phone).child("vehiclename").getValue(String.class);
                            home.mail = snapshot.child(home.phone).child("email").getValue(String.class);
                            home.username = snapshot.child(home.phone).child("name").getValue(String.class);
                            String phone = Paper.book().read(Prevalent.UserPhoneKey);
                            Toast.makeText(HomeActivity.this, "Welcome "+home.username, Toast.LENGTH_LONG).show();
//                            Toast.makeText(HomeActivity.this, phone, Toast.LENGTH_LONG).show();
                        }else
                        {
//                            Toast.makeText(HomeActivity.this, "Asset d", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setCancelable(false);
                            builder.setTitle(Html.fromHtml("<font color ='#509324'>Attention Please</font>"));
                            builder.setMessage("Before proceeding further please fill the asset details under my profile");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }

//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.putExtra("username",username);
//                    intent.putExtra("mailid",mail);
//                    intent.putExtra("model",model);
//                    intent.putExtra("cc",cc);
//                    intent.putExtra("vehicle",vehicle);
//                    startActivity(intent);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }




        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                String phone = Paper.book().read(Prevalent.UserPhoneKey);
//                Toast.makeText(HomeActivity.this, phone, Toast.LENGTH_LONG).show();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));

            }
        });

        Settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HomeActivity.this,SettingsActivity.class);
                intent1.putExtra("phone",home.phone);
                Toast.makeText(HomeActivity.this,"Mobile Check no. is "+home.phone,Toast.LENGTH_SHORT).show();
                startActivity(intent1);


            }
        });

        mytravel.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeActivity.this,decider.class);
//            Toast.makeText(HomeActivity.this, "Sending"+home.username, Toast.LENGTH_LONG).show();
            intent.putExtra("username",home.username);
            intent.putExtra("mailid",home.mail);
            intent.putExtra("model",home.model);
            intent.putExtra("cc",home.cc);
            intent.putExtra("vehicle",home.vehicle);
            intent.putExtra("phone",home.phone);

            startActivity(intent);

        }
    });


        myprofile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,myprofileActivity.class));

            }
        });


        calender.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
//                Toast.makeText(HomeActivity.this, "under development", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeActivity.this,CalendarMainActivity.class);
                intent.putExtra("phone",home.phone);
                intent.putExtra("username",home.username);
                startActivity(intent);

            }
        });

        mywork.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,attendancemain.class);
                    intent.putExtra("username",home.username);
                startActivity(intent);

            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        locationRequest = LocationRequest.create();
                        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
                        locationRequest.setInterval(5000);
                        locationRequest.setFastestInterval(1000);
                        LocationSettingsRequest.Builder lbuilder = new LocationSettingsRequest.Builder().addAllLocationRequests(Collections.singleton(locationRequest));
                        lbuilder.setAlwaysShow(true);
                        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(HomeActivity.this).checkLocationSettings(lbuilder.build());
                        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                                try {
                                    LocationSettingsResponse response = task.getResult(ApiException.class);
                                    Toast.makeText(HomeActivity.this, "Gps is on" ,Toast.LENGTH_LONG).show();
                                } catch (final ApiException e) {
                                    e.printStackTrace();
                                    Toast.makeText(HomeActivity.this, "GPS is off check whether gps is on" ,Toast.LENGTH_LONG).show();
//
                                    switch (e.getStatusCode()){
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                            try {
                                                resolvableApiException.startResolutionForResult(HomeActivity.this,REQUEST_CHECK_SETTING);
                                            } catch (IntentSender.SendIntentException ex) {
                                                ex.printStackTrace();
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                            break;
                                    }
                                }
                            }
                        });


                        Toast.makeText(HomeActivity.this, "Intitiated gps... This might take some time", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
