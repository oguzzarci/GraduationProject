package com.app.diceroid.nerede.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.diceroid.nerede.R;
import com.app.diceroid.nerede.ResourceFiles.CoutdownValidate;
import com.app.diceroid.nerede.UserInformation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xw.repo.BubbleSeekBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;


public class KidsAddActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Button kidsAddButon;
    private EditText child_name;
    private DatabaseReference myLocationRef;


    String keyChildren;
    String name;
    String keyLocation;

    private RadioButton saat1;
    private RadioButton saat3;
    private RadioButton saat5;
    private RadioButton saat8;


    double myLocationLatitude;
    double myLocationLongitude;
    int selectTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids_add);

        getLocation();

        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        kidsAddButon = (Button) findViewById(R.id.kidsAddButon);
        child_name = (EditText) findViewById(R.id.child_name);

        saat1 = (RadioButton) findViewById(R.id.saat1);
        saat3 = (RadioButton) findViewById(R.id.saat3);
        saat5 = (RadioButton) findViewById(R.id.saat5);
        saat8 = (RadioButton) findViewById(R.id.saat8);



        //kullanıcı girişi yoksa anasayfaya yönlendiriyoru
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }


        myLocationRef = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        kidsAddButon.setOnClickListener(this);
        saat1.setOnClickListener(this);
        saat3.setOnClickListener(this);
        saat5.setOnClickListener(this);
        saat8.setOnClickListener(this);

        getSupportActionBar().setTitle("Çocuk Ekle");


    }

    public void kidsAdd(){

        // getTime();
        name = child_name.getText().toString().trim();
        UserInformation userInformation = new UserInformation(name);
        keyChildren = databaseReference.push().getKey();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child("cocuklar").child(user.getUid()).child(keyChildren).setValue(userInformation);
        Toast.makeText(this,"Kayıt Başarılı",Toast.LENGTH_SHORT).show();


        CoutdownValidate.countdown(new CoutdownValidate.ICallback() {

            @Override
            public void onEndTime() {


                if(myLocationLatitude == 0 && myLocationLongitude == 0){

                }else{
                    FirebaseUser user2 = firebaseAuth.getCurrentUser();
                    keyLocation = myLocationRef.push().getKey();
                    myLocationRef.child("cocuklar").child(user2.getUid()).child(keyChildren).child(keyLocation).child("Longitude").setValue(myLocationLongitude);
                    myLocationRef.child("cocuklar").child(user2.getUid()).child(keyChildren).child(keyLocation).child("Latitude").setValue(myLocationLatitude);
                }


            }
        },selectTime);




        Intent intent = new Intent(this, KidsActivity.class);
        startActivity(intent);

    }

    @Override
    public void onClick(View view) {

        if(view == kidsAddButon){
            kidsAdd();
        }if(view ==saat1){
            selectTime = 60000;
            Toast.makeText(this,"1 Saat Aralıklarla Konum Alınacak",Toast.LENGTH_SHORT).show();
        }
        if(view == saat3){
            selectTime = 60000;
            Toast.makeText(this,"3 Saat Aralıklarla Konum Alınacak",Toast.LENGTH_SHORT).show();
        }
        if(view == saat5){
            selectTime = 60000;
            Toast.makeText(this,"5 Saat Aralıklarla Konum Alınacak",Toast.LENGTH_SHORT).show();
        }
        if(view==saat8){
            selectTime = 60000;
            Toast.makeText(this,"8 Saat Aralıklarla Konum Alınacak",Toast.LENGTH_SHORT).show();
        }
    }

    public void getLocation() {
        LocationManager locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        LocationListener locationListener = new LocationListener() {
            //Bu alttaki 3 fonksiyon abstract edilmek zorunda konum servisleri açık mı kapalı mı diye bakıyor.
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getApplicationContext(), R.string.gps_info, Toast.LENGTH_SHORT).show();
                //location_info.setText("GPS Veri bilgileri Alınıyor...");
            }
            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getApplicationContext(), R.string.gps_notification_disable, Toast.LENGTH_SHORT).show();
                //location_info.setText("GPS Bağlantı Bekleniyor...");
            }

            @Override //get location data
            public void onLocationChanged(final Location location) {
                myLocationLatitude= location.getLatitude();
                myLocationLongitude= location.getLongitude();

            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

    }

}