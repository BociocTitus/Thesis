package app.com.titus.communityapp.util.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.activitiy.rent.ViewSpaceActivity;
import app.com.titus.communityapp.dto.PublicSpaceDto;
import app.com.titus.communityapp.util.constant.ParametersUtils;

public class SpaceAdapter extends ArrayAdapter<PublicSpaceDto> {

    private List<PublicSpaceDto> spaceDtoList;
    private Context context;

    public SpaceAdapter(@NonNull Context context, List<PublicSpaceDto> publicSpaceDtos) {
        super(context, R.layout.space_view, publicSpaceDtos);
        this.context = context;
        this.spaceDtoList = publicSpaceDtos;
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.space_view, null);
        }

        PublicSpaceDto publicSpaceDto = spaceDtoList.get(position);

        if (publicSpaceDto != null) {
            TextView nameView = v.findViewById(R.id.textView);
            TextView capacityView = v.findViewById(R.id.textView2);
            Button button = v.findViewById(R.id.space_details_button);

            nameView.setText(publicSpaceDto.getName());
            capacityView.setText(Integer.toString(publicSpaceDto.getCapacity()));
            button.setOnClickListener(ev -> {
                Intent intent = new Intent(context, ViewSpaceActivity.class);
                Gson gson = new Gson();
                intent.putExtra(ParametersUtils.PUBLIC_SPACE_PARAM, gson.toJson(publicSpaceDto));
                context.startActivity(intent);
            });
        }

        return v;
    }
}
