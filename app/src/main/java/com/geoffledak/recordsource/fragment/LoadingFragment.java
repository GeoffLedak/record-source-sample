package com.geoffledak.recordsource.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geoffledak.recordsource.R;
import com.geoffledak.recordsource.activity.MainActivity;
import com.geoffledak.recordsource.model.RecordStore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by geoff on 4/19/16.
 */
public class LoadingFragment extends Fragment {

    private static final String TAG = LoadingFragment.class.getSimpleName();
    MainActivity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mActivity = (MainActivity)getActivity();

        try {
            loadRecordStoreData();
        } catch (Exception exception) {
            Log.e(TAG, "Failed to load Record Store List json file!");
        }

        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }


    public void loadRecordStoreData() throws Exception {

        final OkHttpClient client = new OkHttpClient();
        final Gson gson = new Gson();
        final Request request = new Request.Builder().url(getResources().getString(R.string.store_list_url)).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Type collectionType = new TypeToken<List<RecordStore>>() {}.getType();
                mActivity.setRecordStoreList( (List<RecordStore>)gson.fromJson(response.body().charStream(), collectionType) );

                insertSectionFragment();
            }
        });
    }


    private void insertSectionFragment() {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = mActivity.getCurrentFragment();

                if( fragment == null ) {
                    mActivity.selectItem(0);
                }
                else {
                    if( fragment instanceof StoreListFragment )
                        mActivity.selectItem(0);
                    else if ( fragment instanceof MapFragment )
                        mActivity.selectItem(1);
                    else
                        mActivity.selectItem(0);
                }

                mActivity.getSupportActionBar().show();
            }
        });
    }

}