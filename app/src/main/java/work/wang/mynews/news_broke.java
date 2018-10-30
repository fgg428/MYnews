package work.wang.mynews;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import work.wang.mynews.sqlite.MyHelper;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class news_broke extends AppCompatActivity implements View.OnClickListener {
    private EditText time,local,state,phone,email;
    private Button ok,cancel;
    private String b_time="",b_local="",b_state="",b_phone="",b_email="";
    MyHelper myHelper=new MyHelper(this);
    SQLiteDatabase sd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broke_news);
        init();
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
    private void init(){
        time=(EditText)findViewById(R.id.broke_et_time);
        local=(EditText)findViewById(R.id.broke_et_local);
        state=(EditText)findViewById(R.id.broke_et_state);
        phone=(EditText)findViewById(R.id.broke_et_phone);
        email=(EditText)findViewById(R.id.broke_et_email);
        ok=(Button)findViewById(R.id.brokr_btn_ok);
        cancel=(Button)findViewById(R.id.brokr_btn_cancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.brokr_btn_ok:
                broke();
                break;
            case R.id.brokr_btn_cancel:
                cancel();
                break;
        }
    }
    private void broke(){
        b_time=time.getText().toString().trim();
        b_local=local.getText().toString().trim();
        b_state=state.getText().toString().trim();
        b_phone=phone.getText().toString().trim();
        b_email=email.getText().toString().trim();
        if (b_time.equals("")){
            Toast.makeText(this,"时间不能为空",Toast.LENGTH_SHORT).show();
        }else if(checkDate(b_time,"yyyy-MM-dd")==false){
            Toast.makeText(this,"日期格式错误",Toast.LENGTH_SHORT).show();
        }else if(b_local.equals("")){
            Toast.makeText(this,"地点不能为空",Toast.LENGTH_SHORT).show();
        }else if(b_state.equals("")){
            Toast.makeText(this,"具体事项/说明不能为空",Toast.LENGTH_SHORT).show();
        }else if(b_email.equals("")){
            Toast.makeText(this,"email不能为空",Toast.LENGTH_SHORT).show();
        }else{
            sd=myHelper.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put("time",b_time);
            values.put("local",b_local);
            values.put("state",b_state);
            values.put("phone",b_phone);
            values.put("email",b_email);
            sd.insert("broke_news",null,values);
            Toast.makeText(this,"上传成功",Toast.LENGTH_SHORT).show();
            sd.close();
            finish();
        }
    }
    public void cancel(){
        finish();
    }
    public static boolean checkDate(String date,String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date d = null;
        try{
            d = df.parse(date);
        }catch(Exception e){
            //如果不能转换,肯定是错误格式
            return false;
        }
        String s1 = df.format(d);
        // 转换后的日期再转换回String,如果不等,逻辑错误.如format为"yyyy-MM-dd",date为
        // "2006-02-31",转换为日期后再转换回字符串为"2006-03-03",说明格式虽然对,但日期
        // 逻辑上不对.
        return date.equals(s1);
    }
}
