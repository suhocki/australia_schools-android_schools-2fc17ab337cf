package au.com.websitemasters.schools.thornlie.loader;


import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import au.com.websitemasters.schools.thornlie.utils.SQLiteHelper;
import me.leolin.shortcutbadger.ShortcutBadger;
import au.com.websitemasters.schools.thornlie.SchoolsApplication;


public class LoaderBadge extends Loader<Bitmap> {

    Task task;

    SQLiteDatabase db;
    SQLiteHelper dbHelper;

    public int notReaded = 0;

    public LoaderBadge(Context context, Bundle args) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();


        //need this condition because loader every time starts whem MainAct shows

            if (task != null)
                task.cancel(true);
            task = new Task();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");

    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    class Task extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {

            countAlertsBadges();
            Log.d("rklogs", "startToCountBadges");

            //delete it
            Bitmap btm = null;
            return btm;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            deliverResult(result);
        }

        public void countAlertsBadges(){

            dbHelper = new SQLiteHelper(getContext());
            db = dbHelper.getWritableDatabase();

            // Log.d("rklogs", "--- Rows in mytable: ---");
            // Cursor c = db.query("mytable", null, null, null, null, null, "date DESC");

            String query = "SELECT * FROM mytable WHERE category = 'ALERTS' ORDER BY date DESC";
            Cursor c = db.rawQuery(query, null);

            if (c.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int categoryIndex = c.getColumnIndex("category");
                int was_readedIndex = c.getColumnIndex("was_readed");

                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d("rklogs",
                            "ID = " + c.getInt(idColIndex) +
                                    ", category = " + c.getString(categoryIndex) +
                                    ", was_readed = " + c.getString(was_readedIndex)
                    );

                    if (c.getString(was_readedIndex).equals("NO")){
                        notReaded = notReaded + 1;
                    }

                } while (c.moveToNext());
            } else
                Log.d("rklogs", "0 rows");
            c.close();
            dbHelper.close();
            db.close();

            countEventsBadges();
        }


        public void countEventsBadges(){

            dbHelper = new SQLiteHelper(getContext());
            db = dbHelper.getWritableDatabase();

            // Log.d("rklogs", "--- Rows in mytable: ---");
            // Cursor c = db.query("mytable", null, null, null, null, null, "date DESC");

            String query = "SELECT * FROM mytable WHERE category = 'EVENTS' ORDER BY date DESC";
            Cursor c = db.rawQuery(query, null);

            if (c.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int categoryIndex = c.getColumnIndex("category");
                int was_readedIndex = c.getColumnIndex("was_readed");

                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d("rklogs",
                            "ID = " + c.getInt(idColIndex) +
                                    ", category = " + c.getString(categoryIndex) +
                                    ", was_readed = " + c.getString(was_readedIndex)
                    );

                    if (c.getString(was_readedIndex).equals("NO")){
                        notReaded = notReaded + 1;
                    }

                } while (c.moveToNext());
            } else
                Log.d("rklogs", "0 rows");
            c.close();
            dbHelper.close();
            db.close();

            countNewslettersBadges();
        }


        public void countNewslettersBadges(){

            dbHelper = new SQLiteHelper(getContext());
            db = dbHelper.getWritableDatabase();

            // Log.d("rklogs", "--- Rows in mytable: ---");
            // Cursor c = db.query("mytable", null, null, null, null, null, "date DESC");

            String query = "SELECT * FROM mytable WHERE category = 'NEWSLETTERS' ORDER BY date DESC";
            Cursor c = db.rawQuery(query, null);

            if (c.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex("id");
                int categoryIndex = c.getColumnIndex("category");
                int was_readedIndex = c.getColumnIndex("was_readed");

                do {
                    // получаем значения по номерам столбцов и пишем все в лог
                    Log.d("rklogs",
                            "ID = " + c.getInt(idColIndex) +
                                    ", category = " + c.getString(categoryIndex) +
                                    ", was_readed = " + c.getString(was_readedIndex)
                    );

                    if (c.getString(was_readedIndex).equals("NO")){
                        notReaded = notReaded + 1;
                    }

                } while (c.moveToNext());
            } else
                Log.d("rklogs", "0 rows");
            c.close();
            dbHelper.close();
            db.close();

            //set badge by library
            ShortcutBadger.applyCount(getContext(), notReaded);

            //set badge by intent
            setBadge(getContext(), notReaded);

            Log.d("rklogs", "notReadedBadges_" + notReaded);

            ((SchoolsApplication) getContext()).saveBadgesCount(notReaded);
        }

        public  void setBadge(Context context, int count) {
            String launcherClassName = getLauncherClassName(context);
            if (launcherClassName == null) {
                return;
            }
            Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count", count);
            intent.putExtra("badge_count_package_name", context.getPackageName());
            intent.putExtra("badge_count_class_name", launcherClassName);
            context.sendBroadcast(intent);
        }

        public  String getLauncherClassName(Context context) {

            PackageManager pm = context.getPackageManager();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
            for (ResolveInfo resolveInfo : resolveInfos) {
                String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
                if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                    String className = resolveInfo.activityInfo.name;
                    return className;
                }
            }
            return null;
        }
    }
}
