package thronlie.balinasoft.by.sacredheartschoolthronlie.activities;

import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
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

import thronlie.balinasoft.by.sacredheartschoolthronlie.R;
import thronlie.balinasoft.by.sacredheartschoolthronlie.loader.LoaderBadge;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.DateHelper;

public class NewslettersConcreteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsletters_concrete);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        final String pdf_link = intent.getStringExtra("pdf_link");
        String date = intent.getStringExtra("date");

        TextView tvDate = (TextView)findViewById(R.id.tvDate);
        DateHelper dateHelper = new DateHelper();
        String dateIs = dateHelper.convertSecondsToDate(date, "dd MMMM, yyyy");
        tvDate.setText("Last Updated: " + dateIs);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        TextView tvFullText = (TextView)findViewById(R.id.tvFullText);

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewslettersConcreteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewslettersConcreteActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewslettersConcreteActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewslettersConcreteActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        Button btnViewNewsletter = (Button) findViewById(R.id.btnViewNewsletter);
        btnViewNewsletter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPDF(pdf_link);
            }
        });

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

    private void openPDF(String url){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        browserIntent.setDataAndType(Uri.parse(
               // url),
                        "http://docs.google.com/viewer?url=" + url),
                "text/html");



        startActivity(browserIntent);

    }

}
