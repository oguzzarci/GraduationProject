package com.app.diceroid.nerede.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.diceroid.nerede.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Deniz TERZI on 2/26/2018.
 */
public class LocationActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,View.OnClickListener {
    //BindView ButterKnife library nin sunmus oldugu bir binding mekanizmasi;
    DatabaseReference oku1;
    DatabaseReference okuson;
    @BindView(R.id.buttonFindRoute)
    Button findRoute;
    Button buttonMarker;

    @BindView(R.id.location_info_text)
    TextView location_info_text;
    @BindView(R.id.destination_place_text)
    TextView destination_place_text;
    private GoogleMap mMap;

    ArrayList<LatLng> mMarkerList = new ArrayList<LatLng>();
    ArrayList<String> childrenKeys = new ArrayList<>();
    ArrayList<DatabaseReference> dbReferanceOku = new ArrayList<>();
    int keyUzunluk;
    String keyValue;

    Double myLocationLatitude;
    Double myLocationLongitude;
    LatLng myLocationView;
    String rootKey;
    private FirebaseAuth firebaseAuth;

    public FirebaseUser userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_location);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        buttonMarker = (Button) findViewById(R.id.buttonMarker);
        //keyChildren();

        findRoute.setOnClickListener((OnClickListener) this);
        buttonMarker.setOnClickListener(this);

        internetKontrol();
        start();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser();
        keyChildren();
    }

    //Kids activityden gelen veriler alınıyor;
    private void start() {
        Intent i = getIntent();
        rootKey=i.getStringExtra("rootKey");
        Log.d("rootkey->",rootKey);
    }



    //Harita hazır oldugunda calisicak bolum;
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

        } else {
            return;
        }

    }


    public void getLocationKids() {
        keyUzunluk = childrenKeys.size();
        for (int i = 0; i < keyUzunluk; i++) {
            keyValue = childrenKeys.get(i);
            oku1 = FirebaseDatabase.getInstance().getReference().child("cocuklar").child(userId.getUid()).child(rootKey).child(keyValue);
            dbReferanceOku.add(oku1);
        }
        for (int i = 0; i < keyUzunluk; i++) {
            okuson = dbReferanceOku.get(i);
            ValueEventListener dinle = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User aa = new User();
                    aa = dataSnapshot.getValue(User.class);

                    myLocationLatitude = aa.Latitude;
                    myLocationLongitude = aa.Longitude;
                    myLocationView = new LatLng(myLocationLatitude, myLocationLongitude);
                    mMarkerList.add(myLocationView);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            okuson.addValueEventListener(dinle);
        }

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    public void markerAdd(){
        keyUzunluk = childrenKeys.size();
        if(keyUzunluk != 0){
            for (int i = 0; i < keyUzunluk; i++){
                LatLng position = mMarkerList.get(i);
                mMap.addMarker(new MarkerOptions().title((i+1) + ". Konum").snippet((i+1) + ". Gidilen yer").position(position));
            }
        }else {
            Toast.makeText(getApplicationContext(), "Şuana kadar belirlenmiş mevcut konum bulunamamıştır.", Toast.LENGTH_SHORT).show();
        }



    }
    public void keyChildren(){


        oku1 = FirebaseDatabase.getInstance().getReference().child("cocuklar").child(userId.getUid()).child(rootKey);

        ValueEventListener dinle = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot keyShot:dataSnapshot.getChildren()){

                    if(keyShot.getKey().equals("name")){
                        continue;
                    }

                    childrenKeys.add(keyShot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        oku1.addValueEventListener(dinle);

    }

    public  void internetKontrol(){

        ConnectivityManager cm = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);//internet kontrol

        if (cm.getActiveNetworkInfo() == null ){

            Toast.makeText(getApplicationContext(),"Cihazın internet Bağlantısı Yok",Toast.LENGTH_LONG).show();
        }


    }



    @Override
    public void onClick(View view) {
        if(view == findRoute){
            getLocationKids();

        }

        if(view == buttonMarker){
            markerAdd();
        }

    }
}
