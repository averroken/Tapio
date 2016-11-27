package be.howest.nmct.myapplication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import be.howest.nmct.myapplication.auth.Contract;

public class LoginActivity extends Activity {
    private AccountManager mAccountManager;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;
    private EditText usernameEditText, passwordEditText;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidgets();


        mAccountManager = AccountManager.get(this);
        mAccountAuthenticatorResponse = this.getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
        if (mAccountAuthenticatorResponse != null){
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    private void initWidgets() {
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signInButton = (Button) findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount(usernameEditText.getText().toString());
            }
        });
    }

    private void addAccount(String usernameString){
        Account[] accountsByType = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        Account account;

        if (accountsByType.length == 0){
            account = new Account(usernameString, Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
        }else if (!usernameString.equals(accountsByType[0].name)){
            mAccountManager.removeAccount(accountsByType[0], this, null, null);
            account = new Account(usernameString, Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
        }else {
            account = accountsByType[0];
        }

        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, usernameString);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);

        if (mAccountAuthenticatorResponse != null){
            Bundle bundle = intent.getExtras();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, usernameString);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);
            mAccountAuthenticatorResponse.onResult(bundle);
        }

        setResult(RESULT_OK, intent);
        finish();
    }
}
