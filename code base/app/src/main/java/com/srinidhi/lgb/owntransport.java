package com.srinidhi.lgb;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.srinidhi.lgb.Prevalent.Prevalent;
import com.srinidhi.lgb.utility.NetworkChangeListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
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

import io.paperdb.Paper;

import static com.srinidhi.lgb.HomeActivity.REQUEST_CHECK_SETTING;

class Global {
    public static float costpercc;
    public static double totalcost,distance;
    public static String frommail,tomail,model_name,cc_name,vehicle_name,username,frompassword,fromplace,toplace;
    public static boolean state;

}

public class owntransport extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
//    private LocationRequest locationRequest;
    Button Start, Stop,Reset;
    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView, model, cc,vehicle;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addresses;
    DatabaseReference reff, read_reff, read_user;
    Travel travel;
    int journey_id;

    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    String formattedDate = df.format(c);
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own);
        Start = findViewById(R.id.start);
        Stop = findViewById(R.id.stop);
        Reset = findViewById(R.id.reset);
        Stop.setEnabled(false);
//Sender mail id and password
        Global.frommail="projectlgb7@gmail.com";
        Global.frompassword="Frontyard@lgb2021";
        Global.state = true;
//        Places.initialize(getApplicationContext(),"AIzaSyDoWxZkx6WMF4PUV-pkU-8mqgeYnKSXJPQ");
        textView1 = findViewById(R.id.text_view1);
        textView = findViewById(R.id.text_view);
        textView2 = findViewById(R.id.text_view2);
        textView3 = findViewById(R.id.text_view3);
        textView4 = findViewById(R.id.text_view4);
        textView5 = findViewById(R.id.text_view5);
        textView6 = findViewById(R.id.text_view6);
        textView7 = findViewById(R.id.text_view7);
        vehicle = findViewById(R.id.vehicle);
        model = findViewById(R.id.model);
        cc = findViewById(R.id.cc);
        Intent intent = getIntent();
        Global.model_name = intent.getStringExtra("modeln");
        Global.vehicle_name = intent.getStringExtra("vehiclen");
        Global.cc_name = intent.getStringExtra("ccn");
        Global.username = intent.getStringExtra("usernamen");
        Global.frommail = intent.getStringExtra("mailidn");
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        if (TextUtils.isEmpty(home.phone))
        {
            final String globalphone = Paper.book().read(Prevalent.UserPhoneKey);
//            Toast.makeText(this, "Please write your phone number..."+globalphone, Toast.LENGTH_SHORT).show();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
            Query checkuser = reference.orderByChild("phone").equalTo(globalphone);
            Toast.makeText(owntransport.this, "Welcome "+Global.username, Toast.LENGTH_LONG).show();
            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if (snapshot.child(globalphone).hasChild("VehicleModalName")) {
                            //run some code
                            Global.model_name = snapshot.child(globalphone).child("VehicleModalName").getValue(String.class);
                            Global.cc_name = Global.model_name;
                            Global.vehicle_name = snapshot.child(globalphone).child("vehiclename").getValue(String.class);
                            Global.frommail = snapshot.child(globalphone).child("email").getValue(String.class);
                            Global.username = snapshot.child(globalphone).child("name").getValue(String.class);
                            String phone = Paper.book().read(Prevalent.UserPhoneKey);
                            Toast.makeText(owntransport.this, "Welcome "+Global.username, Toast.LENGTH_LONG).show();
//                            Toast.makeText(HomeActivity.this, phone, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
//                            Toast.makeText(owntransport.this, "Failed", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(owntransport.this);
                            builder.setCancelable(false);
                            builder.setTitle(Html.fromHtml("<font color ='#509324'>Attention please</font>"));
                            builder.setMessage("Before proceeding further please fill the asset details under my profile");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }

                    }else{
//                        Toast.makeText(owntransport.this, "Failed", Toast.LENGTH_LONG).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(owntransport.this);
                        builder.setCancelable(false);
                        builder.setTitle(Html.fromHtml("<font color ='#509324'>Error</font>"));
                        builder.setMessage("NO snapshot exists");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        vehicle.setText(Html.fromHtml("<font color='#6200EE'><b>You had opted for Your own vehicle - </b><br></font>" + Global.vehicle_name));
//        model.setText(Html.fromHtml("<font color='#6200EE'><b>Model/cc:</b><br></font>" + Global.model_name));
//        cc.setText(Html.fromHtml("<font color='#6200EE'><b>CC:</b><br></font>" + Global.cc_name));
//        Toast.makeText(owntransport.this, "Model/cc:"+Global.model_name, Toast.LENGTH_LONG).show();
//        Toast.makeText(owntransport.this, "cc:"+Global.cc_name, Toast.LENGTH_LONG).show();
        // Fetching the client mail id
        DatabaseReference mailfetch = FirebaseDatabase.getInstance().getReference().child("admin").child("mail");
        mailfetch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Global.tomail = snapshot.getValue(String.class);
//                    Toast.makeText(owntransport.this, "Admin mail id is "+ Global.tomail+"my mail id is"+Global.frommail, Toast.LENGTH_LONG).show();
                    Toast.makeText(owntransport.this, "Fetched mailid", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(owntransport.this, "Unable to fetch mailid", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Fetching the cost as per the cc:
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("admin").child("costs").child(Global.vehicle_name);
//        Query checkuser = reference.orderByKey().equalTo(Global.cc_name);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    Global.costpercc = snapshot.child(Global.model_name).getValue(Float.class);
//                    Toast.makeText(owntransport.this, "Cost for "+ Global.model_name+"is "+ Global.costpercc, Toast.LENGTH_LONG).show();

                }
                else{
//                    Toast.makeText(MainActivity.this, "Unable to fetch cpcc. Please report to the IT support", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        travel = new Travel();
        journey_id = Prefconfig.loadjourneyid(this);
        textView.setText(Html.fromHtml("<font color='#6200EE'><b>Journey ID:</b><br></font>" + journey_id));
        reff = FirebaseDatabase.getInstance().getReference().child("Travel").child(Global.username).child(formattedDate);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop.setEnabled(true);
                Start.setEnabled(true);
            }
        });
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(owntransport.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }
                    else {
                        getstartLocation();
                    }
                } else {
                    ActivityCompat.requestPermissions(owntransport.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }
            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(owntransport.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }
                    else {
                        if (Global.state == true) {
                            Start.setEnabled(true);
                            Stop.setEnabled(false);
                            AlertDialog dialog = new AlertDialog.Builder(owntransport.this)
                                    .setTitle("Journey")
                                    .setMessage("Do you really want to stop the journey?")
                                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            getstopLocation();

                                            final String password = "Frontyard@lgb2021";
                                            final String frommail = "projectlgb7@gmail.com";
                                            Properties properties = new Properties();
                                            properties.put("mail.smtp.auth", "true");
                                            properties.put("mail.smtp.starttls.enable", "true");
                                            properties.put("mail.smtp.host", "smtp.gmail.com");
                                            properties.put("mail.smtp.port", "587");
                                            //Checking the password
                                            Session session = Session.getInstance(properties, new Authenticator() {
                                                @Override
                                                protected PasswordAuthentication getPasswordAuthentication() {
                                                    return new PasswordAuthentication(frommail, password);
                                                }
                                            });

                                            try {
                                                Message message = new MimeMessage(session);
                                                message.setFrom(new InternetAddress(frommail));
                                                message.setRecipient(Message.RecipientType.TO, new InternetAddress(Global.tomail.trim()));
                                                //                                        message.setRecipient(Message.RecipientType.TO, new InternetAddress("pjcip1999@gmail.com"));
                                                String msg = "Our employee " + Global.username + " has finished his travel from " + Global.fromplace + " to " + Global.toplace + ". The allowance for using "+Global.vehicle_name +"-"+Global.model_name+" is Rs. "+Global.costpercc+" /km.The total cost involved for travelling a distance of " + Global.distance + "KM is Rs." + Global.totalcost + ".";
                                                message.setSubject("Our employee " + Global.username + " travel reimbursement");
                                                message.setText(msg);
                                                new SendMail().execute(message);
                                            } catch (MessagingException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    })
                                    .setNegativeButton("no", null)
                                    .show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(owntransport.this);
                            builder.setCancelable(false);
                            builder.setTitle(Html.fromHtml("<font color ='#509324'>Please wait</font>"));
                            builder.setMessage("GPS has not yet fetched the location. This might take about 10 minutes");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }

                } else {
                    ActivityCompat.requestPermissions(owntransport.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }
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
                        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(owntransport.this).checkLocationSettings(lbuilder.build());
                        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                                try {
                                    LocationSettingsResponse response = task.getResult(ApiException.class);
                                    Toast.makeText(owntransport.this, "Gps is on" ,Toast.LENGTH_LONG).show();
                                } catch (final ApiException e) {
                                    e.printStackTrace();
                                    Toast.makeText(owntransport.this, "GPS is off check whether gps is on" ,Toast.LENGTH_LONG).show();
//
                                    switch (e.getStatusCode()){
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                            try {
                                                resolvableApiException.startResolutionForResult(owntransport.this,REQUEST_CHECK_SETTING);
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


                        Toast.makeText(owntransport.this, "Intitiated gps... This might take some time", Toast.LENGTH_LONG).show();
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

    private void getstartLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(owntransport.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Global.fromplace = addresses.get(0).getAddressLine(0);
                        textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Current Latitude:</b><br></font>" + addresses.get(0).getLatitude()));
                        textView2.setText(Html.fromHtml("<font color='#6200EE'><b>Current Longitude:</b><br></font>" + addresses.get(0).getLongitude()));
                        textView3.setText(Html.fromHtml("<font color='#6200EE'><b>Current Locality:</b><br></font>" + addresses.get(0).getLocality()));
                        textView5.setText(Html.fromHtml("<font color='#6200EE'><b>Current Address:</b><br></font>" + addresses.get(0).getAddressLine(0)));
                        textView6.setText(Html.fromHtml("<font color='#6200EE'><b>Current Pincode:</b><br></font>" + addresses.get(0).getPostalCode()));
                        textView4.setText(Html.fromHtml("<font color='#6200EE'><b>Current sub Locality:</b><br></font>" + addresses.get(0).getSubLocality()));
                        Long pincode = Long.parseLong(addresses.get(0).getPostalCode().toString().trim());
                        String status = "Started";
                        travel.setStart_address(addresses.get(0).getAddressLine(0));
                        travel.setStart_cont(addresses.get(0).getCountryName());
                        travel.setStart_lat(addresses.get(0).getLatitude());
                        travel.setStart_lon(addresses.get(0).getLongitude());
                        travel.setStart_subloc(addresses.get(0).getSubLocality());
                        travel.setStart_loc(addresses.get(0).getLocality());
                        travel.setStartPincode(pincode);
                        travel.setJourney_status(status);
                        travel.setEnd_address("");
                        travel.setEnd_cont("");
                        travel.setEnd_lat(0);
                        travel.setEnd_lon(0);
                        travel.setEnd_subloc("");
                        travel.setEnd_loc("");
                        travel.setEndPincode(0);
//                textView7.setText(Html.fromHtml("<font color='#6200EE'><b>Country Name:</b><br></font>"+travel.getStart_address()));
                        reff.child(String.valueOf(journey_id)).setValue(travel);
                        Toast.makeText(owntransport.this, "Start values setted", Toast.LENGTH_LONG).show();
                        Stop.setEnabled(true);
                        Start.setEnabled(false);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Current Latitude:</b><br></font>" + location.getLatitude()));
                    textView2.setText(Html.fromHtml("<font color='#6200EE'><b>Current Longitude:</b><br></font>" + location.getLongitude()));
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(owntransport.this);
                    builder.setCancelable(false);
                    builder.setTitle(Html.fromHtml("<font color ='#509324'>Please wait</font>"));
                    builder.setMessage("1. GPS has not yet fetched the location. This might take about 10 minutes");
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();

                }




            }
        });
    }

    private void getstopLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(owntransport.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        textView4.setText(Html.fromHtml("<font color='#6200EE'><b>Start_address:</b><br></font>" + addresses.get(0).getPostalCode()));
                        Long pincode = Long.parseLong(addresses.get(0).getPostalCode().toString().trim());
                        String status = "Completed";
                        read_reff = FirebaseDatabase.getInstance().getReference().child("Travel").child(Global.username).child(formattedDate).child(String.valueOf(journey_id));
                        read_reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String address = snapshot.child("start_address").getValue().toString();
                                Long pin = (Long) snapshot.child("startPincode").getValue();
                                if (address == null) {
                                    Toast.makeText(owntransport.this, "No journey is started to end", Toast.LENGTH_LONG).show();
                                } else {
                                    Double lat1 = (Double) snapshot.child("start_lat").getValue();
                                    Double lon1 = (Double) snapshot.child("start_lon").getValue();
                                    Double lat2 = addresses.get(0).getLatitude();
                                    Double lon2 = addresses.get(0).getLongitude();
                                    distance(lat1, lon1, lat2, lon2);

                                    textView1.setText(Html.fromHtml("<font color='#6200EE'><b>Current Latitude:</b><br></font>" + addresses.get(0).getLatitude()));
                                    textView2.setText(Html.fromHtml("<font color='#6200EE'><b>Current Longitude:</b><br></font>" + addresses.get(0).getLongitude()));
                                    textView3.setText(Html.fromHtml("<font color='#6200EE'><b>Start pincode:</b><br></font>" + pin));
                                    textView4.setText(Html.fromHtml("<font color='#6200EE'><b>End pincode:</b><br></font>" + addresses.get(0).getPostalCode()));
                                    textView5.setText(Html.fromHtml("<font color='#6200EE'><b>Start Address:</b><br></font>" + address));
                                    textView6.setText(Html.fromHtml("<font color='#6200EE'><b>End Address:</b><br></font>" + addresses.get(0).getAddressLine(0)));
                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        reff.child(String.valueOf(journey_id)).child("end_address").setValue(addresses.get(0).getAddressLine(0));
                        reff.child(String.valueOf(journey_id)).child("end_cont").setValue(addresses.get(0).getCountryName());
                        Global.toplace = addresses.get(0).getAddressLine(0);
                        reff.child(String.valueOf(journey_id)).child("end_lat").setValue(addresses.get(0).getLatitude());
                        reff.child(String.valueOf(journey_id)).child("end_lon").setValue(addresses.get(0).getLongitude());
                        reff.child(String.valueOf(journey_id)).child("end_subloc").setValue(addresses.get(0).getSubLocality());
                        reff.child(String.valueOf(journey_id)).child("end_loc").setValue(addresses.get(0).getLocality());
                        reff.child(String.valueOf(journey_id)).child("endPincode").setValue(pincode);
                        reff.child(String.valueOf(journey_id)).child("journey_status").setValue("Completed");
                        journey_id++;
                        Prefconfig.savejourneyid(getApplicationContext(), journey_id);
                        Toast.makeText(owntransport.this, "Journey is ended", Toast.LENGTH_LONG).show();
                        Global.state = true;

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }
                else
                {
                    Global.state = false;

                }



            }
        });
    }


    private void distance(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double longDiff = lon1 - lon2;
        Double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = (distance * 1.609344) * 100;
        Global.distance = distance;
        String dis = distance + "KM";
        textView7.setText(Html.fromHtml("<font color='#6200EE'><b>Distance:</b><br></font>" + dis));
        Global.totalcost = distance * Global.costpercc;
        reff.child(String.valueOf(journey_id - 1)).child("Total cost of travel(Rs)").setValue(Global.totalcost);
        reff.child(String.valueOf(journey_id - 1)).child("Distance(KM)").setValue(dis);

        //Note: Just done to find the better accuracy the one with less need to be terminated
        // Checking with another distance formula
        double rlat1 = Math.PI*lat1/180;
        double rlat2 = Math.PI*lat2/180;
        double theta = lon1 - lon2;
        double rtheta = Math.PI*theta/180;
        double dist =
                Math.sin(rlat1)* Math.sin(rlat2) + Math.cos(rlat1)*
                        Math.cos(rlat2)* Math.cos(rtheta);
        dist = Math.acos(dist);
        dist = dist*180/ Math.PI;
        dist = dist*60*1.1515;
        double distinkm = dist*1.609344;
        reff.child(String.valueOf(journey_id - 1)).child("Distance(KM)2").setValue(dist);

    }

    private double retdistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double longDiff = lon1 - lon2;
        Double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = (distance * 1.609344) * 100;
        String dis = distance + "KM";
//        textView7.setText(Html.fromHtml("<font color='#6200EE'><b>Distance:</b><br></font>" + dis));
//        reff.child(String.valueOf(journey_id - 1)).child("Distance(KM)").setValue(dis);
        return distance;
    }

    private Double rad2deg(Double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(Double lat1) {
        return (lat1 * Math.PI / 180.0);
    }

    private class SendMail extends AsyncTask<Message, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(owntransport.this,"Please wait","Ending the journey",true,false);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(owntransport.this);
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