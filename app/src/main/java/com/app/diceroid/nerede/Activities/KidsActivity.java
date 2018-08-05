package com.app.diceroid.nerede.Activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.diceroid.nerede.R;
import com.app.diceroid.nerede.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class KidsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private Button buttonLogout;
    private Button buttonKidsAdd;
    private DatabaseReference databaseReference;
    private DatabaseReference oku;
    private FirebaseUser userId;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private  ArrayList<String> rootKey = new ArrayList<String>();
    private String uid;
    String uzunSilinecek;

    String putPosition;
    String cocukKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kids);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser();
        uid = FirebaseAuth.getInstance().getUid();
        oku = FirebaseDatabase.getInstance().getReference().child("cocuklar").child(userId.getUid());


        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonKidsAdd = (Button) findViewById(R.id.buttonKidsAdd);

        buttonKidsAdd.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);


        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);

        listView.setLongClickable(true);
        listView.setOnItemClickListener(new ListClickHandler());
        listView.setOnItemLongClickListener(new ListClickHandlerDelete());


        //çocuklar listeleniyor
        select();


    }

    public class ListClickHandlerDelete implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

            uzunSilinecek = rootKey.get(i);
            silCocuk();
            return true;
        }

    }



    public class ListClickHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {



            //tıklanan çocuğun keyi alınıp LocationActivitiy e aktarılıyor;

            putPosition= rootKey.get(position);

            Intent intent = new Intent(KidsActivity.this, LocationActivity.class);
            intent.putExtra("rootKey", putPosition);
            startActivity(intent);
        }


    }


    private void select(){

        oku.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                cocukKey = dataSnapshot.getKey();
                rootKey.add(dataSnapshot.getKey());
                UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);
                arrayList.add(userInformation.name);
                listView.setAdapter(adapter);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                UserInformation userInformation=dataSnapshot.getValue(UserInformation.class);
                arrayList.remove(userInformation.name);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }


    //silme işlemi

    private void silCocuk(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delete_dialog,null);
        dialogBuilder.setView(dialogView);

        Button buttonDelete = (Button) dialogView.findViewById(R.id.delete_button);
        dialogBuilder.setTitle("ÇOCUK SİL !");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cocukSil(firebaseAuth.getUid());
                alertDialog.dismiss();
            }
        });

    }


    private void cocukSil(String uid) {



        DatabaseReference databaseReferenceSil = FirebaseDatabase.getInstance().getReference("cocuklar").child(uid).child(uzunSilinecek);
        databaseReferenceSil.removeValue();
        Toast.makeText(this,"Çocuk Silindi",Toast.LENGTH_LONG).show();

    }


    private void kidsAdd() {
        Intent intent = new Intent(this, KidsAddActivity.class);
        startActivity(intent);

    }



    //İNTERNET KONTROL
    public  void internetKontrol(){

        ConnectivityManager cm = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);//internet kontrol

        if (cm.getActiveNetworkInfo() == null ){

            Toast.makeText(getApplicationContext(),"Cihazın internet Bağlantısı Yok",Toast.LENGTH_LONG).show();
        }else {
            kidsAdd();
        }


    }


    //Geri Tuşu Kontrol
    @Override
    public void onBackPressed(){

    }



    @Override
    public void onClick(View view) {



        if(view == buttonLogout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        if(view == buttonKidsAdd){
            internetKontrol();
        }

    }
}