package com.example.friendszone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<String> datafromdb;
    final LatLng location = new LatLng(23.7839321,72.6371252);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        datafromdb = new ArrayList<String>();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;

            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            case R.id.satelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.log:
                //here we are persorm a operation for Log out.
                new logoutclass().execute();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        new addToMap().execute();

}

public class logoutclass extends AsyncTask<Void,Void,Void>
{

    FirebaseUser user;
    DatabaseReference databaseReference;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(user.getUid().toString());
    }


    @Override
    protected Void doInBackground(Void... voids) {
        databaseReference.child("Status").setValue("UnActive").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //when all set to Logout.
                }
                else
                {
                    //here error message will show.
                }
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MapsActivity.this,MainActivity.class));
    }

}


public class addToMap extends AsyncTask<Void,Void,Void>
{
    DatabaseReference reference;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        reference = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    protected Void doInBackground(Void... voids) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    datafromdb.clear();
                    Iterator<DataSnapshot> it = snap.getChildren().iterator();
                    while (it.hasNext())
                    {
                        String val = it.next().getValue().toString();
                        datafromdb.add(val);
                    }

                   double latitude = Double.parseDouble(datafromdb.get(0));
                   double longitude = Double.parseDouble(datafromdb.get(1));
                   String name = datafromdb.get(3);
                   String status = datafromdb.get(2);
                   LatLng latLng = new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .anchor(0.0f,1.0f).title(name)
                    .snippet("Status: "+status));
                     CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(10)
                            .bearing(0)
                            .tilt(30)
                            .build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

}

}
