package work.wang.mynews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import work.wang.mynews.R;

public class url_dialog extends Dialog {
    public interface urlListener{
        public void getSelected(String selected);
    }
    private EditText et;
    private Button ok,cancel;
    urlListener Listener;
    public url_dialog(Context context, final urlListener Listener) {
        super(context);
        this.Listener=Listener;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.url_dialog);
        init();
        ok.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Listener.getSelected(et.getText().toString());
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    private void init(){
        et=(EditText)findViewById(R.id.url_et);
        ok=(Button)findViewById(R.id.url_btn_ok);
        cancel=(Button)findViewById(R.id.url_btn_cancel);
    }
}
