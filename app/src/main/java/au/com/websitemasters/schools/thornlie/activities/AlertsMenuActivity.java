package au.com.websitemasters.schools.thornlie.activities;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.SchoolsApplication;
import au.com.websitemasters.schools.thornlie.adapters.AlertsMenuAdapter;
import au.com.websitemasters.schools.thornlie.connection_retrofit.RetrofitClient;
import au.com.websitemasters.schools.thornlie.constants.Constants;
import au.com.websitemasters.schools.thornlie.loader.LoaderInserter;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.AlertsMenuObject;
import au.com.websitemasters.schools.thornlie.objects_for_retrofit.ANNEObject;
import au.com.websitemasters.schools.thornlie.push.MyGcmListenerService;
import au.com.websitemasters.schools.thornlie.utils.SQLiteHelper;
import au.com.websitemasters.schools.thornlie.utils.ServerObserverRetrofit;

public class AlertsMenuActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Bitmap>,
        ServerObserverRetrofit<Object, String> {

    private AlertsMenuAdapter adapter;

    public SQLiteDatabase db;
    public SQLiteHelper dbHelper;

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH.MM");
    private String currentDate = dateFormat.format(new Date());

    ArrayList<AlertsMenuObject> list = new ArrayList<>();

    private RetrofitClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alerts_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.alerts));
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertsMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertsMenuActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertsMenuActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        registerReceiver(broadcastReceiver, new IntentFilter(MyGcmListenerService.BROADCAST_ACTION));

        try{
            readFromDbAndFillList();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings_inactive_white, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }


    public void readFromDbAndFillList(){

        list.clear();

        dbHelper = new SQLiteHelper(getApplicationContext());
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
            int dateIndex = c.getColumnIndex("date");
            int titleIndex = c.getColumnIndex("title");
            int full_textIndex = c.getColumnIndex("full_text");
            int pictureIndex = c.getColumnIndex("picture");
            int pdfIndex = c.getColumnIndex("pdf");
            int url_full_newsIndex = c.getColumnIndex("url_full_news");
            int serverIdIndex = c.getColumnIndex("serverId");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("rklogs",
                        "ID = " + c.getInt(idColIndex) +
                                ", category = " + c.getString(categoryIndex) +
                                ", was_readed = " + c.getString(was_readedIndex)+
                                ", date = " + c.getString(dateIndex) +
                                ", title = " + c.getString(titleIndex)+
                                ", full_text = " + c.getString(full_textIndex) +
                                ", picture = " + c.getString(pictureIndex)+
                                ", pdf = " + c.getString(pdfIndex) +
                                ", url_full_news = " + c.getString(url_full_newsIndex) +
                                ", serverId = " + c.getString(serverIdIndex)
                );

                //------------If we found alerts, create obj and fill list-----------------

                //if (c.getString(categoryIndex).equals("ALERTS")){

                    list.add(new AlertsMenuObject(c.getString(titleIndex), c.getString(full_textIndex),
                            c.getString(was_readedIndex), c.getString(dateIndex), c.getString(serverIdIndex)));
                //}

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());

           setList();

        } else
            Log.d("rklogs", "0 rows");
        c.close();

        dbHelper.close();
        db.close();
    }

    private void setList(){
        ListView listView = (ListView) findViewById(R.id.listAlertsMenu);
        adapter = new AlertsMenuAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //**********set like a WAS READED clicked item*********

                dbHelper = new SQLiteHelper(getApplicationContext());
                db = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("was_readed", "YES");

                //**********get readry TITLE and FULL INFO to next screen (concrete)*********

                AlertsMenuObject amd = (AlertsMenuObject) adapter.getItem(position);
                Intent intent = new Intent(AlertsMenuActivity.this, AlertsConcreteActivity.class);
                intent.putExtra("title", amd.getTitle());
                intent.putExtra("full_text", amd.getFullTitle());
                intent.putExtra("date", amd.getDate());

                db.update("mytable", cv, "id = ?", new String[]{
                        //Integer.toString(position + 1)
                        Integer.toString(getID(amd.getServerId()))
                });
                dbHelper.close();
                db.close();
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(MyGcmListenerService.BROADCAST_ACTION));

        try{
            readFromDbAndFillList();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();
        }

        if (Constants.LoaderIsWorking == true){
            //do nothing, loader is busy
            Log.d("rklogs", "Constants.LoaderIsWorking == true");
        } else {
            Log.d("rklogs", "Constants.LoaderIsWorking == false");
            makeRequest();
        }
    }

