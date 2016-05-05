package thronlie.balinasoft.by.sacredheartschoolthronlie.activities;

import android.app.LoaderManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GcmListenerService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import me.leolin.shortcutbadger.ShortcutBadger;
import thronlie.balinasoft.by.sacredheartschoolthronlie.R;
import thronlie.balinasoft.by.sacredheartschoolthronlie.SchoolsApplication;
import thronlie.balinasoft.by.sacredheartschoolthronlie.connection_retrofit.RetrofitClient;
import thronlie.balinasoft.by.sacredheartschoolthronlie.constants.Constants;
import thronlie.balinasoft.by.sacredheartschoolthronlie.loader.LoaderInserter;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_retrofit.ANNEObject;
import thronlie.balinasoft.by.sacredheartschoolthronlie.push.RegistrationIntentService;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.DateHelper;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.SQLiteHelper;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.ServerObserver;

    public class MainActivity extends AppCompatActivity implements ServerObserver<Object, String>,
            LoaderManager.LoaderCallbacks<Bitmap>, SwipeRefreshLayout.OnRefreshListener {

    private LinearLayout linParentsMenu, linParentsInfo, linAlerts, linEvents, linNewsletters,
        linNews, linContactUs, linUniform, linCanteen, linPFAssociation, linFees, linEnrolements;

    private Button btnAlertsMessages, btnEventsMessages, btnNewsLettersMessages, btnNewsMessages,
        btnParentsInfoMessages, btnUniformShopMessages, btnCanteenMessages, btnPFAssociationMessages,
        btnFeesMessages, btnEnrolementsMessages;

    private ImageView ivParentsInfoArrow;

    private int alertsMessages, eventsMessages, newsLettersMessages, newsMessages,
        parents_uniform, parents_canteen, parents_pfassociation, parents_fees,
        parents_enrolements;

    private final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    private boolean parentsMenuVisible = false;

    public SQLiteDatabase db;
    public SQLiteHelper dbHelper;

    private RetrofitClient client;

    private SharedPreferences prefs;

    private DateHelper dateHelper = new DateHelper();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.home));
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        ShortcutBadger.applyCount(this, 5);

        btnAlertsMessages = (Button)findViewById(R.id.btnAlertsMessages);
        btnEventsMessages = (Button)findViewById(R.id.btnEventsMessages);
        btnNewsLettersMessages = (Button)findViewById(R.id.btnNewsLettersMessages);
        btnNewsMessages = (Button)findViewById(R.id.btnNewsMessages);
        btnParentsInfoMessages = (Button)findViewById(R.id.btnParentsInfoMessages);
        btnUniformShopMessages = (Button)findViewById(R.id.btnUniformShopMessages);
        btnCanteenMessages = (Button)findViewById(R.id.btnCanteenMessages);
        btnPFAssociationMessages = (Button)findViewById(R.id.btnPFAssociationMessages);
        btnFeesMessages = (Button)findViewById(R.id.btnFeesMessages);
        btnEnrolementsMessages = (Button)findViewById(R.id.btnEnrolementsMessages);

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, AlertsMenuActivity.class);
        startActivity(intent);
        }
        });

        ImageView btnFeed = (ImageView)findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, FeedActivity.class);
        startActivity(intent);
        }
        });

        ImageView btnCalendar = (ImageView)findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
        startActivity(intent);
        }
        });

        ivParentsInfoArrow = (ImageView)findViewById(R.id.ivParentsInfoArrow);
        ivParentsInfoArrow.setImageResource(R.drawable.arrow_right);

        linAlerts = (LinearLayout) findViewById(R.id.linAlerts);
        linAlerts.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, AlertsMenuActivity.class);
        startActivity(intent);
        }
        });

        linEvents = (LinearLayout) findViewById(R.id.linEvents);
        linEvents.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, EventsMenuActivity.class);
        startActivity(intent);
        }
        });

        linNewsletters = (LinearLayout) findViewById(R.id.linNewsLetters);
        linNewsletters.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, NewslettersMenuActivity.class);
        startActivity(intent);
        }
        });

        linNews = (LinearLayout) findViewById(R.id.linNews);
        linNews.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, NewsMenuActivity.class);
        startActivity(intent);
        }
        });

        linContactUs = (LinearLayout) findViewById(R.id.linContactUs);
        linContactUs.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
        startActivity(intent);
        }
        });

        linParentsMenu = (LinearLayout) findViewById(R.id.linParentsMenu);

        linParentsInfo = (LinearLayout) findViewById(R.id.linParentsInfo);

        linParentsInfo.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        if (parentsMenuVisible == false) {
        linParentsMenu.setVisibility(View.VISIBLE);
        parentsMenuVisible = true;
        ivParentsInfoArrow.setImageResource(R.drawable.arrow_down);
        } else {
            linParentsMenu.setVisibility(View.GONE);
            parentsMenuVisible = false;
            ivParentsInfoArrow.setImageResource(R.drawable.arrow_right);
         }
        }
        });

        linUniform = (LinearLayout) findViewById(R.id.linUniform);
        linUniform.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        markReadedAllUnreadedFromDB_byCategory("PARENTS_UNIFORM");
        Intent intent = new Intent(MainActivity.this, ParentsUniformActivity.class);
        startActivity(intent);
        }
        });

        linCanteen = (LinearLayout) findViewById(R.id.linCanteen);
        linCanteen.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        markReadedAllUnreadedFromDB_byCategory("PARENTS_CANTEEN");
        Intent intent = new Intent(MainActivity.this, ParentsCanteenActivity.class);
        startActivity(intent);
        }
        });

        linPFAssociation = (LinearLayout) findViewById(R.id.linPFAssociation);
        linPFAssociation.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        markReadedAllUnreadedFromDB_byCategory("PARENTS_PFASSOCIATION");
        Intent intent = new Intent(MainActivity.this, ParentsPFAssociationActivity.class);
        startActivity(intent);
        }
        });

        linFees = (LinearLayout) findViewById(R.id.linFees);
        linFees.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        markReadedAllUnreadedFromDB_byCategory("PARENTS_FEES");
        Intent intent = new Intent(MainActivity.this, ParentsFeesActivity.class);
        startActivity(intent);
        }
        });

        linEnrolements = (LinearLayout) findViewById(R.id.linEnrolements);
        linEnrolements.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        markReadedAllUnreadedFromDB_byCategory("PARENTS_ENROLEMENTS");
        Intent intent = new Intent(MainActivity.this, ParentsEnrolementsActivity.class);
        startActivity(intent);
        }
        });

        ivParentsInfoArrow = (ImageView)findViewById(R.id.ivParentsInfoArrow);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context);
        boolean sentToken = sharedPreferences
        .getBoolean(getResources().getString(R.string.sent_token_to_server), false);
        if (sentToken) {
        Log.d("rklogs", getString(R.string.gcm_send_message));
        } else {
        Log.d("rklogs", getString(R.string.token_error_message));
        }
        }
        };

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.color_blue, R.color.color_teal,
                R.color.color_dark_blue, R.color.color_green);

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
        }


    }


        public class GcmIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(this.getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d("rklogs", "refreshed token_" + token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //send token to app server
    }
}

