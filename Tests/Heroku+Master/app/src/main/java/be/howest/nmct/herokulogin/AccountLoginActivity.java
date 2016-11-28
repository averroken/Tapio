package be.howest.nmct.herokulogin;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.howest.nmct.herokulogin.auth.Contract;
import be.howest.nmct.herokulogin.models.LoginResponse;
import be.howest.nmct.herokulogin.models.TokenResponse;
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
    private Retrofit retrofit;
    private Boolean LoginSuccess;
    private SharedPreferences preferences;
    private String tokenKey = "be.howest.nmct.herokulogin.token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity_login);

        preferences = this.getSharedPreferences(
                "be.howest.nmct.herokulogin", Context.MODE_PRIVATE);

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
    }

    private void addAccount(String username, String password) {
            Account[] accountsByType = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
            Account account;

            if (accountsByType.length == 0) {
                account = new Account(usernameString, Contract.ACCOUNT_TYPE);
                mAccountManager.addAccountExplicitly(account, null, null);
            } else if (!usernameString.equals(accountsByType[0].name)) {
                mAccountManager.removeAccount(accountsByType[0], this, null, null);
                account = new Account(usernameString, Contract.ACCOUNT_TYPE);
                mAccountManager.addAccountExplicitly(account, null, null);
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
                    preferences.edit().putString(tokenKey, token).apply();
                    addAccount(username, password);
                } else {
                    Toast.makeText(mContext, "Login failed", Toast.LENGTH_LONG).show();
                    passwordEditText.setText("");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d("login", t.getMessage());
                LoginSuccess = false;
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
}
