package com.appbasket.project.retailnfc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
Button btnRegister;
    SharedPreferences prefs;
    EditText etNumber;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        etNumber = (EditText) findViewById(R.id.etNumber);

prefs = getSharedPreferences("myprefs",MODE_PRIVATE);
        String apikey = prefs.getString("apikey","nil");
        if(!apikey.equals("nil")){
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }




        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String mPhoneNumber = "No num";
                mPhoneNumber = etNumber.getText().toString();
                if(mPhoneNumber.equals("No num")){
                    Toast.makeText(Login.this,"Please enter your number",Toast.LENGTH_LONG).show();
                }else {
                    mPhoneNumber = EncrytDecrypt.EncryptData(mPhoneNumber);
                    String formingmessage = "NRETAILER" + mPhoneNumber;
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage("9008108650", null, formingmessage, null, null);
                    progress = new ProgressDialog(Login.this);
                    progress.setMessage("Registering");
                    progress.setIndeterminate(true);
                    progress.setCancelable(false);
                    progress.show();
                }
            }
        });



    }

   public void recivedSmsinLogin(String message){



   }
}
