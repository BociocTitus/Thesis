package app.com.titus.communityapp.service;

import java.util.List;

import app.com.titus.communityapp.dto.ReportDto;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ReportService {
    @Headers({ConstantUtils.HEADERS})
    @POST("saveReport")
    Call<Boolean> saveReport(@Header("Authorization") String headers, @Body ReportDto reportDto);

    @Headers({ConstantUtils.HEADERS})
    @GET("getReports")
    Call<List<ReportDto>> getReports(@Header("Authorization") String headers);

    @Headers({ConstantUtils.HEADERS})
    @GET("getActiveReports")
    Call<List<ReportDto>> getActiveReports(@Header("Authorization") String headers);
}
