package au.com.websitemasters.schools.thornlie.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.adapters.EventsMenuAdapter;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.EventsMenuHeaderObject;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.EventsMenuObject;
import au.com.websitemasters.schools.thornlie.utils.DateHelper;
import au.com.websitemasters.schools.thornlie.utils.SQLiteHelper;


public class EventsMenuActivity extends AppCompatActivity {

    private EventsMenuAdapter adapter;

    public SQLiteDatabase db;
    public SQLiteHelper dbHelper;

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH.MM");
    private String currentDate = dateFormat.format(new Date());

    private ArrayList list = new ArrayList();

    private DateHelper dateHelper = new DateHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_menu);

        dbHelper = new SQLiteHelper(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.events_calendar));
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsMenuActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsMenuActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsMenuActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

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


        String query = "SELECT * FROM mytable WHERE category = 'EVENTS' and date > '" +
                String.valueOf(dateHelper.getCurrentDate_Seconds()) + "' ORDER BY date ASC";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {


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

                    if (list.size() == 0){
                        list.add(new EventsMenuHeaderObject(c.getString(dateIndex)));
                    }

                    //last added object get date
                    //default: until we dont get datePrevious - we it = dateNow
                   String dateNow = dateHelper.convertSecondsToDate(c.getString(dateIndex), "MM.yyyy");
                    String datePrevious = dateNow;

                    Object lastObjInList = list.get(list.size()-1);
                    if (lastObjInList instanceof EventsMenuObject){
                        EventsMenuObject lastEventInList = (EventsMenuObject) lastObjInList;
                        //get month and year
                        datePrevious = dateHelper.convertSecondsToDate(lastEventInList.getDate(), "MM.yyyy");

                        Log.d("rklogs", "dateNow_" + dateNow);
                        Log.d("rklogs", "datePrevious_" + datePrevious);
                    }


                    if ((list.size() != 0) && (!datePrevious.equals(dateNow))){
                        list.add(new EventsMenuHeaderObject(c.getString(dateIndex)));
                        Log.d("rklogs", "datePrevious != dateNow");
                    }

                    list.add(new EventsMenuObject(c.getString(titleIndex), c.getString(full_textIndex),
                            c.getString(url_full_newsIndex), c.getString(was_readedIndex),
                            c.getString(dateIndex), c.getString(serverIdIndex)));

            } while (c.moveToNext());

            setList();

        } else
            Log.d("rklogs", "0 rows");
        c.close();

        dbHelper.close();

        Log.d("rklogs", "v EVENT list.size_" + list.size());
    }

    private void setList(){
        ListView listView = (ListView) findViewById(R.id.listEventsMenu);
        adapter = new EventsMenuAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (adapter.getItem(position) instanceof EventsMenuObject) {

                    //**********set like a WAS READED clicked item*********

                    db = dbHelper.getWritableDatabase();
                    ContentValues cv = new ContentValues();
                    cv.put("was_readed", "YES");

                    //**********get readry TITLE and FULL INFO to next screen (concrete)*********

                    EventsMenuObject amd = (EventsMenuObject) adapter.getItem(position);
                    Intent intent = new Intent(EventsMenuActivity.this, EventsConcreteActivity.class);
                    intent.putExtra("title", amd.getTitle());
                    intent.putExtra("date", amd.getDate());
                    intent.putExtra("date_end", amd.getDateEnd());
                    intent.putExtra("dateUp", amd.getDateUp());

                    db.update("mytable", cv, "id = ?", new String[]{
                            //Integer.toString(position + 1)
                            Integer.toString(getID(amd.getServerId()))
                    });
                    dbHelper.close();
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            readFromDbAndFillList();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();
        }
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
    }



//*************** get id by Sring, which it searches for ALERTS only (returns only LAST searched
// *VALUE (dont use with same value in table))**************************************************

    public int getID(String searchInAlertsString){

        int id = 0;

        //db = dbHelper.getWritableDatabase();

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

                if (c.getString(categoryIndex).equals("EVENTS")){

                    if (c.getString(serverIdIndex).equals(searchInAlertsString)){
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
}
