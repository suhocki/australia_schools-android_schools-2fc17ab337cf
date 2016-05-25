package au.com.websitemasters.schools.thornlie.activities;

import android.app.LoaderManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.SchoolsApplication;
import au.com.websitemasters.schools.thornlie.connection_retrofit.RetrofitClient;
import au.com.websitemasters.schools.thornlie.constants.Constants;
import au.com.websitemasters.schools.thornlie.loader.LoaderInserter;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.ParentsAdapterObject;
import au.com.websitemasters.schools.thornlie.objects_for_retrofit.ANNEObject;
import au.com.websitemasters.schools.thornlie.push.MyGcmListenerService;
import au.com.websitemasters.schools.thornlie.push.RegistrationIntentService;
import au.com.websitemasters.schools.thornlie.utils.SQLiteHelper;
import au.com.websitemasters.schools.thornlie.utils.ServerObserverRetrofit;


public class MainActivity extends AppCompatActivity implements ServerObserverRetrofit<Object, String>,
            LoaderManager.LoaderCallbacks<Bitmap> {

    private LinearLayout linParentsMenu, linParentsInfo, linAlerts, linEvents, linNewsletters,
        linNews, linContactUs, linPar1, linPar2, linPar3, linPar4, linPar5, linPar6, linPar7,
        linPar8, linPar9, linPar10;

    private Button btnAlertsMessages, btnEventsMessages, btnNewsLettersMessages, btnNewsMessages,
        btnParentsInfoMessages, btnUniformShopMessages, btnCanteenMessages, btnPFAssociationMessages,
        btnFeesMessages, btnEnrolementsMessages;

    private ImageView ivParentsInfoArrow;

    private RecyclerView lvParents;

    private TextView tvPar1, tvPar2, tvPar3, tvPar4, tvPar5, tvPar6, tvPar7, tvPar8, tvPar9, tvPar10;

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

    ArrayList listParents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        client = new RetrofitClient();
        listParents = new ArrayList();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.home));
        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        btnAlertsMessages = (Button) findViewById(R.id.btnAlertsMessages);
        btnEventsMessages = (Button) findViewById(R.id.btnEventsMessages);
        btnNewsLettersMessages = (Button) findViewById(R.id.btnNewsLettersMessages);
        btnNewsMessages = (Button) findViewById(R.id.btnNewsMessages);
        btnParentsInfoMessages = (Button) findViewById(R.id.btnParentsInfoMessages);

        tvPar1 = (TextView)findViewById(R.id.tvPar1);
        tvPar2 = (TextView)findViewById(R.id.tvPar2);
        tvPar3 = (TextView)findViewById(R.id.tvPar3);
        tvPar4 = (TextView)findViewById(R.id.tvPar4);
        tvPar5 = (TextView)findViewById(R.id.tvPar5);
        tvPar6 = (TextView)findViewById(R.id.tvPar6);
        tvPar7 = (TextView)findViewById(R.id.tvPar7);
        tvPar8 = (TextView)findViewById(R.id.tvPar8);
        tvPar9 = (TextView)findViewById(R.id.tvPar9);
        tvPar10 = (TextView)findViewById(R.id.tvPar10);

        ImageView btnAlerts = (ImageView) findViewById(R.id.btnAlerts);
        btnAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AlertsMenuActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnFeed = (ImageView) findViewById(R.id.btnFeed);
        btnFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FeedActivity.class);
                startActivity(intent);
            }
        });

        ImageView btnCalendar = (ImageView) findViewById(R.id.btnCalendar);
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        ivParentsInfoArrow = (ImageView) findViewById(R.id.ivParentsInfoArrow);
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
                    fillListParents();
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
        linParentsInfo.setClickable(false);

        ivParentsInfoArrow = (ImageView) findViewById(R.id.ivParentsInfoArrow);

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

        linPar1 = (LinearLayout) findViewById(R.id.linPar1);
        linPar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(0);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });
        linPar2 = (LinearLayout) findViewById(R.id.linPar2);
        linPar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(1);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });
        linPar3 = (LinearLayout) findViewById(R.id.linPar3);
        linPar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(2);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });
        linPar4 = (LinearLayout) findViewById(R.id.linPar4);
        linPar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(3);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });
        linPar5 = (LinearLayout) findViewById(R.id.linPar5);
        linPar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(4);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });
        linPar6 = (LinearLayout) findViewById(R.id.linPar6);
        linPar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(5);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });
        linPar7 = (LinearLayout) findViewById(R.id.linPar7);
        linPar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(6);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });

        linPar8 = (LinearLayout) findViewById(R.id.linPar8);
        linPar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(7);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });

        linPar9 = (LinearLayout) findViewById(R.id.linPar9);
        linPar9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(8);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });

        linPar10 = (LinearLayout) findViewById(R.id.linPar10);
        linPar10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParentsAdapterObject pdo = (ParentsAdapterObject) listParents.get(9);
                Intent intent = new Intent(MainActivity.this, ParentsActivity.class);
                intent.putExtra("title", pdo.getTitle());
                intent.putExtra("date", pdo.getDate());
                intent.putExtra("id", pdo.getId());
                intent.putExtra("text", pdo.getText());
                startActivity(intent);
            }
        });

        try {

            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("name not found", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        }

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
                .setSmallIcon(R.drawable.day)
                .setContentTitle("Test")
                .setContentText(message);
        notificationManager.notify(1, mBuilder.build());
    }
}

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("rklogs", "onResume");

        //clear notifications
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(0);

        //hide Parents Info
        linParentsMenu.setVisibility(View.GONE);
        parentsMenuVisible = false;
        ivParentsInfoArrow.setImageResource(R.drawable.arrow_right);

        registerReceiver();
        registerReceiver(broadcastReceiver, new IntentFilter(MyGcmListenerService.BROADCAST_ACTION));

        try{
            readFromDBAndMarkRed();
        } catch (NullPointerException e){
            e.printStackTrace();
        } catch (SQLiteDatabaseLockedException e){
            e.printStackTrace();
        }

        if (Constants.LoaderIsWorking == true){
            //do nothing, loader is busy
            Log.d("rklogs", "Constants.LoaderIsWorking == true");
        } else {
            Log.d("rklogs", "Constants.LoaderIsWorking == false");
            makeRequest();
        }
    }

    public void makeRequest(){
        client = new RetrofitClient();
        client.setServerObserverRetrofit(this);
        client.postANNE(
                ((SchoolsApplication) getApplicationContext()).getUid(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_newsletters(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_events(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_news(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_alerts(),
                ((SchoolsApplication) getApplicationContext()).getLast_date_pinfo());
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("rklogs", "broadcastReceiver сработало в main");
            if (Constants.LoaderIsWorking == true){
                //do nothing, loader is busy
                Log.d("rklogs", "Constants.LoaderIsWorking == true");
            } else {
                Log.d("rklogs", "Constants.LoaderIsWorking == false");
                makeRequest();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        dbHelper.close();
        db.close();
        unregisterReceiver(broadcastReceiver);
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

            do {
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
        linParentsInfo.setClickable(true);
    }

    @Override
    public void failedExecute(String err) {
        Toast.makeText(this, getResources().getString(R.string.error_connection),
                Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(Object obj){

        ANNEObject anneObject = (ANNEObject) obj;

        Log.d("rklogs", "anneObject last_date_news:_"+ anneObject.last_date_news);
        Log.d("rklogs", "anneObject last_date_newsletters:_"+ anneObject.last_date_newsletters);
        Log.d("rklogs", "anneObject last_date_events:_"+ anneObject.last_date_events);
        Log.d("rklogs", "anneObject last_date_alerts:_"+ anneObject.last_date_alerts);


        ((SchoolsApplication) getApplicationContext()).saveDates(
                anneObject.last_date_newsletters,
                anneObject.last_date_events,
                anneObject.last_date_news,
                anneObject.last_date_alerts,
                anneObject.last_date_pinfo);

        JSONArray alert_jsonArray = convertToJsonArray((ArrayList) anneObject.alerts);
        JSONArray news_jsonArray = convertToJsonArray((ArrayList) anneObject.news);
        JSONArray newsletters_jsonArray = convertToJsonArray((ArrayList) anneObject.newsletters);
        JSONArray events_jsonArray = convertToJsonArray((ArrayList) anneObject.events);
        JSONArray pinfo_jsonArray = convertToJsonArray((ArrayList) anneObject.parents_info);

        fillBDbyArrays(alert_jsonArray, news_jsonArray, newsletters_jsonArray, events_jsonArray,
                pinfo_jsonArray);
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
                                JSONArray newsletters_jsonArray, JSONArray events_jsonArray,
                                JSONArray pinfo_jsonArray){

        Bundle bndl = new Bundle();
        bndl.putString("events_jsonArray", events_jsonArray.toString());
        bndl.putString("alert_jsonArray", alert_jsonArray.toString());
        bndl.putString("news_jsonArray", news_jsonArray.toString());
        bndl.putString("newsletters_jsonArray", newsletters_jsonArray.toString());
        bndl.putString("pinfo_jsonArray", pinfo_jsonArray.toString());

        getLoaderManager().initLoader(1, bndl, this);
    }


    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        Log.d("rklogs", "onCreateLoader");
        Loader<Bitmap> loader = new LoaderInserter(this, args);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap result) {
        Log.d("rklogs", "onLoadFinished in Main");
        readFromDBAndMarkRed();
        fillListParents();
    }

    public void fillListParents(){

        JSONArray pinfo_jsonArray = ((SchoolsApplication) getApplicationContext()).loadListParents();

        int j = 0;
        ParentsAdapterObject parObj;

           for (j = 0; j < pinfo_jsonArray.length(); j++){
                try {
                    JSONObject pinfo_json = pinfo_jsonArray.getJSONObject(j);

                    parObj = new ParentsAdapterObject(
                            pinfo_json.getString("pinfo_id"),
                            pinfo_json.getString("pinfo_title"),
                            pinfo_json.getString("pinfo_date"),
                            pinfo_json.getString("pinfo_text"));

                    listParents.add(parObj);

                    if (j == 0) {
                        setAvailablePar(tvPar1, linPar1, parObj);
                    }

                    if (j == 1) {
                        setAvailablePar(tvPar2, linPar2, parObj);
                    }

                    if (j == 2) {
                        setAvailablePar(tvPar3, linPar3, parObj);
                    }

                    if (j == 3) {
                        setAvailablePar(tvPar4, linPar4, parObj);
                    }

                    if (j == 4) {
                        setAvailablePar(tvPar5, linPar5, parObj);
                    }

                    if (j == 5) {
                        setAvailablePar(tvPar6, linPar6, parObj);
                    }

                    if (j == 6) {
                        setAvailablePar(tvPar7, linPar7, parObj);
                    }

                    if (j == 7) {
                        setAvailablePar(tvPar8, linPar8, parObj);
                    }

                    if (j == 8) {
                        setAvailablePar(tvPar9, linPar9, parObj);
                    }

                    if (j == 9) {
                        setAvailablePar(tvPar10, linPar10, parObj);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    public void setAvailablePar(TextView tv, LinearLayout lin, ParentsAdapterObject parObj){
        tv.setText(parObj.getTitle());
        lin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        Log.d("rklogs", "onLoadReset");
    }


}





