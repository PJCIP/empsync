package com.srinidhi.lgb;

import android.Manifest;
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
import android.os.Bundle;
import android.text.Html;
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
import com.srinidhi.lgb.Attendance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.srinidhi.lgb.HomeActivity.REQUEST_CHECK_SETTING;

class aGlobal {
    //    public static int costpercc;
//    public static double totalcost;
    public static String username;

}
public class attendancemain extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    private LocationRequest locationRequest;
    Button Login, Logout,Reset;
    TextView logintime, logouttime,aid;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addresses;
    DatabaseReference reff, read_reff, read_user;
    Attendance attendance;
    int attendance_id;
    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
    String formattedDate = df.format(c);
    String strDateFormat = "hh:mm:ss a";
    SimpleDateFormat time = new SimpleDateFormat(strDateFormat, Locale.getDefault());
    String formattedtime = time.format(c);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Login = findViewById(R.id.login);
        Logout = findViewById(R.id.logout);
        Logout = findViewById(R.id.logout);
        Logout.setEnabled(false);
        Reset = findViewById(R.id.reset);
        aid = findViewById(R.id.attendance_id);
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        logintime = findViewById(R.id.logintime);
        logouttime = findViewById(R.id.logoutime);

        attendance = new Attendance();
        attendance_id = Prefconfig.loadattendanceid(this);
