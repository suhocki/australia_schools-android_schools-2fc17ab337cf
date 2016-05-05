package thronlie.balinasoft.by.sacredheartschoolthronlie.activities;

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

import thronlie.balinasoft.by.sacredheartschoolthronlie.R;
import thronlie.balinasoft.by.sacredheartschoolthronlie.connection_retrofit.RetrofitClient;
import thronlie.balinasoft.by.sacredheartschoolthronlie.constants.Constants;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_retrofit.ParentsObject;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.ServerObserver;

public class ParentsFeesActivity extends AppCompatActivity implements ServerObserver<Object, String> {

    private WebView myWebView;

    private RetrofitClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_info_uniform);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.fees));
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

        client = new RetrofitClient();
        client.postParents(Constants.PARENTS_ID_FEES);
        client.setServerObserver(this);

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


    @Override
    public void successExecuteObject(Object obj) {
        setInfo(obj);
    }




    @Override
    public void failedExecute(String err) {
    }

    public void setInfo(Object obj){

        ParentsObject parentsObject = (ParentsObject) obj;
        String html = "<html><body></body></html>";
        html =  parentsObject.pinfo_text;

        html = html.replace("images/content/",
                "http://shthorn.wa.edu.au/images/content/");

        String mime = "text/html";
        String encoding = "utf-8";
        myWebView.loadDataWithBaseURL(null, html, mime, encoding, null);

        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        TextView tvDate = (TextView)findViewById(R.id.tvDate);
        tvTitle.setText(parentsObject.pinfo_title);

        long date = parentsObject.pinfo_date;

        String dateString = new SimpleDateFormat("dd.MM.yyyy").format(new Date(date * 1000));

        tvDate.setText(dateString);
    }
}