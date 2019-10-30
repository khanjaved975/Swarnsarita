package com.project.jewelmart.swarnsarita;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;


public class ServiceAlert extends Activity {

    String error;
    String ack;

    private SharedPreferences pref;
    private Editor editor;
    public static final String KEY_FLAG = "0";

    int PRIVATE_MODE = 0;
    protected Intent intent;
    private static final String PREF_NAME = "JewelFlow";
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent i = getIntent();
        error = i.getStringExtra("error");
        ack = i.getStringExtra("ack");
        status = i.getStringExtra("status");

        //Log.e("Screenshot", "ACK :: " + ack);
        Log.e("Screenshot", "ERROR :: " + error);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
//                File pix = Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                final File screenshots = badgenew File(pix, "Screenshots");
//                String deletefile = screenshots.getAbsolutePath()
//                        + File.separator
//                        + SingletonClass.getinstance().getFilename();
//                File myFile = badgenew File(deletefile);
////                if (myFile.exists())
////                    myFile.delete();
                if (status.equals("Blocked")) {

                    intent = new Intent(ServiceAlert.this, MainActivity.class);
                    startActivity(intent);
                    dialog.cancel();
                    finish();
                } else {
                    dialog.cancel();
                   // dialog.dismiss();
                    finish();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
