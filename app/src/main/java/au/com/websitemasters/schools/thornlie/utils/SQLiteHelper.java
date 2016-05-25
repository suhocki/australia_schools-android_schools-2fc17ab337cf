package au.com.websitemasters.schools.thornlie.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {



    public SQLiteHelper(Context context) {
        // конструктор суперкласса
        super(context, "mytable", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("rklogs", "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "category text,"
                + "was_readed text,"
                + "date text,"
                + "title text,"
                + "full_text text,"
                + "picture text,"
                + "pdf text,"
                + "url_full_news text,"
                + "serverId text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}

