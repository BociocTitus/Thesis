package app.com.titus.communityapp.service;

import org.json.JSONObject;

import java.util.HashMap;

import app.com.titus.communityapp.dto.AccountCredentials;
import app.com.titus.communityapp.dto.UserDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface AuthenticationService {
    @POST("register")
    Call<Boolean> register(@Body UserDto userDto);

    @POST("login")
    Call<HashMap> login(@Body AccountCredentials accountCredentials);
}
