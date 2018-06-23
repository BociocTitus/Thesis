package app.com.titus.communityapp.activitiy.report;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.dto.ReportDto;
import app.com.titus.communityapp.service.ReportService;
import app.com.titus.communityapp.service.ServiceFactory;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.ParametersTags;
import app.com.titus.communityapp.util.constant.ParametersUtils;
import app.com.titus.communityapp.util.factory.ToastFactory;
import app.com.titus.communityapp.util.mapper.ReportCategoryMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText detailsText;
    private Spinner spinner;
    private EditText locationEdit;
    private Button reportButton;

    private final String TAG = ReportActivity.class.getName();
    private final String CAMERA_NOT_AVAILABLE = "Camera not available";
    private final int REQUEST_IMAGE_CAPTURE = 2;
    private final int REQUEST_LOCATION = 1;

    private double xCoordinate;
    private double yCoordinate;
    private byte[] reportPhoto;
    private String category;

    private final ReportService reportService = ServiceFactory.createRetrofitService(ReportService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = findViewById(R.id.back);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        imageView = findViewById(R.id.imageView);
        detailsText = findViewById(R.id.editText2);
        spinner = findViewById(R.id.spinner);
        reportButton = findViewById(R.id.report);
        locationEdit = findViewById(R.id.locationEdit);

        Log.v(TAG, "Intent extras:" + getIntent().getStringExtra(ParametersUtils.REPORT_PARAM));
        String reportString = getIntent().getStringExtra(ParametersUtils.REPORT_PARAM);
        if (reportString != null) {
            Log.v(TAG, "Received report" + reportString);
            Gson gson = new Gson();
            ReportDto reportDto = gson.fromJson(reportString, ReportDto.class);
            initWithReport(reportDto);
        } else {
            Log.v(TAG, "No report received");
            initWithoutReport();
        }

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

    private void initWithoutReport() {
        locationEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivityForResult(intent, REQUEST_LOCATION);
        });
        imageView.setOnClickListener(v -> {
            if (!hasCamera())
                ToastFactory.getToast(this, CAMERA_NOT_AVAILABLE).show();
            launchCamera();
        });
        initSpinnerValues();
        reportButton.setOnClickListener(v -> saveReport());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void initWithReport(ReportDto reportDto) {
        String address = MapsActivity.getAddresses(new LatLng(reportDto.getXCoordinate(), reportDto.getYCoordinate()), this);
        List<String> spinnerValues = ReportCategoryMapper.getCategoryStrings();
        final int INDEX = spinnerValues.indexOf(reportDto.getCategory());
        byte[] decoded = Base64.getDecoder().decode(reportDto.getImage());
        Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);

        locationEdit.setText(address);
        imageView.setImageBitmap(bitmap);
        detailsText.setText(reportDto.getDetails());
        detailsText.setEnabled(Boolean.FALSE);
        spinner.setSelection(INDEX);
        spinner.setEnabled(Boolean.FALSE);

        initSpinnerValues();
        reportButton.setEnabled(Boolean.FALSE);
        reportButton.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                if (resultCode == RESULT_OK) {
                    xCoordinate = data.getDoubleExtra(ParametersTags.LATITUDE_TAG, 0);
                    yCoordinate = data.getDoubleExtra(ParametersTags.LONGITUDE_TAG, 0);
                    Log.v(TAG, "received coords:" + xCoordinate + ConstantUtils.EMPTY_STRING + yCoordinate);
                    String address = data.getStringExtra(ParametersTags.ADDRESS_TAG);
                    if (address != null) {
                        locationEdit.setText(address);
                    }
                    Log.v(TAG, "Result from get coordinates:" + xCoordinate + +yCoordinate);
                } else if (resultCode == RESULT_CANCELED) {
                    return;
                } else {
                    String LOCATION_ERROR = "There were errors on providing location";
                    ToastFactory.getToast(this, LOCATION_ERROR).show();
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap photo = (Bitmap) extras.get("data");
                        if (photo != null) {
                            imageView.setImageBitmap(photo);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            reportPhoto = stream.toByteArray();
                        }
                    }
                } else {
                    String CAMERA_ERROR = "There were errors on taking picture";
                    ToastFactory.getToast(this, CAMERA_ERROR).show();
                }
                break;
        }
    }

    //Check if the user has a camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void saveReport() {
        Log.v(TAG, "saveReport called");
        String details = detailsText.getText().toString();
        if (category == null || xCoordinate == 0.0 || yCoordinate == 0.0 || reportPhoto == null) {
            Log.v(TAG, category + ConstantUtils.EMPTY_STRING + xCoordinate + ConstantUtils.EMPTY_STRING + yCoordinate + ConstantUtils.EMPTY_STRING);
            ToastFactory.getToast(this, "Please complete location, photo and category").show();
        } else {
            ReportDto reportDto = null;
            try {
                reportDto = ReportDto.builder()
                        .category(category)
                        .details(details)
                        .xCoordinate(xCoordinate)
                        .yCoordinate(yCoordinate)
                        .image(new String(Base64.getEncoder().encode(reportPhoto), "UTF-8"))
                        .date(LocalDate.now().toString())
                        .isActive(Boolean.TRUE)
                        .build();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.v(TAG, "Saving reportDto:" + reportDto);
            Call<Boolean> addReportTask = reportService.saveReport(ConstantUtils.JWT, reportDto);
            Callback<Boolean> res = new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, Response<Boolean> response) {
                    if (!response.isSuccessful()) {
                        createSaveFailureToast("Unable to save report");
                        return;
                    }
                    Log.v(TAG, "saveReport result:" + response);
                    finish();
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    createSaveFailureToast(t.getMessage());
                }
            };
            addReportTask.enqueue(res);
        }

    }

    private void initSpinnerValues() {
        List<String> spinnerValues = ReportCategoryMapper.getCategoryStrings();
        final ArrayAdapter<String> spinnerListAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, spinnerValues);
        spinner.setAdapter(spinnerListAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category = (String) adapterView.getAdapter().getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void createSaveFailureToast(String message) {
        ToastFactory.getToast(this, message).show();
    }

}
