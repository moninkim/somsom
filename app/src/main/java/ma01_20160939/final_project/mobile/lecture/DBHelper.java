package ma01_20160939.final_project.mobile.lecture;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "store_db";
    public final static String TABLE_NAME = "store_table";
    public final static String COL_NAME = "name";
    public final static String COL_HOUR = "hour";
    public final static String COL_PHONE = "phone";
    public final static String COL_ADDRESS = "address";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 11);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( _id integer primary key autoincrement,"
                + COL_NAME + " TEXT, " + COL_HOUR + " TEXT, " + COL_PHONE + " TEXT, " + COL_ADDRESS + " TEXT);");

//		샘플 데이터
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '지지고 동덕여대점', '10:30 ~ 8:30','02-913-6031', '서울 성북구 화랑로13길 20');");
        db.execSQL("INSERT INTO " + TABLE_NAME + " VALUES (null, '디에이피자', '11:00 ~ 23:00','02-942-1044','서울특별시 성북구 하월곡동 21-88');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);
        onCreate(db);
    }

}
