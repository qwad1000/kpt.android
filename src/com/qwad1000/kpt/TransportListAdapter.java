package com.qwad1000.kpt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Сергій on 14.09.2014.
 */
public class TransportListAdapter extends BaseAdapter {
    private List<TransportItem>mContent;
    private Context mContext;

    public TransportListAdapter(Context context, List<TransportItem> list){
        mContent = list;
        mContext = context;
    }
    @Override
    public int getCount() {
        return mContent.size();
    }

    @Override
    public Object getItem(int position) {
        return mContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mContent.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (LinearLayout) infalInflater.inflate(R.layout.transport_list_item, null);
        }
        TextView transportNumber = (TextView)((LinearLayout)view).findViewById(R.id.numberTextView);
        transportNumber.setText(mContent.get(position).getNumber());

        TextView transportURL = (TextView)((LinearLayout)view).findViewById(R.id.urlTextView);
        transportURL.setText(mContent.get(position).getUrl().getPath());

        return view;
    }
}
