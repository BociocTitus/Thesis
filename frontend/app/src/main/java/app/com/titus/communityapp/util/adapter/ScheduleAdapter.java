package app.com.titus.communityapp.util.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.dto.PublicSpaceDto;
import app.com.titus.communityapp.dto.ScheduleDto;
import app.com.titus.communityapp.dto.ScheduleJoinDto;
import app.com.titus.communityapp.service.PublicSpaceService;
import app.com.titus.communityapp.service.ServiceFactory;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.factory.ToastFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleAdapter extends ArrayAdapter<ScheduleDto> {
    private List<ScheduleDto> scheduleDtos;
    private Context context;
    private PublicSpaceService publicSpaceService = ServiceFactory.createRetrofitService(PublicSpaceService.class);
    private PublicSpaceDto publicSpaceDto;
    private SharedPreferences sharedPreferences;
    private final String SHARED_PREFFERENCES_MODE = "userdetails";

    public ScheduleAdapter(@NonNull Context context, List<ScheduleDto> scheduleDtos, PublicSpaceDto publicSpaceDto) {
        super(context, R.layout.space_view, scheduleDtos);
        this.context = context;
        this.scheduleDtos = scheduleDtos;
        this.publicSpaceDto = publicSpaceDto;
        sharedPreferences = context.getSharedPreferences(SHARED_PREFFERENCES_MODE, Context.MODE_PRIVATE);
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.schedule_view, null);
        }

        ScheduleDto scheduleDto = scheduleDtos.get(position);

        if (scheduleDto != null) {
            TextView startView = v.findViewById(R.id.start_date);
            TextView endView = v.findViewById(R.id.end_date);
            TextView statusView = v.findViewById(R.id.status);
            Button button = v.findViewById(R.id.join_button);

            startView.setText(scheduleDto.getBegin());
            endView.setText(scheduleDto.getEnd());
            if (scheduleDto.getScheduleJoinDtoList() == null) {
                scheduleDto.setScheduleJoinDtoList(new ArrayList<>());
                scheduleDto.setIsFull(Boolean.FALSE);
            } else {
                scheduleDto.setIsFull(
                        scheduleDto.getScheduleJoinDtoList().stream()
                                .mapToInt(ScheduleJoinDto::getMembers)
                                .sum() >= publicSpaceDto.getCapacity()
                );
            }

            if (scheduleDto.getIsFull()) {
                final String FULL = "Full";
                statusView.setText(FULL);
                statusView.setTextColor(Color.RED);
                button.setVisibility(View.INVISIBLE);
            } else {
                final long STATUS;
                if (scheduleDto.getScheduleJoinDtoList() != null) {
                    STATUS = scheduleDto.getScheduleJoinDtoList().stream().mapToInt(ScheduleJoinDto::getMembers).sum();
                } else {
                    STATUS = 0L;
                }

                statusView.setText(Long.toString(STATUS));
                statusView.setTextColor(Color.GREEN);
                button.setOnClickListener(buttonClick -> {
                    onCreateDialog(scheduleDto).show();
                });
            }
        }
        return v;
    }

    private Dialog onCreateDialog(ScheduleDto scheduleDto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.activity_schedule_join, null);
        builder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.members_edit);

        builder.setTitle("How many members?");
        builder.setPositiveButton("Save", (dialogInterface, i) -> {
            long id = sharedPreferences.getLong("ID", -1);
            if (id == -1) {
                throw new RuntimeException("State error");
            }
            ScheduleJoinDto scheduleJoinDto = ScheduleJoinDto.builder()
                    .members(Integer.parseInt(editText.getText().toString()))
                    .userId(id)
                    .build();

            if (sumOfMembers(scheduleDto) + scheduleJoinDto.getMembers() <= publicSpaceDto.getCapacity()) {
                scheduleDto.getScheduleJoinDtoList().add(scheduleJoinDto);
                notifyDataSetChanged();
                savePublicSpace();
            } else {
                ToastFactory.getToast(context, "Too many people");
            }

        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        });

        return builder.create();
    }

    private void savePublicSpace() {
        Call<PublicSpaceDto> call = this.publicSpaceService.updatePublicSpace(ConstantUtils.JWT, publicSpaceDto);
        Callback<PublicSpaceDto> callback = new Callback<PublicSpaceDto>() {
            @Override
            public void onResponse(@NonNull Call<PublicSpaceDto> call, @NonNull Response<PublicSpaceDto> response) {

            }

            @Override
            public void onFailure(@NonNull Call<PublicSpaceDto> call, @NonNull Throwable t) {

            }
        };
        call.enqueue(callback);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private long sumOfMembers(ScheduleDto scheduleDto) {
        return scheduleDto.getScheduleJoinDtoList().stream().mapToInt(ScheduleJoinDto::getMembers)
                .sum();
    }
}
