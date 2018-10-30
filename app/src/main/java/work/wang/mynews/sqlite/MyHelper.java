package work.wang.mynews.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/12/24 0024.
 */

public class MyHelper extends SQLiteOpenHelper {
    public MyHelper(Context context) {
        super(context,"news.db",null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE broke_news(_id integer primary key autoincrement,time varchar(40),local varchar(100)," +
                "state varchar(200),phone varchar(30),email varchar(30) )");
        db.execSQL("CREATE TABLE user_comments(_id integer primary key autoincrement,url varchar(40)," +
                "comments varchar(200) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
