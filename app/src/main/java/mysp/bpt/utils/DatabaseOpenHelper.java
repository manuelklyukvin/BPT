package mysp.bpt.utils;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import android.content.Context;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "BPTDatabase.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}