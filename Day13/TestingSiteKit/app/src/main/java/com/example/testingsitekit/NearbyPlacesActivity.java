package com.example.testingsitekit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationCallback;
import com.huawei.hms.location.LocationRequest;
import com.huawei.hms.location.LocationResult;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.location.LocationSettingsRequest;
import com.huawei.hms.location.LocationSettingsResponse;
import com.huawei.hms.location.LocationSettingsStatusCodes;
import com.huawei.hms.location.SettingsClient;
import com.huawei.hms.site.api.SearchResultListener;
import com.huawei.hms.site.api.SearchService;
import com.huawei.hms.site.api.SearchServiceFactory;
import com.huawei.hms.site.api.model.AddressDetail;
import com.huawei.hms.site.api.model.Coordinate;
import com.huawei.hms.site.api.model.CoordinateBounds;
import com.huawei.hms.site.api.model.LocationType;
import com.huawei.hms.site.api.model.NearbySearchRequest;
import com.huawei.hms.site.api.model.NearbySearchResponse;
import com.huawei.hms.site.api.model.Poi;
import com.huawei.hms.site.api.model.SearchStatus;
import com.huawei.hms.site.api.model.Site;

import java.util.Arrays;
import java.util.List;

public class NearbyPlacesActivity extends AppCompatActivity implements View.OnClickListener {
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    FusedLocationProviderClient fusedLocationProviderClient;
    SettingsClient settingsClient;

    static Double lat = 0.0, lng = 0.0;

    TextView tv_result_status, tvResult;
    Button btn_Search;

