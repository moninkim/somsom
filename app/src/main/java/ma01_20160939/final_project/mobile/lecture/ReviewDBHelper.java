package ma01_20160939.final_project.mobile.lecture;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReviewDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "reivew_db";
    public final static String TABLE_NAME = "review_table";
    public final static String COL_TITLE = "title";
    public final static String COL_CONTENT = "content";
    public final static String COL_PATH = "path";


    public ReviewDBHelper(Context context) {
        super(context, DB_NAME, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( _id integer primary key autoincrement,"
                + COL_TITLE + " TEXT, " + COL_CONTENT + " TEXT, " + COL_PATH + " TEXT);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }

}
