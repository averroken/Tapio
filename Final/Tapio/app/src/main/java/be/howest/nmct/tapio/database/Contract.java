package be.howest.nmct.tapio.database;

import android.provider.BaseColumns;

public class Contract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "database.db";


    //interface uitbreiden
    public interface ProductsColumns extends BaseColumns {
        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_LANDMARK_ID = "id";
        public static final String COLUMN_LANDMARK_NAME = "name";
        public static final String COLUMN_LANDMARK_DESCRIPTION = "description";
        public static final String COLUMN_LANDMARK_TYPE = "type";
    }

    public static abstract class ProductsDB implements ProductsColumns {

        public static final String CREATE_TABLE = "create table "
                + TABLE_NAME + "(" + _ID
                + " integer primary key autoincrement, "
                + COLUMN_LANDMARK_ID + " integer not null, "
                + COLUMN_LANDMARK_NAME + " text not null, "
                + COLUMN_LANDMARK_DESCRIPTION + " text not null, "
                + COLUMN_LANDMARK_TYPE + " text not null  "
                + ");";


        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
