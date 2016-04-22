package com.geoffledak.recordsource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.geoffledak.recordsource.R;
import com.geoffledak.recordsource.model.RecordStore;

import java.util.List;

/**
 * Created by geoff on 4/18/16.
 */
public class StoreCardListAdapter extends ArrayAdapter<RecordStore> {

    private Context mContext;
    private LayoutInflater mLayoutInflator;
    private List<RecordStore> mRecordStoreList;


    public StoreCardListAdapter(Context context, List<RecordStore> recordStoreList) {
        super(context, R.layout.listview_item_store_card, recordStoreList);

        mContext = context;
        mRecordStoreList = recordStoreList;
        mLayoutInflator = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null )
            convertView = mLayoutInflator.inflate(R.layout.listview_item_store_card, parent, false);

        ImageView storeImage = (ImageView) convertView.findViewById(R.id.store_image);
        TextView storeName = (TextView) convertView.findViewById(R.id.store_name);
        TextView storeCityState = (TextView) convertView.findViewById(R.id.city_and_state);

        Glide.with(mContext).load(mRecordStoreList.get(position).getImage()).into(storeImage);
        storeName.setText(mRecordStoreList.get(position).getName());
        storeCityState.setText( mRecordStoreList.get(position).getCity() + ", " + mRecordStoreList.get(position).getState() );

        return convertView;
    }

}
