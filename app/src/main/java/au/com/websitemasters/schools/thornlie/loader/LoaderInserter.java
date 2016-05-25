package au.com.websitemasters.schools.thornlie.loader;


import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import au.com.websitemasters.schools.thornlie.constants.Constants;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.ParentsAdapterObject;
import au.com.websitemasters.schools.thornlie.utils.SQLiteHelper;
import au.com.websitemasters.schools.thornlie.SchoolsApplication;


public class LoaderInserter extends Loader<Bitmap> {

    JSONArray events_jsonArray;
    JSONArray news_jsonArray;
    JSONArray newsletters_jsonArray;
    JSONArray alert_jsonArray;
    JSONArray pinfo_jsonArray;

    Task task;

    SQLiteDatabase db;
    SQLiteHelper dbHelper;

    ArrayList<ParentsAdapterObject> listParents = new ArrayList<>();

    public LoaderInserter(Context context, Bundle args) {
        super(context);

        if (args != null)
            try {

                events_jsonArray = new JSONArray(args.getString("events_jsonArray"));
                news_jsonArray = new JSONArray(args.getString("news_jsonArray"));
                newsletters_jsonArray = new JSONArray(args.getString("newsletters_jsonArray"));
                alert_jsonArray = new JSONArray(args.getString("alert_jsonArray"));
                pinfo_jsonArray = new JSONArray(args.getString("pinfo_jsonArray"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
        if ( Constants.LoaderIsWorking == false){
            if (task != null)
                task.cancel(true);
            task = new Task();
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
        }
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

            Constants.LoaderIsWorking = true;
            Log.d("rklogs", "LoaderIsWorking = true");

            if ((((SchoolsApplication) getContext()).checkItFirstRun()) == true){
                fillBDbyArrays_FirstStartApp(alert_jsonArray, news_jsonArray, newsletters_jsonArray,
                        events_jsonArray, pinfo_jsonArray);
            } else {
                fillByArrays(alert_jsonArray, news_jsonArray, newsletters_jsonArray,
                        events_jsonArray, pinfo_jsonArray);
            }

            //delete it
            Bitmap btm = null;
            return btm;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            Constants.LoaderIsWorking = false;
            deliverResult(result);

            Log.d("rklogs", "LoaderIsWorking = false");
        }

        private void fillBDbyArrays_FirstStartApp(JSONArray alert_jsonArray, JSONArray news_jsonArray,
                                                  JSONArray newsletters_jsonArray, JSONArray events_jsonArray,
                                                  JSONArray pinfo_jsonArray){

            Log.d("rklogs", "events_jsonArray.length()_"+ events_jsonArray.length());
            Log.d("rklogs", "alert_jsonArray.length()_" + alert_jsonArray.length());
            Log.d("rklogs", "newsletters_jsonArray.length()_" + newsletters_jsonArray.length());
            Log.d("rklogs", "news_jsonArray.length()_" + news_jsonArray.length());
            Log.d("rklogs", "pinfo_jsonArray.length()_" + pinfo_jsonArray.length());


            ((SchoolsApplication) getContext()).saveListParents(pinfo_jsonArray);

            for (int i = 0; i < alert_jsonArray.length(); i++){
                try {
                    JSONObject alert_json = alert_jsonArray.getJSONObject(i);
                    String serverId = alert_json.getString("alert_id");
                    String title = alert_json.getString("alert_title");
                    String full_text = alert_json.getString("alert_text");
                    String date = alert_json.getString("alert_date");
                    String cat = alert_json.getString("alert_cat");
                    String isDelete = alert_json.getString("alert_isDelete");
                            if (cat.equals("28")){ //kindy
                                if (isDelete.equals("0")){
                                    insertToDB("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                                } else {
                                    //upgrade to empty category
                                }
                            }

                            if (cat.equals("29")){ //preprimary
                                if (isDelete.equals("0")){
                                    insertToDB("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                                } else {
                                    //upgrade to empty category
                                }
                            }

                            if (cat.equals("30")){ //year1
                                if (isDelete.equals("0")){
                                    insertToDB("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                                } else {
                                    //upgrade to empty category
                                }
                            }

                            if (cat.equals("31")){
                                if (isDelete.equals("0")){
                                    insertToDB("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                                } else {
                                    //upgrade to empty category
                                }
                            }

                            if (cat.equals("32")){
                                if (isDelete.equals("0")){
                                    insertToDB("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                                } else {
                                    //upgrade to empty category
                                }
                            }

                            if (cat.equals("33")){
                                if (isDelete.equals("0")){
                                    insertToDB("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                                } else {
                                    //upgrade to empty category
                                }
                            }

                            if (cat.equals("34")){
                                if (isDelete.equals("0")){
                                    insertToDB("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                                } else {
                                    //upgrade to empty category
                                }
                            }

                            if (cat.equals("35")){
                                if (isDelete.equals("0")){
                                    insertToDB("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                                } else {
                                    //upgrade to empty category
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < news_jsonArray.length(); i++){
                        try {
                            JSONObject news_json = news_jsonArray.getJSONObject(i);
                            String serverId = news_json.getString("news_id");
                            String title = news_json.getString("news_title");
                            String full_text = news_json.getString("news_full_text");
                            String url_full_text = news_json.getString("news_short_text");
                            String date = news_json.getString("news_date");
                            String isDelete = news_json.getString("news_isDelete");
                            if (isDelete.equals("0")){
                                insertToDB("NEWS", "YES", date, title, full_text, null, null, url_full_text, serverId);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < events_jsonArray.length(); i++){
                        try {
                            JSONObject events_json = events_jsonArray.getJSONObject(i);
                            String serverId = events_json.getString("event_id");
                            String title = events_json.getString("event_title");
                            String full_text = events_json.getString("event_date");
                            String url_full_text = events_json.getString("event_date_end");
                            // String date = events_json.getString("event_date");
                            String date = events_json.getString("event_date_start");
                            String isDelete = events_json.getString("event_isDelete");
                            if (isDelete.equals("0")){
                                //insertToDB("EVENTS", "NO", date, title, full_text, null, null, url_full_text, serverId);
                                insertToDB("EVENTS", "YES", date, title, full_text, null, null, url_full_text, serverId);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    for (int i = 0; i < newsletters_jsonArray.length(); i++){
                        try {
                            JSONObject newsletters_json = newsletters_jsonArray.getJSONObject(i);
                            String serverId = newsletters_json.getString("newsletter_id");
                            String title = newsletters_json.getString("newsletter_title");
                            String date = newsletters_json.getString("newsletter_date");
                            String pdf = newsletters_json.getString("newsletter_url");
                            String isDelete = newsletters_json.getString("newsletter_isDelete");
                            if (isDelete.equals("0")){
                                insertToDB("NEWSLETTERS", "YES", date, title, null, null, pdf, null, serverId);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
        }
    }

        public void fillByArrays(JSONArray alert_jsonArray, JSONArray news_jsonArray,
                                 JSONArray newsletters_jsonArray, JSONArray events_jsonArray,
                                 JSONArray pinfo_jsonArray) {
            Log.d("rklogs", "events_jsonArray.length()_"+ events_jsonArray.length());
            Log.d("rklogs", "alert_jsonArray.length()_" + alert_jsonArray.length());
            Log.d("rklogs", "newsletters_jsonArray.length()_" + newsletters_jsonArray.length());
            Log.d("rklogs", "news_jsonArray.length()_" + news_jsonArray.length());
            Log.d("rklogs", "pinfo_jsonArray.length()_" + pinfo_jsonArray.length());


            ((SchoolsApplication) getContext()).saveListParents(pinfo_jsonArray);


            for (int i = 0; i < alert_jsonArray.length(); i++){
                try {
                    JSONObject alert_json = alert_jsonArray.getJSONObject(i);
                    String serverId = alert_json.getString("alert_id");
                    String title = alert_json.getString("alert_title");
                    String full_text = alert_json.getString("alert_text");
                    String date = alert_json.getString("alert_date");
                    String cat = alert_json.getString("alert_cat");
                    String isDelete = alert_json.getString("alert_isDelete");

                    if (cat.equals("28")){ //kindy
                        if (isDelete.equals("0")){
                            if (((SchoolsApplication) getContext()).getSetting("Kindy") == true){
                                insertToDBwithUpdade("ALERTS", "NO", date, title, full_text, null, null, null, serverId);

                            }
                            if (((SchoolsApplication) getContext()).getSetting("Kindy") == false){
                                //insertToDBwithUpdade("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                            }
                        } else {
                            //upgrade to empty category
                        }
                    }

                    if (cat.equals("29")){ //preprimary
                        if (isDelete.equals("0")){

                            if (((SchoolsApplication) getContext()).getSetting("Pre-Primary") == true){
                                insertToDBwithUpdade("ALERTS", "NO", date, title, full_text, null, null, null, serverId);
                            }

                            if (((SchoolsApplication) getContext()).getSetting("Pre-Primary") == false){
                                //insertToDBwithUpdade("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                            }

                        } else {
                            //upgrade to empty category
                        }
                    }

                    if (cat.equals("30")){ //year1
                        if (isDelete.equals("0")){
                            if (((SchoolsApplication) getContext()).getSetting("Year1") == true){
                                insertToDBwithUpdade("ALERTS", "NO", date, title, full_text, null, null, null, serverId);
                            }
                            if (((SchoolsApplication) getContext()).getSetting("Year1") == false){
                                //insertToDBwithUpdade("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                            }
                        } else {
                            //upgrade to empty category
                        }
                    }

                    if (cat.equals("31")){
                        if (isDelete.equals("0")){

                            if (((SchoolsApplication) getContext()).getSetting("Year2") == true){
                                insertToDBwithUpdade("ALERTS", "NO", date, title, full_text, null, null, null, serverId);
                            }

                            if (((SchoolsApplication) getContext()).getSetting("Year2") == false){
                                //insertToDBwithUpdade("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                            }

                        } else {
                            //upgrade to empty category
                        }
                    }

                    if (cat.equals("32")){
                        if (isDelete.equals("0")){

                            if (((SchoolsApplication) getContext()).getSetting("Year3") == true){
                                insertToDBwithUpdade("ALERTS", "NO", date, title, full_text, null, null, null, serverId);
                            }
                            if (((SchoolsApplication) getContext()).getSetting("Year3") == false) {
                                //insertToDBwithUpdade("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                            }

                        } else {
                            //upgrade to empty category
                        }
                    }

                    if (cat.equals("33")){
                        if (isDelete.equals("0")){

                            if (((SchoolsApplication) getContext()).getSetting("Year4") == true){
                                insertToDBwithUpdade("ALERTS", "NO", date, title, full_text, null, null, null, serverId);
                            }

                            if (((SchoolsApplication) getContext()).getSetting("Year4") == false){
                                //insertToDBwithUpdade("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                            }

                        } else {
                            //upgrade to empty category
                        }
                    }

                    if (cat.equals("34")){
                        if (isDelete.equals("0")){
                            if (((SchoolsApplication) getContext()).getSetting("Year5") == true){
                                insertToDBwithUpdade("ALERTS", "NO", date, title, full_text, null, null, null, serverId);
                            }
                            if (((SchoolsApplication) getContext()).getSetting("Year5") == false){
                                //insertToDBwithUpdade("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                            }
                        } else {
                            //upgrade to empty category
                        }
                    }

                    if (cat.equals("35")){
                        if (isDelete.equals("0")){
                            if (((SchoolsApplication) getContext()).getSetting("Year6") == true){
                                insertToDBwithUpdade("ALERTS", "NO", date, title, full_text, null, null, null, serverId);
                            }
                            if (((SchoolsApplication) getContext()).getSetting("Year6") == false){
                                //insertToDBwithUpdade("ALERTS", "YES", date, title, full_text, null, null, null, serverId);
                            }

                        } else {
                            //upgrade to empty category
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < news_jsonArray.length(); i++){
                try {
                    JSONObject news_json = news_jsonArray.getJSONObject(i);
                    String serverId = news_json.getString("news_id");
                    String title = news_json.getString("news_title");
                    String full_text = news_json.getString("news_full_text");
                    String url_full_text = news_json.getString("news_short_text");
                    String date = news_json.getString("news_date");
                    String isDelete = news_json.getString("news_isDelete");
                    if (isDelete.equals("0")){
                        if (((SchoolsApplication) getContext()).getSetting("News") == true){
                            insertToDBwithUpdade("NEWS", "NO", date, title, full_text, null, null, url_full_text, serverId);
                        }

                        if (((SchoolsApplication) getContext()).getSetting("News") == false){
                            insertToDBwithUpdade("NEWS", "YES", date, title, full_text, null, null, url_full_text, serverId);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < events_jsonArray.length(); i++){
                try {
                    JSONObject events_json = events_jsonArray.getJSONObject(i);
                    String serverId = events_json.getString("event_id");
                    String title = events_json.getString("event_title");
                    String full_text = events_json.getString("event_date_start");
                    String url_full_text = events_json.getString("event_date_end");
                    // String date = events_json.getString("event_date");
                    String date = events_json.getString("event_date_start");
                    String isDelete = events_json.getString("event_isDelete");
                    if (isDelete.equals("0")){
                        insertToDBwithUpdade("EVENTS", "NO", date, title, full_text, null, null, url_full_text, serverId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < newsletters_jsonArray.length(); i++){
                try {
                    JSONObject newsletters_json = newsletters_jsonArray.getJSONObject(i);
                    String serverId = newsletters_json.getString("newsletter_id");
                    String title = newsletters_json.getString("newsletter_title");
                    String date = newsletters_json.getString("newsletter_date");
                    String pdf = newsletters_json.getString("newsletter_url");
                    String isDelete = newsletters_json.getString("newsletter_isDelete");
                    if (isDelete.equals("0")){

                        if (((SchoolsApplication) getContext()).getSetting("Newsletters") == true){
                            insertToDBwithUpdade("NEWSLETTERS", "NO", date, title, null, null, pdf, null, serverId);
                        }

                        if (((SchoolsApplication) getContext()).getSetting("Newsletters") == false){
                            insertToDBwithUpdade("NEWSLETTERS", "YES", date, title, null, null, pdf, null, serverId);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



        }

        public void insertToDB(String category, String was_readed, String date, String title,
                               String full_text, String picture, String pdf, String url_full_news,
                               String serverId){
            ContentValues cv = new ContentValues();
            cv.put("category", category);
            cv.put("was_readed", was_readed);
            cv.put("date", date);
            cv.put("title", title);
            cv.put("full_text", full_text);
            cv.put("picture", picture);
            cv.put("pdf", pdf);
            cv.put("url_full_news", url_full_news);
            cv.put("serverId", serverId);

            dbHelper = new SQLiteHelper(getContext());
            db = dbHelper.getWritableDatabase();
            db.insert("mytable", null, cv);
            db.close();

            Log.d("rklogs", "insert (in loader) category_" + category);
        }

        public void insertToDBwithUpdade(String category, String was_readed, String date, String title,
                                         String full_text, String picture, String pdf, String url_full_news,
                                         String serverId){
            dbHelper = new SQLiteHelper(getContext());
            db = dbHelper.getWritableDatabase();

            Cursor c = db.query("mytable", null, null, null, null, null, null);

            if (c.moveToFirst() == false){
                insertToDB(category, was_readed, date, title, full_text, picture, pdf,
                        url_full_news, serverId);

                c.close();
                dbHelper.close();
                db.close();
                return;

            } else {

                do {
                    int idColIndex = c.getColumnIndex("id");
                    int categoryIndex = c.getColumnIndex("category");
                    int was_readedIndex = c.getColumnIndex("was_readed");
                    int dateIndex = c.getColumnIndex("date");
                    int titleIndex = c.getColumnIndex("title");
                    int full_textIndex = c.getColumnIndex("full_text");
                    int pictureIndex = c.getColumnIndex("picture");
                    int pdfIndex = c.getColumnIndex("pdf");
                    int url_full_newsIndex = c.getColumnIndex("url_full_news");
                    int serverIdIndex = c.getColumnIndex("serverId");

                    // получаем значения по номерам столбцов и пишем все в лог
               /* Log.d("rklogs",
                        "ID = " + c.getInt(idColIndex) +
                                ", category = " + c.getString(categoryIndex) +
                                ", was_readed = " + c.getString(was_readedIndex) +
                                ", date = " + c.getString(dateIndex) +
                                ", title = " + c.getString(titleIndex) +
                                ", full_text = " + c.getString(full_textIndex) +
                                ", picture = " + c.getString(pictureIndex) +
                                ", pdf = " + c.getString(pdfIndex) +
                                ", url_full_news = " + c.getString(url_full_newsIndex) +
                                ", serverId = " + c.getString(serverIdIndex)
                );*/
                    //when i found same object with serverId and category name in DB - UPDATE
                    //but was_read from get old

                    //id и категория найдены
                    if (((c.getString(categoryIndex)).equals(category) &&
                            ((c.getString(serverIdIndex)).equals(serverId)))) {

                        Log.d("rklogs", "//categor == and serverId ==");

                        //something different
                        ContentValues cv = new ContentValues();
                        cv.put("was_readed", c.getString(was_readedIndex));
                        cv.put("title", title);
                        cv.put("full_text", full_text);
                        cv.put("picture", picture);
                        cv.put("pdf", pdf);
                        cv.put("url_full_news", url_full_news);
                        db.update("mytable", cv, "id = ?", new String[]{
                                Integer.toString(c.getInt(idColIndex))});

                        c.close();
                        db.close();
                        dbHelper.close();
                        return;
                    }
                } while (c.moveToNext());

                Log.d("rklogs", "No matches into DB___ unical");
                insertToDB(category, was_readed, date, title, full_text, picture, pdf,
                        url_full_news, serverId);
            }
        }

}
