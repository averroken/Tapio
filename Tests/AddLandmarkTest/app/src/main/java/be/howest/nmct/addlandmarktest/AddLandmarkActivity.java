package be.howest.nmct.addlandmarktest;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import be.howest.nmct.addlandmarktest.models.LandmarkBody;
import be.howest.nmct.addlandmarktest.models.LandmarkPostResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddLandmarkActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_CHOOSE_IMAGE = 100;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 200;
    private EditText titleEditText, typeEditText, tagsEditText, descriptionEditText;
    private Switch publicSwitch;
    private Retrofit retrofit;
    private TapioService service;
    private View view;
    private Activity thisActivity;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Context mContext;
    private FloatingActionsMenu floatingActionsMenu;
    private RelativeLayout mLayout;
    ProgressDialog progress;

    // TODO: 11/12/16 Add icon picker
    // TODO: 11/12/16 Send picture 
    // TODO: 11/12/16 Fix FAB hidden when collapsed
    // TODO: 11/12/16 show location/coordinates
    // TODO: 11/12/16 check if GPS is on
    // TODO: 11/12/16 timeout for getting location


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_landmark);
        view = findViewById(R.id.content_add_landmark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;

        initWidgets();
        initGoogleLocationClient();
        initRetrofit();
        initFABs();
    }

    private void initFABs() {
        FloatingActionButton uploadLandmarkFAB = (FloatingActionButton) findViewById(R.id.upload_landmark_FAB);
        uploadLandmarkFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog("Uploading Landmark", "Connecting to Tapio");
                checkLocationPermission();
                addLandmark(view);
                closeFloatingActionMenu();
            }
        });

        FloatingActionButton choosePictureFAB = (FloatingActionButton) findViewById(R.id.change_picture_FAB);
        choosePictureFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(choosePictureIntent, REQUEST_CHOOSE_IMAGE);
                closeFloatingActionMenu();
            }
        });

        FloatingActionButton changeLocation = (FloatingActionButton) findViewById(R.id.change_location_FAB);
        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog("Getting coordinates", "Checking GPS signal");
                closeFloatingActionMenu();
                checkLocationPermission();
            }
        });
    }

    private void showProgressDialog(String titleString, String messageString) {
        progress = ProgressDialog.show(this, titleString,
                messageString, false, true);
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                mContext, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddLandmarkActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(view, "Please grant location permission", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Enable", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(AddLandmarkActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(AddLandmarkActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        } else {


            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                hideProgressDialog();
                Toast.makeText(mContext, "Latitude: " + String.valueOf(mLastLocation.getLatitude()), Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "Longitude: " + String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
//                    Snackbar.make(v, "Latitude: " + String.valueOf(mLastLocation.getLatitude()), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    Snackbar.make(v, "Longitude: " + String.valueOf(mLastLocation.getLongitude()), Snackbar.LENGTH_SHORT)
//                            .setAction("Action", null).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//            checkLocationPermission();
//        }
    }

    private void closeFloatingActionMenu() {
        floatingActionsMenu.collapse();
    }

    private void initGoogleLocationClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        checkLocationPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_IMAGE) {
            if (resultCode == RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(thisActivity.getContentResolver(), Uri.parse(data.getDataString()));
                    AppBarLayout appBar = (AppBarLayout) findViewById(R.id.app_bar);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    appBar.setBackgroundDrawable(drawable);
                    Snackbar.make(view, "Image set", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }


    private void addLandmark(final View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        LandmarkBody body = new LandmarkBody(
                titleEditText.getText().toString(),
                typeEditText.getText().toString(),
                "50",
                "50",
                "Belgium",
                "https://media.mnn.com/assets/images/2015/03/forest-path-germany.jpg.653x0_q80_crop-smart.jpg",
                true,
                descriptionEditText.getText().toString()
        );

        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6IkJyaWFuIiwiaWF0IjoxNDgxNDE1MDQzLCJleHAiOjE0ODE0MTY0ODN9.y7k-ckyaoKCi9EzIEIccRzAnErB-A-_f8QTP8iHk9hM";
        Call<LandmarkPostResponse> call = service.addLandmark(token, body);

        call.enqueue(new Callback<LandmarkPostResponse>() {
            @Override
            public void onResponse(Call<LandmarkPostResponse> call, Response<LandmarkPostResponse> response) {
                hideProgressDialog();
                Integer code = response.code();
                LandmarkPostResponse postResponse = new LandmarkPostResponse("", "");
                if (code == 201) {
                    postResponse = response.body();
                    Snackbar.make(view, "Landmark added", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (code == 403) {
                    postResponse = response.body();
                    Snackbar.make(view, "Invalid token", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (code == 503) {
                    Snackbar.make(view, "Tapio is down, please try again later", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        postResponse = new LandmarkPostResponse(
                                jObjError.getString("awnser"), jObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Snackbar.make(view, postResponse.getMessage(), 8000)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<LandmarkPostResponse> call, Throwable t) {
                hideProgressDialog();
                Log.d("login", t.getMessage());
                Snackbar.make(view, "No network connection found", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initWidgets() {
        titleEditText = (EditText) findViewById(R.id.TitleEditText);
        typeEditText = (EditText) findViewById(R.id.TypeEditText);
        tagsEditText = (EditText) findViewById(R.id.TagsEditText);
        descriptionEditText = (EditText) findViewById(R.id.DescriptionEditText);
        publicSwitch = (Switch) findViewById(R.id.PublicSwitch);
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floating_action_menu);
//        mLayout = (RelativeLayout) findViewById(R.id.content_add_landmark);
//
//        mLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeFloatingActionMenu();
//            }
//        });

        thisActivity = this;
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://back-end-tapio-test.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TapioService.class);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
