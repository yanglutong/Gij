package com.example.gij.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthNr;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gij.OrmSqlLite.Bean.CellBean;
import com.example.gij.OrmSqlLite.Bean.CellBeanGSM;
import com.example.gij.OrmSqlLite.Bean.CellBeanNr;
import com.example.gij.OrmSqlLite.manager.DBManager;
import com.example.gij.R;
import com.example.gij.activity.MainActivity;
import com.example.gij.adapter.My4GAdapter;
import com.example.gij.recycler.RecyclerViewNoBugLinearLayoutManager;
import com.example.linechart.Bean;
import com.example.linechart.ChartView;
import com.example.linechart.ChartView2;
import com.example.utils.DtUtils;
import com.example.utils.MyUtils;
import com.example.utils.ToastUtils;
import com.github.mikephil.charting.charts.LineChart;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;
import static com.example.Constants.isCell;
import static com.example.Constants.isRuing;
import static com.example.gij.base.App.context;
import static com.example.linechart.ChartView.list00;
import static com.example.linechart.ChartView2.initChart;
import static com.example.linechart.ChartView2.list0;
import static com.example.linechart.ChartView2.list1;
import static com.example.linechart.ChartView2.list11;
import static com.example.linechart.ChartView2.list2;
import static com.example.utils.DtUtils.getBand;
import static com.example.utils.DtUtils.getBands;
import static com.example.utils.DtUtils.getGsmBand;

