package com.srinidhi.lgb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.srinidhi.lgb.Prevalent.Prevalent;
import com.srinidhi.lgb.R;
import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {
    private String check = "";
    private TextView titlereset, titlequestions;
    private EditText phoneNumber, question1, question2;
    private Button verifybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passowrd);

        check = getIntent().getStringExtra("check");

        titlereset = findViewById(R.id.title_rst);
        titlequestions = findViewById(R.id.ans_title);
        phoneNumber = findViewById(R.id.find_phone_no);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifybtn = findViewById(R.id.ques_submit_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        phoneNumber.setVisibility(View.GONE);
        if (check.equals("settings")) {
            titlereset.setText("Set Questions");

            titlequestions.setText(" Please Set Answers For The Security Questions...");
            verifybtn.setText("Set");
            displayPreviousanswers();


            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAnswers();


                }
            });

        } else if (check.equals("login")) {
            phoneNumber.setVisibility(View.VISIBLE);

            verifybtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ResetPasswordActivity.this, "Verifying the user please wait", Toast.LENGTH_SHORT).show();
                    verifyuser();
                }
            });

        }
    }
    private void verifyuser() {
        final String phone = phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase().trim();
        final String answer2 = question2.getText().toString().toLowerCase().trim();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")) {
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(phone);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String mphone = snapshot.child("phone").getValue().toString();
                        if (snapshot.hasChild("Security Questions")) {
                            String ans1 = snapshot.child("Security Questions").child("answer1").getValue().toString().trim();
                            String ans2 = snapshot.child("Security Questions").child("answer2").getValue().toString().trim();
                            if (!ans1.equals(answer1)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your First Answer Is Wrong", Toast.LENGTH_SHORT).show();

                            } else if (!ans2.equals(answer2)) {
                                Toast.makeText(ResetPasswordActivity.this, "Your Second Answer Is Wrong", Toast.LENGTH_SHORT).show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newpassword = new EditText(ResetPasswordActivity.this);
                                newpassword.setHint("Write New Password Here...");
                                builder.setView(newpassword);
                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!newpassword.getText().toString().equals("") && newpassword.getText().toString().length() > 5)

                                        {

                                            ref.child("password").setValue(newpassword.getText().toString())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ResetPasswordActivity.this, "Your Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
                                                                startActivity(intent);

                                                            }

                                                        }
                                                    });

                                        }
                                        else
                                        {
                                            Toast.makeText(ResetPasswordActivity.this, "Password length must be greater than 5", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();

                                    }
                                });
                                builder.show();

                            }


                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "You Have Not Set The Security Questions Please Contact", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this,"This Phone Number Not Exists",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            Toast.makeText(this,"Please Complete The Form",Toast.LENGTH_SHORT).show();

        }


    }

    private  void setAnswers(){
        String answer1=question1.getText().toString().toLowerCase();
        String answer2=question2.getText().toString().toLowerCase();

        if(question1.equals(null) && question2.equals(null))
        {
            Toast.makeText(ResetPasswordActivity.this,"Please Answer Both The Questions",Toast.LENGTH_SHORT).show();

        }
        else
        {
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
            HashMap<String, Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1",answer1);
            userdataMap.put("answer2",answer2);
            ref.child("Security Questions").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ResetPasswordActivity.this,"You Have Answered The Security Questions Successfully",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }
    }


    private  void  displayPreviousanswers()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ans1 = snapshot.child("answer1").getValue().toString();
                    String ans2 = snapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}