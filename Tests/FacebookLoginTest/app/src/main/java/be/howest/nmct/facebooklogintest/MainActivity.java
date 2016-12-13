package be.howest.nmct.facebooklogintest;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import be.howest.nmct.facebooklogintest.models.facebookTokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView info, emailTextview;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Retrofit retrofit;
    private TapioService service;
    private View view;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        view = findViewById(R.id.activity_main);

        initRetrofit();

        initwidgets();
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://back-end-tapio-test.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TapioService.class);
    }

    private void initwidgets() {
        info = (TextView) findViewById(R.id.info);
        emailTextview = (TextView) findViewById(R.id.email);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        initFacebookSignIn();
    }

    private void initFacebookSignIn() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String authToken = loginResult.getAccessToken().getToken();
                info.setText("user id: " + loginResult.getAccessToken().getUserId() +
                        "\n AuthToken: " + authToken);

                loginTapioWithToken(authToken);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String email = object.getString("email");
//                                    String birthday = object.getString("birthday"); // 01/31/1980 format
                                    emailTextview.setText(email);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                info.setText("Login cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login failed");
            }
        });
    }

    private void loginTapioWithToken(String facebookToken) {
        Call<facebookTokenResponse> call = service.facebookTokenLogin(facebookToken);

        call.enqueue(new Callback<facebookTokenResponse>() {
            @Override
            public void onResponse(Call<facebookTokenResponse> call, Response<facebookTokenResponse> response) {
                Integer code = response.code();
                if (code == 200) {
                    Snackbar.make(view, "Facebook login succeeded", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                        Snackbar.make(view, "Facebook login failed", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<facebookTokenResponse> call, Throwable t) {
                Log.d("login", t.getMessage());
            }
        });
    }
}
