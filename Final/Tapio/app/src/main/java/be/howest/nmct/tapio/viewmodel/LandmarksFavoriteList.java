package be.howest.nmct.tapio.viewmodel;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.util.Log;

import be.howest.nmct.tapio.database.Contract;
import be.howest.nmct.tapio.database.FavoriteLandmarksLoader;
import be.howest.nmct.tapio.model.Landmarks;

/**
 * Created by Osedx on 30/11/2016.
 */

public class LandmarksFavoriteList extends BaseObservable implements LoaderManager.LoaderCallbacks<Cursor>  {

    private ObservableList<Landmarks> favoriteLandmarksList;

    private Context context;

    public LandmarksFavoriteList(Context context){
        this.context = context;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new FavoriteLandmarksLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Placing data from Cursor in ObservableArrayList
        favoriteLandmarksList = new ObservableArrayList<>();

        while (cursor.moveToNext()){
            Landmarks landmark = new Landmarks();
            landmark.set_id(cursor.getString(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_LANDMARK_ID)));
            landmark.setName(cursor.getString(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_LANDMARK_NAME)));
            landmark.setType(cursor.getString(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_LANDMARK_TYPE)));
            landmark.setDescription(cursor.getString(cursor.getColumnIndex(Contract.ProductsColumns.COLUMN_LANDMARK_DESCRIPTION)));
            favoriteLandmarksList.add(landmark);
        }
        Log.d("FavoriteLandmark", "you favorited "+ favoriteLandmarksList.size());
        cursor.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
