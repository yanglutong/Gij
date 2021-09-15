package com.example.gij.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gij.OrmSqlLite.Bean.DevicePdBean;
import com.example.gij.R;
import com.example.gij.adapter.DeviceReAdapter;
import com.example.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;



/*设备界面*/
public class DeviceSettingFragment extends Fragment implements View.OnClickListener {
    private List<String> list = new ArrayList<String>();
    private TextView textview;
    private Spinner spinnertext;
    private ArrayAdapter<String> adapter;

    private boolean isShowView=false;
    private LinearLayout liner_deviceOption;
    private TextView cell_search;
    private RecyclerView recycler;

    public DeviceSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank3, container, false);
        isShowView=true;
        initData(view);
        return view;
    }

    private void initData(View view) {
        //初始化spinner
        initSpinner(view);
        //查找控件
        findView(view);
        //控件监听
        setOnClick();
        //加载数据
        setData();
    }

    private void setOnClick() {
        cell_search.setOnClickListener(this);
    }

    private void setData() {
        //添加十条假数据
        ArrayList<DevicePdBean> pdBeans = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            pdBeans.add(new DevicePdBean("46000", "12795", "-68", "pci", "38950", "40", "45", "12", ""+i));
        }
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(new DeviceReAdapter(pdBeans,getActivity()));
    }

    private void findView(View view) {
        cell_search = view.findViewById(R.id.cell_search);
        recycler = view.findViewById(R.id.recycler);
    }


    private void initSpinner(View view){
        list.clear();
        //第一步：定义下拉列表内容
        list.add("移动");
        list.add("联通");
        list.add("电信");
        spinnertext = (Spinner) view.findViewById(R.id.spinner1);
        //第二步：为下拉列表定义一个适配器
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        //第三步：设置下拉列表下拉时的菜单样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinnertext.setAdapter(adapter);
        //第五步：添加监听器，为下拉列表设置事件的响应
        spinnertext.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO Auto-generated method stub
                /* 将所选spinnertext的值带入myTextView中*/
//                textview.setText("你的血型是:" + adapter.getItem(i));
                /* 将 spinnertext 显示^*/
                adapterView.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> argO) {
                // TODO Auto-generated method stub
                textview.setText("NONE");
                argO.setVisibility(View.VISIBLE);
            }
        });
//        //将spinnertext添加到OnTouchListener对内容选项触屏事件处理
//        spinnertext.setOnTouchListener(new Spinner.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                // 将mySpinner隐藏
//                v.setVisibility(View.INVISIBLE);
//                Log.i("spinner", "Spinner Touch事件被触发!");
//                return false;
//            }
//        });
//        //焦点改变事件处理
//        spinnertext.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                v.setVisibility(View.VISIBLE);
//                Log.i("spinner", "Spinner FocusChange事件被触发！");
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cell_search:
                ToastUtils.showToast("小区搜索");
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isShowView&&isVisibleToUser){
        }
    }
}