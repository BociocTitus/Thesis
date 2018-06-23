package app.com.titus.communityapp.activitiy.rent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.dto.PublicSpaceDto;
import app.com.titus.communityapp.service.PublicSpaceService;
import app.com.titus.communityapp.service.ServiceFactory;
import app.com.titus.communityapp.util.adapter.SpaceAdapter;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.mapper.ActivityCategoryMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RentActivity extends AppCompatActivity {

    private Spinner spinner;
    private ListView listView;
    private PublicSpaceService publicSpaceService = ServiceFactory.createRetrofitService(PublicSpaceService.class);
    private final String TAG = RentActivity.class.getName();
    private List<PublicSpaceDto> publicSpaceDtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);

        spinner = findViewById(R.id.spinner2);
        listView = findViewById(R.id.space_list);
        Button goButton = findViewById(R.id.button);

        initSpinnerValues();
        goButton.setOnClickListener(v -> {
            Log.v(TAG, "goButton clicked:" + spinner.getSelectedItem().toString());
            Log.v(TAG, spinner.getSelectedItem().toString());
            Call<List<PublicSpaceDto>> call = publicSpaceService.getPublicSpaces(ConstantUtils.JWT, spinner.getSelectedItem().toString());
            Callback<List<PublicSpaceDto>> callback = new Callback<List<PublicSpaceDto>>() {
                @Override
                public void onResponse(@NonNull Call<List<PublicSpaceDto>> call, Response<List<PublicSpaceDto>> response) {
                    if (response.isSuccessful()) {
                        Log.v(TAG, "Received public space:" + response.body());
                        publicSpaceDtos = response.body();
                        SpaceAdapter spaceAdapter = new SpaceAdapter(getApplicationContext(), publicSpaceDtos);
                        listView.setAdapter(spaceAdapter);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<PublicSpaceDto>> call, @NonNull Throwable t) {
                    Log.v(TAG, t.getMessage());
                }
            };
            call.enqueue(callback);
        });
    }

    private void initSpinnerValues() {
        List<String> spinnerValues = ActivityCategoryMapper.getCategoryStrings();
        final ArrayAdapter<String> spinnerListAdapter = new ArrayAdapter<>(this
                , R.layout.support_simple_spinner_dropdown_item, spinnerValues);

        spinner.setAdapter(spinnerListAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
