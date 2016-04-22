package com.geoffledak.recordsource.fragment;

import android.os.Bundle;
import android.text.TextUtils;

import com.geoffledak.recordsource.activity.MainActivity;
import com.geoffledak.recordsource.model.RecordStore;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by geoff on 4/19/16.
 */
public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        List<RecordStore> recordStoreList = ((MainActivity)getActivity()).getRecordStoreList();
        List<String> storeNameList = new ArrayList<>();
        List<LatLng> latLngList = new ArrayList<>();

        for( RecordStore recordStore : recordStoreList ) {
            if( !TextUtils.isEmpty(recordStore.getName()) && !TextUtils.isEmpty(recordStore.getLatitude()) && !TextUtils.isEmpty(recordStore.getLongitude()) ) {
                storeNameList.add( recordStore.getName() );
                latLngList.add( new LatLng( Float.parseFloat(recordStore.getLatitude()), Float.parseFloat(recordStore.getLongitude()) ) );
            }
        }

        for( int i = 0; i < storeNameList.size(); i++ ) {
            googleMap.addMarker( new MarkerOptions().position( latLngList.get(i) ).title( storeNameList.get(i) ) );
        }

        LatLng boulder = new LatLng(40.014763, -105.269334);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(boulder, 12.5f));
    }
}
