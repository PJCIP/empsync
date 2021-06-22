package com.srinidhi.lgb;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.srinidhi.lgb.Model.Users;
import com.srinidhi.lgb.Prevalent.Prevalent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment3 extends Fragment {

    private Button submitbtn;
    //    private EditText inputjbr, inputdepart, inputvname, inputvmodal, inputvcc;
    private ProgressDialog loadingdbar;
    private ProgressDialog loadingdbar2;
    private String parentDbName = "Users";
    private int count = 0;
    DatabaseReference read_reff, read_type, read_vechmodelcc;
    Spinner spinner, jobrole, vehic_type, eng_cc;
    List<String> names;
    DatabaseReference read_job;
    //    String[] deps = { "Sales", "Packaging", "Delivery", "Accounts", "HR" };
    List<String> dept;
    List<String> role;
    List<String> cc;
    List<String> type;
    String depdetail, rolejob, vehicmodelcc, vehictype;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    int pos,el,cl;

    public BlankFragment3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_fragment3, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submitbtn = (Button) view.findViewById(R.id.asset_btn);
//        inputjbr = (EditText) view.findViewById(R.id.Job_role);
//        inputdepart = (EditText) view.findViewById(R.id.Dep);
//        inputvname = (EditText) view.findViewById(R.id.Vehi);
//        inputvmodal = (EditText) view.findViewById(R.id.Model_name);
//        inputvcc = (EditText) view.findViewById(R.id.eng_cc);
        //inputcphone=(EditText) view.findViewById(R.id.cnfrm_phone);
        loadingdbar = new ProgressDialog(getActivity());

        spinner = view.findViewById(R.id.spinner1);
        jobrole = view.findViewById(R.id.spinner2);
        eng_cc = view.findViewById(R.id.eng_cc);
        vehic_type = view.findViewById(R.id.vehic_type);
        dept = new ArrayList<>();
        role = new ArrayList<>();
        type = new ArrayList<>();
        cc = new ArrayList<>();
//        For job role and department fetching
        read_reff = FirebaseDatabase.getInstance().getReference().child("Dept");
        read_reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String department = String.valueOf(childSnapshot.getKey());
                    dept.add(department);
                }
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dept);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
//                spinner.setOnItemSelectedListener(BlankFragment3.this);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        depdetail = dept.get(position);
                        if(depdetail.equals("LUBES"))
                        {
                            el = 12;
                            cl =7;
                        }else
                        {
                            el =15;
                            cl =4;
                        }
//                        Toast.makeText(getActivity(), "Selected department is " + depdetail, Toast.LENGTH_SHORT).show();
                        role.clear();
                        read_job = FirebaseDatabase.getInstance().getReference().child("Dept").child(depdetail);
                        read_job.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                    String jobroles = String.valueOf(childSnapshot.getKey());
                                    role.add(jobroles);
                                }
//                                Toast.makeText(getActivity(), "Roles are" + role, Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, role);
                                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                jobrole.setAdapter(adapter2);
                                jobrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        rolejob = role.get(position);
//                                        Toast.makeText(getActivity(), "Selected job role is " + rolejob, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        Toast.makeText(getActivity(), "Please select a job role", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Couldnt fetch job role details from db", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getActivity(), "Please select a Department", Toast.LENGTH_SHORT).show();
                    }
                });
//                End of job role and department
                read_type = FirebaseDatabase.getInstance().getReference().child("admin").child("costs");
                read_type.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String department = String.valueOf(childSnapshot.getKey());
                            type.add(department);
                        }
                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type);
                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        vehic_type.setAdapter(adapter3);
//                spinner.setOnItemSelectedListener(BlankFragment3.this);
                        vehic_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                vehictype = type.get(position);
//                                Toast.makeText(getActivity(), "Types are "+type, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "Selected department is " + depdetail, Toast.LENGTH_SHORT).show();
                                cc.clear();
                                read_vechmodelcc = FirebaseDatabase.getInstance().getReference().child("admin").child("costs").child(vehictype);
                                read_vechmodelcc.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                            String jobroles = String.valueOf(childSnapshot.getKey());
                                            cc.add(jobroles);
                                        }
