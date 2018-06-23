package app.com.titus.communityapp.service;

import java.util.List;

import app.com.titus.communityapp.dto.PublicSpaceDto;
import app.com.titus.communityapp.dto.ScheduleDto;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PublicSpaceService {
    @Headers({ConstantUtils.HEADERS})
    @GET("getPublicSpaces/{category}")
    Call<List<PublicSpaceDto>> getPublicSpaces(@Header("Authorization") String headers, @Path("category") String category);

    @Headers({ConstantUtils.HEADERS})
    @POST("updatePublicSpace")
    Call<PublicSpaceDto> updatePublicSpace(@Header("Authorization") String headers, @Body PublicSpaceDto publicSpaceDto);

    @Headers({ConstantUtils.HEADERS})
    @GET("/getSchedulesForPublicSpace/{id}")
    Call<List<ScheduleDto>> getSchedulesForPublicSpace(@Header("Authorization") String headers, @Path("id") Long id);
}
