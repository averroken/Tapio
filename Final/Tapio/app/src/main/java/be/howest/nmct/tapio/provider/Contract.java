package be.howest.nmct.tapio.provider;

import android.net.Uri;

public class Contract {

    public static final String AUTHORITY = "be.howest.nmct.TapioLandmarks";

    //CONTENT-URIS
    public static final Uri FAVORITE_LANDMARKS_URI = Uri.parse("content://" + AUTHORITY + "/favorite_landmarks");
    public static final Uri PRODUCTS_ITEM_URI = Uri.parse("content://" + AUTHORITY + "/favorite_landmarks/");


    //MIME-TYPES
    public static final String FAVORITE_LANDMARKS_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.howest.favorite_landmarks";
    public static final String FAVORITE_LANDMARKS_ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.howest.product";

    public static final int FAVORITE_LANDMARKS_ID_PATH_POSITION = 1;
}
