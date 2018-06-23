package app.com.titus.communityapp.activitiy.authentication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.dto.UserDto;
import app.com.titus.communityapp.service.AuthenticationService;
import app.com.titus.communityapp.service.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private final AuthenticationService authenticationService = ServiceFactory.createRetrofitService(AuthenticationService.class);

    private Button registerButton;
    private EditText username;
    private EditText password;
    private final String TAG = RegisterActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            Log.v(TAG, "Register called:");
            Boolean isValidated = username.getText().toString().length() >= 5 && password.getText().toString().length() >= 5;
            if (!isValidated)
                return;
            UserDto userDto = new UserDto(username.getText().toString(), password.getText().toString());
            Call<Boolean> task = authenticationService.register(userDto);
            Callback<Boolean> res = new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, Response<Boolean> response) {
                    Log.v(TAG, "Res:" + response.body());
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, Throwable t) {
                    t.printStackTrace();
                }
            };
            task.enqueue(res);
        });

    }
}
