package com.example.mytimetable.messages;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public class messagePopup {
    static TextToSpeech t1;
    private static messagePopup ourInstance = new messagePopup();
    private Context appContext;
    private boolean resultValue;
    public static boolean confirmation;
    private messagePopup() { }
    public static Context get() {
        return getInstance().getContext();
    }
    public static synchronized messagePopup getInstance() {
        return ourInstance;
    }
    public void init(Context context) {
        if (appContext == null) {
            this.appContext = context;
        }
    }
    private Context getContext() {
        return appContext;
    }

//    public boolean isConfirmation(final MainActivity mainActivity,String message){
//        awit AlertDialog(mainActivity,message);
//        return confirmation;
//    }

    public boolean getDialogValueBack(Context context,String message)
    {
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message mesg)
            {
                throw new RuntimeException();
            }
        };

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = true;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                resultValue = false;
                handler.sendMessage(handler.obtainMessage());
            }
        });
        alert.show();

        try{ Looper.loop(); }
        catch(RuntimeException e){}

        return resultValue;
    }

//    private void AlertDialog(final MainActivity mainActivity,String message) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivity);
//        alertDialogBuilder.setMessage(message);
//        alertDialogBuilder.setPositiveButton("yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        confirmation = true;
//                        //Toast.makeText(mainActivity, "You clicked yes button", Toast.LENGTH_LONG).show();
//                    }
//                });
//        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                confirmation = false;
//                dialog.cancel();
//            }
//        });
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }
}
