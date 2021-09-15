package com.example.gij.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.gij.fragment.DeviceSettingFragment;
import com.example.gij.fragment.GijFragment1;
import com.example.gij.fragment.GijFragment2;
import com.example.gij.R;
import com.example.gij.viewpager.JigPagerAdapter;
import com.example.utils.MyUtils;
import com.example.utils.TitleBar;
import com.example.utils.ToastUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ViewPager vp;
    private ImageView iv_settings;//标题栏 设置按钮
    private TextView tv_title;//标题栏 头部标题
    private TitleBar titleBar;//标题栏
    private ImageView iv_scrollbar1,iv_scrollbar2,iv_scrollbar3;//下划线
    private TextView tv_Device,tv_SimI1,tv_SimI2;//卡槽字体
    private RelativeLayout re_simI1,re_simI2,re_device;//卡槽界面
    private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();;//用来装载Fragment
    private static  int position=0;//viewpager滑动的下标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleBar = findViewById(R.id.title);
        tv_title = titleBar.findViewById(R.id.tv_title);
        iv_settings = titleBar.findViewById(R.id.iv_settings);
        vp = findViewById(R.id.Vp);
        re_device = findViewById(R.id.re_Device);
        re_simI1 = findViewById(R.id.re_SimI1);
        re_simI2 = findViewById(R.id.re_SimI2);
        iv_scrollbar1 = findViewById(R.id.iv_scrollbar1);
        iv_scrollbar2 = findViewById(R.id.iv_scrollbar2);
        iv_scrollbar3 = findViewById(R.id.iv_scrollbar3);
        tv_SimI1 = findViewById(R.id.tv_SimI1);
        tv_SimI2 = findViewById(R.id.tv_SimI2);
        tv_Device = findViewById(R.id.tv_Device);




        tv_title.setOnClickListener(this);//标题监听
        iv_settings.setOnClickListener(this);//选项监听
        re_simI1.setOnClickListener(this);//卡槽1监听
        re_simI2.setOnClickListener(this);//卡槽2监听
        re_device.setOnClickListener(this);//设备监听

        MyUtils.getPermissions(this);

        setDataVpAndButton();//ViewPager和Button联动
    }

    public static int  getPagerPosition(){
        Log.e(TAG, "getPagerPosition: "+position );
        return position;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 授予权限
                //处理授权之后逻辑
                ToastUtils.showToast("权限通过");
            } else {
                ToastUtils.showToast("没有权限");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void setDataVpAndButton() {
        //创建Fragment
        fragmentArrayList.add(new GijFragment1());
        fragmentArrayList.add(new GijFragment2());
        fragmentArrayList.add(new DeviceSettingFragment());
        vp.setAdapter(new JigPagerAdapter(getSupportFragmentManager(),fragmentArrayList));
        //默认卡槽1选中状态
        iv_scrollbar2.setVisibility(View.GONE);
        iv_scrollbar3.setVisibility(View.GONE);
        tv_SimI1.setTextColor(getResources().getColor(R.color.colorJigTextColor));
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //ViewPager 这个方法会在屏幕滚动过程中不断被调用
            }
            @Override
            public void onPageSelected(int position) {
                MainActivity.this.position=position;
                //代表哪个页面被选中  position下标
                if(position==0){
                    //设置字体颜色
                    tv_SimI1.setTextColor(getResources().getColor(R.color.colorJigTextColor));
                    tv_SimI2.setTextColor(getResources().getColor(R.color.colorJigTextColorCancel));
                    tv_Device.setTextColor(getResources().getColor(R.color.colorJigTextColorCancel));
                    //下划线显示隐藏
                    iv_scrollbar1.setVisibility(View.VISIBLE);
                    iv_scrollbar2.setVisibility(View.GONE);
                    iv_scrollbar3.setVisibility(View.GONE);
                }else if(position==1){
                    //设置字体颜色
                    tv_SimI1.setTextColor(getResources().getColor(R.color.colorJigTextColorCancel));
                    tv_SimI2.setTextColor(getResources().getColor(R.color.colorJigTextColor));
                    tv_Device.setTextColor(getResources().getColor(R.color.colorJigTextColorCancel));
                    //下划线隐藏
                    iv_scrollbar2.setVisibility(View.VISIBLE);
                    iv_scrollbar1.setVisibility(View.GONE);
                    iv_scrollbar3.setVisibility(View.GONE);
                }else if(position==2){
                    //设置字体颜色
                    tv_SimI1.setTextColor(getResources().getColor(R.color.colorJigTextColorCancel));
                    tv_SimI2.setTextColor(getResources().getColor(R.color.colorJigTextColorCancel));
                    tv_Device.setTextColor(getResources().getColor(R.color.colorJigTextColor));
                    //下划线隐藏
                    iv_scrollbar3.setVisibility(View.VISIBLE);
                    iv_scrollbar1.setVisibility(View.GONE);
                    iv_scrollbar2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //这个方法在手指操作屏幕的时候发生变化  手指按下为1 抬起时滑动了为2
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_title://标题
                Toast.makeText(this, "你点击了标题", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_settings://选项
                startActivity(new Intent(this, MainActivity2.class));
                Toast.makeText(this, "你点击了图片", Toast.LENGTH_SHORT).show();
                break;
            case R.id.re_SimI1://卡槽1
                //点击卡槽1将其他的按钮下划线隐藏
                iv_scrollbar1.setVisibility(View.VISIBLE);
                iv_scrollbar2.setVisibility(View.GONE);
                iv_scrollbar3.setVisibility(View.GONE);

                vp.setCurrentItem(0);//设置当前ViewPager的条目
                this.position=0;
                break;
            case R.id.re_SimI2://卡槽2
                //点击卡槽2将其他的按钮下划线隐藏
                iv_scrollbar2.setVisibility(View.VISIBLE);
                iv_scrollbar1.setVisibility(View.GONE);
                iv_scrollbar3.setVisibility(View.GONE);

                vp.setCurrentItem(1);//设置当前ViewPager的条目
                this.position=1;
                break;
            case R.id.re_Device://设备
                //点击设备将其他的按钮下划线隐藏
                iv_scrollbar3.setVisibility(View.VISIBLE);
                iv_scrollbar1.setVisibility(View.GONE);
                iv_scrollbar2.setVisibility(View.GONE);
                vp.setCurrentItem(2);//设置当前ViewPager的条目
                this.position=2;
                break;
        }
    }
}