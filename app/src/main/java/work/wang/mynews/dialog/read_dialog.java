package work.wang.mynews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import work.wang.mynews.R;

/**
 * Created by Administrator on 2017/12/23 0023.
 */

public class read_dialog extends Dialog {
    public interface radioSelectedListener{
        public void getSelected(String selected);
    }
    private RadioGroup rdg;
    private RadioButton rdb_webView;
    private RadioButton rdb_browser;
    radioSelectedListener Listener;

    public read_dialog(Context context,final radioSelectedListener Listener){
        super(context);
        this.Listener=Listener;
    }
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.read_dialog);
        init();
        rdg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rdb_webView:
                        Listener.getSelected("webView");
                        dismiss();
                        break;
                    case R.id.rdb_browser:
                        Listener.getSelected("browser");
                        dismiss();
                        break;
                }
            }
        });
    }
    protected void init(){
        rdg=(RadioGroup)findViewById(R.id.rdg);
        rdb_webView=(RadioButton)findViewById(R.id.rdb_webView);
        rdb_browser=(RadioButton)findViewById(R.id.rdb_browser);
    }
}
