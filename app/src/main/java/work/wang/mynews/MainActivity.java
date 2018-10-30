package work.wang.mynews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Message;
import android.provider.DocumentsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import work.wang.mynews.adapter.news_adapter;
import work.wang.mynews.adapter.per_center_adapter;
import work.wang.mynews.dialog.city_dialog;
import work.wang.mynews.dialog.read_dialog;
import work.wang.mynews.dialog.update_dialog;
import work.wang.mynews.dialog.url_dialog;

import static android.support.v7.widget.AppCompatDrawableManager.get;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_search;
    private Button btn_search;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<News> newsList;
    private ListView lv;
    private news_adapter adapter;
    private android.os.Handler handler;
    private String way="webView";//打开新闻的方式，默认为webView
    private String search="";
    private String sort[]={"国内","国际","军事","财经","评论","体育","文化"};
    private Boolean income=false;//attainNews()方法中用到，用于控制清除newList；
    private Boolean come=true;//attainNews()方法中用到，用于控制清除newList；
    private long mExitTime;//按返回键退出的，保存前一个按下的时间
    private String jsoup_uri="http://world.cankaoxiaoxi.com/";
    private int min=10;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isNetworkAvailable(MainActivity.this)){
            showProgressDialog("提示", "正在加载......");
        }
        initView();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable(){
                    public void run(){
                            et_search.setText("");
                            income=true;
                            come=true;
                            search="";
                            attainNews();
                            if(isNetworkAvailable(MainActivity.this)){
                                Toast.makeText(MainActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MainActivity.this,"刷新失败",Toast.LENGTH_SHORT).show();
                            }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
        newsList=new ArrayList<>();
        btn_search.setOnClickListener(this);
        changeUri();
        attainNews();//获得新闻
        new Thread(new update_time()).start();
        handler=new android.os.Handler() {
            public void handleMessage(android.os.Message msg){
                if (msg.what==1){
                    hideProgressDialog();
                    adapter=new news_adapter(MainActivity.this,newsList);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            News n = newsList.get(position);
                            if (way.equals("webView")) {
                                Intent i = new Intent(MainActivity.this, news_webdisplay.class);
                                i.putExtra("uri", n.getNewsUrl());//给webView传网址
                                //Toast.makeText(MainActivity.this,n.getNewsUrl(),Toast.LENGTH_LONG).show();
                                startActivity(i);
                            }
                            if(way.equals("browser")){
                                Uri uri=Uri.parse(n.getNewsUrl());
                                Intent i=new Intent(Intent.ACTION_VIEW,uri);
                                startActivity(i);
                            }
                        }
                    });
                    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                       int position, long id) {
                            News n = newsList.get(position);
                            Intent send=new Intent();
                            send.setAction(Intent.ACTION_SEND);
                            send.putExtra(Intent.EXTRA_TEXT, n.getNewsUrl());
                            send.setType("text/plain");
                            startActivity(Intent.createChooser(send,"share to..."));
                            return true;
                        }

                    });

                }
                if(msg.what==2){
                    Toast.makeText(MainActivity.this,"网络不可连接,请检查网络是否打开",Toast.LENGTH_SHORT).show();
                }
                if(msg.what==3){
                    //Toast.makeText(MainActivity.this,"网络不可连接,请检查网络是否打开",Toast.LENGTH_SHORT).show();
                    hideProgressDialog();
                }
            }
        };
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);//这里是调用menu文件夹中的main.xml，在登陆界面label右上角的三角里显示其他功能
        return true;
    }
    //监听菜单项
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_read:
                read_dialog rd=new read_dialog(this,new read_dialog.radioSelectedListener(){
                    @Override
                    public void getSelected(String selected) {
                        way=selected;
                    }
                });
                rd.show();
                return true;
            case R.id.action_update:
                update_dialog ud=new update_dialog(this,new update_dialog.updateListener(){
                    public void getSelected(String selected){
                        min=Integer.parseInt(selected);
                    }
                });
                ud.show();
                return true;
            case R.id.action_weather:
                    Intent i=new Intent(MainActivity.this,weather.class);
                    i.putExtra("city","http://wthrcdn.etouch.cn/WeatherApi?city=佛山");
                    i.putExtra("s_city","佛山");
                    startActivity(i);
                return true;
            case R.id.action_broke_the_news:
                Intent it=new Intent(this,news_broke.class);
                startActivity(it);
                return true;
            case R.id.action_personal:
                final Intent intent=new Intent(this,per_center.class);
                startActivity(intent);
                return true;
            case R.id.action_url:
                url_dialog udg=new url_dialog(MainActivity.this,new url_dialog.urlListener(){
                    @Override
                    public void getSelected(String selected) {
                        Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                        if(selected.equals("http://china.cankaoxiaoxi.com")||selected.equals("http://world.cankaoxiaoxi.com")
                                ||selected.equals("http://finance.cankaoxiaoxi.com/")){
                            intent1.putExtra("uri",selected);
                            startActivity(intent1);
                        }else {

                        }
                    }
                });
                udg.show();
                return true;
            case R.id.action_sort:
                AlertDialog.Builder incomeAlertDialog=new AlertDialog.Builder(this);
                incomeAlertDialog.setSingleChoiceItems(sort, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        // which表示点击的条目
                        Object checkedItem = lw.getAdapter().getItem(which);
                        // 既然你没有cancel或者ok按钮，所以需要在点击item后使dialog消失
                        dialog.dismiss();
                        // 更新你的view
                        if(((String)checkedItem).equals("国内")){
                            Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                            intent1.putExtra("China","http://china.cankaoxiaoxi.com/");
                            startActivity(intent1);
                            finish();
                        }
                        if(((String)checkedItem).equals("国际")){
                            Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                            intent1.putExtra("world","http://world.cankaoxiaoxi.com/");
                            startActivity(intent1);
                            finish();
                        }
                        if(((String)checkedItem).equals("军事")){
                            /*Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                            intent1.putExtra("mil","http://mil.cankaoxiaoxi.com/");
                            startActivity(intent1);
                            finish();*/
                        }
                        if(((String)checkedItem).equals("财经")){
                            Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                            intent1.putExtra("finance","http://finance.cankaoxiaoxi.com/");
                            startActivity(intent1);
                            finish();
                        }
                        if(((String)checkedItem).equals("评论")){
                            /*Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                            intent1.putExtra("column","http://column.cankaoxiaoxi.com/");
                            startActivity(intent1);
                            finish();*/
                        }
                        if(((String)checkedItem).equals("体育")){
                            /*Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                            intent1.putExtra("sports","http://sports.cankaoxiaoxi.com/");
                            startActivity(intent1);
                            finish();*/
                        }
                        if(((String)checkedItem).equals("文化")){
                            /*Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                            intent1.putExtra("culture","http://culture.cankaoxiaoxi.com/");
                            startActivity(intent1);
                            finish();*/
                        }
                    }
                });
                AlertDialog dialog = incomeAlertDialog.create();
                dialog.show();
                return true;
            case R.id.action_mail:
                Intent intent2=new Intent(MainActivity.this,subscription_news.class);
                startActivity(intent2);
                return true;
            case R.id.action_day:
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    recreate();
                return true;
            case R.id.action_Night:
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
            default:
                return false;
        }
    }
    private void initView(){
        lv=(ListView)findViewById(R.id.news_lv);
        et_search=(EditText)findViewById(R.id.et_search);
        btn_search=(Button)findViewById(R.id.btn_search);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_container);
    }
    private void changeUri(){
        if(getIntent().getStringExtra("uri")!=null){
            jsoup_uri=getIntent().getStringExtra("uri");
        }
        if(getIntent().getStringExtra("China")!=null){
            jsoup_uri=getIntent().getStringExtra("China");
        }
        if(getIntent().getStringExtra("world")!=null){
            jsoup_uri=getIntent().getStringExtra("world");
        }
        if(getIntent().getStringExtra("mil")!=null){
            jsoup_uri=getIntent().getStringExtra("mil");
        }
        if(getIntent().getStringExtra("finance")!=null){
            jsoup_uri=getIntent().getStringExtra("finance");
        }
        if(getIntent().getStringExtra("column")!=null){
            jsoup_uri=getIntent().getStringExtra("column");
        }
        if(getIntent().getStringExtra("sports")!=null){
            jsoup_uri=getIntent().getStringExtra("sports");
        }
        if(getIntent().getStringExtra("culture")!=null){
            jsoup_uri=getIntent().getStringExtra("culture");
        }
    }
    private void attainNews(){
        //Toast.makeText(MainActivity.this,search,Toast.LENGTH_SHORT).show();
        final Thread t=new Thread(new Runnable() {
            private HttpURLConnection conn;
            @Override
            public void run() {
                try {
                    if(isNetworkAvailable(MainActivity.this)){
                        Document doc = Jsoup.connect(jsoup_uri).get();
                        Elements imgLinks=doc.select("a.f-l");
                        Elements allLinks=doc.select("div.f-r");//网页中的总父节点
                        for (int j = 0; j < 100; j++) {
                            String title = allLinks.get(j).select("h3").select("a").text();//获取标题
                            String uri = allLinks.get(j).select("h3").select("a").attr("href");//获取新闻的链接地址
                            String pict = imgLinks.get(j).select("img").attr("src");//获取新闻的图片
                            String source = allLinks.get(j).select("p.tag").select("span").text();//获取新闻标签关键词

                            if (title.contains(search)& !search.equals("")) {
                                if(income){
                                    newsList.clear();
                                    income=false;
                                }
                                News n = new News(title, uri, pict, source);
                                newsList.add(n);
                            }
                            if(search.equals("")){
                                if(come) {
                                    newsList.clear();
                                    come = false;
                                }
                                News news = new News(title, uri, pict, source);
                                newsList.add(news);
                            }
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }else {
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
                t.start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                if(et_search.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this,"请输入关键词",Toast.LENGTH_SHORT).show();
                }else {
                    income=true;
                    come=true;
                    search=et_search.getText().toString().trim();
                    attainNews();
                }
                break;
        }
    }
    //监听返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            hideProgressDialog();
            search="";
            et_search.setText("");
            attainNews();
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出MYnews", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
    public class update_time implements Runnable{
        @Override
        public void run() {
            try {
                while (true){
                    Thread.sleep(min*60000);
                    income=true;
                    come=true;
                    search="";
                    attainNews();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(MainActivity.this, title,
                    message, true, false);
        } else if (progressDialog.isShowing()) {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }

        progressDialog.show();

    }

    /*
     * 隐藏提示加载
     */
    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
}