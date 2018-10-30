package work.wang.mynews;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import work.wang.mynews.fragment.thirdDayFragment;
import work.wang.mynews.fragment.todayFragment;
import work.wang.mynews.fragment.tomorrowFragment;


public class weather extends FragmentActivity implements View.OnClickListener {
    private EditText et_city;
    private Button btn_city;
    private List<Fragment> fragmentList;
    private List<WeatherInfo> weatherInfos=null;
    private WeatherInfo weatherInfo=null;
    private android.os.Handler handler;
    String xmlUrl;//默认佛山天气api
    String city;//城市名
    boolean come=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main);
        Intent intent=getIntent();
        xmlUrl=intent.getStringExtra("city");
        city=intent.getStringExtra("s_city");
        et_city=(EditText)findViewById(R.id.weather_et);
        btn_city=(Button) findViewById(R.id.weather_btn);
        btn_city.setOnClickListener(this);
        weatherInfos=new ArrayList<>();
        attainWeatherInfo();
        handler=new android.os.Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == 1) {
                    fragmentList=new ArrayList<Fragment>();
                    fragmentList.add(new todayFragment());
                    fragmentList.add(new tomorrowFragment());
                    fragmentList.add(new thirdDayFragment());
                    FragAdapter adapter=new FragAdapter(getSupportFragmentManager(),fragmentList);
                    ViewPager vp=(ViewPager)findViewById(R.id.weather_viewpager);
                    vp.setAdapter(adapter);
                }
                if(msg.what==2){
                    Toast.makeText(weather.this,"请检查输入是否有误",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==3){
                    Toast.makeText(weather.this,"请打开网络",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.weather_btn:
                xmlUrl=et_city.getText().toString().trim();
                if(xmlUrl.length()>4){
                    Toast.makeText(weather.this,"格式错误，如（北京）",Toast.LENGTH_SHORT).show();
                    break;
                }
                city=xmlUrl;
                Intent intent=new Intent(this,weather.class);
                intent.putExtra("city","http://wthrcdn.etouch.cn/WeatherApi?city="+xmlUrl);
                intent.putExtra("s_city",city);
                startActivity(intent);
                finish();
                break;
        }
    }

    //为viewpager添加适配器
    public class FragAdapter extends FragmentPagerAdapter{
        public FragAdapter(FragmentManager fm,List<Fragment> fragments){
            super(fm);
            fragmentList=fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

    }
    //获取天气信息
    private void attainWeatherInfo(){
        Thread t=new Thread(new Runnable() {
            HttpURLConnection connection;
            Boolean status=false;//判断输入的天气城市是否有误；
            @Override
            public void run() {
                try{
                    if (come){
                        weatherInfos.clear();
                        come=false;
                    }
                    int i=0;//设置标志位
                    URL httpUrl = new URL(xmlUrl);
                    connection = (HttpURLConnection) httpUrl.openConnection();
                    connection.setReadTimeout(3000);
                    connection.setRequestMethod("GET");
                    int code =connection.getResponseCode();
                    if(code==200){
                        InputStream in = connection.getInputStream();
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser parser = factory.newPullParser();
                        parser.setInput(in,"utf-8");
                        int type=parser.getEventType();
                        while (type != XmlPullParser.END_DOCUMENT){
                            switch (type){
                                case XmlPullParser.START_TAG:
                                    if ("weather".equals(parser.getName())){
                                        status=true;
                                        weatherInfo=new WeatherInfo();
                                    }
                                    if("date".equals(parser.getName())){
                                        String date=parser.nextText();
                                        weatherInfo.setDate(date);
                                    }
                                    if("high".equals(parser.getName())){
                                        String high=parser.nextText();
                                        weatherInfo.setHigh(high);
                                    }
                                    if("low".equals(parser.getName())){
                                        String low=parser.nextText();
                                        weatherInfo.setLow(low);
                                    }
                                    if("day".equals(parser.getName())) {
                                        i=1;
                                    }
                                    if("type".equals(parser.getName()) && i==1){
                                        String w_t=parser.nextText();
                                        weatherInfo.setWhite_type(w_t);
                                    }
                                    if("fengxiang".equals(parser.getName()) && i==1){
                                        String w_fx=parser.nextText();
                                        weatherInfo.setWhite_fengxiang(w_fx);
                                    }
                                    if("fengli".equals(parser.getName()) && i==1){
                                        String w_fl=parser.nextText();
                                        weatherInfo.setWhite_fengli(w_fl);
                                    }
                                    if("night".equals(parser.getName())) {
                                        i=2;
                                    }
                                    if("type".equals(parser.getName()) && i==2){
                                        String b_t=parser.nextText();
                                        weatherInfo.setBlack_type(b_t);
                                    }
                                    if("fengxiang".equals(parser.getName()) && i==2){
                                        String b_fx=parser.nextText();
                                        weatherInfo.setBlack_fengxiang(b_fx);
                                    }
                                    if("fengli".equals(parser.getName()) && i==2){
                                        String b_fl=parser.nextText();
                                        weatherInfo.setBlack_fengli(b_fl);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if("weather".equals(parser.getName())){
                                        weatherInfos.add(weatherInfo);
                                        weatherInfo=null;
                                    }
                                    break;
                            }
                            type=parser.next();
                        }
                        if(status){
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }else {
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    }else {
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
                connection.disconnect();
            }
        });
        t.start();
    }
    public List<WeatherInfo> info(){
        return weatherInfos;
    }
    public String city(){
        return city;
    }
}

