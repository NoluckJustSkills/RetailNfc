package com.appbasket.project.retailnfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Parvez on 2016-01-10.
 */
public class IncomingSmsService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs;
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    MainActivity Sms = new MainActivity();
                   Login Sms2 = new Login();

//                    try {
//                        if(message.contains("RET-Y")){
//                         message = message.replace("RET-Y-", "");
//                            String apikey = EncrytDecrypt.DecryptData(message);
//
//                            Sms2.recivedSmsinLogin(message);
//
//                        }else if(message.contains("RET-N")) {
//                            Sms2.recivedSmsinLogin("NIl");
//                        }
//
//                        else{


                            Sms.recivedSms(message, context);


//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }

            }


        } catch (Exception e) {

        }
    }
}