package app.com.titus.communityapp.activitiy.rent;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.dto.PublicSpaceDto;
import app.com.titus.communityapp.dto.ScheduleDto;
import app.com.titus.communityapp.service.PublicSpaceService;
import app.com.titus.communityapp.service.ServiceFactory;
import app.com.titus.communityapp.util.ParametersTags;
import app.com.titus.communityapp.util.adapter.ScheduleAdapter;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.constant.ParametersUtils;
import app.com.titus.communityapp.util.factory.ToastFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewSpaceActivity extends AppCompatActivity {

    private PublicSpaceService publicSpaceService = ServiceFactory.createRetrofitService(PublicSpaceService.class);
    private TextView nameView;
    private TextView detailsView;
    private TextView addressView;
    private TextView capacityView;
    private PublicSpaceDto publicSpaceDto;
    private Gson gson = new Gson();

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_space);

        nameView = findViewById(R.id.name);
        detailsView = findViewById(R.id.details);
        addressView = findViewById(R.id.address);
        capacityView = findViewById(R.id.capacity);
        Button viewOnMap = findViewById(R.id.view_on_map_button);
        Button viewSchedules = findViewById(R.id.view_schedules);

        String spaceString = getIntent().getStringExtra(ParametersUtils.PUBLIC_SPACE_PARAM);
        if (spaceString != null) {
            publicSpaceDto = gson.fromJson(spaceString, PublicSpaceDto.class);
            initFields();
        }

        viewOnMap.setOnClickListener(v -> {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", publicSpaceDto.getXCoordinate(), publicSpaceDto.getYCoordinate());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

        viewSchedules.setOnClickListener(v -> {
            Call<List<ScheduleDto>> call = publicSpaceService.getSchedulesForPublicSpace(ConstantUtils.JWT, publicSpaceDto.getId());
            Callback<List<ScheduleDto>> callback = new Callback<List<ScheduleDto>>() {
                @Override
                public void onResponse(Call<List<ScheduleDto>> call, Response<List<ScheduleDto>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        publicSpaceDto.setSchedules(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<ScheduleDto>> call, Throwable t) {

                }
            };
            call.enqueue(callback);
            Intent intent = new Intent(this, SchedulesViewActivity.class);
            String publicSpaceJson = gson.toJson(publicSpaceDto);
            intent.putExtra(ParametersTags.PUBLIC_SPACE_TAG, publicSpaceJson);
            startActivity(intent);
        });
    }

    private void initFields() {
        nameView.setText(publicSpaceDto.getName());
        detailsView.setText(publicSpaceDto.getDetails());
        addressView.setText(publicSpaceDto.getAddress());
        capacityView.setText(Integer.toString(publicSpaceDto.getCapacity()));
    }


}
