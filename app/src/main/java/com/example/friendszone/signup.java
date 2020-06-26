package com.example.friendszone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {

    EditText signUSERNAME ,signPASSWORD;
    Button signADD;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        signUSERNAME = findViewById(R.id.signUSERNAME);
        signPASSWORD = findViewById(R.id.signPASSWORD);
        signADD = findViewById(R.id.signADD);

        signADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = signUSERNAME.getText().toString().trim();
                String password = signPASSWORD.getText().toString().trim();



                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(),"Please enter all the data",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    signUpuser(username,password);
                }
             }
        });
    }

    private void signUpuser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                           Toast.makeText(getApplicationContext(),"Added successfully",Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(signup.this,MainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            signUSERNAME.setText("");
                            signPASSWORD.setText("");
                            Toast.makeText(getApplicationContext(),"There is a problem in signup",Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }
}
