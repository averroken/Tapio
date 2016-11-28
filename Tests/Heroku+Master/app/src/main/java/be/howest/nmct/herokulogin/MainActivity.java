package be.howest.nmct.herokulogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import be.howest.nmct.herokulogin.auth.AuthHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (AuthHelper.isUserLoggedIn(this)){
            showLoggedInActivity();
        }else {
            showSignIn();
        }
    }

    private void showSignIn() {
        Intent intent = new Intent(this, AccountLoginActivity.class);
        startActivity(intent);
    }

    private void showLoggedInActivity() {
        Intent intent = new Intent(this, LoggedIn.class);
        startActivity(intent);
    }
}