public class GijFragment2 extends Fragment implements View.OnClickListener {
    private int countMax=0;
    //???????????? ???????????????1????????????2
    private List<String> kaList = new ArrayList<>();
    private boolean linShowRe=true;
    private boolean isShowView=false;
    private ArrayList<CellBeanGSM> listLiShiGsm=new ArrayList<>();//??????????????????2G
    private ArrayList<CellBean> listLiShiLte=new ArrayList<>();//??????????????????4G 1
    private ArrayList<CellBean> listLiShiLte2=new ArrayList<>();//??????????????????4G 2
    private ArrayList<CellBeanNr> listLiShiNr=new ArrayList<>();//??????????????????5G
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Log.i("ylteeee", "handleMessage: ");
                if (kaList != null && kaList.size() > 0) {//???????????????
                    setNullData();//????????????????????????
                    Log.e("handler2", "handleMessage1: " + MainActivity.getPagerPosition() + "   " + cellBeanListCurrent.size());
                    setLiShiData();//???????????????????????????????????????????????????
                    if(MainActivity.getPagerPosition()==0){//??????????????????1
                    }else if(MainActivity.getPagerPosition()==1){//??????2
                        if(MyUtils.readSimState(getActivity())==1){//?????????????????????
                                if(listNR.size()>1){
                                    if(isAdded()){
                                        setNr(5);//??????5G
                                        Log.e("????????????", "????????????2: 5G");
                                    }
                                    return;
                                }
                                if(cellBeanListCurrent.size()>0){
                                    if(isAdded()){
                                        Log.e("????????????", "????????????2: 4G");
                                        setLte2(0, cellBeanList2, 4);
                                    }
                                    return;
                                }
                                if(listGsm.size()>1){
                                    if(isAdded()){
                                        setGsm(2);
                                        Log.e("????????????", "????????????2: 2G");
                                    }
                                    return;
                                }
                        }else if(MyUtils.readSimState(getActivity())==2){
                                    if(kaList.get(kaList.size()-1).equals("4G")){//???4G??????
                                            if(cellBeanListCurrent.size()>1){
                                                if(isAdded()){
                                                    Log.e("????????????", "??????2: 4G");
                                                    setLte2(1, cellBeanList2, 4);
                                                }
                                            }else if(cellBeanListCurrent.size()>0){
                                                if(isAdded()){
                                                    Log.e("????????????", "??????2: 4G");
                                                    setLte2(0, cellBeanList2, 4);
                                                }
                                            }

                                    }else if(kaList.get(kaList.size()-1).equals("5G")){//??????4G???????????????
                                        if(isAdded()){
                                            setNr(5);//??????5G
                                            Log.e("????????????", "??????2: 5G");
                                        }
                                    }else if(kaList.get(kaList.size()-1).equals("2G")){
                                        if(isAdded()){
                                            setGsm(2);
                                            Log.e("????????????", "??????2: 2G");
                                        }
                                    }else{
                                        setNullData();
                                    }
                        }
                    }
                }else{
                    setNullData();//?????????????????????
                }
            }
        }
    };
    private LinearLayout liner_rsSi,Linear_RSRP,Linear_SINR,Linear_RSRQ;
    private int RssiStat=0;
    private RelativeLayout xinhaoVisible;
    private LinearLayout xinhaoVisible2;
    private TextView tv_rssi,tv_RSRQ,tv_RSRP,tv_SINR,RSSP,RSSQ,RSSI;
    private ImageView iv_xinhao;
    private LineChart ChartView;

    private void setLiShi(int type,int i){
        if(type==5){
            if(listLiShiNr.size()>1){
                Collections.reverse(listLiShiNr);
                for (int i1 = 0; i1 < listLiShiNr.size(); i1++) {
                    if(i1>=49){
                        listLiShiNr.remove(i1);
                    }
                }
                adapter = new My4GAdapter(5, listLiShiNr, getActivity());
            }else{
                adapter = new My4GAdapter(4, listItem, getActivity());
            }
        }
        if(type==4){
            if(i==0){
                if(listLiShiLte.size()>1){
                    Collections.reverse(listLiShiLte);
                    for (int i1 = 0; i1 < listLiShiLte.size(); i1++) {
                        if(i1>=49){
                            listLiShiLte.remove(i1);
                        }
                    }
                    adapter = new My4GAdapter(4, listLiShiLte, getActivity());
                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    adapter = new My4GAdapter(4, listItem, getActivity());
                    recycler.setAdapter(adapter);
                }
            }

            if(i==1){
                if(listLiShiLte2.size()>1){
                    Collections.reverse(listLiShiLte2);
                    for (int i1 = 0; i1 < listLiShiLte2.size(); i1++) {
                        if(i1>=49){
                            listLiShiLte2.remove(i1);
                        }
                    }
                    adapter = new My4GAdapter(4, listLiShiLte2, getActivity());
                }else{
                    adapter = new My4GAdapter(4, listItem, getActivity());
                }
            }

        }
        if(type==2){
            if(listLiShiGsm.size()>1){
                Collections.reverse(listLiShiGsm);
                for (int i1 = 0; i1 < listLiShiGsm.size(); i1++) {
                    if(i1>=49){
                        listLiShiGsm.remove(i1);
                    }
                }
                adapter = new My4GAdapter(2, listLiShiGsm, getActivity());
            }else{
                adapter = new My4GAdapter(4, listItem, getActivity());
            }
        }
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    private void setLiShiData() {
        if (countMax <= 4) {
            if (listGsm.size() > 1) {//2G
                for (int i = 0; i < listGsm.size(); i++) {
                    if (countMax % 2 == 0) {
                        CellBeanGSM gsm = listGsm.get(i);
                        gsm.setI(countMax);
                        gsm.setCellShow(true);
                        listLiShiGsm.add(gsm);
                    } else {
                        CellBeanGSM gsm = listGsm.get(i);
                        gsm.setI(countMax);
                        gsm.setCellShow(false);
                        listLiShiGsm.add(gsm);
                    }
                }
            }
            if(listNR.size()>1){//5G
                for (int i = 0; i < listNR.size(); i++) {
                    if (countMax % 2 == 0) {
                        CellBeanNr gsm = listNR.get(i);

                        gsm.setI(countMax);

                        gsm.setCellShow(true);
                        listLiShiNr.add(gsm);
                    } else {
                        CellBeanNr gsm = listNR.get(i);
                        gsm.setI(countMax);
                        gsm.setCellShow(false);
                        listLiShiNr.add(gsm);
                    }
                }
            }

            if(cellBeanList.size()>1){//4G
                for (int i = 0; i < cellBeanList.size(); i++) {
                    if (countMax % 2 == 0) {
                        CellBean gsm = cellBeanList.get(i);
                        gsm.setI(countMax);

                        gsm.setCellLiShi(true);
                        listLiShiLte.add(gsm);
                    } else {
                        CellBean gsm = cellBeanList.get(i);
                        gsm.setI(countMax);
                        gsm.setCellLiShi(false);
                        listLiShiLte.add(gsm);
                    }
                }
                if (listLiShiLte.size() > 0) {
                    for (int i = 0; i < listLiShiLte.size(); i++) {
                        CellBean beanGSM = listLiShiLte.get(i);
                        if (beanGSM.getBand().equals("BAND")) {
                            listLiShiLte.remove(i);
                        }
                    }
                }
            }

            if(cellBeanList2.size()>1){//4G
                for (int i = 0; i < cellBeanList2.size(); i++) {
                    if (countMax % 2 == 0) {
                        CellBean gsm = cellBeanList2.get(i);
                        gsm.setI(countMax);
                        gsm.setCellLiShi(true);
                        listLiShiLte2.add(gsm);
                    } else {
                        CellBean gsm = cellBeanList2.get(i);
                        gsm.setI(countMax);
                        gsm.setCellLiShi(false);
                        listLiShiLte2.add(gsm);
                    }
                }
                if (listLiShiLte2.size() > 0) {
                    for (int i = 0; i < listLiShiLte2.size(); i++) {
                        CellBean beanGSM = listLiShiLte2.get(i);
                        if (beanGSM.getBand().equals("BAND")) {
                            listLiShiLte2.remove(i);
                        }
                    }
                }
            }
            countMax++;
        } else {
            countMax = 0;
        }
    }

    private Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public void run() {
            if(isRuing){
                Log.e("Mr.Y2", "run22222: =--------------------------------4G????????????"+cellBeanListCurrent.size());
                //????????????????????????
                Clear();
                //??????4G??????
                getDemo();
                Message message = Message.obtain();
                message.obj=kaList;
                message.what = 1;
                handler.sendMessage(message);
                Log.e("kalist", "kalist: "+kaList.toString());
                handler.postDelayed(this, 2000);
            }
        }
    };

    private void setGsm(int type) {
        if(listGsm.size()>1){
            //????????????
            tvLTE_LTE.setText("GSM");
            tvKey_tac.setText("LAC");
            tvKey_ci.setText("CI");
            tvKey_pci.setText("BSIC");
            tvKey_arfcn.setText("ARFCN");
            tvKey_band.setText("BAND");
            if(listGsm.get(1).getMncString()!=null){
                if(listGsm.get(1).getMncString().equals("00")||listGsm.get(1).getMncString().equals("02")){
                    tv_plmn.setText("460"+listGsm.get(1).getMncString()+"(????????????)");
                }else if(listGsm.get(1).getMncString().equals("11")){
                    tv_plmn.setText("460"+listGsm.get(1).getMncString()+"(????????????)");
                }else if(listGsm.get(1).getMncString().equals("01")){
                    tv_plmn.setText("460"+listGsm.get(1).getMncString()+"(????????????)");
                }else{
                    tv_plmn.setText("46000(????????????)");
                }
            }
            tv_tac.setText(listGsm.get(1).getLac());
            tv_eci.setText(listGsm.get(1).getCid());
            tv_pci.setText(listGsm.get(1).getBsic());
            tv_EARFCN.setText(listGsm.get(1).getArfcn());
            if(listGsm.get(1).getArfcn()!=null){
                tv_BAND.setText(getGsmBand(Integer.parseInt(listGsm.get(1).getArfcn())));
            }


            //???????????? ?????????
            setGSMQx();


            //?????????
            if(!isCell) {
                setLiShi(type, 0);
            }else
            if(listGsm.size()>2){
                listGsm.remove(1);
                adapter = new My4GAdapter(2,listGsm, getActivity());
            }else{
                adapter = new My4GAdapter(4,listItem, getActivity());
            }
            recycler.setAdapter(adapter);
        }else{
            setNullData();
        }
    }

