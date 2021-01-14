package com.example.workmanagerexample;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvStatus;
    Button btnNoConstraint, btnStorage, btnBattery, btnCharging, btnIdle, btnNetwork;
    OneTimeWorkRequest mRequest;
    WorkManager mWorkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        tvStatus = findViewById(R.id.textViewStatus);
        btnNoConstraint = findViewById(R.id.btnNoConstraint);
        btnStorage = findViewById(R.id.btnStorageNotLow);
        btnBattery = findViewById(R.id.btnBatteryNotLow);
        btnCharging = findViewById(R.id.btnRequiresCharging);
        btnIdle = findViewById(R.id.btnRequiresDeviceIdle);
        btnNetwork = findViewById(R.id.btnRequiredNetwork);
        btnNoConstraint.setOnClickListener(this);
        btnStorage.setOnClickListener(this);
        btnBattery.setOnClickListener(this);
        btnCharging.setOnClickListener(this);
        btnIdle.setOnClickListener(this);
        btnNetwork.setOnClickListener(this);
        mWorkManager = WorkManager.getInstance();
    }

    @Override
    public void onClick(View view) {
        tvStatus.setText("");
        Constraints mConstraints;
        switch (view.getId()) {
            case R.id.btnNoConstraint:
                mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
                break;
            case R.id.btnBatteryNotLow:
                mConstraints = new Constraints.Builder().setRequiresStorageNotLow(true).build();
                mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints).build();
                break;
            case R.id.btnStorageNotLow:
                mConstraints = new Constraints.Builder().setRequiresBatteryNotLow(true).build();
                mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints).build();
                break;
            case R.id.btnRequiresCharging:
                mConstraints = new Constraints.Builder().setRequiresCharging(true).build();
                mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints).build();
                break;
            case R.id.btnRequiresDeviceIdle:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mConstraints = new Constraints.Builder().setRequiresDeviceIdle(true).build();
                    mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints).build();
                }
                break;
            case R.id.btnRequiredNetwork:
                mConstraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build();
                mRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class).setConstraints(mConstraints).build();
                break;
            default:
                break;
        }
        mWorkManager.getWorkInfoByIdLiveData(mRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null) {
                    WorkInfo.State state = workInfo.getState();
                    tvStatus.append(state.toString() + "\n");
                }
            }
        });
        mWorkManager.enqueue(mRequest);
    }
}