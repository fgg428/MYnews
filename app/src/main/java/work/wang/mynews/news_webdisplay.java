package work.wang.mynews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

import work.wang.mynews.sqlite.MyHelper;

/**
 * Created by Administrator on 2017/12/23 0023.
 */

public class news_webdisplay extends FragmentActivity implements View.OnClickListener {
    MyHelper myHelper=new MyHelper(this);
    SQLiteDatabase sd;
    EditText et;
    TextView tv_useName,tv_comments;
    Button btn,back,fx,dayNight;
    WebView wv;
    private String uri;
    String pl;//评论
    Boolean night=true;
    private ProgressDialog progressDialog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.new_webview);
        uri=getIntent().getStringExtra("uri");
        init();
        if(isNetworkAvailable(news_webdisplay.this)){
            wv=(WebView)findViewById(R.id.news_webView);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient() {

                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    showProgressDialog("提示", "正在加载......");
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                   hideProgressDialog();
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);//当打开新的连接时,使用当前的webview,不使用系统其他浏览器
                    return true;
                }
            });
            wv.loadUrl(uri);
        }else {
            Toast.makeText(this,"网络连接失败",Toast.LENGTH_SHORT).show();
            finish();
        }

    }
    public void init(){
        tv_useName=(TextView)findViewById(R.id.web_tv_userName);
        tv_comments=(TextView)findViewById(R.id.web_tv_comments);
        et=(EditText)findViewById(R.id.web_comment);
        btn=(Button)findViewById(R.id.web_submit);
        back=(Button)findViewById(R.id.news_web_back);
        fx=(Button)findViewById(R.id.news_web_fx);
        dayNight=(Button)findViewById(R.id.news_web_night);
        btn.setOnClickListener(this);
        back.setOnClickListener(this);
        fx.setOnClickListener(this);
        dayNight.setOnClickListener(this);
        user_comments_dispaly();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.web_submit:
                comment();
                break;
            case R.id.news_web_back:
                finish();
                break;
            case R.id.news_web_fx:
                Intent send=new Intent();
                send.setAction(Intent.ACTION_SEND);
                send.putExtra(Intent.EXTRA_TEXT, uri);
                send.setType("text/plain");
                startActivity(Intent.createChooser(send,"share to..."));
                break;
            case R.id.news_web_night:
                if(night){
                    setLight(this,2);
                    dayNight.setBackgroundResource(R.mipmap.ic_launcher_day);
                    night=false;
                }else {
                    setLight(this,200);
                    dayNight.setBackgroundResource(R.mipmap.ic_launcher_night);
                    night=true;
                }

        }
    }
    public void comment(){
        String comment=et.getText().toString();
        if(comment.equals("")){
            Toast.makeText(this,"评论不能为空",Toast.LENGTH_SHORT).show();

        }else {
            sd=myHelper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("url",uri);
            values.put("comments",comment);
            sd.insert("user_comments",null,values);
            user_comments_dispaly();
        }
    }
    public void user_comments_dispaly(){
        sd=myHelper.getReadableDatabase();
        String[] selectionArgs={uri};
        Cursor cursor=sd.query("user_comments",null,"url=?",selectionArgs,null,null,null);
        while (cursor.moveToNext()){
            if (cursor.isFirst()){
                pl=cursor.getString(cursor.getColumnIndex("comments"));
                tv_comments.setVisibility(View.VISIBLE);
                tv_useName.setVisibility(View.VISIBLE);
                break;
            }
        }
        tv_comments.setText(pl);
        tv_useName.setText("我是用户名:");
        cursor.close();
        sd.close();
    }
    private void setLight(Activity context, int brightness) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        context.getWindow().setAttributes(lp);
    }
    public void showProgressDialog(String title, String message) {
        if (progressDialog == null) {

            progressDialog = ProgressDialog.show(news_webdisplay.this, title,
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
}
