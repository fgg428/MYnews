package work.wang.mynews.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;



import java.util.List;

import work.wang.mynews.News;
import work.wang.mynews.R;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class news_adapter extends BaseAdapter{

    private List<News> newsList;
    private View view;
    private Context mContext;
    private ViewHolder viewHolder; //优化Listview
    public news_adapter(Context mContext, List<News> newsList) {
        this.newsList = newsList;
        this.mContext= mContext;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.mynews_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.newsTitle = (TextView) view.findViewById(R.id.item_title);
            viewHolder.newspict = (SmartImageView) view.findViewById(R.id.item_pict);
            viewHolder.newsSource = (TextView) view.findViewById(R.id.item_source);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.newsTitle.setText(newsList.get(position).getNewsTitle());
        viewHolder.newspict.setImageUrl(newsList.get(position).getNewsPict(),R.mipmap.ic_launcher_round,R.mipmap.ic_launcher_round);
        viewHolder.newsSource.setText("标签: "+newsList.get(position).getNewsSource());
        return view;
    }


    class ViewHolder{
        TextView newsTitle;
        SmartImageView newspict;
        TextView newsSource;
    }
}
