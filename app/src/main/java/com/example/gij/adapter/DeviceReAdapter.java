package com.example.gij.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gij.OrmSqlLite.Bean.DevicePdBean;
import com.example.gij.R;
import com.example.utils.ToastUtils;

import java.util.ArrayList;

/**
 * @author: 小杨同志
 * @date: 2021/9/1
 */
public class DeviceReAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<DevicePdBean> pdBeans;
    public DeviceReAdapter(ArrayList<DevicePdBean> pdBeans, Context context) {
        this.context=context;
        this.pdBeans=pdBeans;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.device_recycler_item, null);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MyHolder myHolder= (MyHolder) holder;
            myHolder.tv_band.setText(pdBeans.get(position).getBand());
            myHolder.tv_EARFCN.setText(pdBeans.get(position).getEarfcn());
            myHolder.tv_eci.setText(pdBeans.get(position).getEci());
            myHolder.tv_item.setText(pdBeans.get(position).getPosition());
            myHolder.tv_plmn.setText(pdBeans.get(position).getPlmn());
            myHolder.tv_tac.setText(pdBeans.get(position).getTac());
            myHolder.tv_rsrp.setText(pdBeans.get(position).getRsrp());
            myHolder.tv_rsrq.setText(pdBeans.get(position).getRsrq());
            myHolder.tv_addEarFcn.setOnClickListener(view -> ToastUtils.showToast("频点"));

    }

    @Override
    public int getItemCount() {
        return pdBeans.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        private final TextView tv_rsrp;
        private final TextView tv_band;
        private final TextView tv_EARFCN;
        private final TextView tv_eci;
        private final TextView tv_pci;
        private final TextView tv_tac;
        private final TextView tv_item;
        private final TextView tv_plmn;
        private final TextView tv_rsrq;
        private final TextView tv_addEarFcn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_rsrp = itemView.findViewById(R.id.tv_rsrp);
            tv_rsrq=itemView.findViewById(R.id.tv_rsrq);
            tv_band=itemView.findViewById(R.id.tv_band);
            tv_EARFCN=itemView.findViewById(R.id.tv_EARFCN);
            tv_eci=itemView.findViewById(R.id.tv_eci);
            tv_pci=itemView.findViewById(R.id.tv_pci);
            tv_tac=itemView.findViewById(R.id.tv_tac);
            tv_plmn=itemView.findViewById(R.id.tv_plmn);
            tv_item=itemView.findViewById(R.id.tv_item);
            tv_addEarFcn=itemView.findViewById(R.id.tv_addEarFcn);
        }
    }
}
