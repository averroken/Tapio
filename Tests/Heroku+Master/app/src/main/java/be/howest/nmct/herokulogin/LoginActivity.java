package be.howest.nmct.herokulogin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.howest.nmct.herokulogin.models.LoginResponse;
import be.howest.nmct.herokulogin.models.TokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button getButton, postButton;
    private Retrofit retrofit;
    private TapioService service;
    private String token = null;
    private Context activityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activityContext = this;


        initWidgets();
        initRetrofit();
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<LoginResponse> call = service.logIn(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());

                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        Integer code = response.code();
                        if (code  == 200){
                            LoginResponse login = response.body();
                            Log.d("login", login.getUsername());
                            Log.d("login", login.getToken());
                            token = login.getToken();
                            Toast.makeText(activityContext, "Logged In", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(activityContext, "Login failed", Toast.LENGTH_LONG).show();
                            passwordEditText.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Log.d("login", t.getMessage());
                    }
                });
            }
        });

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token != null){
                    Call<TokenResponse> tokenCall = service.testToken(token);

                    tokenCall.enqueue(new Callback<TokenResponse>() {
                        @Override
                        public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                            Integer code = response.code();
                            if (code != 200){
                                Toast.makeText(activityContext, "Error. Please login again", Toast.LENGTH_LONG).show();
                            }else {
                                TokenResponse token = response.body();
                                Log.d("login", "token: " + token.getMessage());
                                Toast.makeText(activityContext, token.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TokenResponse> call, Throwable t) {
                            Log.d("login", "token: FAILED");
                        }
                    });
                }else {
                    Toast.makeText(activityContext, "FAILED: no token found", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://back-end-tapio-test.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TapioService.class);
    }

    private void initWidgets() {
        usernameEditText = (EditText) findViewById(R.id.usernameInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);
        getButton = (Button) findViewById(R.id.loginButtonGet);
        postButton = (Button) findViewById(R.id.loginButtonPost);
    }
}
