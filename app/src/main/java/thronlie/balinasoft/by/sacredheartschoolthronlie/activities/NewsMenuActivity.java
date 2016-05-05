package thronlie.balinasoft.by.sacredheartschoolthronlie.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import thronlie.balinasoft.by.sacredheartschoolthronlie.R;
import thronlie.balinasoft.by.sacredheartschoolthronlie.adapters.NewsMenuAdapter;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_adapters.NewsMenuObject;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.SQLiteHelper;

public class NewsMenuActivity extends AppCompatActivity {

    private NewsMenuAdapter adapter;

    public SQLiteDatabase db;
    public SQLiteHelper dbHelper;

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH.MM");
    private String currentDate = dateFormat.format(new Date());

    ArrayList<NewsMenuObject> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.news));
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new SQLiteHelper(this);

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsMenuActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsMenuActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsMenuActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        readFromDbAndFillList();
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

        db = dbHelper.getWritableDatabase();

        //Log.d("rklogs", "--- Rows in mytable: ---");
        //Cursor c = db.query("mytable", null, null, null, null, null, "date DESC");

        String query = "SELECT * FROM mytable WHERE category = 'NEWS' ORDER BY date DESC";
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

                if (c.getString(categoryIndex).equals("NEWS")){

                    list.add(new NewsMenuObject(c.getString(titleIndex), c.getString(pictureIndex),
                            c.getString(full_textIndex), c.getString(was_readedIndex), c.getString(dateIndex),
                            c.getString(url_full_newsIndex), c.getString(serverIdIndex)));
                }

                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());

            setList();

        } else
            Log.d("rklogs", "0 rows");
        c.close();

        dbHelper.close();
    }

    private void setList(){
        ListView listView = (ListView) findViewById(R.id.listNewsMenu);
        adapter = new NewsMenuAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //**********set like a WAS READED clicked item*********

                db = dbHelper.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put("was_readed", "YES");

                //**********get readry TITLE and FULL INFO to next screen (concrete)*********

                NewsMenuObject amd = (NewsMenuObject) adapter.getItem(position);
                Intent intent = new Intent(NewsMenuActivity.this, NewsConcreteActivity.class);
                intent.putExtra("title", amd.getTitle());
                intent.putExtra("date", amd.getDate());
                intent.putExtra("full_text", amd.getFullTitle());
                intent.putExtra("photo_link", amd.getPhotolink());
                db.update("mytable", cv, "id = ?", new String[]{Integer.toString(getID(amd.getServerId()))

                });
                dbHelper.close();
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        readFromDbAndFillList();
    }

//*************** get id by Sring, which it searches for ALERTS only (returns only LAST searched
// *VALUE (dont use with same value in table))**************************************************

    public int getID(String searchById){
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

                //if (c.getString(categoryIndex).equals("NEWS")){

                    if (c.getString(serverIdIndex).equals(searchById)){
                        id = c.getInt(idColIndex);
                    }
                //}
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
