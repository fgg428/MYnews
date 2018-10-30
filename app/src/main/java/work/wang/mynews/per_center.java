package work.wang.mynews;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import work.wang.mynews.adapter.per_center_adapter;
import work.wang.mynews.sqlite.MyHelper;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class per_center extends AppCompatActivity implements View.OnClickListener {
    private TextView user,time,local,email;
    private Button style;
    private ListView lv;
    private String style_style[]={"已投","已采用","审计中"};//类别选项
    private MyHelper myHelper;
    private per_center_adapter pca;
    SQLiteDatabase sd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity);
        init();
        style.setOnClickListener(this);
    }
    private void init(){
        user=(TextView)findViewById(R.id.per_tv);
        style=(Button) findViewById(R.id.per_btn_style);
        time=(TextView)findViewById(R.id.per_tv_time);
        local=(TextView)findViewById(R.id.per_tv_local);
        email=(TextView)findViewById(R.id.per_tv_email);
        lv=(ListView)findViewById(R.id.lv_state);
    }

    @Override
    public void onClick(View v) {
        myHelper=new MyHelper(this);
        sd=myHelper.getReadableDatabase();
        switch (v.getId()){
            case R.id.per_btn_style:
                AlertDialog.Builder incomeAlertDialog=new AlertDialog.Builder(this);
                incomeAlertDialog.setSingleChoiceItems(style_style, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        // which表示点击的条目
                        Object checkedItem = lw.getAdapter().getItem(which);
                        // 既然你没有cancel或者ok按钮，所以需要在点击item后使dialog消失
                        dialog.dismiss();
                        // 更新你的view
                        style.setText(((String)checkedItem));
                        if(((String)checkedItem).equals("已投")){
                            Cursor c=sd.query("broke_news",null,null, null,null,null,null);
                           pca =new per_center_adapter(per_center.this,c);
                            lv.setAdapter(pca);
                        }
                    }
                });

                AlertDialog dialog = incomeAlertDialog.create();
                dialog.show();
                break;
        }
    }
}
