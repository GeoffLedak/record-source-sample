package com.geoffledak.recordsource.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.geoffledak.recordsource.R;
import com.geoffledak.recordsource.activity.MainActivity;
import com.geoffledak.recordsource.adapter.StoreCardListAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by geoff on 4/18/16.
 */
public class StoreListFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView mStoreCardListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_store_list, container, false);
        mStoreCardListView = (ListView) v.findViewById(R.id.image_list);
        mStoreCardListView.setAdapter(new StoreCardListAdapter(getContext(), ((MainActivity)getActivity()).getRecordStoreList()));

        mStoreCardListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mStoreCardListView.setOnItemClickListener(this);


        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        MainActivity activity = ((MainActivity)getActivity());
        activity.setSlidingPanelDetails(position);
        activity.getSlidingUpPanel().setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        activity.getSupportActionBar().hide();
    }

}