//                                        Toast.makeText(getActivity(), "CC's are "+cc, Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getActivity(), "Roles are" + role, Toast.LENGTH_SHORT).show();
                                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cc);
                                        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        eng_cc.setAdapter(adapter3);
                                        eng_cc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                vehicmodelcc = cc.get(position);
//                                                Toast.makeText(getActivity(), "Selected cc/modelname is " + vehicmodelcc, Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                                Toast.makeText(getActivity(), "Please select a cc/model name", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(), "Couldnt fetch cc/model name details from db", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                Toast.makeText(getActivity(), "Please select a Vehicle type", Toast.LENGTH_SHORT).show();
                            }
                        });

                        submitbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Prevalent.currentOnlineUser.getJobrole() == null) {
                                    submitdetails();

                                } else {
                                    System.out.println("Its Not Approved.");
                                    System.out.println(Prevalent.currentOnlineUser.getJobrole());
                                    Toast.makeText(getActivity(), "Its Not Approved.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }

                    private void submitdetails() {
//                String jobrole = inputjbr.getText().toString();
//                String vdepart = inputdepart.getText().toString();
//                String vname = inputvname.getText().toString();
//                String vmodalname = inputvmodal.getText().toString();
//                String vehicc = inputvcc.getText().toString();


                        if (TextUtils.isEmpty(vehicmodelcc)) {
                            Toast.makeText(getActivity(), "Please Select Your Vehicle Model/cc", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(vehictype)) {
                            Toast.makeText(getActivity(), "Please Select Your Vehicle type", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(depdetail)) {
                            Toast.makeText(getActivity(), "department is not selected", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(rolejob)) {
                            Toast.makeText(getActivity(), "job role is not selected", Toast.LENGTH_SHORT).show();
                        } else {

                            loadingdbar.setTitle("Updating Credentials");
                            loadingdbar.setMessage("Please wait, while we are updating the credentials.");
                            loadingdbar.setCanceledOnTouchOutside(false);
                            loadingdbar.show();


                            Validateacc(vehictype, vehicmodelcc, depdetail, rolejob);
                        }
                    }


                    private void Validateacc(final String vehictype, final String vehicmodelcc, final String depdetail, final String rolejob) {
                        final DatabaseReference Rootref;
                        Rootref = FirebaseDatabase.getInstance().getReference();


                        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(parentDbName).child(Prevalent.currentOnlineUser.getPhone()).child(rolejob).exists()) {
                                    loadingdbar2.setTitle("Alert!!!");
                                    loadingdbar2.setMessage("You Can Update Only Once!!!!");
                                    loadingdbar2.setCanceledOnTouchOutside(false);
                                    loadingdbar2.show();
                                    loadingdbar2.dismiss();
                                    Toast.makeText(getActivity(), "This " + rolejob + " already exists.", Toast.LENGTH_SHORT).show();
                                    loadingdbar.dismiss();
                                    Toast.makeText(getActivity(), "Its Not Approved.", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getActivity(), myprofileActivity.class);
                                    startActivity(intent);

                                }

                                if (!(dataSnapshot.child(parentDbName).child(Prevalent.currentOnlineUser.getPhone()).child(rolejob).exists())) {


                                    Users usersData = dataSnapshot.child(parentDbName).child(Prevalent.currentOnlineUser.getPhone()).getValue(Users.class);

                                    HashMap<String, Object> userdataMap = new HashMap<>();
                                    userdataMap.put("jobrole", rolejob);
                                    userdataMap.put("department", depdetail);
//                                    userdataMap.put("VehicleModalName/cc", vehicmodelcc);
//                                    userdataMap.put("Vehicletype", vehictype);
                                    userdataMap.put("vehiclename", vehictype);
                                    userdataMap.put("VehicleModalName", vehicmodelcc);
                                    userdataMap.put("EL", el);
                                    userdataMap.put("CL", cl);


                                    Rootref.child(parentDbName).child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userdataMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Congratulations, your account has been updated.", Toast.LENGTH_SHORT).show();
                                                        loadingdbar.dismiss();
                                                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                                                        startActivity(intent);


                                                    } else {
                                                        loadingdbar.dismiss();
                                                        Toast.makeText(getActivity(), "Network Error: Please try again after some time...", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Couldnt fetch department details from db", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}



