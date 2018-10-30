package work.wang.mynews.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import work.wang.mynews.R;
import work.wang.mynews.WeatherInfo;
import work.wang.mynews.weather;

import static android.R.id.list;

/**
 * A simple {@link Fragment} subclass.
 */
public class todayFragment extends Fragment {
    TextView city,date,high,low,white_type,white_fengxiang,white_fengli,black_type,black_fengxiang,black_fengli;
    String s_city,s_date,s_high,s_low,s_w_type,s_w_fengxiang,s_w_fengli,s_b_type,s_b_fengxiang,s_b_fengli;
    List<WeatherInfo> infoList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_today, container, false);
        init(view);
        initView();
        if(s_w_type.equals("小雨") || s_w_type.equals("中雨") || s_w_type.equals("大雨") || s_w_type.equals("小到中雨") || s_w_type.equals("中到大雨")){
            view.setBackgroundResource(R.drawable.rain);
        }else if(s_w_type.equals("多云")){
            view.setBackgroundResource(R.drawable.yun);
        }else if(s_w_type.equals("晴")){
            view.setBackgroundResource(R.drawable.qing);
        }else if(s_w_type.equals("阴")){
            view.setBackgroundResource(R.drawable.ying);
        }else {
            view.setBackgroundResource(R.drawable.el);
        }
        return view;
    }
    public void init(View v ){
        city=(TextView)v.findViewById(R.id.today_tv);
        date=(TextView)v.findViewById(R.id.today_date);
        high=(TextView)v.findViewById(R.id.today_high);
        low=(TextView)v.findViewById(R.id.today_low);
        white_type=(TextView)v.findViewById(R.id.today_white_type);
        white_fengxiang=(TextView)v.findViewById(R.id.today_white_fengxiang);
        white_fengli=(TextView)v.findViewById(R.id.today_white_fengli);
        black_type=(TextView)v.findViewById(R.id.today_black_type);
        black_fengxiang=(TextView)v.findViewById(R.id.today_black_fengxiang);
        black_fengli=(TextView)v.findViewById(R.id.today_black_fengli);
    }
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        s_city=((weather)activity).city();
        infoList = ((weather) activity).info();
        s_date=infoList.get(0).getDate().toString();
        s_high=infoList.get(0).getHigh().toString();
        s_low=infoList.get(0).getLow().toString();
        s_w_type=infoList.get(0).getWhite_type().toString();
        s_w_fengxiang=infoList.get(0).getWhite_fengxiang().toString();
        s_w_fengli=infoList.get(0).getWhite_fengli().toString();
        s_b_type=infoList.get(0).getBlack_type().toString();
        s_b_fengxiang=infoList.get(0).getBlack_fengxiang();
        s_b_fengli=infoList.get(0).getBlack_fengli();
    }
    public void initView(){
        city.setText(s_city);
        date.setText(s_date);
        high.setText(s_high);
        low.setText(s_low);
        white_type.setText("类型: "+s_w_type);
        white_fengxiang.setText("风向: "+s_w_fengxiang);
        white_fengli.setText("风力: "+s_w_fengli);
        black_type.setText("类型: "+s_b_type);
        black_fengxiang.setText("风向: "+s_b_fengxiang);
        black_fengli.setText("风力: "+s_b_fengli);
    }
}
