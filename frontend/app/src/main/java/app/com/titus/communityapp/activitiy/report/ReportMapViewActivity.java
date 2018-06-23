package app.com.titus.communityapp.activitiy.report;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.List;
import java.util.stream.Collectors;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.dto.ReportDto;
import app.com.titus.communityapp.util.adapter.CityAppInfoWindowGoogleMap;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.constant.ParametersUtils;

public class ReportMapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String TAG = this.getClass().getName();
    private GoogleMap mMap;
    private List<ReportDto> reportDtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_view);
        Toolbar toolbar = findViewById(R.id.back);

        getReports();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.v(TAG, "Map Ready");
        mMap = googleMap;
        CityAppInfoWindowGoogleMap cityAppInfoWindowGoogleMap = new CityAppInfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(cityAppInfoWindowGoogleMap);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ConstantUtils.clujLatitude, ConstantUtils.clujLongitude)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(ConstantUtils.ZOOM));

        reportDtos.forEach(reportDto -> {
            Float color;
            if (reportDto.isActive()) {
                color = BitmapDescriptorFactory.HUE_RED;
            } else {
                color = BitmapDescriptorFactory.HUE_GREEN;
            }
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(color))
                    .position(new LatLng(reportDto.getXCoordinate(), reportDto.getYCoordinate()))
                    .title(reportDto.getCategory())
                    .snippet(reportDto.getDetails()));
            marker.setTag(reportDto);
        });
        mMap.setOnInfoWindowClickListener(marker -> {
            ReportDto reportDto = (ReportDto) marker.getTag();
            Intent intent = new Intent(this, ReportActivity.class);
            Gson gson = new Gson();
            intent.putExtra(ParametersUtils.REPORT_PARAM, gson.toJson(reportDto));
            startActivity(intent);
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void getReports() {
        Gson gson = new Gson();
        reportDtos = getIntent().getStringArrayListExtra(ParametersUtils.REPORTS_PARAM)
                .stream().map(s -> gson.fromJson(s, ReportDto.class)).collect(Collectors.toList());
    }
}
