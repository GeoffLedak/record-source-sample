package com.geoffledak.recordsource.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.geoffledak.recordsource.R;
import com.geoffledak.recordsource.view.BarChartView;

/**
 * Created by geoff on 4/21/16.
 */
public class ChartsFragment extends Fragment {

    static final String STATE_LEFT_VALUE = "leftValue";
    static final String STATE_RIGHT_VALUE = "rightValue";

    private String mLeftValueString;
    private String mRightValueString;
    private BarChartView mBarChartView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_charts, container, false);
        Button drawButton = (Button) v.findViewById(R.id.draw_button);
        final EditText leftValue = (EditText) v.findViewById(R.id.left_value_edit_text);
        final EditText rightValue = (EditText) v.findViewById(R.id.right_value_edit_text);
        mBarChartView = (BarChartView) v.findViewById(R.id.bar_chart_view);

        if( savedInstanceState != null ) {
            mLeftValueString = savedInstanceState.getString(STATE_LEFT_VALUE);
            mRightValueString = savedInstanceState.getString(STATE_RIGHT_VALUE);
            setValues();
        }

        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLeftValueString = leftValue.getText().toString();
                mRightValueString = rightValue.getText().toString();
                setValues();
            }
        });

        return v;
    }


    private void setValues() {

        if(TextUtils.isEmpty(mLeftValueString))
            mLeftValueString = "0";
        if(TextUtils.isEmpty(mRightValueString))
            mRightValueString = "0";

        mBarChartView.setStats(mLeftValueString, mRightValueString);
        mBarChartView.clear();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(STATE_LEFT_VALUE, mLeftValueString);
        outState.putString(STATE_RIGHT_VALUE, mRightValueString);

        super.onSaveInstanceState(outState);
    }
}
