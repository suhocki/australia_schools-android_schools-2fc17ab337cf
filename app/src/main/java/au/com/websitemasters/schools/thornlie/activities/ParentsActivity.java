package au.com.websitemasters.schools.thornlie.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import au.com.websitemasters.schools.thornlie.R;


public class ParentsActivity extends AppCompatActivity {

    private WebView myWebView;

    String text, date, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        text = intent.getStringExtra("text");
        date = intent.getStringExtra("date");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);

        myWebView = (WebView)this.findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        showInfo();
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

    public void showInfo(){

        String html = "<html><body></body></html>";
        html = text;

        html = html.replace("images/content/",
                "http://shthorn.wa.edu.au/images/content/");

        html = html.replace("index",
                "http://shthorn.wa.edu.au/index");

        String mime = "text/html";
        String encoding = "utf-8";
        myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);

        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        TextView tvDate = (TextView)findViewById(R.id.tvDate);
        tvTitle.setText(title);

       long date_long = Long.valueOf(date);

       String dateString = new SimpleDateFormat("dd.MM.yyyy").format(new Date(date_long * 1000));

        tvDate.setText(dateString);
    }
}