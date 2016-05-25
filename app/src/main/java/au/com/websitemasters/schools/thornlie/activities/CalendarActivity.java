package au.com.websitemasters.schools.thornlie.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.adapters.CalendarAdapter;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.CalendarObject;
import au.com.websitemasters.schools.thornlie.utils.DateHelper;
import au.com.websitemasters.schools.thornlie.utils.DateStringGetter;
import au.com.websitemasters.schools.thornlie.utils.SQLiteHelper;

public class CalendarActivity extends AppCompatActivity {

    public CaldroidFragment caldroidFragment;

    public CalendarAdapter adapter;

    public SQLiteDatabase db;
    public SQLiteHelper dbHelper;

    private DateFormat dayFormat = new SimpleDateFormat("dd.MM.yyyy");
    private String currentDate = dayFormat.format(new Date());

    private DateStringGetter dateGetter = new DateStringGetter();
    private DateHelper dateHelper = new DateHelper();

    private ArrayList<CalendarObject> list = new ArrayList<>();

    private ArrayList<Date> dateList = new ArrayList<>();

    private Drawable white, selected, blue;

    ListView listView;

    LinearLayout linCalendarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.events_calendar));
        actionBar.setDisplayHomeAsUpEnabled(true);

        linCalendarFragment = (LinearLayout)findViewById(R.id.linCalendarFragment);

        dbHelper = new SQLiteHelper(this);

        listView = (ListView) findViewById(R.id.listCalendar);
        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();

        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);
        if (diagonalInches>=6.5){
            // 6.5inch device or bigger
            selected = ResourcesCompat.getDrawable(getResources(), R.drawable.selested_date_high, null);
        }else{
            // smaller device
            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
            selected = ResourcesCompat.getDrawable(getResources(), R.drawable.selested_date_low, null);
        }

        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);
        showCurrentDate();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.linCalendarFragment, caldroidFragment);
        t.commit();

        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {

                if (dateList.isEmpty() == false){
                    white = ResourcesCompat.getDrawable(getResources(), R.drawable.empty_date_low, null);
                    caldroidFragment.setBackgroundDrawableForDate(white, dateList.get(0));
                    dateList.clear();
                }
                readFromDbAndFillList(dayFormat.format(date));

                blue = ResourcesCompat.getDrawable(getResources(), R.drawable.blue, null);
                caldroidFragment.setBackgroundDrawableForDate(blue, date);

                showCurrentDate();
                caldroidFragment.refreshView();
                dateList.add(date);
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Button leftArrow = caldroidFragment.getLeftArrowButton();
                    leftArrow.setBackgroundResource(R.drawable.arrow_ll);
                    Button rightArrow = caldroidFragment.getRightArrowButton();
                    rightArrow.setBackgroundResource(R.drawable.arrow_rr);
                    readFromDbAndFillList(currentDate);
                    showCurrentDate();
                }
            }
        };
        caldroidFragment.setCaldroidListener(listener);
    }


    public void readFromDbAndFillList(String date){

        list.clear();
        db = dbHelper.getWritableDatabase();
        String query = "SELECT * FROM mytable WHERE category = '" + "EVENTS'";
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            //int idColIndex = c.getColumnIndex("id");
            int categoryIndex = c.getColumnIndex("category");
            int was_readedIndex = c.getColumnIndex("was_readed");
            int dateIndex = c.getColumnIndex("date");
            int titleIndex = c.getColumnIndex("title");
            int full_textIndex = c.getColumnIndex("full_text");
            //int pictureIndex = c.getColumnIndex("picture");
            //int pdfIndex = c.getColumnIndex("pdf");
            int url_full_newsIndex = c.getColumnIndex("url_full_news");
            int serverIdIndex = c.getColumnIndex("serverId");

            do {
                String dateFromDB = dateHelper.convertSecondsToDate(c.getString(dateIndex), "dd.MM.yyyy HH.mm");
                if (dateGetter.getWithoutHour(dateFromDB).equals(date)){
                    list.add(new CalendarObject(c.getString(titleIndex),
                            c.getString(full_textIndex),
                            c.getString(url_full_newsIndex),
                            c.getString(was_readedIndex),
                            c.getString(dateIndex),
                            c.getString(serverIdIndex)));
                }
             //-------------------add marks to calendar fragment------------------------------------
                if (c.getString(dateIndex) != null){
                    String dbDate = dateHelper.convertSecondsToDate(c.getString(dateIndex), "dd.MM.yyyy HH.mm");
                    caldroidFragment.setBackgroundDrawableForDate(selected, dateGetter.convertStringToDate(
                            dateGetter.getWithoutHour(dbDate)));
                }
            } while (c.moveToNext());

            fillListOfEvents();
        } else
            Log.d("rklogs", "0 rows");
        c.close();
        dbHelper.close();
    }

    private void fillListOfEvents(){
        adapter = new CalendarAdapter(this, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CalendarObject cmd = (CalendarObject) adapter.getItem(position);
                Intent intent = new Intent(CalendarActivity.this, EventsConcreteActivity.class);
                intent.putExtra("title", cmd.getTitle());
                intent.putExtra("date", cmd.getDate());
                intent.putExtra("date_end", cmd.getDateEnd());
                intent.putExtra("date_start", cmd.getDateStart());
                startActivity(intent);
            }
        });
    }

    private void showCurrentDate() {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, 0);
        Date date = cal.getTime();

        if (caldroidFragment != null) {
            blue = ResourcesCompat.getDrawable(getResources(), R.drawable.blue, null);
            caldroidFragment.setBackgroundDrawableForDate(blue, date);

            caldroidFragment.setTextColorForDate(R.color.color_white, date);
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
}
