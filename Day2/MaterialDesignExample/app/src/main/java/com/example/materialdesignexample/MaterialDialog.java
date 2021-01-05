package com.example.materialdesignexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MaterialDialog extends AppCompatActivity implements View.OnClickListener {
    Button btnAlert, btnSimple, btnConfirm;
    String[] items = {"Item 1", "Item 2", "Item 3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_dialog);
        findViewAndSetListener();
    }

    private void findViewAndSetListener() {
        btnAlert = findViewById(R.id.btnAlertDialog);
        btnSimple = findViewById(R.id.btnSimpleDialog);
        btnConfirm = findViewById(R.id.btnConfirmation);
        btnAlert.setOnClickListener(this);
        btnSimple.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAlertDialog:
                new MaterialAlertDialogBuilder(MaterialDialog.this)
                        .setTitle("AlertDialogTitle")
                        .setIcon(R.drawable.ic_baseline_add_alert_24)
                        .setMessage("This is material alert dialog")
                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MaterialDialog.this, "OK Button clicked", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MaterialDialog.this, "Cancel Button clicked", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;
            case R.id.btnSimpleDialog:
                new MaterialAlertDialogBuilder(MaterialDialog.this)
                        .setTitle("SimpleDialogTitle")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String itemName = items[i];
                                Toast.makeText(getApplicationContext(),itemName,Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(R.drawable.ic_android).show();
                break;
            case R.id.btnConfirmation:
                    new  MaterialAlertDialogBuilder(MaterialDialog.this)
                            .setTitle("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MaterialDialog.this, "Yes Button clicked", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MaterialDialog.this, "No Button clicked", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setMessage("You can add confirmation message here!")
                            .setIcon(R.drawable.ic_baseline_help_24).show();
                break;
        }
    }
}