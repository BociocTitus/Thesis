package app.com.titus.communityapp.util.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.activitiy.report.ReportActivity;
import app.com.titus.communityapp.dto.ReportDto;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import app.com.titus.communityapp.util.constant.ParametersUtils;

public class ReportAdapter extends ArrayAdapter<ReportDto> {

    private static final String AT = " at ";
    private List<ReportDto> reportDtos;
    private Context context;

    public ReportAdapter(@NonNull Context context, List<ReportDto> reportDtoList) {
        super(context, R.layout.reports_view, reportDtoList);
        this.context = context;
        this.reportDtos = reportDtoList;
    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Log.v("adapter", "entered adapter");
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.reports_view, null);
        }

        ReportDto reportDto = reportDtos.get(position);

        if (reportDto != null) {
            final String OPEN_TEXT = "OPEN";
            final String CLOSED_TEXT = "CLOSED";

            TextView textView = v.findViewById(R.id.report_description);
            ImageView imageView = v.findViewById(R.id.report_image);
            TextView category_view = v.findViewById(R.id.report_category);
            TextView status_view = v.findViewById(R.id.status_text);
            CardView cardView = v.findViewById(R.id.status_card);

            category_view.setText(reportDto.getCategory());
            StringBuilder titleBuilder = new StringBuilder();

            String address = getAddresses(new LatLng(reportDto.getXCoordinate(), reportDto.getYCoordinate()));
            titleBuilder.append(reportDto.getCategory());
            titleBuilder.append(AT);
            titleBuilder.append(address);
            category_view.setText(titleBuilder.toString());

            textView.setText(reportDto.getDetails());

            byte[] decoded = Base64.getDecoder().decode(reportDto.getImage());
            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            imageView.setImageBitmap(bitmap);
            if (reportDto.isActive()) {
                cardView.setBackgroundColor(Color.RED);
                status_view.setText(OPEN_TEXT);
            } else {
                cardView.setBackgroundColor(Color.GREEN);
                status_view.setText(CLOSED_TEXT);
            }

            v.setOnClickListener(view -> {
                Intent intent = new Intent(context, ReportActivity.class);
                Gson gson = new Gson();
                intent.putExtra(ParametersUtils.REPORT_PARAM, gson.toJson(reportDto));
                context.startActivity(intent);
            });
        }


        return v;
    }

    private String getAddresses(LatLng position) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                return returnedAddress.getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ConstantUtils.EMPTY_STRING;
    }
}
