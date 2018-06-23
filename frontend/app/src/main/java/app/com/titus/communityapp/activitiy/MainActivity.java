package app.com.titus.communityapp.activitiy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.activitiy.rent.RentActivity;
import app.com.titus.communityapp.activitiy.report.ReportActivity;
import app.com.titus.communityapp.activitiy.report.ViewReports;

public class MainActivity extends Activity {

    private CardView makeReport;
    private CardView viewReports;
    private CardView viewSpaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeReport = findViewById(R.id.report);
        viewReports = findViewById(R.id.view_reports);
        viewSpaces = findViewById(R.id.spaces);

        makeReport.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
        });
        viewReports.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewReports.class);
            startActivity(intent);
        });
        viewSpaces.setOnClickListener(v -> {
            Intent intent = new Intent(this, RentActivity.class);
            startActivity(intent);
        });
        askPersmissions();
    }

    private void askPersmissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA}, 1);
    }
}
