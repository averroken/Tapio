package be.howest.nmct.tapio.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import be.howest.nmct.tapio.provider.*;

import static be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_ID;

/**
 * Created by Osedx on 01/12/2016.
 */

public class DeleteLandmarkFromDBTask  extends AsyncTask<String, Void, Void> {
    private Context mContext;

    public DeleteLandmarkFromDBTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(String... id) {

        mContext.getContentResolver().delete(be.howest.nmct.tapio.provider.Contract.FAVORITE_LANDMARKS_URI, COLUMN_LANDMARK_ID +"=?", id);

        return (null);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