public class GcmIntentService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Test")
                .setContentText(message);
        notificationManager.notify(1, mBuilder.build());
    }
}

    @Override
    protected void onResume() {
        super.onResume();

        //clear notifications
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(0);

        //hide Parents Info
        linParentsMenu.setVisibility(View.GONE);
        parentsMenuVisible = false;
        ivParentsInfoArrow.setImageResource(R.drawable.arrow_right);

        registerReceiver();
        readFromDBAndMarkRed();
        Log.d("rklogs", "onResume");


        if (Constants.LoaderIsWorking == true){
            //do nothing, loader is busy
            Log.d("rklogs", "Constants.LoaderIsWorking == true");
        } else {
            Log.d("rklogs", "Constants.LoaderIsWorking == false");
            mSwipeRefreshLayout.setRefreshing(true);
            client = new RetrofitClient();
            client.setServerObserver(this);
            client.postANNE(
                    getUid(),
                    getLast_date_newsletters(),
                    getLast_date_events(),
                    getLast_date_news(),
                    getLast_date_alerts());
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        dbHelper.close();
        db.close();

    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(getResources().getString(R.string.registration_complete)));
            isReceiverRegistered = true;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.d("rklogs", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings_inactive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertToDB(String category, String was_readed, String date, String title,
                           String full_text, String picture, String pdf, String url_full_news,
                           String serverId){

        ContentValues cv = new ContentValues();
        cv.put("category", category);
        cv.put("was_readed", was_readed);
        cv.put("date", date);
        cv.put("title", title);
        cv.put("full_text", full_text);
        cv.put("picture", picture);
        cv.put("pdf", pdf);
        cv.put("url_full_news", url_full_news);
        cv.put("serverId", serverId);

        dbHelper = new SQLiteHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        db.insert("mytable", null, cv);
        db.close();

        Log.d("rklogs", "insert (activity) category_" + category);
    }


    public void readFromDBAndMarkRed(){

        try {

            newsMessages = 0;
            alertsMessages = 0;
            newsLettersMessages = 0;
            eventsMessages = 0;
            parents_uniform = 0;
            parents_canteen = 0;
            parents_pfassociation = 0;
            parents_fees = 0;
            parents_enrolements = 0;

            dbHelper = new SQLiteHelper(getApplicationContext());
            db = dbHelper.getWritableDatabase();

            Log.d("rklogs", "--- Rows in mytable: ---");
            Cursor c = db.query("mytable", null, null, null, null, null, null);

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
                    // получаем значения по номерам столбцов и пишем все в лог
                /*Log.d("rklogs",
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
                );*/

                    //------------Count of not writen messages-----------------

                    if (c.getString(categoryIndex).equals("ALERTS")&&
                            (c.getString(was_readedIndex).equals("NO"))){
                        alertsMessages = alertsMessages + 1;
                    }

                /*if (c.getString(categoryIndex).equals("EVENTS")&&
                        (c.getString(was_readedIndex).equals("NO"))){
                    eventsMessages = eventsMessages + 1;
                }*/

                    if (c.getString(categoryIndex).equals("NEWSLETTERS")&&
                            (c.getString(was_readedIndex).equals("NO"))){
                        newsLettersMessages = newsLettersMessages + 1;
                    }

                    if (c.getString(categoryIndex).equals("NEWS")&&
                            (c.getString(was_readedIndex).equals("NO"))){
                        newsMessages = newsMessages + 1;
                    }

                    if (c.getString(categoryIndex).equals("PARENTS_UNIFORM")&&
                            (c.getString(was_readedIndex).equals("NO"))){
                        parents_uniform = 1;
                    }

                    if (c.getString(categoryIndex).equals("PARENTS_CANTEEN")&&
                            (c.getString(was_readedIndex).equals("NO"))){
                        parents_canteen = 1;
                    }

                    if (c.getString(categoryIndex).equals("PARENTS_PFASSOCIATION")&&
                            (c.getString(was_readedIndex).equals("NO"))){
                        parents_pfassociation = 1;
                    }

                    if (c.getString(categoryIndex).equals("PARENTS_FEES")&&
                            (c.getString(was_readedIndex).equals("NO"))){
                        parents_fees = 1;
                    }

                    if (c.getString(categoryIndex).equals("PARENTS_ENROLEMENTS")&&
                            (c.getString(was_readedIndex).equals("NO"))){
                        parents_enrolements = 1;
                    }

                } while (c.moveToNext());

                //------------Show of not writen messages-----------------

                if (alertsMessages != 0){
                    btnAlertsMessages.setBackgroundResource(R.drawable.message_active);
                    btnAlertsMessages.setText(Integer.toString(alertsMessages));
                } else {
                    btnAlertsMessages.setBackgroundResource(R.drawable.message_inactive);
                    btnAlertsMessages.setText("");
                }

            /*if (eventsMessages != 0){
                btnEventsMessages.setBackgroundResource(R.drawable.message_active);
                btnEventsMessages.setText(Integer.toString(eventsMessages));
            } else {
                btnEventsMessages.setBackgroundResource(R.drawable.message_inactive);
                btnEventsMessages.setText("");
            }*/

                if (newsLettersMessages != 0) {
                    btnNewsLettersMessages.setBackgroundResource(R.drawable.message_active);
                    btnNewsLettersMessages.setText(Integer.toString(newsLettersMessages));
                } else {
                    btnNewsLettersMessages.setBackgroundResource(R.drawable.message_inactive);
                    btnNewsLettersMessages.setText("");
                }

                if (newsMessages != 0){
                    btnNewsMessages.setBackgroundResource(R.drawable.message_active);
                    btnNewsMessages.setText(Integer.toString(newsMessages));
                } else {
                    btnNewsMessages.setBackgroundResource(R.drawable.message_inactive);
                    btnNewsMessages.setText("");
                }

                if (parents_uniform != 0){
                    btnUniformShopMessages.setBackgroundResource(R.drawable.message_active);
                    btnParentsInfoMessages.setBackgroundResource(R.drawable.message_active);
                } else {
                    btnUniformShopMessages.setBackgroundResource(R.drawable.message_inactive);
                    btnParentsInfoMessages.setBackgroundResource(R.drawable.message_inactive);
                }

                if (parents_canteen != 0){
                    btnCanteenMessages.setBackgroundResource(R.drawable.message_active);
                    btnParentsInfoMessages.setBackgroundResource(R.drawable.message_active);
                } else {
                    btnCanteenMessages.setBackgroundResource(R.drawable.message_inactive);
                }

                if (parents_pfassociation != 0){
                    btnPFAssociationMessages.setBackgroundResource(R.drawable.message_active);
                    btnParentsInfoMessages.setBackgroundResource(R.drawable.message_active);
                } else {
                    btnPFAssociationMessages.setBackgroundResource(R.drawable.message_inactive);
                }

                if (parents_fees != 0){
                    btnFeesMessages.setBackgroundResource(R.drawable.message_active);
                    btnParentsInfoMessages.setBackgroundResource(R.drawable.message_active);
                } else {
                    btnFeesMessages.setBackgroundResource(R.drawable.message_inactive);
                }

                if (parents_enrolements != 0){
                    btnEnrolementsMessages.setBackgroundResource(R.drawable.message_active);
                    btnParentsInfoMessages.setBackgroundResource(R.drawable.message_active);
                } else {
                    btnEnrolementsMessages.setBackgroundResource(R.drawable.message_inactive);
                }

            } else
                Log.d("rklogs", "0 rows");
            c.close();
            db.close();
            dbHelper.close();

        } catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();

        }
    }

    public void markReadedAllUnreadedFromDB_byCategory(String category){

        db = dbHelper.getWritableDatabase();

        Cursor c = db.query("mytable", null, null, null, null, null, null);

        if (c.moveToFirst()) {

            int idColIndex = c.getColumnIndex("id");
            int categoryIndex = c.getColumnIndex("category");
            int was_readedIndex = c.getColumnIndex("was_readed");
            //int dateIndex = c.getColumnIndex("date");
            //int titleIndex = c.getColumnIndex("title");
            //int full_textIndex = c.getColumnIndex("full_text");
            //int pictureIndex = c.getColumnIndex("picture");
            //int pdfIndex = c.getColumnIndex("pdf");
            //int url_full_newsIndex = c.getColumnIndex("url_full_news");

            do {
                /*Log.d("rklogs",
                        "ID = " + c.getInt(idColIndex) +
                                ", category = " + c.getString(categoryIndex) +
                                ", was_readed = " + c.getString(was_readedIndex)+
                                ", date = " + c.getString(dateIndex) +
                                ", title = " + c.getString(titleIndex)+
                                ", full_text = " + c.getString(full_textIndex) +
                                ", picture = " + c.getString(pictureIndex)+
                                ", pdf = " + c.getString(pdfIndex) +
                                ", url_full_news = " + c.getString(url_full_newsIndex)
                );*/

                if ((c.getString(categoryIndex).equals(category)) &&
                        (c.getString(was_readedIndex).equals("NO"))){

                    ContentValues cv = new ContentValues();
                    cv.put("was_readed", "YES");
                    db.update("mytable", cv, "id = ?",
                            new String[]{ Integer.toString(c.getInt(idColIndex))});
                }
            } while (c.moveToNext());

        } else
            Log.d("rklogs", "0 rows");

        db.close();
        c.close();
    }

    @Override
    public void successExecuteObject(Object obj) {
        handleResponse(obj);
        ((SchoolsApplication) getApplicationContext()).setLastUpdated();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void failedExecute(String err) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, getResources().getString(R.string.error_connection),
                Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(Object obj){

        ANNEObject anneObject = (ANNEObject) obj;

        Log.d("rklogs", "anneObject last_date_news:_"+ anneObject.last_date_news);
        Log.d("rklogs", "anneObject last_date_newsletters:_"+ anneObject.last_date_newsletters);
        Log.d("rklogs", "anneObject last_date_events:_"+ anneObject.last_date_events);
        Log.d("rklogs", "anneObject last_date_alerts:_"+ anneObject.last_date_alerts);

        saveDates(anneObject.last_date_newsletters, anneObject.last_date_events,
                anneObject.last_date_news, anneObject.last_date_alerts);

        JSONArray alert_jsonArray = convertToJsonArray((ArrayList) anneObject.alerts);
        JSONArray news_jsonArray = convertToJsonArray((ArrayList) anneObject.news);
        JSONArray newsletters_jsonArray = convertToJsonArray((ArrayList) anneObject.newsletters);
        JSONArray events_jsonArray = convertToJsonArray((ArrayList) anneObject.events);

        fillBDbyArrays(alert_jsonArray, news_jsonArray, newsletters_jsonArray, events_jsonArray);
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
                                JSONArray newsletters_jsonArray, JSONArray events_jsonArray){

        Bundle bndl = new Bundle();
        bndl.putString("events_jsonArray", events_jsonArray.toString());
        bndl.putString("alert_jsonArray", alert_jsonArray.toString());
        bndl.putString("news_jsonArray", news_jsonArray.toString());
        bndl.putString("newsletters_jsonArray", newsletters_jsonArray.toString());

        getLoaderManager().initLoader(1, bndl, this);

        readFromDBAndMarkRed();
        checkItWasPushRequest();
    }

    private void checkItWasPushRequest(){
        if (((SchoolsApplication) getApplicationContext()).getRedirect("RedirectTo").equals("News")){
            ((SchoolsApplication) getApplicationContext()).setRedirect("RedirectTo", "");
            Intent redirect = new Intent(this, NewsMenuActivity.class);
            startActivity(redirect);
        }

        if (((SchoolsApplication) getApplicationContext()).getRedirect("RedirectTo").equals("Newsletters")) {
            ((SchoolsApplication) getApplicationContext()).setRedirect("RedirectTo", "");
            Intent redirect = new Intent(this, NewslettersMenuActivity.class);
            startActivity(redirect);
        }

        if (((SchoolsApplication) getApplicationContext()).getRedirect("RedirectTo").equals("Alerts")){
            ((SchoolsApplication) getApplicationContext()).setRedirect("RedirectTo", "");
            Intent redirect = new Intent(this, AlertsMenuActivity.class);
            startActivity(redirect);
        }

        if (((SchoolsApplication) getApplicationContext()).getRedirect("RedirectTo").equals("ParentsEnrolements")){
            ((SchoolsApplication) getApplicationContext()).setRedirect("RedirectTo", "");
            insertToDB("PARENTS_ENROLEMENTS", "NO", null, null, null, null, null, null, null);
            readFromDBAndMarkRed();
        }

        if (((SchoolsApplication) getApplicationContext()).getRedirect("RedirectTo").equals("ParentsFees")){
            ((SchoolsApplication) getApplicationContext()).setRedirect("RedirectTo", "");
            insertToDB("PARENTS_FEES", "NO", null, null, null, null, null, null, null);
            readFromDBAndMarkRed();
        }

        if (((SchoolsApplication) getApplicationContext()).getRedirect("RedirectTo").equals("ParentsPF")){
            ((SchoolsApplication) getApplicationContext()).setRedirect("RedirectTo", "");
            insertToDB("PARENTS_PFASSOCIATION", "NO", null, null, null, null, null, null, null);
            readFromDBAndMarkRed();
        }

        if (((SchoolsApplication) getApplicationContext()).getRedirect("RedirectTo").equals("ParentsCanteen")){
            ((SchoolsApplication) getApplicationContext()).setRedirect("RedirectTo", "");
            insertToDB("PARENTS_CANTEEN", "NO", null, null, null, null, null, null, null);
            readFromDBAndMarkRed();
        }

        if (((SchoolsApplication) getApplicationContext()).getRedirect("RedirectTo").equals("ParentsUniform")){
            ((SchoolsApplication) getApplicationContext()).setRedirect("RedirectTo", "");
            insertToDB("PARENTS_UNIFORM", "NO", null, null, null, null, null, null, null);
            readFromDBAndMarkRed();
        }
    }

    private void saveDates(long last_date_newsletters, long last_date_events, long last_date_news,
                           long last_date_alerts){
        prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putLong("last_date_newsletters", last_date_newsletters);
        ed.putLong("last_date_events", last_date_events);
        ed.putLong("last_date_news", last_date_news);
        ed.putLong("last_date_alerts", last_date_alerts);
        ed.commit();
    }

    private long getLast_date_newsletters(){
        prefs = getPreferences(MODE_PRIVATE);
        long last_date_newsletters = prefs.getLong("last_date_newsletters", 0);
        return last_date_newsletters;
    }

    private long getLast_date_news(){
        prefs = getPreferences(MODE_PRIVATE);
        long last_date_news = prefs.getLong("last_date_news",
                dateHelper.getCurrentDateMinusFourWeeks_Seconds());
        return last_date_news;
    }

    private long getLast_date_events(){
        prefs = getPreferences(MODE_PRIVATE);
        long last_date_events = prefs.getLong("last_date_events", 0);
        return last_date_events;
    }

    private long getLast_date_alerts(){
        prefs = getPreferences(MODE_PRIVATE);
        long last_date_alerts = prefs.getLong("last_date_alerts",
                dateHelper.getCurrentDateMinusTwoWeeks_Seconds());
        return last_date_alerts;
    }

        @Override
        public void onRefresh() {
            if (Constants.LoaderIsWorking == false){

                mSwipeRefreshLayout.setRefreshing(true);
                client = new RetrofitClient();
                client.setServerObserver(this);
                client.postANNE(
                        getUid(),
                        getLast_date_newsletters(),
                        getLast_date_events(),
                        getLast_date_news(),
                        getLast_date_alerts());
            }
        }

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        Log.d("rklogs", "onCreateLoader");
        Loader<Bitmap> loader = new LoaderInserter(this, args);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap result) {
        Log.d("rklogs", "onLoadFinished");
        mSwipeRefreshLayout.setRefreshing(false);
        readFromDBAndMarkRed();
        checkItWasPushRequest();
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        Log.d("rklogs", "onLoadReset");
    }

}

