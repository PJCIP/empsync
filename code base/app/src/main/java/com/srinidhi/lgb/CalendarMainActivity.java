package com.srinidhi.lgb;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.srinidhi.lgb.Prevalent.Prevalent;
import com.srinidhi.lgb.utility.NetworkChangeListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CalendarMainActivity extends AppCompatActivity {
    TextView el_text,
            cl_text,el_title,cl_title;
    ListView cl_his_view,el_his_view;
    Button btn_minimal,
            btn_maximal,
            cari;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ArrayList<dataUser> list = new ArrayList<>();
    AdapterItem adapterItem;
    RecyclerView recyclerView;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton fab_add;
    AlertDialog builderAlert;
    Context context;
    LayoutInflater layoutInflater;
    View showInput;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY",Locale.ENGLISH);
    long diff;
    int el,cl;
    String cat,phone,username;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity3_main);

        context = this;
        fab_add = findViewById(R.id.fab_add);
        el_text = findViewById(R.id.EL);
        cl_text = findViewById(R.id.CL);
        el_title = findViewById(R.id.EL_hist);
        cl_title = findViewById(R.id.CL_hist);
        cl_his_view = findViewById(R.id.CL_his);
        el_his_view = findViewById(R.id.EL_his);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        username = intent.getStringExtra("username");
        if(TextUtils.isEmpty(phone))
        {
            phone = Paper.book().read(Prevalent.UserPhoneKey);
            Toast.makeText(context, "Phone no. is not passed from intent", Toast.LENGTH_SHORT).show();
        }
        else
        {
            final String globalphone = Paper.book().read(Prevalent.UserPhoneKey);
            Toast.makeText(context, "Hi "+username, Toast.LENGTH_SHORT).show();
        }
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        showData();
    }

    EditText et_num, date,
            text,enddate;
    Button btnDateDaftar,
            simpleData,btnenddate;
    RadioGroup rb_group;
    RadioButton radioButton;
    Date Start_date,end_date;

    private void inputData() {
        builderAlert = new AlertDialog.Builder(context).create();
        layoutInflater = getLayoutInflater();
        showInput = layoutInflater.inflate(R.layout.input_layout, null);
        builderAlert.setView(showInput);

        et_num = showInput.findViewById(R.id.et_name);
        date = showInput.findViewById(R.id.Start_date);
        text = showInput.findViewById(R.id.et_reason);
        btnDateDaftar = showInput.findViewById(R.id.btnDateDaftar);
        simpleData = showInput.findViewById(R.id.simpanData);
        rb_group = showInput.findViewById(R.id.rb_group);
        enddate = showInput.findViewById(R.id.renddate);
        btnenddate = showInput.findViewById(R.id.btnendddate);
        builderAlert.show();

        simpleData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = et_num.getText().toString();
                String reason = text.getText().toString();
                String Start = date.getText().toString();
                String end = enddate.getText().toString();
                String key = Start +"<--->"+end;
                Boolean status = true;
                if (subject.isEmpty()) {
                    et_num.setError("Data cannot be empty");
                    et_num.requestFocus();
                } else if (reason.isEmpty()) {
                    text.setError("Data cannot be empty");
                    text.requestFocus();
                } else if (Start.isEmpty()) {
                    date.setError("Data cannot be empty");
                    date.requestFocus();
                } else if (end.isEmpty()) {
                    enddate.setError("Data cannot be empty");
                    enddate.requestFocus();
                } else {
                    int selected = rb_group.getCheckedRadioButtonId();
                    radioButton = showInput.findViewById(selected);

                    if(el == 0 && cl == 0){
                        Toast.makeText(context, "You have exhausted you vacations", Toast.LENGTH_SHORT).show();
                        status = false;
                        builderAlert.dismiss();
                    }

                        //                    SimpleDateFormat dateFormat;
                    try {
                        diff = (end_date.getTime() - Start_date.getTime())/86400000;
                        if (diff == 0)
                        {
                            diff = 1;
                        }
                        Toast.makeText(context, "Total no. of holidays taken is "+Math.abs(diff), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "Unable to calculate the time differnce between dates", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    cat = radioButton.getText().toString();
                    if (el == 0 && cat.equals("EL")) {
                        Toast.makeText(context, "You have exhausted you EL vacations. You shall try CL", Toast.LENGTH_SHORT).show();
                        status = false;
                        builderAlert.dismiss();
                    }
                    if (cl == 0 && cat.equals("CL")) {
                        Toast.makeText(context, "You have exhausted you CL vacations. You shall try VL", Toast.LENGTH_SHORT).show();
                        status = false;
                        builderAlert.dismiss();
                    }
                    Toast.makeText(context, "Selected category "+cat, Toast.LENGTH_SHORT).show();
                    if(el != 0 && cat.equals("EL")){
//                        Toast.makeText(context, "Entered the update session", Toast.LENGTH_SHORT).show();
                        final long up_el = el - diff;
                        Toast.makeText(context, "Current el is"+up_el, Toast.LENGTH_SHORT).show();
                        HashMap hasMap = new HashMap();
                        hasMap.put("EL",up_el);
                        database.child("Users").child(phone).updateChildren(hasMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(context, "EL is being updated from "+el+"to"+up_el, Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                    if(cl!=0 && cat.equals("CL")){
//                        Toast.makeText(context, "Entered the update session", Toast.LENGTH_SHORT).show();
                        final long up_cl = cl - diff;
                        Toast.makeText(context, "Current cl is"+up_cl, Toast.LENGTH_SHORT).show();
                        HashMap hasMap = new HashMap();
                        hasMap.put("CL",up_cl);
                        database.child("Users").child(phone).updateChildren(hasMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(context, "CL is being updated from "+el+"to"+up_cl, Toast.LENGTH_SHORT).show();
                                builderAlert.dismiss();
                            }
                        });
                    }

                    if (status) {
                        database.child("Users").child(phone).child("leaves").child(cat).child(key).setValue(new dataUser(
                                subject,
                                radioButton.getText().toString(),
                                reason,
                                Start_date.getTime(),
                                end_date.getTime(),
                                diff


                        )).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Data base updated", Toast.LENGTH_SHORT).show();

                                builderAlert.dismiss();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "There exist some error in db side" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                builderAlert.dismiss();
                            }
                        });

                    }

                }
            }
        });

        btnDateDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        date.setText(simpleDateFormat.format(calendar.getTime()));
                        Start_date = calendar.getTime();
