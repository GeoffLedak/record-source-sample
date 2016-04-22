package com.geoffledak.recordsource.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.geoffledak.recordsource.R;
import com.geoffledak.recordsource.fragment.ChartsFragment;
import com.geoffledak.recordsource.fragment.LoadingFragment;
import com.geoffledak.recordsource.fragment.MapFragment;
import com.geoffledak.recordsource.fragment.StoreListFragment;
import com.geoffledak.recordsource.model.RecordStore;
import com.geoffledak.recordsource.model.Section;
import com.geoffledak.recordsource.utils.NetworkUtils;
import com.google.gson.reflect.TypeToken;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    static final String STATE_SECTION_LIST = "sectionList";
    static final String STATE_RECORD_STORE_LIST = "recordStoreList";
    static final String STATE_ACTION_BAR_TITLE = "actionBarTitle";
    static final String STATE_STORE_CARD_NAME = "storeCardName";
    static final String STATE_STORE_CARD_FULL_ADDRESS = "storeCardFullAddress";
    static final String STATE_STORE_CARD_PHONE_NUMBER = "storeCardPhoneNumber";
    static final String CONTENT_FRAME_TAG = "contentFrameTag";
    final int PERMISSIONS_REQUEST_CALL_PHONE = 0;

    private CharSequence mActionBarTitle;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private List<Section> mSectionList;
    private List<RecordStore> mRecordStoreList;
    private static Fragment mCurrentFragment = null;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private ImageView mSlidingUpPanelCollapseButton;

    private TextView mStoreCardName;
    private TextView mStoreCardAddress;
    private TextView mStoreCardPhoneNumber;

    private LinearLayout mPhoneContainer;

    private String mStoreCardNameString;
    private String mStoreCardFullAddressString;
    private String mStoreCardPhoneNumberString;
    private String mDialPhoneString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStoreCardName = (TextView)findViewById(R.id.store_card_name);
        mStoreCardAddress = (TextView)findViewById(R.id.store_card_address);
        mStoreCardPhoneNumber = (TextView)findViewById(R.id.store_card_phone);
        mPhoneContainer = (LinearLayout)findViewById(R.id.store_card_phone_container);

        if( savedInstanceState != null ) {
            mSectionList = Parcels.unwrap(savedInstanceState.getParcelable(STATE_SECTION_LIST));
            mRecordStoreList = Parcels.unwrap(savedInstanceState.getParcelable(STATE_RECORD_STORE_LIST));
            mActionBarTitle = savedInstanceState.getString(STATE_ACTION_BAR_TITLE);

            mStoreCardNameString = savedInstanceState.getString(STATE_STORE_CARD_NAME);
            mStoreCardFullAddressString = savedInstanceState.getString(STATE_STORE_CARD_FULL_ADDRESS);
            mStoreCardPhoneNumberString = savedInstanceState.getString(STATE_STORE_CARD_PHONE_NUMBER);

            mStoreCardName.setText(mStoreCardNameString);
            mStoreCardAddress.setText(mStoreCardFullAddressString);
            mStoreCardPhoneNumber.setText(mStoreCardPhoneNumberString);
        }

        initDrawer();
        initSlidingUpPanel();
        initPhoneDialing();

        if( mActionBarTitle != null && !TextUtils.isEmpty(mActionBarTitle))
            getSupportActionBar().setTitle(mActionBarTitle);

        if( mCurrentFragment == null || mRecordStoreList == null || mSectionList == null )
            insertLoadingFragment();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Re-hide the actionbar on device rotation
        if( mSlidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED) )
            getSupportActionBar().hide();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(STATE_SECTION_LIST, Parcels.wrap(mSectionList));
        outState.putParcelable(STATE_RECORD_STORE_LIST, Parcels.wrap(mRecordStoreList));
        outState.putString(STATE_ACTION_BAR_TITLE, mActionBarTitle.toString());
        outState.putString(STATE_STORE_CARD_NAME, mStoreCardNameString);
        outState.putString(STATE_STORE_CARD_FULL_ADDRESS, mStoreCardFullAddressString);
        outState.putString(STATE_STORE_CARD_PHONE_NUMBER, mStoreCardPhoneNumberString);

        super.onSaveInstanceState(outState);
    }

    public void initDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        String sectionListData = NetworkUtils.loadJSONFromResource(this, R.raw.sections);
        Type collectionType = new TypeToken<List<Section>>() {}.getType();
        mSectionList = (List<Section>)NetworkUtils.loadJson(sectionListData, collectionType);

        String[] sectionTitles = new String[mSectionList.size()];
        for( int i = 0; i < mSectionList.size(); i++ ) { sectionTitles[i] = mSectionList.get(i).getTitle(); }

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, sectionTitles));
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    private void initSlidingUpPanel() {

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mSlidingUpPanelLayout.setPanelHeight(0);
        mSlidingUpPanelLayout.setOverlayed(true);
        mSlidingUpPanelLayout.setTouchEnabled(false);
        mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        mSlidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) { }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if( previousState.equals(SlidingUpPanelLayout.PanelState.EXPANDED) )
                    getSupportActionBar().show();
            }
        });

        mSlidingUpPanelCollapseButton = (ImageView) findViewById(R.id.store_card_collapse_button);
        mSlidingUpPanelCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( mSlidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED) )
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }


    @Override
    public void onBackPressed() {

        if( mSlidingUpPanelLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED) )
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else
        super.onBackPressed();
    }


    public void setSlidingPanelDetails(int position) {

        final RecordStore store = mRecordStoreList.get(position);

        mStoreCardNameString = store.getName();
        mStoreCardFullAddressString = store.getAddress() + " " + store.getCity() + ", " + store.getState() + " " + store.getZip();
        mStoreCardPhoneNumberString = store.getPhone();

        // Underline the phone number
        SpannableString underlinedPhoneNumber = new SpannableString(mStoreCardPhoneNumberString);
        underlinedPhoneNumber.setSpan(new UnderlineSpan(), 0, underlinedPhoneNumber.length(), 0);

        mStoreCardName.setText(mStoreCardNameString);
        mStoreCardAddress.setText(mStoreCardFullAddressString);
        mStoreCardPhoneNumber.setText(underlinedPhoneNumber);
    }


    private void initPhoneDialing() {

        // Set up phone dialing for when phone number is tapped
        if( mPhoneContainer != null )
            mPhoneContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialPhoneString = mStoreCardPhoneNumberString;
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_CALL_PHONE);
                    else
                        makeCall();
                }
            });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();
                }
                return;
            }
        }
    }


    private void makeCall() {

        String phoneNumber = "tel:" + mDialPhoneString.replaceAll("[^0-9]","");
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(callIntent);
    }


    // Drawer navigation fragment transactions occur in this method
    public void selectItem(int position) {

        String sectionTitle = (String) mDrawerList.getAdapter().getItem(position);
        Fragment fragment = null;

        if( sectionTitle.equalsIgnoreCase("Store List")) {
            fragment = new StoreListFragment();
        }
        else if( sectionTitle.equalsIgnoreCase("Map") ) {
            fragment = new MapFragment();
        }
        else if( sectionTitle.equalsIgnoreCase("Charts") ) {
            fragment = new ChartsFragment();
        }

        if( fragment != null ) {
            mCurrentFragment = fragment;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, CONTENT_FRAME_TAG).commit();

            mDrawerList.setItemChecked(position, true);
            mActionBarTitle = sectionTitle;
            getSupportActionBar().setTitle(mActionBarTitle);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }


   private void insertLoadingFragment() {
        getSupportActionBar().hide();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new LoadingFragment(), CONTENT_FRAME_TAG).commit();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public List<RecordStore> getRecordStoreList() { return mRecordStoreList; }

    public void setRecordStoreList(List<RecordStore> recordStoreList) { mRecordStoreList = recordStoreList; }

    public Fragment getCurrentFragment() { return mCurrentFragment; }

    public SlidingUpPanelLayout getSlidingUpPanel() { return mSlidingUpPanelLayout; }
}
