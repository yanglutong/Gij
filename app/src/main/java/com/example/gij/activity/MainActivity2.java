package com.example.gij.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.gij.R;

public class MainActivity2 extends Activity {

    //    private TextView tv_cell;
//    private BatteryReceiver batteryReceiver;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };
    private Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            Log.e("Mr.Y", "run: =--------------------------------");
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);
            handler.postDelayed(this, 1000);
        }
    };
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        SubscriptionManager mSubscriptionManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            mSubscriptionManager = SubscriptionManager.from(this);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            int simNumberCard = mSubscriptionManager.getActiveSubscriptionInfoCount();//????????????sim?????????
            Log.i("ylt", "onCreate: "+simNumberCard);
        }

//        handler.post(runnable);
//
//        tv_cell = (TextView) findViewById(R.id.tv_cell);
//        tv_cell.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                handler.removeCallbacks(runnable);
//                handler.removeMessages(1);
//            }
//        });
//        // ?????????????????????java??????
//        IntentFilter intentFilter = new IntentFilter(
//                Intent.ACTION_BATTERY_CHANGED);
//        batteryReceiver = new BatteryReceiver();
//        // ??????receiver
//        registerReceiver(batteryReceiver, intentFilter);
    }

//    /**
//     * ???????????????
//     */
//    private class BatteryReceiver extends BroadcastReceiver {
//        @SuppressLint("WrongConstant")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
//                int level = intent.getIntExtra("level", 0);
//                tv_cell.setText("???????????????" + level + "%");
//
//                if (level < 15) {
//                    Toast.makeText(MainActivity2.this, "??????????????????15%??????????????????", 0)
//                            .show();
//                }
//            }
//        }
//
//    }
public void readSIMCard() {
    TelephonyManager manager = (TelephonyManager) this

            .getSystemService(TELEPHONY_SERVICE);// ????????????????????????

    String imsi = manager.getSubscriberId(); // ??????IMSI

    System.out.println("??????IMSI" + imsi);

    if (imsi == null || imsi.length() <= 0) {
        System.out.println("?????????sim?????????????????????sim?????????????????????");

//APIFailSimBuyJNI();

    } else {
        System.out.println("???SIM???");

    }

}
    @Override
    protected void onDestroy() {
        super.onDestroy();

//        unregisterReceiver(batteryReceiver);
    }
}