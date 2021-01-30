/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package com.example.testingsitekit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hms.site.api.model.AddressDetail;
import com.huawei.hms.site.api.model.Coordinate;
import com.huawei.hms.site.api.model.CoordinateBounds;
import com.huawei.hms.site.api.model.Poi;
import com.huawei.hms.site.api.model.Site;
import com.huawei.hms.site.widget.SearchIntent;

import java.util.Arrays;

/***
 * Mian activity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SearchIntent searchIntent;
    TextView tvResults;

    private static final String[] RUNTIME_PERMISSIONS_BEFORE_P = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET};
    private static final String[] RUNTIME_PERMISSIONS_AFTER_P = {Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_BACKGROUND_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        findViewById(R.id.get_nearby_places_button).setOnClickListener(this);
        findViewById(R.id.text_search_button).setOnClickListener(this);
        findViewById(R.id.detail_search_button).setOnClickListener(this);
        findViewById(R.id.nearby_search_button).setOnClickListener(this);
        findViewById(R.id.query_suggestion_button).setOnClickListener(this);
        findViewById(R.id.query_auto_complete_button).setOnClickListener(this);
        findViewById(R.id.widget_button).setOnClickListener(this);
        tvResults = findViewById(R.id.textViewResults);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // Jump to nearby places activity
            case R.id.get_nearby_places_button:
                startActivity(new Intent(this, NearbyPlacesActivity.class));
                break;
            // Jump to keyword search activity.
            case R.id.text_search_button:
                startActivity(new Intent(this, TextSearchActivity.class));
                break;
            // Jump to place detail search activity.
            case R.id.detail_search_button:
                startActivity(new Intent(this, DetailSearchActivity.class));
                break;
            // Jump to nearby place search activity.
            case R.id.nearby_search_button:
                startActivity(new Intent(this, NearbySearchActivity.class));
                break;
            // Jump to keyword search activity.
            case R.id.query_suggestion_button:
                startActivity(new Intent(this, QuerySuggestionActivity.class));
                break;
            // Jump to auto complete activity.
            case R.id.query_auto_complete_button:
                startActivity(new Intent(this, QueryAutoCompleteActivity.class));
                break;
            // Jump to widget activity
            case R.id.widget_button:
                searchWithWidget();
                break;
            default:
                break;
        }
    }

    private void searchWithWidget() {
        searchIntent = new SearchIntent();
        searchIntent.setApiKey(Utils.getApiKey());
        Intent intent = searchIntent.getIntent(this);
        startActivityForResult(intent, SearchIntent.SEARCH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SearchIntent.SEARCH_REQUEST_CODE == requestCode) {
            if (SearchIntent.isSuccess(resultCode)) {
                Site site;
                site = searchIntent.getSiteFromIntent(data);
                StringBuilder stringBuilder = new StringBuilder();
                AddressDetail addressDetail = site.getAddress();
                Coordinate location = site.getLocation();
                Poi poi = site.getPoi();
                CoordinateBounds viewport = site.getViewport();
                stringBuilder.append(String.format(
                        "name: %s\nformatAddress: %s\ncountry: %s\ncountryCode: %s\nlocation: %s\npoiTypes: %s\nviewport is %s"
                        , site.getName(), site.getFormatAddress(),
                        (addressDetail == null ? "" : addressDetail.getCountry()),
                        (addressDetail == null ? "" : addressDetail.getCountryCode()),
                        (location == null ? "" : (location.getLat() + "," + location.getLng())),
                        (poi == null ? "" : Arrays.toString(poi.getPoiTypes())),
                        (viewport == null ? "" : "northeast{lat=" + viewport.getNortheast().getLat() + ", lng=" + viewport.getNortheast().getLng() + "},"
                                + "southwest{lat=" + viewport.getSouthwest().getLat() + ", lng=" + viewport.getSouthwest().getLng() + "}")));
                tvResults.setText(stringBuilder.toString());
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            if (!hasPermissions(this, RUNTIME_PERMISSIONS_BEFORE_P)) {
                ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS_BEFORE_P, 1);
            }
        } else {
            if (!hasPermissions(this, RUNTIME_PERMISSIONS_AFTER_P)) {
                ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS_AFTER_P, 1);
            }
        }
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "onRequestPermissionsResult: apply LOCATION PERMISSION successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "onRequestPermissionsResult: apply LOCATION PERMISSION  failed", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == 2) {
            if (grantResults.length > 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION failed", Toast.LENGTH_LONG).show();
            }
        }
    }

}
