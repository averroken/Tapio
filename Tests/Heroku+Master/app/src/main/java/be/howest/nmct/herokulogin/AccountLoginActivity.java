package be.howest.nmct.herokulogin;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import be.howest.nmct.herokulogin.auth.Contract;
import be.howest.nmct.herokulogin.models.LoginResponse;
import be.howest.nmct.herokulogin.models.TokenResponse;
import be.howest.nmct.herokulogin.models.facebookTokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountLoginActivity extends Activity {
    private AccountManager mAccountManager;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;
    private EditText usernameEditText, passwordEditText;
    private Button signInButton;
    private Context mContext;
    private TapioService service;
    private String token = null, usernameString, passwordString;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Retrofit retrofit;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.account_activity_login);
        view = findViewById(R.id.activity_login);

        initWidgets();
        initRetrofit();


        mAccountManager = AccountManager.get(this);
        mAccountAuthenticatorResponse = this.getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    private void initWidgets() {
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signInButton = (Button) findViewById(R.id.signInButton);
        mContext = this;

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameString = usernameEditText.getText().toString();
                if (usernameString.isEmpty()) {
                    Toast.makeText(mContext, "Username can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    passwordString = passwordEditText.getText().toString();
                    LoginToTapio(usernameString, passwordString);
                }
            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        initFacebookSignIn();
    }

    private void addAccount(String token) {
            Account[] accountsByType = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
            Account account;

            if (accountsByType.length == 0) {
                account = new Account(usernameString, Contract.ACCOUNT_TYPE);
                mAccountManager.addAccountExplicitly(account, null, null);
                mAccountManager.setAuthToken(account, Contract.AUTH_TYPE, token);
            } else if (!usernameString.equals(accountsByType[0].name)) {
                mAccountManager.removeAccount(accountsByType[0], this, null, null);
                account = new Account(usernameString, Contract.ACCOUNT_TYPE);
                mAccountManager.addAccountExplicitly(account, null, null);
                mAccountManager.setAuthToken(account, Contract.AUTH_TYPE, token);
            } else {
                account = accountsByType[0];
            }

            Intent intent = new Intent();
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, usernameString);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);

            if (mAccountAuthenticatorResponse != null) {
                Bundle bundle = intent.getExtras();
                bundle.putString(AccountManager.KEY_ACCOUNT_NAME, usernameString);
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);
                mAccountAuthenticatorResponse.onResult(bundle);
            }

            setResult(RESULT_OK, intent);
            finish();
    }

    private void LoginToTapio(final String username, final String password) {
        Call<LoginResponse> call = service.logIn(username, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Integer code = response.code();
                if (code == 200) {
                    LoginResponse login = response.body();
                    Log.d("login", login.getUsername());
                    Log.d("login", login.getToken());
                    token = login.getToken();
                    Toast.makeText(mContext, "Logged In", Toast.LENGTH_LONG).show();
                    addAccount(token);
                } else {
                    Toast.makeText(mContext, "Login failed", Toast.LENGTH_LONG).show();
                    passwordEditText.setText("");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("login", t.getMessage());
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

    private void initFacebookSignIn() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String authToken = loginResult.getAccessToken().getToken();

                loginTapioWithToken(authToken);

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    usernameString = object.getString("name");
                                    String email = object.getString("email");
                                    addAccount(authToken);
//                                    String birthday = object.getString("birthday"); // 01/31/1980 format
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
            }

            @Override
            public void onError(FacebookException error) {
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
