package app.com.titus.communityapp.activitiy.report;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.dto.ReportDto;
import app.com.titus.communityapp.service.ReportService;
import app.com.titus.communityapp.service.ServiceFactory;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.adapter.ReportAdapter;
import app.com.titus.communityapp.util.constant.ParametersUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewReports extends AppCompatActivity {

    private ProgressBar progressBar;

    private final String TAG = ViewReports.class.getName();

    private ListView listView;

    private ReportService reportService = ServiceFactory.createRetrofitService(ReportService.class);

    private List<ReportDto> reportDtoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);
        Button mapView = findViewById(R.id.button_map_view);
        Toolbar backButton = findViewById(R.id.back);
        progressBar = findViewById(R.id.progressBar);

        listView = findViewById(R.id.reports_list);

        getReports();


        setSupportActionBar(backButton);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mapView.setOnClickListener(v -> goToViewOnMap());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void goToViewOnMap() {
        Gson gson = new Gson();
        ArrayList<String> jsonReports = reportDtoList.stream().map(gson::toJson)
                .collect(Collectors.toCollection(ArrayList::new));
        Intent intent = new Intent(this, ReportMapViewActivity.class);
        intent.putStringArrayListExtra(ParametersUtils.REPORTS_PARAM, jsonReports);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void getReports() {
        Call<List<ReportDto>> call = reportService.getReports(ConstantUtils.JWT);
        progressBar.setVisibility(View.VISIBLE);
        Callback<List<ReportDto>> callback = new Callback<List<ReportDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReportDto>> call, Response<List<ReportDto>> response) {
                if (response.isSuccessful()) {
                    Log.v(TAG, "Received reports:" + response.body());
                    reportDtoList = response.body();
                    ReportAdapter reportsAdapter = new ReportAdapter(getApplicationContext(), reportDtoList);
                    listView.setAdapter(reportsAdapter);
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Log.v(TAG, "Could not retrieve reports");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReportDto>> call, Throwable t) {
                Log.v(TAG, "Error occured:" + t.getMessage());
            }
        };
        call.enqueue(callback);
    }
}
