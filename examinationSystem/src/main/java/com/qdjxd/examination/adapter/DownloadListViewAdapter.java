package com.qdjxd.examination.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qdjxd.examination.R;
import com.qdjxd.examination.examacitvity.listener.DownloadListener;
import com.qdjxd.examination.utils.SharedPreferencesHelper;

/**
 * 试卷下载选择列表
 *
 * @author asus
 */
public class DownloadListViewAdapter extends BaseAdapter {
    private SharedPreferencesHelper sp;
    private ArrayList<HashMap<String, String>> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public DownloadListViewAdapter() {
    }

    public DownloadListViewAdapter(Context context, ArrayList<HashMap<String, String>> mList, SharedPreferencesHelper sp) {
        super();
        this.sp = sp;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_download_exam_item, null);
            holder.tv_download = (TextView) convertView.findViewById(R.id.tv_download);
            holder.tv_progress = (TextView) convertView.findViewById(R.id.tv_progress);
            holder.progressbar_download = (ProgressBar) convertView.findViewById(R.id.profressbar_download);
            holder.img_download = (ImageView) convertView.findViewById(R.id.img_download);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HashMap<String, String> map = mList.get(position);
        holder.tv_download.setText("下载" + map.get("CLASSNAME"));
        //下载点击事件，自定义监听
        holder.img_download.setOnClickListener(
                new DownloadListener(position, mContext, holder, map.get("CLASSID"), sp));
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_download;
        public TextView tv_progress;
        public ProgressBar progressbar_download;
        public ImageView img_download;
    }
}
