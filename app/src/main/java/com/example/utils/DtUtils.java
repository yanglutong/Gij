package com.example.utils;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.gij.R;

/**
 * @author: 小杨同志
 * @date: 2021/8/12
 */
public class DtUtils {
        public static String getGsmBand(int Arfcn){
            String string="";
            if(Arfcn>=1&&Arfcn<=94){
                string="GSM 900";
            }
            if(Arfcn>=975&&Arfcn<=1023){
                string="EGSM";
            }
            if(Arfcn>=512&&Arfcn<=561||Arfcn>=587&&Arfcn<=636){
                string="DCS 1800";
            }
            return string;
        }
        public static String getBand(int down) {
        String BAND = "1";

        if (down >= 0 && down <= 599) {
            BAND = "1";
            return BAND;
        }

        if (down >= 1200 && down <= 1949) {
            BAND = "3";
            return BAND;
        }

        if (down >= 2400 && down <= 2649) {
            BAND = "5";
            return BAND;
        }


        if (down >= 3450 && down <= 3799) {
            BAND = "8";
            return BAND;
            //FDD
        }

        if (down >= 36200 && down <= 36349) {
            BAND = "34";
            return BAND;
        }
        if (down >= 37750 && down <= 38249) {
            BAND = "38";
            return BAND;
        }
        if (down >= 38250 && down <= 38649) {
            BAND = "39";
            return BAND;
        }
        if (down >= 38650 && down <= 39649) {
            BAND = "40";
            return BAND;
        }
        if (down >= 39650 && down <= 41589) {
            BAND = "41";
            return BAND;
        }
        return BAND;

    }
    public static String getBands(int down) {
        String BAND = "0";
        if (down==1) {
            BAND = "1(FDD)";
            return BAND;
        }

        if (down ==3) {
            BAND = "3(FDD)";
            return BAND;
        }

        if (down ==5) {
            BAND = "5(FDD)";
            return BAND;
        }


        if (down ==8) {
            BAND = "8(FDD)";
            return BAND;
            //FDD
        }

        if (down ==34) {
            BAND = "34(TDD)";
            return BAND;
        }
        if (down ==38) {
            BAND = "38(TDD)";
            return BAND;
        }
        if (down ==39) {
            BAND = "39(TDD)";
            return BAND;
        }
        if (down ==40) {
            BAND = "40(TDD)";
            return BAND;
        }
        if (down ==41) {
            BAND = "41(TDD)";
            return BAND;
        }
        return BAND;
    }
    public static String getECI(String down) {
        String i=down;
        //将数值转为16进制
        String string = JK.hex10To16(Integer.parseInt(down));
        Log.e("yleeet", "将数值转为16进制: "+string);
        if(down.length()>4){
            //将数值 后尾两位拆分

            String substring1 = string.substring(string.length() - 2, string.length());
            Log.e("yleeet", "后尾两位拆分: "+substring1);


            String substring2 = string.substring(0, string.length() - 2);
            Log.e("yleeet", "前段: "+JK.hex16To10(substring2));

            i =down+"("+JK.hex16To10(substring2)+"-"+JK.hex16To10(substring1)+")";
        }
        return i;
    }

    public interface ShowDialog{
         void showDialog(View view,String type);
    }
    public static ShowDialog showDialog(Context context,String text,ShowDialog showDialog){

        Dialog dialog = new Dialog(context, R.style.menuDialogStyleDialogStyle);
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_item_title, null);
        TextView tv_title = inflate.findViewById(R.id.tv_title);//标题
        tv_title.setText(text);//设置弹出的标题
        Button bt_confirm = inflate.findViewById(R.id.bt_confirm);
        Button bt_cancel = inflate.findViewById(R.id.bt_cancel);
        bt_confirm.setOnClickListener(new View.OnClickListener() {//确定按钮
            @Override
            public void onClick(View view) {
                showDialog.showDialog(view,"确定");
                dialog.dismiss();
                dialog.cancel();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {//确定按钮
            @Override
            public void onClick(View view) {
                showDialog.showDialog(view,"取消");
                dialog.dismiss();
                dialog.cancel();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//                           lp.y = 20;//设置Dialog距离底部的距离
//                          将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

        return showDialog;
    }
    //      用Math进行比较0~10 5是否在此区间

    public static boolean rangeInDefined(int current, int min, int max)
    {
        return Math.max(min, current) == Math.min(current, max);
    }
    public static int  rangeInDefinedS(int  nrarfcn){
        int band=0;
        //判断是什么频点
        if (rangeInDefined(nrarfcn, 151600, 160600)) {
            band = 28;
        }
        if (rangeInDefined(nrarfcn, 499200, 537999)) {
            band = 41;
        }
        if (rangeInDefined(nrarfcn, 620000, 653333)) {
            band = 78;
        }
        if (rangeInDefined(nrarfcn, 693333, 733333)) {
            band = 79;
        }
        return band;
    }
}
