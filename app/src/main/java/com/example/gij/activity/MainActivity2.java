package com.example.gij.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

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
    };;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



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
//        // 注册广播接受者java代码
//        IntentFilter intentFilter = new IntentFilter(
//                Intent.ACTION_BATTERY_CHANGED);
//        batteryReceiver = new BatteryReceiver();
//        // 注册receiver
//        registerReceiver(batteryReceiver, intentFilter);
    }

//    /**
//     * 广播接受者
//     */
//    private class BatteryReceiver extends BroadcastReceiver {
//        @SuppressLint("WrongConstant")
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
//                int level = intent.getIntExtra("level", 0);
//                tv_cell.setText("电池电量为" + level + "%");
//
//                if (level < 15) {
//                    Toast.makeText(MainActivity2.this, "电池电量不足15%，请及时充电", 0)
//                            .show();
//                }
//            }
//        }
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        unregisterReceiver(batteryReceiver);
    }
}