//*************** get id by Sring, which it searches for ALERTS only (returns only LAST searched
// *VALUE (dont use with same value in table))**************************************************

    public int getID(String searchByServerId){

      int id = 0;

        db = dbHelper.getWritableDatabase();

        Cursor c = db.query("mytable", null, null, null, null, null, null);

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
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

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d("rklogs",
                        "ID = " + c.getInt(idColIndex) +
                                ", category = " + c.getString(categoryIndex) +
                                ", was_readed = " + c.getString(was_readedIndex)+
                                ", date = " + c.getString(dateIndex) +
                                ", title = " + c.getString(titleIndex)+
                                ", full_text = " + c.getString(full_textIndex) +
                                ", picture = " + c.getString(pictureIndex)+
                                ", pdf = " + c.getString(pdfIndex) +
                                ", url_full_news = " + c.getString(url_full_newsIndex) +
                                ", serverId = " + c.getString(serverIdIndex)
                );

                //------------If we found alerts, create obj and fill list-----------------

                if (c.getString(categoryIndex).equals("ALERTS")){

                    if (c.getString(serverIdIndex).equals(searchByServerId)){
                        id = c.getInt(idColIndex);
                    }
                }

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());

        } else
            Log.d("rklogs", "0 rows");
        c.close();

        //dbHelper.close();
        return id;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            dbHelper.close();
            db.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        unregisterReceiver(broadcastReceiver);
    }

//all under this line needs to refresh list after push in real time

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        Loader<Bitmap> loader = new LoaderInserter(this, args);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap result) {
        Log.d("rklogs", "onLoadFinished in alerts");
        try{
            readFromDbAndFillList();

        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("rklogs", "broadcastReceiver сработал");
            if (Constants.LoaderIsWorking == true){
                //do nothing, loader is busy
                Log.d("rklogs", "Constants.LoaderIsWorking == true");
            } else {
                Log.d("rklogs", "Constants.LoaderIsWorking == false");
                makeRequest();
            }
        }
    };

    public void makeRequest(){
        client = new RetrofitClient();
        client.setServerObserverRetrofit(this);
        client.postANNE(
                ((SchoolsApplication) getApplicationContext()).getUid(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_newsletters(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_events(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_news(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_alerts(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_pinfo());
    }

    @Override
    public void successExecuteObject(Object obj) {
        handleResponse(obj);
        ((SchoolsApplication) getApplicationContext()).setLastUpdated();
    }

    @Override
    public void failedExecute(String err) {
        Toast.makeText(this, getResources().getString(R.string.error_connection),
                Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(Object obj){

        ANNEObject anneObject = (ANNEObject) obj;
        Log.d("rklogs", "anneObject last_date_news:_"+ anneObject.last_date_news);
        Log.d("rklogs", "anneObject last_date_newsletters:_"+ anneObject.last_date_newsletters);
        Log.d("rklogs", "anneObject last_date_events:_"+ anneObject.last_date_events);
        Log.d("rklogs", "anneObject last_date_alerts:_"+ anneObject.last_date_alerts);
        Log.d("rklogs", "anneObject last_date_pinfo:_"+ anneObject.last_date_pinfo);

        JSONArray alert_jsonArray = convertToJsonArray((ArrayList) anneObject.alerts);
        JSONArray news_jsonArray = convertToJsonArray((ArrayList) anneObject.news);
        JSONArray newsletters_jsonArray = convertToJsonArray((ArrayList) anneObject.newsletters);
        JSONArray events_jsonArray = convertToJsonArray((ArrayList) anneObject.events);
        JSONArray pinfo_jsonArray = convertToJsonArray((ArrayList) anneObject.parents_info);

        fillBDbyArrays(alert_jsonArray, news_jsonArray, newsletters_jsonArray, events_jsonArray,
                pinfo_jsonArray);
    }

    private JSONArray convertToJsonArray(ArrayList list){
        Gson gson = new GsonBuilder().create();
        String listStringAlerts = gson.toJson((list), new TypeToken<ArrayList>() {
        }.getType());
        JSONArray jsonArray = null;
        try {
            jsonArray  = new JSONArray(listStringAlerts);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    private void fillBDbyArrays(JSONArray alert_jsonArray, JSONArray news_jsonArray,
                                JSONArray newsletters_jsonArray, JSONArray events_jsonArray,
                                JSONArray pinfo_jsonArray){
        Bundle bndl = new Bundle();
        bndl.putString("events_jsonArray", events_jsonArray.toString());
        bndl.putString("alert_jsonArray", alert_jsonArray.toString());
        bndl.putString("news_jsonArray", news_jsonArray.toString());
        bndl.putString("newsletters_jsonArray", newsletters_jsonArray.toString());
        bndl.putString("pinfo_jsonArray", pinfo_jsonArray.toString());
        getLoaderManager().initLoader(1, bndl, this);
    }
}