//        aid.setText(Html.fromHtml("<font color='#6200EE'><b>Attendance ID:</b><br></font>" + attendance_id));
        Intent intent = getIntent();
        aGlobal.username = intent.getStringExtra("username");
        Toast.makeText(attendancemain.this, "Hi "+aGlobal.username, Toast.LENGTH_LONG).show();
        reff = FirebaseDatabase.getInstance().getReference().child("Attendance").child(aGlobal.username).child(formattedDate);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout.setEnabled(true);
                Login.setEnabled(true);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(attendancemain.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }
                    else {
                        AlertDialog dialog = new AlertDialog.Builder(attendancemain.this)
                                .setTitle("Login")
                                .setMessage("Have you entered the office?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getstartLocation();
                                        Toast.makeText(attendancemain.this, "Have a good day sir", Toast.LENGTH_LONG).show();

                                    }
                                })
                                .setNegativeButton("no", null)
                                .show();

                    }

                } else {
                    ActivityCompat.requestPermissions(attendancemain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

                }
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(attendancemain.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }
                    else {
                        AlertDialog dialog = new AlertDialog.Builder(attendancemain.this)
                                .setTitle("Logout")
                                .setMessage("Are you leaving the office?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getstopLocation();
                                        Toast.makeText(attendancemain.this, "good bye see you tomorrow", Toast.LENGTH_LONG).show();

                                    }
                                })
                                .setNegativeButton("no", null)
                                .show();
                    }

                } else {
                    ActivityCompat.requestPermissions(attendancemain.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

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
                        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(attendancemain.this).checkLocationSettings(lbuilder.build());
                        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                                try {
                                    LocationSettingsResponse response = task.getResult(ApiException.class);
                                    Toast.makeText(attendancemain.this, "Gps is on" ,Toast.LENGTH_LONG).show();
                                } catch (final ApiException e) {
                                    e.printStackTrace();
                                    Toast.makeText(attendancemain.this, "GPS is off check whether gps is on" ,Toast.LENGTH_LONG).show();
//
                                    switch (e.getStatusCode()){
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                            try {
                                                resolvableApiException.startResolutionForResult(attendancemain.this,REQUEST_CHECK_SETTING);
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


//                        Toast.makeText(HomeActivity.this, "Intitiated gps... This might take some time", Toast.LENGTH_LONG).show();

                        Toast.makeText(attendancemain.this, "Intitiated gps... This might take some time", Toast.LENGTH_LONG).show();
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
                        Geocoder geocoder = new Geocoder(attendancemain.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Long pincode = Long.parseLong(addresses.get(0).getPostalCode().toString().trim());
                        String status = "Logged in";
                        Date c = Calendar.getInstance().getTime();
                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);
                        String strDateFormat = "hh:mm:ss a";
                        SimpleDateFormat time = new SimpleDateFormat(strDateFormat, Locale.getDefault());
                        String loginformattedtime = time.format(c);
                        attendance.setLoginTime(formattedtime);
                        attendance.setLogin_address(addresses.get(0).getAddressLine(0));
                        attendance.setLogin_cont(addresses.get(0).getCountryName());
                        attendance.setLogin_lat(addresses.get(0).getLatitude());
                        attendance.setLogin_lon(addresses.get(0).getLongitude());
                        attendance.setLogin_subloc(addresses.get(0).getSubLocality());
                        attendance.setLogin_loc(addresses.get(0).getLocality());
                        attendance.setLoginPincode(pincode);
                        attendance.setAttendance_status(status);
                        attendance.setLogout_address("");
                        attendance.setLogout_cont("");
                        attendance.setLogout_lat(0);
                        attendance.setLogout_lon(0);
                        attendance.setLogout_subloc("");
                        attendance.setLogout_loc("");
                        attendance.setLogoutPincode(0);
                        logintime.setText(Html.fromHtml("<font color='#6200EE'><b>Login Time:</b><br></font>"+loginformattedtime));
                        reff.child(String.valueOf(attendance_id)).setValue(attendance);
                        Toast.makeText(attendancemain.this, "Logged in for the day", Toast.LENGTH_LONG).show();
                        Logout.setEnabled(true);
                        Login.setEnabled(false);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(attendancemain.this);
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
                        Geocoder geocoder = new Geocoder(attendancemain.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Long pincode = Long.parseLong(addresses.get(0).getPostalCode().toString().trim());
                        String status = "Loggedout";
                        read_reff = FirebaseDatabase.getInstance().getReference().child("Attendance").child(aGlobal.username).child(formattedDate).child(String.valueOf(attendance_id));
                        read_reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String address = snapshot.child("login_address").getValue().toString();
                                Long pin = (Long) snapshot.child("loginPincode").getValue();
                                if (address == null) {
                                    Toast.makeText(attendancemain.this, "No journey is started to end", Toast.LENGTH_LONG).show();
                                } else {
                                    Double lat1 = (Double) snapshot.child("login_lat").getValue();
                                    Double lon1 = (Double) snapshot.child("login_lon").getValue();
                                    Double lat2 = addresses.get(0).getLatitude();
                                    Double lon2 = addresses.get(0).getLongitude();
                                    distance(lat1, lon1, lat2, lon2);

                                }

                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        reff.child(String.valueOf(attendance_id)).child("logout_address").setValue(addresses.get(0).getAddressLine(0));
                        reff.child(String.valueOf(attendance_id)).child("logout_cont").setValue(addresses.get(0).getCountryName());
                        reff.child(String.valueOf(attendance_id)).child("logout_lat").setValue(addresses.get(0).getLatitude());
                        reff.child(String.valueOf(attendance_id)).child("logout_lon").setValue(addresses.get(0).getLongitude());
                        reff.child(String.valueOf(attendance_id)).child("logout_subloc").setValue(addresses.get(0).getSubLocality());
                        reff.child(String.valueOf(attendance_id)).child("logout_loc").setValue(addresses.get(0).getLocality());
                        reff.child(String.valueOf(attendance_id)).child("logoutPincode").setValue(pincode);

                        reff.child(String.valueOf(attendance_id)).child("attendance_status").setValue("Logged out");

                        Prefconfig.saveattendanceid(getApplicationContext(), attendance_id);
                        Date logoutc = Calendar.getInstance().getTime();
                        SimpleDateFormat logoutdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        String logoutformattedDate = logoutdf.format(logoutc);
                        String logoutstrDateFormat = "hh:mm:ss a";
                        SimpleDateFormat outtime = new SimpleDateFormat(logoutstrDateFormat, Locale.getDefault());
                        String logoutformattedtime = outtime.format(logoutc);
                        attendance.setLogoutTime(formattedtime);
                        logouttime.setText(Html.fromHtml("<font color='#6200EE'><b>Logout Time:</b><br></font>" + logoutformattedtime));
                        reff.child(String.valueOf(attendance_id)).child("logoutTime").setValue(formattedtime);
                        Toast.makeText(attendancemain.this, "Complete Data inserted", Toast.LENGTH_LONG).show();
                        attendance_id++;
                        Login.setEnabled(true);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(attendancemain.this);
                    builder.setCancelable(false);
                    builder.setTitle(Html.fromHtml("<font color ='#509324'>Please wait</font>"));
                    builder.setMessage("1. GPS has not yet fetched the location.This might take about 10 minutes.");
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


    private void distance(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double longDiff = lon1 - lon2;
        Double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = (distance * 1.609344) * 100;
        String dis = distance + "KM";
//        Global.totalcost = distance * Global.costpercc;
//        reff.child(String.valueOf(attendance_id - 1)).child("Total cost of attendance(Rs)").setValue(Global.totalcost);
        reff.child(String.valueOf(attendance_id - 1)).child("Distance(KM)").setValue(dis);

    }

    private double retdistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        Double longDiff = lon1 - lon2;
        Double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(longDiff));
        distance = Math.acos(distance);
        distance = rad2deg(distance);
        distance = (distance * 1.609344) * 100;
        String dis = distance + "KM";
//        textView7.setText(Html.fromHtml("<font color='#6200EE'><b>Distance:</b><br></font>" + dis));
        reff.child(String.valueOf(attendance_id - 1)).child("Distance(KM)").setValue(dis);
        return distance;
    }

    private Double rad2deg(Double distance) {
        return (distance * 180.0 / Math.PI);
    }

    private double deg2rad(Double lat1) {
        return (lat1 * Math.PI / 180.0);
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


