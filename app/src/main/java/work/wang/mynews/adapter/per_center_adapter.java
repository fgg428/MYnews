package work.wang.mynews.adapter;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import work.wang.mynews.R;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class per_center_adapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private Holder viewHolder;
    private View view;
    private String s_time="",s_local="",s_state="";
    public per_center_adapter(Context context,Cursor cursor){
        this.context=context;
        this.cursor=cursor;
    }
    public void setCursor(Cursor c) {
        this.cursor = c;
    }
    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        boolean available = cursor.moveToPosition(position);
        if (!available){
            return null;
        }
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.broke_news_item,
                    null);
            viewHolder = new Holder();
            viewHolder.time=(TextView)view.findViewById(R.id.broke_news_time);
            viewHolder.local=(TextView)view.findViewById(R.id.broke_news_local);
            viewHolder.state=(TextView)view.findViewById(R.id.broke_news_state);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (Holder) view.getTag();
        }
        s_time=cursor.getString(cursor.getColumnIndex("time"));
        s_local=cursor.getString(cursor.getColumnIndex("local"));
        s_state=cursor.getString(cursor.getColumnIndex("state"));
        viewHolder.time.setText(s_time);
        viewHolder.local.setText(s_local);
        viewHolder.state.setText(s_state);
        return view;
    }
}
    class Holder{
        TextView time;
        TextView local;
        TextView state;
    }
