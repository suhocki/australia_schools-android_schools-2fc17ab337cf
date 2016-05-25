package au.com.websitemasters.schools.thornlie.activities;

import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.loader.LoaderBadge;
import au.com.websitemasters.schools.thornlie.utils.DateHelper;



public class AlertsConcreteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts_concrete);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String full_text = intent.getStringExtra("full_text");
        String date = intent.getStringExtra("date");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        TextView tvFullText = (TextView)findViewById(R.id.tvFullText);
        tvFullText.setText(full_text);

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertsConcreteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertsConcreteActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertsConcreteActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        TextView tvDate = (TextView)findViewById(R.id.tvDate);
        DateHelper dateHelper = new DateHelper();
        String dateIs = dateHelper.convertSecondsToDate(date, "dd MMMM, yyyy");
        tvDate.setText("Last Updated: " + dateIs);

        //count unreaded alerts, news, newsletters and show badge
        Loader<Bitmap> loaderBadge = new LoaderBadge(this, null);
        loaderBadge.forceLoad();
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
