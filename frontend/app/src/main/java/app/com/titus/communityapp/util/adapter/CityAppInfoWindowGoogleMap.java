package app.com.titus.communityapp.util.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.activitiy.report.ReportActivity;
import app.com.titus.communityapp.dto.ReportDto;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.constant.ParametersUtils;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CityAppInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater()
                .inflate(R.layout.info_window_marker, null);

        TextView name_tv = view.findViewById(R.id.title);
        TextView details_tv = view.findViewById(R.id.description);
        TextView status = view.findViewById(R.id.status);

        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());
        ReportDto reportDto = (ReportDto) marker.getTag();
        if(reportDto.isActive()) {
            status.setText(ConstantUtils.OPEN);
            status.setTextColor(Color.RED);
        } else {
            status.setText(ConstantUtils.CLOSED);
            status.setTextColor(Color.GREEN);
        }

        return view;
    }

}