private void setLte2(int i, ArrayList<CellBean> cellBeanList,int type) {//i ??????????????????i????????????????????????
            if(cellBeanListCurrent.size()>i){//??????????????????????????????????????????
                //??????????????????????????????
                tvKey_pci.setText("PCI");
                tvKey_arfcn.setText("EARFCN");
                tvKey_ci.setText("ECI");
                tvKey_tac.setText("TAC");
                tvKey_band.setText("BAND");
                tvLTE_LTE.setText("LTE");


                if (cellBeanListCurrent.get(i).getPlmn().equals("46000")||cellBeanListCurrent.get(i).getPlmn().equals("46002")) {
                    tv_plmn.setText(cellBeanListCurrent.get(i).getPlmn() + "(????????????)");
                } else if (cellBeanListCurrent.get(i).getPlmn().equals("46011")) {
                    tv_plmn.setText(cellBeanListCurrent.get(i).getPlmn() + "(????????????)");
                } else if (cellBeanListCurrent.get(i).getPlmn().equals("46001")) {
                    tv_plmn.setText(cellBeanListCurrent.get(i).getPlmn() + "(????????????)");
                } else {
                    tv_plmn.setText(cellBeanListCurrent.get(i).getPlmn());
                }
                if(cellBeanListCurrent.size()>i){
                    if(cellBeanListCurrent.get(i).getTac()!=null){
                        tv_tac.setText(cellBeanListCurrent.get(i).getTac());

                    }
                }
                if(cellBeanListCurrent.size()>i){
                    if(cellBeanListCurrent.get(i).getEci()!=null){
                        tv_eci.setText(DtUtils.getECI(cellBeanListCurrent.get(i).getEci()));
                    }
                }
                if(cellBeanListCurrent.size()>i){
                    if(cellBeanListCurrent.get(i).getPci()!=null){
                        tv_pci.setText(cellBeanListCurrent.get(i).getPci());
                    }
                }
                if(cellBeanListCurrent.size()>i){
                    if(cellBeanListCurrent.get(i).getEarfcn()!=null){
                        if (cellBeanListCurrent.get(i).getEarfcn().equals("0")) {
                            tv_EARFCN.setText("");
                        } else {
                            tv_EARFCN.setText(cellBeanListCurrent.get(i).getEarfcn());
                        }
                    }
                }

                if(cellBeanListCurrent.size()>i){
                    int band = 0;
                    if (cellBeanListCurrent.get(i).getBand() != null && !TextUtils.isEmpty(cellBeanListCurrent.get(i).getBand())) {
                        band = Integer.parseInt(cellBeanListCurrent.get(i).getBand());
                    }
                    if (band == 0) {
                        tv_BAND.setText("");
                    } else {
                        tv_BAND.setText(getBands(band));
                    }
                }

                //??????????????????
                Linear_RSRP.setVisibility(View.VISIBLE);
                Linear_RSRQ.setVisibility(View.VISIBLE);

                RSSI.setText("RSSI");
                RSSP.setText("RSRP");
                RSSQ.setText("RSRQ");
                tv_rssi.setText(cellBeanListCurrent.get(i).getRssi());
                tv_RSRP.setText(cellBeanListCurrent.get(i).getRsrp());
                tv_RSRQ.setText(cellBeanListCurrent.get(i).getRsrq());

                //?????????
                setLteQx(i);

                //????????????????????????
                if(!isCell)
                    setLiShi(type,i);
                else
                if (cellBeanList.size() > 1) {
                    adapter = new My4GAdapter(4, cellBeanList, getActivity());
                } else {
                    adapter = new My4GAdapter(4, listItem, getActivity());
                }
                recycler.setAdapter(adapter);
            }else{
                setNullData();
            }
    }

    private void setLteQx(int i) {
        initChart(context);
        if(RssiStat==0){
            //???????????????????????????
            RSSI.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_rssi.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSQ.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.colorJigBlack));

            ChartView2.num_max = -30;
            ChartView2.num_min = -120;
            list0.remove(0);
            list0.add(new Bean("",Float.parseFloat(cellBeanListCurrent.get(i).getRssi())));
            Log.e("TAGGGG", "setLte: "+list0.size());
            if (list0 != null && list0.size() > 0) {
                ChartView2.showLineChart(list0, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }else if(RssiStat==1){

            RSSP.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_RSRP.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSI.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSQ.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_rssi.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.colorJigBlack));

            ChartView2.num_max = -30;
            ChartView2.num_min = -120;
            list1.remove(0);
            list1.add(new Bean("",Float.parseFloat(cellBeanListCurrent.get(i).getRsrp())));
            if (list1 != null && list1.size() > 0) {
                ChartView2.showLineChart(list1, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }else if(RssiStat==2){
            //???????????????????????????
            RSSQ.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSI.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_rssi .setTextColor(getResources().getColor(R.color.colorJigBlack));

            ChartView2.num_max = -1;
            ChartView2.num_min = -20;
            initChart(context);
            list2.remove(0);
            list2.add(new Bean("",Float.parseFloat(cellBeanListCurrent.get(i).getRsrq())));
            if (list2 != null && list2.size() > 0) {
                ChartView2.showLineChart(list2, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }
    }

    private void setNr(int type) {
        //??????????????????????????????????????????
        if(listNR.size()>1){//?????????????????? ?????????
            tvKey_pci.setText("NR_PCI");
            tvKey_arfcn.setText("NR_ARFCN");
            tvKey_ci.setText("NR_CI");
            tvKey_tac.setText("NR_TAC");
            tvKey_band.setText("NR_BAND");
            tvLTE_LTE.setText("NR");
            if(listNR.get(1).getMncString()!=null){
                if (listNR.get(1).getMncString().equals("00")||listNR.get(1).getMncString().equals("02")) {
                    tv_plmn.setText("460"+listNR.get(1).getMncString() + "(????????????)");
                } else if (listNR.get(1).getMncString().equals("11")) {
                    tv_plmn.setText("460"+listNR.get(1).getMncString() + "(????????????)");
                } else if (listNR.get(1).getMncString().equals("01")) {
                    tv_plmn.setText("460"+listNR.get(1).getMncString() + "(????????????)");
                } else {
                    tv_plmn.setText("460"+listNR.get(1).getMncString());
                }
            }
            tv_tac.setText(listNR.get(1).getTac());
            tv_eci.setText(listNR.get(1).getCid());
            tv_pci.setText(listNR.get(1).getPci());
            if (listNR.get(1).getArfcn().equals("0")) {
                tv_EARFCN.setText("");
            } else {
                tv_EARFCN.setText(listNR.get(1).getArfcn());
            }
            Log.e("TAGNR", "setNr: "+listNR.get(1).getBand());
            if (listNR.get(1).getBand() != null && !TextUtils.isEmpty(listNR.get(1).getBand())) {
                tv_BAND.setText(listNR.get(1).getBand()+"");
            }
            //???????????????
            setNrQx();

            if(!isCell){//????????????
                setLiShi(type,0);
            }else
            if(listNR.size()>1){//??????????????????
                listNR.remove(1);
            }
            if (listNR.size() > 1) {
                Log.e("TAGF", "setNr: z??????");
                adapter = new My4GAdapter(5, listNR, getActivity());
            } else {
                adapter = new My4GAdapter(4, listItem, getActivity());
            }
            recycler.setAdapter(adapter);
            Log.e("????????????", "setNr: z??????"+adapter.getItemCount());
            adapter.notifyDataSetChanged();
        }else{
            setNullData();
        }

    }

    private void setNrQx() {
        Linear_RSRP.setVisibility(View.VISIBLE);
        Linear_RSRQ.setVisibility(View.VISIBLE);
        RSSI.setText("SS\nRSRP");
        RSSP.setText("SS\nRSRQ");
        RSSQ.setText("SS\nSINR");

        tv_rssi.setText(listNR.get(1).getRsrp());
        tv_RSRP.setText(listNR.get(1).getRsrq());
        tv_RSRQ.setText(listNR.get(1).getSsSinr());
        initChart(context);//??????????????????
        if(RssiStat==0){//rsrp
            //???????????????????????????
            RSSI.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_rssi.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSQ.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.colorJigBlack));


            ChartView2.num_max = -30;
            ChartView2.num_min = -160;
            list0.remove(0);
            list0.add(new Bean("",Float.parseFloat(listNR.get(1).getRsrp())));
            if (list0 != null && list0.size() > 0) {
                ChartView2.showLineChart(list0, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }else if(RssiStat==1){//rsrq

            RSSP.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_RSRP.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSI.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSQ.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_rssi.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.colorJigBlack));

            ChartView2.num_max = -10;
            ChartView2.num_min = -60;
            list1.remove(0);
            list1.add(new Bean("",Float.parseFloat(listNR.get(1).getRsrq())));
            if (list1 != null && list1.size() > 0) {
                ChartView2.showLineChart(list1, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }else if(RssiStat==2){//sinr
            //???????????????????????????
            RSSQ.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSI.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_rssi .setTextColor(getResources().getColor(R.color.colorJigBlack));

            ChartView2.num_max = 0;
            ChartView2.num_min = -30;
            initChart(context);
            list2.remove(0);
            list2.add(new Bean("",Float.parseFloat(listNR.get(1).getSsSinr())));
            if (list2 != null && list2.size() > 0) {
                ChartView2.showLineChart(list2, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }
    }
    private void setGSMQx() {
        RssiStat=0;//???????????????RXLEV
        Linear_RSRP.setVisibility(View.GONE);
        Linear_RSRQ.setVisibility(View.GONE);
        RSSI.setText("RXLEV");
        RSSP.setText("RSRP");
        RSSQ.setText("RSRQ");

        tv_rssi.setText(listGsm.get(1).getDbmRXL());
        tv_RSRP.setText("---");
        tv_RSRQ.setText("---");

        initChart(context);
        if(RssiStat==0){//rsrp
            //???????????????????????????
            RSSI.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_rssi.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSQ.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.colorJigBlack));

            ChartView2.num_max = -60;
            ChartView2.num_min = -120;
            list0.remove(0);
            list0.add(new Bean("",Float.parseFloat(listGsm.get(1).getDbmRXL())));
            if (list0 != null && list0.size() > 0) {
                ChartView2.showLineChart(list0, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }else if(RssiStat==1){//rsrq
            RSSP.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_RSRP.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSI.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSQ.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_rssi.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.colorJigBlack));

            ChartView2.num_max = -90;
            ChartView2.num_min = -110;
            list1.remove(0);
            list1.add(new Bean("",0));
            if (list1 != null && list1.size() > 0) {
                ChartView2.showLineChart(list1, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }else if(RssiStat==2){//sinr
            //???????????????????????????
            RSSQ.setTextColor(getResources().getColor(R.color.color_3853e8));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.color_3853e8));
            RSSP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSI.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_rssi .setTextColor(getResources().getColor(R.color.colorJigBlack));

            ChartView2.num_max = -90;
            ChartView2.num_min = -110;
            initChart(context);
            list2.remove(0);
            list2.add(new Bean("",0));
            if (list2 != null && list2.size() > 0) {
                ChartView2.showLineChart(list2, "??????", context.getResources().getColor(R.color.color_3853e8));
            }
        }
    }

    private void setNullData() {
        if(isAdded()){//activity???fragment??????????????????
            //???????????????????????????
            RSSI.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_rssi.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            RSSQ.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRP.setTextColor(getResources().getColor(R.color.colorJigBlack));
            tv_RSRQ.setTextColor(getResources().getColor(R.color.colorJigBlack));

            //????????????????????????
            liner_rsSi.setVisibility(View.VISIBLE);
            Linear_RSRQ.setVisibility(View.VISIBLE);
            Linear_RSRP.setVisibility(View.VISIBLE);

            RSSI.setText("RSSI");
            RSSP.setText("RSSP");
            RSSQ.setText("RSSQ");
            tv_rssi.setText("");
            tv_RSRP.setText("");
            tv_RSRQ.setText("");


            tvKey_pci.setText("PCI");
            tvKey_arfcn.setText("EARFCN");
            tvKey_ci.setText("ECI");
            tvKey_tac.setText("TAC");
            tvKey_band.setText("BAND");
            //?????????????????????????????????????????????
            tv_plmn.setText("");
            tv_tac.setText("");
            tv_eci.setText("");
            tv_pci.setText("");
            tv_EARFCN.setText("");
            tv_BAND.setText("");
            tvLTE_LTE.setText("");

            if (listItem.size() > 0) {
                adapter = new My4GAdapter(4, listItem, getActivity());
                recycler.setAdapter(adapter);
            }

//            //?????????????????????
//            ChartView2.num_max = -90;
//            ChartView2.num_min = -110;
//            ChartView2.initChart(context);
//            if (list11 != null && list11.size() > 0) {
//                ChartView2.showLineChart(list11, "??????", context.getResources().getColor(R.color.color_3853e8));
//            }
        }
    }

    private static String TAG = "BlankFragmentLTE";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private RecyclerView recycler;
    private ArrayList<CellBeanGSM> listGsm = new ArrayList<>();//2G
    private ArrayList<CellBeanNr> listNR =new ArrayList<>();//5G
    private ArrayList<CellBean> cellBeanList = new ArrayList<>();//?????????1???????????????
    private ArrayList<CellBean> cellBeanList2 = new ArrayList<>();//?????????2???????????????
    private ArrayList<CellBean> cellBeanListCurrent = new ArrayList<>();//????????????
    private ArrayList<CellBean> listItem = new ArrayList<>();
    private My4GAdapter adapter;
    private int tac;
    private int ci;
    private int earfcn;
    private String lteMccString="";
    private String lteMncString="";
    private int lteRsrp;
    private int lteRsrq;
    private int lteRssi;
    private int lteRssnr;
    private int lteEarfac;
    private int ltePci;
    private String lteBand="";
    private LinearLayoutManager manager;
    private ImageView iv_sjiao;
    private LinearLayout recyclerLayout;
    private int band;
    private TextView tvKey_arfcn,tvKey_band,tvKey_ci,tvKey_tac,tvKey_pci;
    public GijFragment2() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Mr.Y333", "onCreate: ");
    }
    View view;
    TextView tv_plmn, tv_tac, tv_pci, tv_eci, tv_EARFCN, tv_BAND,tvLTE_LTE,tv_linXq,tv_liShi,tv_addEarFcn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("Mr.Y333", "onCreateView: " );


        // Inflate the layout for this fragment
           View  view = inflater.inflate(R.layout.fragment_blank2, container, false);
        isShowView=true;//??????????????????
        initData(view);
        return view;
    }

    private void initData(View view) {
        findView(view);//????????????

        //??????????????????
        ChartView2.initData(ChartView,getActivity(),view);
        //?????????????????????????????????
        setOnClick();
        //??????????????????
        setCell();

        handler.post(runnable);


    }
    private void Clear() {
        /*4G ???*/
        cellBeanList.clear();
        cellBeanList2.clear();
        cellBeanListCurrent.clear();
        cellBeanList.add(new CellBean());
        cellBeanList2.add(new CellBean());
        /*4G ???*/



        /*????????????*/
//        listLiShi.clear();
//        listLiShi.add(new CellBeanGSM());
        /*????????????*/


        /*??????2 4 5G??????*/
        kaList.clear();//??????4 5G??????
        /*??????2 4 5G??????*/

        /*5G*/
        listNR.clear();//5G???????????????
        listNR.add(new CellBeanNr());
        /*5G*/

        /*2G*/
        listGsm.clear();
        listGsm.add(new CellBeanGSM());
        /*2G*/

    }


    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void getDemo() {
        //??????????????????????????????
        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<CellInfo> cellInfoList = manager.getAllCellInfo();
        for (CellInfo info : cellInfoList) {
            if (info.toString().contains("CellInfoLte")) {
                kaList.add("4G");
                Log.e("TAG", "getDemo: "+info.toString());
                @SuppressLint({"NewApi", "LocalSuppress"})
                CellInfoLte cellInfoLte = (CellInfoLte) info;
                @SuppressLint({"NewApi", "LocalSuppress"})
                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();

                tac = cellIdentityLte.getTac();
                ci= cellIdentityLte.getCi();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    lteMccString = cellIdentityLte.getMccString();
                    Log.i("ylt", "get4GDemo: mccString  "+ lteMccString);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    lteMncString = cellIdentityLte.getMncString();
                    Log.i("ylt", "get4GDemo: mncString  "+ lteMncString);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lteRsrp = cellInfoLte.getCellSignalStrength().getRsrp();
                    Log.i("ylt", "get4GDemo: rsrp  "+ lteRsrp);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lteRsrq = cellInfoLte.getCellSignalStrength().getRsrq();
                    Log.i("ylt", "get4GDemo: rsrq "+ lteRsrq);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    lteRssi = cellInfoLte.getCellSignalStrength().getRssi();
                    Log.i("ylt", "get4GDemo: rssi    "+ lteRssi);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    lteRssnr = cellInfoLte.getCellSignalStrength().getRssnr();
                    Log.i("ylt", "get4GDemo: rssnr    "+ lteRssnr);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    lteEarfac = cellIdentityLte.getEarfcn();
                    Log.i("ylt", "get4GDemo: earfcn"+ lteEarfac);
                }
                ltePci = cellIdentityLte.getPci();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    earfcn=cellIdentityLte.getEarfcn();
                }
                Log.i("ylt", "get4GDemo: pci"+ ltePci);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    String mobileNetworkOperator = cellIdentityLte.getMobileNetworkOperator();
                    Log.i("ylt", "get4GDemo: mobileNetworkOperator"+mobileNetworkOperator);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    lteBand = getBand(cellIdentityLte.getEarfcn());
                    Log.i("ylt", "get4GDemo: band"+ lteBand);
                }
                if (tac != 2147483647 && ci != 2147483647) {//?????????????????????  ?????????????????????????????? ?????????????????????
                    cellBeanListCurrent.add(new CellBean(lteMccString+lteMncString,tac+"",ci+"" ,ltePci+"",
                            earfcn+"", lteBand +"", lteRssi +"", lteRsrp +"",
                            lteRsrq +"",  "????????????", true));
                }else{
                    if(cellBeanListCurrent.size()==1){
                        cellBeanList.add(new CellBean(lteMccString+lteMncString,tac+"",ci+"" ,ltePci+"",
                                earfcn+"", lteBand +"", lteRssi +"", lteRsrp +"", lteRsrq +"",  "????????????", true));
                    }
                    if(cellBeanListCurrent.size()==2){
                        cellBeanList2.add(new CellBean(lteMccString+lteMncString,tac+"",ci+"" ,ltePci+"",
                                earfcn+"", lteBand +"", lteRssi +"", lteRsrp +"",
                                lteRsrq +"",  "????????????", true));
                    }
                }
            }else if(info.toString().contains("CellInfoNr")){
                kaList.add("5G");
                //??????5G????????????
                @SuppressLint({"NewApi", "LocalSuppress"})
                CellIdentityNr nr = (CellIdentityNr) ((CellInfoNr) info).getCellIdentity();
                @SuppressLint({"NewApi", "LocalSuppress"})
                CellSignalStrengthNr nrStrength = (CellSignalStrengthNr) ((CellInfoNr) info)
                        .getCellSignalStrength();
                CellBeanNr bean = new CellBeanNr();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    bean.setMncString(nr.getMncString());
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    bean.setTac(nr.getTac()+"");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    bean.setPci(nr.getPci()+"");
                }
                long ci = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ci = nr.getNci();
                }
                String cid = String.valueOf(ci);
                bean.setCid(cid);
                int nrarfcn = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    nrarfcn=nr.getNrarfcn();
                    bean.setArfcn(nr.getNrarfcn()+"");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    bean.setRsrp("-"+nrStrength.getSsRsrp()+"");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    bean.setRsrq("-"+nrStrength.getSsRsrq()+"");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    bean.setSsSinr(nrStrength.getSsSinr()+"");
                }
                Log.e("TAGNR", "getDemo: "+nrarfcn );

                int band = DtUtils.rangeInDefinedS(nrarfcn);
                Log.e("TAGNR", "getDemo: "+band );
                bean.setBand(band+"");
                listNR.add(bean);
            }else if(info instanceof CellInfoGsm){
                kaList.add("2G");
                CellInfoGsm infoGsm = (CellInfoGsm) info;
                CellSignalStrengthGsm strength = infoGsm.getCellSignalStrength();
                CellIdentityGsm gsm = infoGsm.getCellIdentity();

                CellBeanGSM gsm1 = new CellBeanGSM();
                gsm1.setAddArfcn("????????????");
                gsm1.setDbmRXL(strength.getDbm()+"");
                gsm1.setBand(strength.getDbm()+"");
                gsm1.setLac(gsm.getLac()+"");
                gsm1.setMncString(gsm.getMncString()+"");
                gsm1.setBsic(gsm.getBsic()+"");
                gsm1.setArfcn(gsm.getArfcn()+"");
                gsm1.setCid(gsm.getCid()+"");
                gsm1.setCellShow(true);


//                Log.i("ylt2G", "getBitErrorRate: "+strength.getBitErrorRate());
                Log.i("ylt2G", "getAsuLevel: "+strength.getAsuLevel());
                Log.i("ylt2G", "getDbm: "+strength.getDbm());
                Log.i("ylt2G", "getLevel: "+strength.getLevel());
                Log.i("ylt2G", "getTimingAdvance: "+strength.getTimingAdvance());
                Log.i("ylt2G", "getLac: "+gsm.getLac());
                Log.i("ylt2G", "getCid: "+gsm.getCid());
                Log.i("ylt2G", "getBsic: "+gsm.getBsic());
                Log.i("ylt2G", "getArfcn: "+gsm.getArfcn());
                Log.i("ylt2G", "getMncString: "+gsm.getMncString());
                listGsm.add(gsm1);

            }
        }
    }


    private void setOnClick() {
        tv_linXq.setOnClickListener(this);//?????????
        tv_liShi.setOnClickListener(this);//????????????
        tv_addEarFcn.setOnClickListener(this);//???????????? ????????????
        iv_sjiao.setOnClickListener(this);//???????????????

        iv_xinhao.setOnClickListener(this);//????????????

        Linear_SINR.setOnClickListener(this);
        Linear_RSRP.setOnClickListener(this);
        Linear_RSRQ.setOnClickListener(this);
        liner_rsSi.setOnClickListener(this);

        tv_SINR.setOnClickListener(this);
        tv_RSRQ.setOnClickListener(this);
        tv_RSRP.setOnClickListener(this);
        tv_rssi.setOnClickListener(this);


    }

    private void setCell() {
        //??????????????????????????? ?????????????????????
        listItem.clear();
        cellBeanList.clear();
        cellBeanList2.clear();
        cellBeanListCurrent.clear();
        listLiShiGsm.clear();
        listLiShiNr.clear();
        listLiShiLte.clear();
        listLiShiLte2.clear();

        listItem.add(new CellBean());
//        listLiShiGsm.add(new CellBeanGSM());//2G????????????
//        listLiShiNr.add(new CellBeanNr());//5G????????????
//        listLiShiLte.add(new CellBean());//4G????????????
//        listLiShiLte2.add(new CellBean());//4G????????????2
        manager = new RecyclerViewNoBugLinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(manager);//??????Recycler????????????
        adapter = new My4GAdapter(4,listItem, getActivity());
        recycler.setAdapter(adapter);
    }



    private void findView(View view) {
        recycler = view.findViewById(R.id.recycler);//???????????????
        tv_plmn = view.findViewById(R.id.tv_plmn);//?????????
        tv_tac = view.findViewById(R.id.tv_tac);
        tv_eci = view.findViewById(R.id.tv_eci);
        tv_pci = view.findViewById(R.id.tv_pci);
        tv_EARFCN = view.findViewById(R.id.tv_EARFCN);
        tv_BAND = view.findViewById(R.id.tv_BAND);
        tvLTE_LTE = view.findViewById(R.id.tvLTE_LTE);
        tv_linXq = view.findViewById(R.id.tv_linXq);//?????????
        tv_liShi = view.findViewById(R.id.tv_liShi);//????????????
        tv_addEarFcn = view.findViewById(R.id.tv_addEarFcn);//???????????? ????????????
        //???????????? ????????????
        iv_sjiao = view.findViewById(R.id.iv_sjiao);//?????????????????????
        recyclerLayout = view.findViewById(R.id.recyclerLayout);//?????????Recycler??????

        tvKey_arfcn = view.findViewById(R.id.tvKey_Arfcn);
        tvKey_band = view.findViewById(R.id.tvKey_band);
        tvKey_ci = view.findViewById(R.id.tvKey_ci);
        tvKey_pci = view.findViewById(R.id.tvKey_pci);
        tvKey_tac = view.findViewById(R.id.tvKey_tac);



        //????????????
        liner_rsSi = view.findViewById(R.id.liner_RSSi);
        Linear_RSRP = view.findViewById(R.id.Linear_RSRP);
        Linear_RSRQ = view.findViewById(R.id.Linear_RSRQ);
        Linear_SINR = view.findViewById(R.id.Linear_SINR);
        tv_rssi = view.findViewById(R.id.tv_RSSI);
        tv_RSRQ = view.findViewById(R.id.tv_RSRQ);
        tv_RSRP = view.findViewById(R.id.tv_RSRP);
        RSSP = view.findViewById(R.id.RSSP);
        RSSQ = view.findViewById(R.id.RSSQ);
        RSSI = view.findViewById(R.id.RSSI);
        tv_SINR = view.findViewById(R.id.tv_SINR);
        iv_xinhao = view.findViewById(R.id.iv_xinhao);
        xinhaoVisible = view.findViewById(R.id.xinhaoVisible);
        xinhaoVisible2 = view.findViewById(R.id.xinhaoVisible2);

        //???????????????
        ChartView = view.findViewById(R.id.chartview);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isShowView&&isVisibleToUser){
            isCell=true;//??????????????????????????????
            tv_linXq.setTextColor(getResources().getColor(R.color.colorJigTextColor));
            tv_liShi.setTextColor(getResources().getColor(R.color.colorJigBlack));
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_linXq://?????????
                isCell=true;//?????????????????????
                tv_linXq.setTextColor(getResources().getColor(R.color.colorJigTextColor));
                tv_liShi.setTextColor(getResources().getColor(R.color.colorJigBlack));
                break;
            case R.id.tv_liShi://????????????
                isCell=false;//????????????????????????
                tv_liShi.setTextColor(getResources().getColor(R.color.colorJigTextColor));
                tv_linXq.setTextColor(getResources().getColor(R.color.colorJigBlack));
                break;
            case R.id.tv_addEarFcn://???????????? ????????????
                DtUtils.showDialog(getActivity(), "???????????????????????????", new DtUtils.ShowDialog() {
                    @Override
                    public void showDialog(View view,String type) {
                        if(type.equals("??????")){
                            if(MainActivity.getPagerPosition()==0){//???????????????
                                if(cellBeanListCurrent.size()>0){//
                                    //?????????1?????????
                                    try {
                                        DBManager dbManager = new DBManager(getActivity());
                                        if(dbManager.getCellBeanList().size()>0){
                                            for (CellBean cellBean : dbManager.getCellBeanList()) {
                                                dbManager.deleteCellBean(cellBean);
                                            }
                                        }
                                        dbManager.insertCellBean(cellBeanListCurrent.get(0));

                                        List<CellBean> list = dbManager.getCellBeanList();
                                        Log.e("TAG", "???????????????:"+list.toString());
                                        if(list.size()>0){
                                            ToastUtils.showToast("????????????");
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else if(MainActivity.getPagerPosition()==1){//???????????????
                                if(cellBeanListCurrent.size()==2){//?????????
                                    //?????????2?????????
                                    try {
                                        DBManager dbManager = new DBManager(getActivity());
                                        if(dbManager.getCellBeanList().size()>0){
                                            for (CellBean cellBean : dbManager.getCellBeanList()) {
                                                dbManager.deleteCellBean(cellBean);
                                            }
                                        }
                                        dbManager.insertCellBean(cellBeanListCurrent.get(0));
                                        List<CellBean> list = dbManager.getCellBeanList();
                                        Log.e("TAG", "???????????????:"+list.toString());

                                        if(list.size()>0){
                                            ToastUtils.showToast("????????????");
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    ToastUtils.showToast("????????????");
                                }
                            }
                        }else if(type.equals("??????")){}
                    }
                });
                break;
            case R.id.iv_sjiao:
                if(linShowRe){
                    recyclerLayout.setVisibility(View.VISIBLE);
                    linShowRe=false;
                }else{
                    recyclerLayout.setVisibility(View.GONE);
                    linShowRe=true;
                }
                break;
            case R.id.tv_RSRP:
                break;
            case R.id.tv_RSRQ:
                break;
            case R.id.tv_RSSI:
                break;
            case R.id.tv_SINR:
                break;


            case R.id.Linear_RSRP:
                RssiStat=1;//???????????????
                break;
            case R.id.Linear_RSRQ:
                RssiStat=2;
                break;
            case R.id.liner_RSSi:
                RssiStat=0;
                break;
            case R.id.Linear_SINR:
                RssiStat=3;
                break;
            case R.id.iv_xinhao://????????????
                if(xinhaoVisible.getVisibility()==0){//??????
                    xinhaoVisible.setVisibility(View.GONE);
                    xinhaoVisible2.setVisibility(View.GONE);
                }else{
                    xinhaoVisible.setVisibility(View.VISIBLE);
                    xinhaoVisible2.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    @Override
    public void onDestroyView() {
        Log.e("Mr.Y333", "onDestroyView: " );
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(handler!=null){
            handler.post(runnable);
        }
        Log.e("Mr.Y333", "onResume: " );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Mr.Y333", "onStart: " );

    }

    @Override
    public void onPause() {
        super.onPause();
        if(handler!=null){
            handler.removeCallbacks(runnable);
        }
        Log.e("Mr.Y333", "onPause: " );
    }

    @Override
    public void onStop() {        Log.e("Mr.Y333", "onStop: " );

        super.onStop();
    }

    @Override
    public void onDestroy() {        Log.e("Mr.Y333", "onDestroy: " );

        super.onDestroy();
    }

    @Override
    public void onDetach() {        Log.e("Mr.Y333", "onDetach: " );
        super.onDetach();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Log.e("Mr.Y333", "onAttach:" );
        super.onAttach(context);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("Mr.Y333", "onViewCreated: " );
    }
}