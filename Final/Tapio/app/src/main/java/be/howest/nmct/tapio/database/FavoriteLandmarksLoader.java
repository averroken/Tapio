package be.howest.nmct.tapio.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import be.howest.nmct.tapio.provider.*;

public class FavoriteLandmarksLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mData;
    private Context mContext;

    public FavoriteLandmarksLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }


        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        String[] columns = new String[]{
                Contract.ProductsColumns._ID,
                Contract.ProductsColumns.COLUMN_LANDMARK_ID,
                Contract.ProductsColumns.COLUMN_LANDMARK_DESCRIPTION,
                Contract.ProductsColumns.COLUMN_LANDMARK_TYPE,
                Contract.ProductsColumns.COLUMN_LANDMARK_NAME,
        };

        mData = getContext().getContentResolver().query(be.howest.nmct.tapio.provider.Contract.FAVORITE_LANDMARKS_URI, columns, null, null, null);

        mData.getCount();

        return mData;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        Cursor oldData = mData;
        mData = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldData != null && oldData != cursor && !oldData.isClosed()) {
            oldData.close();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
