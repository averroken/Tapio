package be.howest.nmct.tapio.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import be.howest.nmct.tapio.provider.Contract;


public class SaveNewLandmarkToDBTask extends AsyncTask<ContentValues, Void, Void> {

    private Context mContext;

    public SaveNewLandmarkToDBTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(ContentValues... values) {
//        long insertId = DatabaseHelper.getInstance(mContext).getWritableDatabase().insert(
//                Contract.ProductsDB.TABLE_NAME, Contract.ProductsDB.COLUMN_LANDMARK_NR,values[0]);
        ContentValues test =values[0];
        Uri newUri = mContext.getContentResolver().insert(Contract.FAVORITE_LANDMARKS_URI, values[0]);

        return (null);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