    SearchService searchService;
    Spinner poiTypes;
    EditText edtPageIndex, edtPageSize, edtRadius, edtLanguage;
    Switch poiSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);

        initView();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (null == locationCallback) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    if (locationResult != null) {
                        List<Location> locations = locationResult.getLocations();
                        if (!locations.isEmpty()) {
                            for (Location location : locations) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                if (lat != 0 && lng != 0) {
                                    removeLocationUpdatesWithCallback();
                                }
                            }
                        }
                    }
                }
            };
        }
        requestLocationUpdatesWithCallback();
    }

    SearchResultListener searchResultListener = new SearchResultListener<NearbySearchResponse>() {
        @Override
        public void onSearchResult(NearbySearchResponse nearbySearchResponse) {
            StringBuilder stringBuilder = new StringBuilder();
            if (nearbySearchResponse != null) {
                List<Site> siteList = nearbySearchResponse.getSites();
                if (siteList != null && siteList.size() > 0) {
                    tv_result_status.setText("Success");
                    int count = 1;
                    for (Site site : siteList) {
                        AddressDetail addressDetail = site.getAddress();
                        Poi poi = site.getPoi();
                        Coordinate coordinate = site.getLocation();
                        CoordinateBounds bounds = site.getViewport();

                        stringBuilder.append(String.format("[%s] siteId: '%s', name: %s, formatAddress: %s, country: %s, countryCode: %s, location: %s, poiTypes: %s, viewport is %s \n\n",
                                "" + (count++), site.getSiteId(), site.getName(), site.getFormatAddress(),
                                (addressDetail == null ? "" : addressDetail.getCountry()),
                                (addressDetail == null ? "" : addressDetail.getCountryCode()),
                                (coordinate == null ? "" : (coordinate.getLat() + "," + coordinate.getLng())),
                                (poi == null ? "" : Arrays.toString(poi.getPoiTypes())),
                                (bounds == null ? "" : "northeast{lat=" + bounds.getNortheast().getLat() + ", lng=" + bounds.getNortheast().getLng() + "},"
                                        + "southwest{lat=" + bounds.getSouthwest().getLat() + ", lng=" + bounds.getSouthwest().getLng() + "}")));
                    }
                } else {
                    stringBuilder.append("0 results");
                }
                tvResult.setText(stringBuilder);
            }
        }

        @Override
        public void onSearchError(SearchStatus searchStatus) {
            tvResult.setText("Error:" + searchStatus);
            Toast.makeText(NearbyPlacesActivity.this, "Error:" + searchStatus, Toast.LENGTH_SHORT).show();
        }
    };

    private void findNearbyPlaces() {
        NearbySearchRequest nearbySearchRequest = new NearbySearchRequest();
        Coordinate coordinate = new Coordinate(lat, lng);
        nearbySearchRequest.setLocation(coordinate);

        String radius = edtRadius.getText().toString();
        String language = edtLanguage.getText().toString();
        String pageSize = edtPageSize.getText().toString();
        String pageIndex = edtPageIndex.getText().toString();

        LocationType poi = poiTypes.isEnabled() ? (LocationType) poiTypes.getSelectedItem() : null;
        if(poi != null){
            nearbySearchRequest.setPoiType(poi);
        }
        if (!radius.isEmpty()) {
            Integer radiusValue;
            if ((radiusValue = Utils.parseInt(radius)) > 0) {
                nearbySearchRequest.setRadius(radiusValue);
            }
        }
        if (!language.isEmpty()) {
            nearbySearchRequest.setLanguage(language);
        }
        if (!pageSize.isEmpty()) {
            Integer pageSizeInt;
            if ((pageSizeInt = Utils.parseInt(pageSize)) == null || pageSizeInt < 1 || pageSizeInt > 20) {
                Toast.makeText(this, "PageSize must be between 1 and 20", Toast.LENGTH_SHORT).show();
            }
            nearbySearchRequest.setPageSize(pageSizeInt);
        }
        if (!pageIndex.isEmpty()) {
            Integer pageIndexInt;
            if ((pageIndexInt = Utils.parseInt(pageIndex)) == null || pageIndexInt < 1 || pageIndexInt > 60) {
                Toast.makeText(this, "PageIndex Must be between 1 and 60!", Toast.LENGTH_SHORT).show();
                return;
            }
            nearbySearchRequest.setPageIndex(pageIndexInt);
        }
        searchService.nearbySearch(nearbySearchRequest, searchResultListener);
    }

    private void initView() {

        btn_Search = findViewById(R.id.search_nearby_button2);
        poiTypes = findViewById(R.id.spinner_nearby_place_poitype);
        poiSwitch = findViewById(R.id.switch_nearby_place_poitype);
        edtLanguage = findViewById(R.id.nearby_place_language_input);
        edtRadius = findViewById(R.id.nearby_place_radius_input);
        edtPageIndex = findViewById(R.id.edit_text_nearby_place_pageindex);
        edtPageSize = findViewById(R.id.edit_text_nearby_place_pagesize);
        tvResult = findViewById(R.id.nearby_place_result_text);
        tv_result_status = findViewById(R.id.nearby_place_result_status);

        poiTypes.setEnabled(false);
        poiTypes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arrays.asList(LocationType.values())));
        poiSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            poiTypes.setEnabled(isChecked);
        });
        btn_Search.setOnClickListener(this);

        searchService = SearchServiceFactory.create(this, Utils.getApiKey());
    }

    private void requestLocationUpdatesWithCallback() {
        try {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            LocationSettingsRequest locationSettingsRequest = builder.build();
            // Before requesting location update, invoke checkLocationSettings to check device settings.
            Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequest);
            locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    Log.i("TAG", "check location settings success");
                    fusedLocationProviderClient
                            .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        // When the startResolutionForResult is invoked, a dialog box is displayed, asking you to open the corresponding permission.
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult(NearbyPlacesActivity.this, 0);
                                    } catch (IntentSender.SendIntentException sie) {
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
        } catch (Exception e) {
            Log.e("TAG", "requestLocationUpdatesWithCallback exception:" + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        // Removed when the location update is no longer required.
        removeLocationUpdatesWithCallback();
        super.onDestroy();
        searchService = null;
    }

    private void removeLocationUpdatesWithCallback() {
        try {
            Task<Void> voidTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            voidTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i("TAG", "removeLocationUpdatesWithCallback onSuccess");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Log.e("TAG", "removeLocationUpdatesWithCallback onFailure:" + e.getMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e("TAG", "removeLocationUpdatesWithCallback exception:" + e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_nearby_button2:
                requestLocationUpdatesWithCallback();
                findNearbyPlaces();
                break;
        }
    }
}