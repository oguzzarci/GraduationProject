package com.app.diceroid.nerede.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.diceroid.nerede.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class activity_reset_password extends AppCompatActivity implements View.OnClickListener {

    private Button btn_back;
    private Button btn_reset_password;
    private EditText email_reset;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        progressDialog = new ProgressDialog(this);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_reset_password = (Button) findViewById(R.id.btn_reset_password);
        email_reset = (EditText) findViewById(R.id.email_Text);


        btn_back.setOnClickListener(this);
        btn_reset_password.setOnClickListener(this);

        getSupportActionBar().setTitle("Şifremi Unuttum");


    }




    public void reset(){

        String email = email_reset.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), "Bu alanı Boş Bırakamazsınız !", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("İşleminiz Yapılıyor Lütfen Bekleyin...");
        progressDialog.show();


        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            Toast.makeText(activity_reset_password.this, "Şifrenizi Değiştirmek İçin Size Bir Mail Gönderdik.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Toast.makeText(activity_reset_password.this, "Hay Aksi Hata ! Bilgilerinizi Kontol Edin.", Toast.LENGTH_SHORT).show();



                        }


                    }
                });
    }




    @Override
    public void onClick(View view) {

        if (view == btn_back) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        if (view == btn_reset_password) {


                reset();
        }
    }
}