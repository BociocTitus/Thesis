package app.com.titus.communityapp.activitiy.authentication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashMap;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.activitiy.MainActivity;
import app.com.titus.communityapp.dto.AccountCredentials;
import app.com.titus.communityapp.service.AuthenticationService;
import app.com.titus.communityapp.service.ServiceFactory;
import app.com.titus.communityapp.util.constant.ConstantUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private final AuthenticationService authenticationService = ServiceFactory.createRetrofitService(AuthenticationService.class);
    private final String TAG = "LoginActivity";
    private final String TOKEN = "Token";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private EditText username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final String SHARED_PREFFERENCES_MODE = "userdetails";
        sharedPreferences = getSharedPreferences(SHARED_PREFFERENCES_MODE, Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        verifyIfLoggedIn();

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        registerButton.setOnClickListener(v -> {
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            startActivity(registerIntent);
        });
        loginButton.setOnClickListener(v -> {
            final String usernameString = username.getText().toString();
            final String passwordString = password.getText().toString();

            Log.v(TAG, "login called:" + usernameString + " " + passwordString);
            AccountCredentials accountCredentials = AccountCredentials.
                    builder()
                    .username(usernameString)
                    .password(passwordString)
                    .build();
            Call<HashMap> loginTask = authenticationService.login(accountCredentials);
            Callback<HashMap> res = new Callback<HashMap>() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onResponse(@NonNull Call<HashMap> call, Response<HashMap> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        Log.v(TAG, "Login failed");
                        return;
                    }
                    Log.v(TAG, "login result:" + response.body());
                    sharedPreferencesEditor.putString(TOKEN, (String) response.body().get(TOKEN));
                    sharedPreferencesEditor.putString("ROLE", (String) response.body().get("Role"));
                    sharedPreferencesEditor.putLong("ID", Math.round((Double) response.body().get("ID")));
                    sharedPreferencesEditor.commit();
                    ConstantUtils.JWT = (String) response.body().get(TOKEN);
                    verifyIfLoggedIn();
                }

                @Override
                public void onFailure(@NonNull Call<HashMap> call, Throwable t) {
                    Log.v(TAG, t.getMessage());
                }
            };
            loginTask.enqueue(res);
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void verifyIfLoggedIn() {
        String jwt = sharedPreferences.getString(TOKEN, null);
        String role = sharedPreferences.getString("ROLE", null);
        Log.v(TAG, jwt + role);
        if (jwt != null && role != null) {
            ConstantUtils.JWT = jwt;
            goToMainActivity();
        }
    }

}
