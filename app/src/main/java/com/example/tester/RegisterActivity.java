package com.example.tester;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity { //deleted implements View.OnclickListener

    private EditText Username,Cardnumber,Password;
    private Button registerButton;

    //from video (Store Data in Firebase real time database in Android studio - Android Firebase#1 2020)
    //FirebaseDatabase rootNode;
    //DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toast.makeText(RegisterActivity.this, "Let's create your account.",Toast.LENGTH_SHORT).show();

        Username = (EditText) findViewById(R.id.user_editText);
        Cardnumber = (EditText) findViewById(R.id.card_editText);
        Password = (EditText) findViewById(R.id.password_editText);

        registerButton = (Button) findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                reference.setValue("First data storage");*/
                CreateAccount();
                //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));//ลองทำบรรทัดนี้แล้ว ปรากฎว่าปุ่มกดไม่ทำงานต่อ
            }
        });
    }

    //Register method if use CreateAccount() method hide this if use rootNode and reference
    private void CreateAccount(){
        String username = Username.getText().toString();
        String password = Password.getText().toString();
        String card = Cardnumber.getText().toString();

        if (TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please enter your username", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(card)){
            Toast.makeText(this,"Please enter your card number", Toast.LENGTH_SHORT).show();
        }
        else {
            Validate(username, password, card);
        }
    }

    private void Validate(final String username, final String password, final String card) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (!(dataSnapshot.child("Users").child(username).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("username", username);
                    userdataMap.put("password", password);
                    userdataMap.put("cardno", card);

                    RootRef.child("Users").child(username).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterActivity.this,"Congratulation, your account has been created.", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this, "Error: Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "This " + username + " already exists.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(RegisterActivity.this, "Please try again using another UserID.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

}
