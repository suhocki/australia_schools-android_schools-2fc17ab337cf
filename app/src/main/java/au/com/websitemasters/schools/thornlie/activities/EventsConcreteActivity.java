package au.com.websitemasters.schools.thornlie.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.utils.DateHelper;


public class EventsConcreteActivity extends AppCompatActivity {

    private Button btnAdd, btnDay;

    private TextView tvTitle;

    private String date, dateUp, title, date_end, date_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_concrete);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.events_calendar));
        actionBar.setDisplayHomeAsUpEnabled(true);

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsConcreteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsConcreteActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsConcreteActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventsConcreteActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
        btnDay = (Button)findViewById(R.id.btnDay);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        title = intent.getStringExtra("title");
        date_end = intent.getStringExtra("date_end");
        date_start = intent.getStringExtra("date_start");
        dateUp = intent.getStringExtra("dateUp");

        TextView tvDate = (TextView)findViewById(R.id.tvDate);
        DateHelper dateHelper = new DateHelper();

        String dateIs= "";
        try{
            dateIs = dateHelper.convertSecondsToDate(dateUp, "dd MMMM, yyyy");
        } catch (NumberFormatException e){
            e.printStackTrace();
          //  Toast.makeText(this, getResources().getString(R.string.emptyDate),
         //           Toast.LENGTH_SHORT).show();
        }
        tvDate.setText("Last Updated: " + dateIs);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAlreadyInCalendar();
            }
        });

        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        btnDay.setText(dateHelper.convertSecondsToDate(date, "dd"));
    }

    public boolean isEventInCal(Context context, String cal_meeting_id) {

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.android.calendar/events"),
                new String[] { "title" }, " title = ? ",
                new String[] { cal_meeting_id }, null);

        if (cursor.moveToFirst()) {
            //Yes Event Exist...
            return true;
        }
        return false;
    }

    private void checkAlreadyInCalendar(){

       if ((isEventInCal(this, title)) == true){
           AlertDialog.Builder dialog = new AlertDialog.Builder(this);
           dialog.setMessage("You already have this event in your calendar.");
           dialog.setPositiveButton("Add to Calendar", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   addEvent();
               }
           });
           dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   //do nothing
               }
           });
           dialog.show();

       } else {
           addEvent();
       }
    }

    private void addEvent(){
        try {
            Calendar cal = Calendar.getInstance();
            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra("beginTime", (Long.valueOf(date_start) * 1000));
            intent.putExtra("allDay", false);
            //intent.putExtra("rrule", "FREQ=DAILY");
            intent.putExtra("endTime", (Long.valueOf(date_end)* 1000));
            intent.putExtra("title", title);
            startActivity(intent);
        } catch (NumberFormatException e){
            e.printStackTrace();
            Toast.makeText(this, getResources().getString(R.string.emptyDate),
                    Toast.LENGTH_SHORT).show();
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
