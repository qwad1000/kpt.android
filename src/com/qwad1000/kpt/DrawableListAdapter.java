package com.qwad1000.kpt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DrawableListAdapter extends BaseAdapter {
    List<TransportTypeEnum> mTransportNames;
    private Context mContext;

    public DrawableListAdapter(Context mContext, List<TransportTypeEnum> mTransportNames) {
        this.mTransportNames = mTransportNames;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mTransportNames.size();
    }

    @Override
    public Object getItem(int position) {
        return mTransportNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = (LinearLayout) infalInflater.inflate(R.layout.drawer_list_iconed_item, null);
        }

        TextView transportType = (TextView) convertView.findViewById(R.id.transportTypeTextView);
        String str = mTransportNames.get(position).getName(mContext);
        transportType.setText(str);

        ImageView imageView = (ImageView) ((LinearLayout) convertView).findViewById(R.id.transportTypeImageView);
        imageView.setImageDrawable(mTransportNames.get(position).getDrawable(mContext));

        return convertView;
    }
}
