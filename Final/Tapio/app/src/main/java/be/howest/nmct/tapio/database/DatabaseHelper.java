package be.howest.nmct.tapio.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper INSTANCE;
    private static Object object = new Object();

    public static DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (object) {
                INSTANCE = new DatabaseHelper(context.getApplicationContext());
            }
        }
        return INSTANCE;
    }

    private DatabaseHelper(Context context) {
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        onUpgrade(db, 0, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        while (oldVersion < newVersion) {
            switch (oldVersion) {
                case 0:
                    upgradeTo1(db);
                    oldVersion++;
                    break;
                case 1:
                    //upgrade logic next version
                case 2:
                    //upgrade logic next version +1
                case 3:
                    //upgrade logic next version +2
                    break;
                default:
                    throw new IllegalStateException(
                            "onUpgrade() with unknown oldVersion " + oldVersion);
            }
        }
    }

    private void upgradeTo1(SQLiteDatabase db) {
        db.execSQL(Contract.ProductsDB.CREATE_TABLE);
    }

}
