package com.example.friendszone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class getName extends AppCompatActivity {

    EditText etAVATARNAME;
    Button btnADDAVATAR;
    String avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);

        etAVATARNAME = findViewById(R.id.etAVATARNAME);
        btnADDAVATAR = findViewById(R.id.btnADDAVATAR);

        btnADDAVATAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                avatar = etAVATARNAME.getText().toString().trim();
                if(TextUtils.isEmpty(avatar))
                {
                    Toast.makeText(getApplicationContext(),"Please enter all the data",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //when all are set.
                    new addNameToDb().execute();
                }
            }
        });
    }

        public class addNameToDb extends AsyncTask<Void,Void,Void>
        {
            FirebaseUser user;
            DatabaseReference databaseReference;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                 user = FirebaseAuth.getInstance().getCurrentUser();
                 databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid()).child("name");
            }



            @Override
            protected Void doInBackground(Void... voids) {

                databaseReference.setValue(avatar).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(getName.this,MapsActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"There is a problem in adding name",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return null;
            }




    }
}
