package work.wang.mynews.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import work.wang.mynews.R;

public class update_dialog extends Dialog {
    public interface updateListener{
        public void getSelected(String selected);
    }
    private EditText et;
    private Button ok,cancel;
    updateListener Listener;
    public update_dialog(Context context, final updateListener Listener) {
        super(context);
        this.Listener=Listener;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_dialog);
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
        et=(EditText)findViewById(R.id.update_et);
        ok=(Button)findViewById(R.id.update_btn_ok);
        cancel=(Button)findViewById(R.id.update_btn_cancel);
    }
}
