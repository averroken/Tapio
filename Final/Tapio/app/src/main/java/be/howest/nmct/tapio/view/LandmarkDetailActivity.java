package be.howest.nmct.tapio.view;

import android.content.ContentValues;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import be.howest.nmct.tapio.R;
import be.howest.nmct.tapio.database.Contract;
import be.howest.nmct.tapio.database.DeleteLandmarkFromDBTask;
import be.howest.nmct.tapio.database.SaveNewLandmarkToDBTask;
import be.howest.nmct.tapio.databinding.ActivityLandmarkDetailBinding;
import be.howest.nmct.tapio.model.Landmarks;
import be.howest.nmct.tapio.viewmodel.LandmarksFavoriteList;

public class LandmarkDetailActivity extends AppCompatActivity {
    private Landmarks landmark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_detail);
        ActionBar actionBar;
        CollapsingToolbarLayout collapsingToolbarLayout;

        Gson gson = new Gson();
        String landmarkSerialized = getIntent().getStringExtra("landmark");
        landmark= gson.fromJson(landmarkSerialized, Landmarks.class);

        ActivityLandmarkDetailBinding landmarkDetailBinding= DataBindingUtil.setContentView(this, R.layout.activity_landmark_detail);
        landmarkDetailBinding.setLandmark(landmark);
        ImageView imageView = landmarkDetailBinding.DetailedLandmarkImage;

        Toolbar toolbar = landmarkDetailBinding.toolbar;

        if(toolbar != null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            if(actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int height = 0;

        Picasso.with(this)
                .load(landmark.getImageURLBig())
                .resize(displayWidth, height)
                .into(imageView);
       landmarkDetailBinding.executePendingBindings();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landmarkdetail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.favorite:
                if (item.getTitle() == getResources().getString(R.string.favorited)) {
                    deleteFavoriteLandmark();
                    item.setTitle(getResources().getString(R.string.not_favorited));
                    item.setIcon(getDrawable(R.drawable.ic_not_favorited));
                    return true;
                }
                else{
                    saveNewFavoriteLandmark();
                    item.setTitle(getResources().getString(R.string.favorited));
                    item.setIcon(getDrawable(R.drawable.ic_favorited));
                    return true;
                }
            case R.id.like:
                if (item.getTitle() == getResources().getString(R.string.liked)) {
                    item.setTitle(getResources().getString(R.string.not_liked));
                    item.setIcon(getDrawable(R.drawable.not_liked));
                    return true;
                }
                else{
                    getLoaderManager().restartLoader(0, null, new LandmarksFavoriteList(this));
                    item.setTitle(getResources().getString(R.string.liked));
                    item.setIcon(getDrawable(R.drawable.liked));
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveNewFavoriteLandmark() {
        //opslaan van het nieuwe landmark
        Log.d("FavoriteLandmark", "saving to sqlite");
        saveProductToDb();
        Log.d("FavoriteLandmark", "saved to sqlite");
        //Snackbar.make(context., "Landmark saved into database!", Snackbar.LENGTH_LONG).show();
    }

    public void deleteFavoriteLandmark() {
        //verwijderen van landmark
        Log.d("FavoriteLandmark", "deleting from sqlite");
        executeAsyncTask(new DeleteLandmarkFromDBTask(this), landmark.get_id());
        Log.d("FavoriteLandmark", "deleted from sqlite");
        //Snackbar.make(context., "Landmark saved into database!", Snackbar.LENGTH_LONG).show();
    }


    private void saveProductToDb() {

        ContentValues values = new ContentValues();
        values.put(Contract.ProductsColumns.COLUMN_LANDMARK_NAME, landmark.getName());
        values.put(Contract.ProductsColumns.COLUMN_LANDMARK_TYPE, landmark.getType());
        values.put(Contract.ProductsColumns.COLUMN_LANDMARK_DESCRIPTION, landmark.getDescription());
        values.put(Contract.ProductsColumns.COLUMN_LANDMARK_ID, landmark.get_id());

        executeAsyncTask(new SaveNewLandmarkToDBTask(this), values);
    }

    static private <T> void executeAsyncTask(AsyncTask<T, ?, ?> task, T... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            task.execute(params);
        }
    }

    static private <T> void executeAsyncTask(AsyncTask<String, ?, ?> task, String... id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
        } else {
            task.execute(id);
        }
    }
}
