package com.appbasket.project.retailnfc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private TextInputLayout inputLayoutAmount, inputLayoutpasscode;
    private  EditText etAmount,etPasscode,etPurpose;
    private Button btnSubmit;
    private Intent intent;
    String incomingStr="";
    private TextView tvError;
    ProgressDialog progress;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        inputLayoutAmount = (TextInputLayout) findViewById(R.id.input_amount);
        inputLayoutpasscode = (TextInputLayout) findViewById(R.id.input_passcode);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etPasscode = (EditText) findViewById(R.id.etPassword);
        etPurpose = (EditText) findViewById(R.id.etPurpose);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        tvError = (TextView) findViewById(R.id.textView);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


        processIntent();

    }

    public void processIntent(){
        intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent()
                .getAction())) {

            incomingStr = intent.getDataString();

            if (incomingStr.contains("retail://?data=")) {

                try{

                    incomingStr = incomingStr.replace("retail://?data=", "");

                    incomingStr = EncrytDecrypt.DecryptData(incomingStr);

                    Log.d("nfc ", "tagaid :" + incomingStr);

                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),
                            "Tag not recognized", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public void sendSms(String amount,String passcode,String purpose,String incoming) {

            String[] splitData = incoming.split("-");

        Log.i("sp", splitData + "");

        String result = splitData[1];

        if ("7866".equals(passcode)) {

            String uuid = getRandomUUID();
            String msg = amount + ":" + passcode + ":" + purpose + ":" + presentDate() + ":" + "thirdbinary007" + ":" + uuid + ":" + splitData[0];

            msg = EncrytDecrypt.EncryptData(msg);

            String formingmessage = "NPAY" + msg;

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("9008108650", null, formingmessage, null, null);

            progress=new ProgressDialog(this);
            progress.setMessage("Processing Transaction");
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    progress.cancel();
                }
            }, 50*1000);

        } else

        {

            tvError.setText("Passcode mismatch");
        }

    }

   public void recivedSms(String message,Context context)
    {
        if(message.contains("NPAY")) {

            if(message.contains("insufficent")) {

                progress.cancel();
                ShowDialog("insufficient balance in User account");
Toast.makeText(MainActivity.this,"insufficient balance in User account",Toast.LENGTH_LONG).show();
            }

            if(message.contains("credited")) {

                progress.cancel();
                ShowDialog("Payment processed successfully");
                Toast.makeText(MainActivity.this,"Payment done successfully",Toast.LENGTH_LONG).show();

            }


        }

        }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    public void submitForm(){
//        if (!incomingStr.equals("")) {
            if (!validateAmount()) {
                return;
            }

            if (!validatePasscode()) {
                return;
            }

            String amount = etAmount.getText().toString();
            String passcode = etPasscode.getText().toString();
            String purpose = etPurpose.getText().toString();
            Log.i("incoming", incomingStr);
            sendSms(amount, passcode, purpose, incomingStr);
//        }
//        else{
//            tvError.setText("Tap the NFC pay card to proceed");
//        }

    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateAmount() {
        if (etAmount.getText().toString().trim().isEmpty())
        {
            inputLayoutAmount.setError(getString(R.string.error_Amount));
            requestFocus(etAmount);
            return false;
        }

        else
        {
            inputLayoutAmount.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePasscode() {
        if (etPasscode.getText().toString().trim().isEmpty()) {
            inputLayoutpasscode.setError(getString(R.string.error_Passcode));
            requestFocus(etPasscode);
            return false;
        } else {
            inputLayoutpasscode.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    public  String presentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        String format = sdf.format(Calendar.getInstance().getTime());
        return format;
    }

    public static String getRandomUUID() {
        return java.util.UUID.randomUUID().toString();
    }

public void ShowDialog(String message){
    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
    builder1.setMessage(message);
    builder1.setCancelable(true);

    builder1.setPositiveButton(
            "Ok",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });



    AlertDialog alert11 = builder1.create();
    alert11.show();
}

}
