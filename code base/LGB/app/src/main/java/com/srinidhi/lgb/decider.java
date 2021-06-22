package com.srinidhi.lgb;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.srinidhi.lgb.utility.NetworkChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

class decide
{
//    public static int cc;
    public static String phone,model,username,mail,vehicle,cc;
}
public class decider extends AppCompatActivity {
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageButton ownt,publict;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decider);
        ownt = findViewById(R.id.ownt);
        publict=findViewById(R.id.publict);
        ownt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openownvehicleactivity();
            }
        });
        publict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openpublicvehicleactivity();
            }
        });
        Intent intent = getIntent();
        decide.username = intent.getStringExtra("username");
        decide.mail = intent.getStringExtra("mailid");
        decide.model = intent.getStringExtra("model");
        decide.cc = intent.getStringExtra("cc");
        decide.vehicle = intent.getStringExtra("vehicle");
        decide.phone = intent.getStringExtra("phone");
        Toast.makeText(decider.this, "Choose your type of journey "+decide.username, Toast.LENGTH_SHORT).show();

    }
    public void openownvehicleactivity()
    {

                    Intent intent = new Intent(getApplicationContext(), owntransport.class);
                    intent.putExtra("usernamen",decide.username);
                    intent.putExtra("mailidn",decide.mail);
                    intent.putExtra("modeln",decide.model);
                    intent.putExtra("ccn",decide.cc);
                    intent.putExtra("vehiclen",decide.vehicle);
                    intent.putExtra("phonen",decide.phone);
                    startActivity(intent);


    }
    public void openpublicvehicleactivity()
    {
        Intent intent = new Intent(this, publictransport.class);
        intent.putExtra("username",decide.username);
        intent.putExtra("mailid",decide.mail);
        intent.putExtra("phone",decide.phone);
        startActivity(intent);
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