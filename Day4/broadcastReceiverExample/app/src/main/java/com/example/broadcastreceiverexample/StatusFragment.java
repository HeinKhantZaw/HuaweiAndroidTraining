package com.example.broadcastreceiverexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment {
    TextView tvStatus;
    MyReceiver myReceiver = new MyReceiver();
    Intent batteryStatus;
    IntentFilter intentFilter;
    private static String TAG = "MyReceiver";
    float batteryPercent;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(myReceiver);
        Log.d(TAG, "Receiver should be unregistered");
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.BATTERY_LOW");
        intentFilter.addAction("android.intent.action.BATTERY_OKAY");
        Objects.requireNonNull(getContext()).registerReceiver(myReceiver, intentFilter);
        Log.d(TAG, "receiver should be registered");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        tvStatus = view.findViewById(R.id.tvStatus);
        //manual check
        intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = Objects.requireNonNull(getContext()).registerReceiver(null, intentFilter);
        int batteryLvl = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = batteryLvl == BatteryManager.BATTERY_STATUS_CHARGING || batteryLvl == BatteryManager.BATTERY_STATUS_FULL;
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        batteryPercent = level * 100 / (float) scale;
        if (isCharging) {
            tvStatus.setText("Status: Charging");
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            if (usbCharge) {
                tvStatus.append("\nvia USB. Battery level :" + batteryPercent);
            }
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            if (acCharge) {
                tvStatus.append("\nvia AC. Battery level :" + batteryPercent);
            }
        } else {
            tvStatus.setText("Status is not charging. Battery level:" + batteryPercent);
        }
        return view;
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received an intent");
            String info = "\nSomething wrong.";

            int mStatus = 0;

            if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW)) {
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                batteryPercent = level * 100 / (float) scale;
                info = "\nBattery low :" + batteryPercent;
                mStatus = 1;
                Log.d(TAG, "Battery low");
            } else if (intent.getAction().equals(Intent.ACTION_BATTERY_OKAY)) {
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                batteryPercent = level * 100 / (float) scale;
                info = "\nBattery ok :" + batteryPercent;
                mStatus = 2;
                Log.d(TAG, "Battery ok");
            } else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
                info = "\nPower connected :" + batteryPercent;
                mStatus = 3;
                Log.d(TAG, "Power connected");
            } else if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
                info = "\nPower disconnected :" + batteryPercent;
                mStatus = 4;
                Log.d(TAG, "Power disconnected");
            }
            tvStatus.setText("Status :" + mStatus + info);
        }

    }
}