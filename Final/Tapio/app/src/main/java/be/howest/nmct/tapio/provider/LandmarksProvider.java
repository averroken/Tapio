package be.howest.nmct.tapio.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

import be.howest.nmct.tapio.database.DatabaseHelper;


public class LandmarksProvider extends ContentProvider {

    private DatabaseHelper databaseHelper;

    private static final int LANDMARKS = 1;
    private static final int LANDMARKS_ID = 2;

    private static HashMap<String, String> LANDMARKS_PROJECTION_MAP;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, "favorite_landmarks", LANDMARKS);
        uriMatcher.addURI(Contract.AUTHORITY, "favorite_landmarks/#", LANDMARKS_ID);
    }

    @Override
    public boolean onCreate() {
        databaseHelper = DatabaseHelper.getInstance(getContext());
        LANDMARKS_PROJECTION_MAP = new HashMap<>();
        LANDMARKS_PROJECTION_MAP.put(be.howest.nmct.tapio.database.Contract.ProductsColumns._ID, be.howest.nmct.tapio.database.Contract.ProductsColumns._ID);
        LANDMARKS_PROJECTION_MAP.put(be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_ID, be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_ID);
        LANDMARKS_PROJECTION_MAP.put(be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_NAME, be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_NAME);
        LANDMARKS_PROJECTION_MAP.put(be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_DESCRIPTION, be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_DESCRIPTION);
        LANDMARKS_PROJECTION_MAP.put(be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_TYPE, be.howest.nmct.tapio.database.Contract.ProductsColumns.COLUMN_LANDMARK_TYPE);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case LANDMARKS:
                queryBuilder.setTables(be.howest.nmct.tapio.database.Contract.ProductsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(LANDMARKS_PROJECTION_MAP);
                break;

            case LANDMARKS_ID:
                queryBuilder.setTables(be.howest.nmct.tapio.database.Contract.ProductsDB.TABLE_NAME);
                queryBuilder.setProjectionMap(LANDMARKS_PROJECTION_MAP);

                String landmarkid = uri.getPathSegments().get(Contract.FAVORITE_LANDMARKS_ID_PATH_POSITION);
                DatabaseUtils.concatenateWhere(selection, "( " + be.howest.nmct.tapio.database.Contract.ProductsColumns._ID + " = ?" + ")"); //strict genomen haakjes niet nodig
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs, new String[]{"" + landmarkid});

                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor data = queryBuilder.query(
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        data.getCount();

        data.setNotificationUri(getContext().getContentResolver(), uri);
        return data;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case LANDMARKS:
                return Contract.FAVORITE_LANDMARKS_CONTENT_TYPE;
            case LANDMARKS_ID:
                return Contract.FAVORITE_LANDMARKS_ITEM_CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case LANDMARKS:
                long newRowId = db.insert(
                        be.howest.nmct.tapio.database.Contract.ProductsDB.TABLE_NAME, null, values);
                if (newRowId > 0) {
                    Uri landmarkItemUri = ContentUris.withAppendedId(Contract.FAVORITE_LANDMARKS_URI, newRowId);
                    getContext().getContentResolver().notifyChange(landmarkItemUri, null);
                    return landmarkItemUri;
                }

                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String finalWhere;
        int count;
        switch (uriMatcher.match(uri)) {
            case LANDMARKS:
                count = db.delete(
                        be.howest.nmct.tapio.database.Contract.ProductsDB.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            case LANDMARKS_ID:
                String landmarkItemId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + landmarkItemId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.delete(
                        be.howest.nmct.tapio.database.Contract.ProductsDB.TABLE_NAME,
                        finalWhere,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count;
        String finalWhere;

        switch (uriMatcher.match(uri)) {
            case LANDMARKS:
                count = db.update(
                        be.howest.nmct.tapio.database.Contract.ProductsDB.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;

            case LANDMARKS_ID:
                String landmarkId = uri.getPathSegments().get(1);
                finalWhere = "Id = " + landmarkId;

                if (selection != null) {
                    finalWhere = DatabaseUtils.concatenateWhere(finalWhere, selection);
                }

                count = db.update(
                        be.howest.nmct.tapio.database.Contract.ProductsDB.TABLE_NAME,
                        values,
                        finalWhere,
                        selectionArgs
                );
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}

