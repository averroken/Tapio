package be.howest.nmct.tapio;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import be.howest.nmct.tapio.auth.Contract;
import be.howest.nmct.tapio.models.TokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoggedIn extends AppCompatActivity {
    private Context mContext;
    private TextView tokenTextView;
    private Button testTokenButton;
    private TapioService service;
    private Retrofit retrofit;
    private String tokenString;
    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mContext = this;
        mAccountManager = AccountManager.get(this);


        initWidgets();
        initRetrofit();
    }

    private void initWidgets() {
        tokenTextView = (TextView) findViewById(R.id.tokenTextview);
        Account account = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE)[0];
        tokenString = mAccountManager.peekAuthToken(account, Contract.AUTH_TYPE);
        if (tokenString != null) {
            tokenTextView.setText(tokenString);
        }
        testTokenButton = (Button) findViewById(R.id.testTokenButton);
        testTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testToken();
            }
        });
    }

    private void testToken() {
        if (tokenString != null) {
            Call<TokenResponse> tokenCall = service.testToken(tokenString);

            tokenCall.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    Integer code = response.code();
                    if (code != 200) {
                        Toast.makeText(mContext, "Error. Please login again", Toast.LENGTH_LONG).show();
                    } else {
                        TokenResponse token = response.body();
                        Log.d("login", "token: " + token.getMessage());
                        Toast.makeText(mContext, token.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Log.d("login", "token: FAILED");
                }
            });
        } else {
            Toast.makeText(mContext, "FAILED: no token found", Toast.LENGTH_LONG).show();
        }
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://back-end-tapio-test.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TapioService.class);
    }
}