//                        end_date = calendar.getTime();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }

        });

        btnenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        enddate.setText(simpleDateFormat.format(calendar.getTime()));

                        end_date = calendar.getTime();




                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }

        });

    }

    private void showData() {
        database.child("Users").child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                el = snapshot.child("EL").getValue(int.class);
                cl = snapshot.child("CL").getValue(int.class);
                el_text.setText(Html.fromHtml("<font color='#6200EE'><b>Total No. of EL available - </b><br></font>" + el));
                cl_text.setText(Html.fromHtml("<font color='#6200EE'><b>Total No. of CL available - </b><br></font>" + cl));
                final List<String> cl_his = new ArrayList<>();
                database.child("Users").child(phone).child("leaves").child("CL").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String department = String.valueOf(childSnapshot.getKey());
//                            Toast.makeText(context, department, Toast.LENGTH_SHORT).show();
                            cl_his.add(department);
                        }
//                        Toast.makeText(context, "cl history"+cl_his, Toast.LENGTH_SHORT).show();
//                        ArrayAdapter<String> clhistor = new ArrayAdapter<String>();
                        if (cl_his.isEmpty()){
                            cl_title.setText(Html.fromHtml("<font color='#6200EE'><b>CL leaves NILL </b><br></font>"));
//                            cl_title.setText(Html.fromHtml(""));
                        }else {
                            cl_title.setText(Html.fromHtml("<font color='#6200EE'><b>CL leaves History</b><br></font>"));
//                            cl_text.setText(Html.fromHtml("<font color='#6200EE'><b>CL History </b><br></font>" + cl));
                            ArrayAdapter<String> clhistory = new ArrayAdapter<>(CalendarMainActivity.this, android.R.layout.simple_list_item_1, cl_his);
                            cl_his_view.setAdapter(clhistory);
                        }
//                        cl_his_view.setText(Html.fromHtml("<font color='#6200EE'><b> </b><br></font>" + cl_his));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                final List<String> el_his = new ArrayList<>();
                database.child("Users").child(phone).child("leaves").child("EL").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String department = String.valueOf(childSnapshot.getKey());
//                            Toast.makeText(context, department, Toast.LENGTH_SHORT).show();
                            el_his.add(department);
                        }
//                        Toast.makeText(context, "el history"+el_his, Toast.LENGTH_SHORT).show();
//                        ArrayAdapter<String> clhistor = new ArrayAdapter<String>();
                        if (el_his.isEmpty()){
//                            Toast.makeText(context, "EL history empty", Toast.LENGTH_SHORT).show();
                            el_title.setText(Html.fromHtml("<font color='#6200EE'><b>EL leaves NILL </b><br></font>"));
//                            el_title.setText(Html.fromHtml("EL leaves NILL"));
                        }else{
                            el_title.setText(Html.fromHtml("<font color='#6200EE'><b>EL History </b><br></font>"));
//                            Toast.makeText(context, "EL history not empty"+el_his, Toast.LENGTH_SHORT).show();
                            ArrayAdapter<String> elhistory = new ArrayAdapter<>(CalendarMainActivity.this, android.R.layout.simple_list_item_1, el_his);
                            el_his_view.setAdapter(elhistory);
//                        cl_his_view.setText(Html.fromHtml("<font color='#6200EE'><b> </b><br></font>" + cl_his));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showListener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            dataUser user = item.getValue(dataUser.class);
            list.add(user);
        }
        adapterItem = new AdapterItem(context, list);
        recyclerView.setAdapter(adapterItem);
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