package app.com.titus.communityapp.activitiy.rent;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.dto.PublicSpaceDto;
import app.com.titus.communityapp.dto.ScheduleDto;
import app.com.titus.communityapp.dto.ScheduleJoinDto;
import app.com.titus.communityapp.service.PublicSpaceService;
import app.com.titus.communityapp.service.ServiceFactory;
import app.com.titus.communityapp.util.ParametersTags;
import app.com.titus.communityapp.util.adapter.ScheduleAdapter;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.factory.ToastFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchedulesViewActivity extends AppCompatActivity {

    private PublicSpaceDto publicSpaceDto;
    private PublicSpaceService publicSpaceService = ServiceFactory.createRetrofitService(PublicSpaceService.class);
    private SharedPreferences sharedPreferences;
    private Calendar calendar;
    private EditText dateEdit;
    private EditText timeEdit;

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedules_view);

        Gson gson = new Gson();
        publicSpaceDto = gson.fromJson(getIntent().getStringExtra(ParametersTags.PUBLIC_SPACE_TAG), PublicSpaceDto.class);

        List<ScheduleDto> scheduleDtoList = publicSpaceDto.getSchedules();
        ListView scheduleListView = findViewById(R.id.view_schedules);
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(this, scheduleDtoList, publicSpaceDto);
        scheduleListView.setAdapter(scheduleAdapter);
        Button saveSchedule = findViewById(R.id.save_schedule);
        calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener time = (timePicker, hour, minute) -> {
            calendar.set(Calendar.HOUR, hour);
            calendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        };

        timeEdit = findViewById(R.id.edit_time);
        timeEdit.setOnClickListener(v ->
                new TimePickerDialog(SchedulesViewActivity.this, time, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), Boolean.TRUE)
                        .show());
        timeEdit.setShowSoftInputOnFocus(Boolean.FALSE);

        DatePickerDialog.OnDateSetListener date = (datePicker, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        dateEdit = findViewById(R.id.edit_date);
        dateEdit.setOnClickListener(v -> new DatePickerDialog(SchedulesViewActivity.this, date,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show());
        dateEdit.setShowSoftInputOnFocus(Boolean.FALSE);

        EditText membersEdit = findViewById(R.id.edit_members);

        String SHARED_PREFFERENCES_MODE = "userdetails";
        sharedPreferences = getSharedPreferences(SHARED_PREFFERENCES_MODE, Context.MODE_PRIVATE);

        saveSchedule.setOnClickListener(v -> {
            if (dateEdit.getText().equals(ConstantUtils.EMPTY_STRING)) {
                createErrorToast();
                return;
            }
            LocalDate localDate = LocalDate.parse(dateEdit.getText().toString());
            LocalTime localTime = LocalTime.parse(timeEdit.getText().toString());
            LocalDateTime setDate = LocalDateTime.of(localDate, localTime);
            ScheduleDto scheduleDto = ScheduleDto.builder()
                    .begin(setDate.toString())
                    .end(setDate.plusHours(1L).toString())
                    .build();
            long id = sharedPreferences.getLong("ID", -1);
            ScheduleJoinDto scheduleJoinDto = ScheduleJoinDto.builder()
                    .members(Integer.parseInt(membersEdit.getText().toString()))
                    .userId(id)
                    .build();

            List<ScheduleJoinDto> scheduleJoinDtos = new ArrayList<>();
            scheduleJoinDtos.add(scheduleJoinDto);
            scheduleDto.setScheduleJoinDtoList(scheduleJoinDtos);

            if (publicSpaceDto.getSchedules() == null) {
                publicSpaceDto.setSchedules(new ArrayList<>());
            }
            publicSpaceDto.getSchedules().add(scheduleDto);
            Call<PublicSpaceDto> call = publicSpaceService.updatePublicSpace(ConstantUtils.JWT, publicSpaceDto);
            Callback<PublicSpaceDto> callback = new Callback<PublicSpaceDto>() {
                @Override
                public void onResponse(@NonNull Call<PublicSpaceDto> call, @NonNull Response<PublicSpaceDto> response) {
                    createSuccessToast();
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getId() != -1) {
                            publicSpaceDto = response.body();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<PublicSpaceDto> call, @NonNull Throwable t) {

                }
            };
            call.enqueue(callback);
        });
    }


    private void createSuccessToast() {
        final String SUCCESS = "Success!";
        ToastFactory.getToast(this, SUCCESS).show();
    }

    private void createErrorToast() {
        final String INVALID = "Invalid data";
        ToastFactory.getToast(this, INVALID).show();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void updateLabel() {
        dateEdit.setText(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void updateTimeLabel() {
        timeEdit.setText(calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString());
    }
}