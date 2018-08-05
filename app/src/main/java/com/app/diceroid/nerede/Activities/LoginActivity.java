package com.app.diceroid.nerede.Activities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.app.diceroid.nerede.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonReg;
    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private TextView reset_password;

    //private static final String Log_Tag = "Otomatik internet Kontrol"; //internet kontrol
    private NetworkChangeReceiver receiver; //internet kontrol

//deneme butonu hizlica maps islemlerini denemek olusturdum proje butunuyle ilgili degildir.
//Firebase ile ilgili hicbir yer degismedi.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() !=null){
            finish();
            startActivity(new Intent(getApplicationContext(),KidsActivity.class));
        }




        progressDialog = new ProgressDialog(this);
        editTextEmail = (EditText) findViewById(R.id.email_address);
        editTextPassword = (EditText) findViewById(R.id.password_text);
        buttonLogin = (Button) findViewById(R.id.login_now);
        buttonReg = (Button) findViewById(R.id.register_now);
        reset_password = (TextView) findViewById(R.id.reset_password_textView);

        buttonReg.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        reset_password.setOnClickListener(this);
        getSupportActionBar().setTitle("Giriş Yap");

    }

    //Geri Tuşu Kontrol
   @Override
   public void onBackPressed(){

   }





    public  void internetKontrol(){

        ConnectivityManager cm = (ConnectivityManager)getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);//internet kontrol

        if (cm.getActiveNetworkInfo() == null ){

            Toast.makeText(getApplicationContext(),"Cihazın internet Bağlantısı Yok",Toast.LENGTH_LONG).show();
        }else {
            showKidsActivity();
        }


    }

    private void showKidsActivity(){

            String email = editTextEmail.getText().toString().trim();
            String password  = editTextPassword.getText().toString().trim();


            //checking if email and passwords are empty
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this,"Lütfen Email Giriniz",Toast.LENGTH_LONG).show();
                return;
            }

            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"Lütfen Şifrenizi Giriniz",Toast.LENGTH_LONG).show();
                return;
            }



            progressDialog.setMessage("Giriş Yapılıyor Lütfen Bekleyin...");
            progressDialog.show();


            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if(task.isSuccessful()){

                                finish();
                                startActivity(new Intent(getApplicationContext(), KidsActivity.class));
                            }else{
                                loginError();
                            }


                        }
                    });
    }


    private void showRegActivity(){
        startActivity(new Intent(getApplicationContext(),RegActivity.class));
    }

    private void loginError(){
        Toast.makeText(this,"Şifre yada Emailiniz Yanlış",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {


        if(view == buttonReg){
            showRegActivity();
        }

        if(view == buttonLogin){
            internetKontrol();
        }

        if(view==reset_password){
            startActivity(new Intent(getApplicationContext(),activity_reset_password.class));
        }
    }






    private class NetworkChangeReceiver extends BroadcastReceiver { //internet kontrol
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
