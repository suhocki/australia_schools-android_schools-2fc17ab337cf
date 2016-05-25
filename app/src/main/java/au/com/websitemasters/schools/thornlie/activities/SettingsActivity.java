package au.com.websitemasters.schools.thornlie.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.UUID;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.SchoolsApplication;
import au.com.websitemasters.schools.thornlie.connection_retrofit.RetrofitClient;


public class SettingsActivity extends AppCompatActivity {

    private ImageView ivAlerts, ivKindy, ivPrePrimary, ivYear1, ivYear2, ivYear3, ivYear4, ivYear5, ivYear6,
    ivCalendar, ivParentsInfo, ivNewsletters;

    private LinearLayout linPrePrimary, linKindy, linAlerts, linYear1, linYear2, linYear3, linYear4,
    linYear5, linYear6;

    private RetrofitClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.settings));
        actionBar.setDisplayHomeAsUpEnabled(true);
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.color_blue), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        linKindy = (LinearLayout)findViewById(R.id.linKindy);
        linPrePrimary = (LinearLayout)findViewById(R.id.linPreprimary);
        linYear1 = (LinearLayout)findViewById(R.id.linYear1);
        linYear2 = (LinearLayout)findViewById(R.id.linYear2);
        linYear3 = (LinearLayout)findViewById(R.id.linYear3);
        linYear4 = (LinearLayout)findViewById(R.id.linYear4);
        linYear5 = (LinearLayout)findViewById(R.id.linYear5);
        linYear6 = (LinearLayout)findViewById(R.id.linYear6);

        ImageView btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        ivAlerts = (ImageView)findViewById(R.id.ivAlerts);
        ivAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Alerts") == true){
                    ivAlerts.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Alerts", false);
                    linKindy.setVisibility(View.GONE);
                    linPrePrimary.setVisibility(View.GONE);
                    linYear1.setVisibility(View.GONE);
                    linYear2.setVisibility(View.GONE);
                    linYear3.setVisibility(View.GONE);
                    linYear4.setVisibility(View.GONE);
                    linYear5.setVisibility(View.GONE);
                    linYear6.setVisibility(View.GONE);
                } else {
                    ivAlerts.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Alerts", true);
                    linKindy.setVisibility(View.VISIBLE);
                    linPrePrimary.setVisibility(View.VISIBLE);
                    linYear1.setVisibility(View.VISIBLE);
                    linYear2.setVisibility(View.VISIBLE);
                    linYear3.setVisibility(View.VISIBLE);
                    linYear4.setVisibility(View.VISIBLE);
                    linYear5.setVisibility(View.VISIBLE);
                    linYear6.setVisibility(View.VISIBLE);
                }
            }
        });

        ivKindy = (ImageView)findViewById(R.id.ivKindy);
        ivKindy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Kindy") == true){
                    ivKindy.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Kindy", false);
                } else {
                    ivKindy.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Kindy", true);
                }
            }
        });

        ivPrePrimary = (ImageView)findViewById(R.id.ivPrePrimary);
        ivPrePrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Pre-Primary") == true){
                    ivPrePrimary.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Pre-Primary", false);
                } else {
                    ivPrePrimary.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Pre-Primary", true);
                }
            }
        });

        ivYear1 = (ImageView)findViewById(R.id.ivYear1);
        ivYear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Year1") == true){
                    ivYear1.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year1", false);
                } else {
                    ivYear1.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year1", true);
                }
            }
        });

        ivYear2 = (ImageView)findViewById(R.id.ivYear2);
        ivYear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Year2") == true){
                    ivYear2.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year2", false);
                } else {
                    ivYear2.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year2", true);
                }
            }
        });

        ivYear3 = (ImageView)findViewById(R.id.ivYear3);
        ivYear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Year3") == true){
                    ivYear3.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year3", false);
                } else {
                    ivYear3.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year3", true);
                }
            }
        });

        ivYear4 = (ImageView)findViewById(R.id.ivYear4);
        ivYear4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Year4") == true){
                    ivYear4.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year4", false);
                } else {
                    ivYear4.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year4", true);
                }
            }
        });

        ivYear5 = (ImageView)findViewById(R.id.ivYear5);
        ivYear5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Year5") == true){
                    ivYear5.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year5", false);
                } else {
                    ivYear5.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year5", true);
                }
            }
        });

        ivYear6 = (ImageView)findViewById(R.id.ivYear6);
        ivYear6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Year6") == true){
                    ivYear6.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year6", false);
                } else {
                    ivYear6.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Year6", true);
                }
            }
        });

        ivNewsletters = (ImageView)findViewById(R.id.ivNewsletters);
        ivNewsletters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("Newsletters") == true){
                    ivNewsletters.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Newsletters", false);
                } else {
                    ivNewsletters.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("Newsletters", true);
                }
            }
        });

        ivCalendar = (ImageView)findViewById(R.id.ivNews);
        ivCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("News") == true){
                    ivCalendar.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("News", false);
                } else {
                    ivCalendar.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("News", true);
                }
            }
        });

        ivParentsInfo = (ImageView)findViewById(R.id.ivParentsInfo);
        ivParentsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((SchoolsApplication) getApplicationContext()).getSetting("ParentsInfo") == true){
                    ivParentsInfo.setImageResource(R.drawable.setting_off);
                    ((SchoolsApplication) getApplicationContext()).setSetting("ParentsInfo", false);
                } else {
                    ivParentsInfo.setImageResource(R.drawable.setting_on);
                    ((SchoolsApplication) getApplicationContext()).setSetting("ParentsInfo", true);
                }
            }
        });
        setImagesOfActialSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings_active, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImagesOfActialSettings(){

        if (((SchoolsApplication) getApplicationContext()).getSetting("ParentsInfo") == true){
            ivParentsInfo.setImageResource(R.drawable.setting_on);
        } else{
            ivParentsInfo.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Alerts") == true){
            ivAlerts.setImageResource(R.drawable.setting_on);
        } else{
            ivAlerts.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Kindy") == true){
            ivKindy.setImageResource(R.drawable.setting_on);
        } else{
            ivKindy.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Pre-Primary") == true){
            ivPrePrimary.setImageResource(R.drawable.setting_on);
        } else{
            ivPrePrimary.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Year1") == true){
            ivYear1.setImageResource(R.drawable.setting_on);
        } else{
            ivYear1.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Year2") == true){
            ivYear2.setImageResource(R.drawable.setting_on);
        } else{
            ivYear2.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Year3") == true){
            ivYear3.setImageResource(R.drawable.setting_on);
        } else{
            ivYear3.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Year4") == true){
            ivYear4.setImageResource(R.drawable.setting_on);
        } else{
            ivYear4.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Year5") == true){
            ivYear5.setImageResource(R.drawable.setting_on);
        } else{
            ivYear5.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Year6") == true){
            ivYear6.setImageResource(R.drawable.setting_on);
        } else{
            ivYear6.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("News") == true){
            ivCalendar.setImageResource(R.drawable.setting_on);
        } else{
            ivCalendar.setImageResource(R.drawable.setting_off);
        }

        if (((SchoolsApplication) getApplicationContext()).getSetting("Newsletters") == true){
            ivNewsletters.setImageResource(R.drawable.setting_on);
        } else{
            ivNewsletters.setImageResource(R.drawable.setting_off);
        }
    }

    @Override
    public void onPause(){

        client = new RetrofitClient();
        client.postSettings(
                getUid(),
                getNumberOfCategoryOption("Alerts"),
                getNumberOfCategoryOption("Newsletters"),
                getNumberOfCategoryOption("News"),
                getNumberOfCategoryOption("ParentsInfo"),
                getNumberOfCategoryOption("Kindy"),
                getNumberOfCategoryOption("Pre-Primary"),
                getNumberOfCategoryOption("Year1"),
                getNumberOfCategoryOption("Year2"),
                getNumberOfCategoryOption("Year3"),
                getNumberOfCategoryOption("Year4"),
                getNumberOfCategoryOption("Year5"),
                getNumberOfCategoryOption("Year6"));

        super.onPause();
    }

    private String getNumberOfCategoryOption(String category){
        String number = "1";
        if (((SchoolsApplication) getApplicationContext()).getSetting(category) == true){
            number = "1";
        } else {
            number = "0";
        }
        //Log.d("rklogs", "Setting test >> category_" + category + " send_" + number);
        return number;
    }

    private String getUid(){
        String uid;
        try{
            uid = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        catch(NullPointerException e){
            final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            uid = deviceUuid.toString();
        }
        return uid;
    }

}
