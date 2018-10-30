package work.wang.mynews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import work.wang.mynews.MainActivity;
import work.wang.mynews.R;
import work.wang.mynews.weather;

/**
 * Created by Administrator on 2017/12/28 0028.
 */

public class city_dialog extends Dialog{
    public interface cityEtListener{
        public void getSelected(String selected);
    }
    private EditText et;
    private Button ok,cancel;
    cityEtListener Listener;
    public city_dialog(Context context,final cityEtListener Listener) {
        super(context);
        this.Listener=Listener;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.city_dialog);
        init();
        ok.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Listener.getSelected(et.getText().toString());
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Listener.getSelected("佛山");
                dismiss();
            }
        });
    }
    private void init(){
        et=(EditText)findViewById(R.id.city_et);
        ok=(Button)findViewById(R.id.city_btn_ok);
        cancel=(Button)findViewById(R.id.city_btn_cancel);
    }
}
