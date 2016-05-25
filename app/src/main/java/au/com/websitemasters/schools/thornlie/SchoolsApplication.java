package au.com.websitemasters.schools.thornlie;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.UUID;

import au.com.websitemasters.schools.thornlie.utils.DateHelper;

@ReportsCrashes( // will not be used
        mailTo = "summoner-rk@mail.ru", // my email here
        mode = ReportingInteractionMode.SILENT)

public class SchoolsApplication extends Application {
    private Activity mCurrentActivity = null;
    private SharedPreferences sharedPreferences;

    private DateHelper dateHelper = new DateHelper();

    @Override
    public void onCreate() {
        super.onCreate();

        final ACRAConfiguration config = ACRA.getNewDefaultConfig(this);
        ACRA.init(this, config);

        sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(SchoolsApplication.this);
    }


    public void setSetting(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();

    }

    public boolean getSetting(String key) {
        boolean defaultValue = true;
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void setRedirect(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getRedirect(String key) {
        String defaultValue = "";
        return sharedPreferences.getString(key, defaultValue);
    }

    public void setLastUpdated(){
        DateHelper dateHelper = new DateHelper();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        long secs = dateHelper.getCurrentDate_Seconds();
        editor.putString("LastUpdate", "Last Updated: " + dateHelper.convertSecondsToDate(secs, "dd MMMM, yyyy"));
        editor.apply();
    }

    public boolean checkItFirstRun(){
        boolean answer = sharedPreferences.getBoolean("Run", true);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Run", false);
        editor.apply();
        return answer;
    }


    public String getLastUpdated() {
        DateHelper dateHelper = new DateHelper();
        long secs = dateHelper.getCurrentDate_Seconds();
        String def = "Last Updated: " + dateHelper.convertSecondsToDate(secs, "dd MMMM, yyyy");
        return sharedPreferences.getString("LastUpdate", def);
    }


    public void saveBadgesCount(int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Badges", value);
        editor.apply();
    }

    public int loadBadgesCount() {
        return sharedPreferences.getInt("Badges", 0);
    }

    public void saveDates(long last_date_newsletters, long last_date_events, long last_date_news,
                           long last_date_alerts, long last_date_pinfo){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putLong("last_date_newsletters", last_date_newsletters);
        ed.putLong("last_date_events", last_date_events);
        ed.putLong("last_date_news", last_date_news);
        ed.putLong("last_date_alerts", last_date_alerts);
        //ed.putLong("last_date_pinfo", last_date_pinfo);
        ed.putLong("last_date_pinfo", 0);
        ed.commit();
    }

    public long getLast_date_newsletters(){
        long last_date_newsletters = sharedPreferences.getLong("last_date_newsletters", 0);
        return last_date_newsletters;
    }

    public long getLast_date_news(){
        long last_date_news = sharedPreferences.getLong("last_date_news",
                dateHelper.getCurrentDateMinusFourWeeks_Seconds());
        return last_date_news;
    }

    public long getLast_date_events(){
        long last_date_events = sharedPreferences.getLong("last_date_events", 0);
        return last_date_events;
    }

    public long getLast_date_alerts(){
        long last_date_alerts = sharedPreferences.getLong("last_date_alerts",
                dateHelper.getCurrentDateMinusTwoWeeks_Seconds());
        return last_date_alerts;
    }

    public long getLast_date_pinfo(){
        long last_date_pinfo = sharedPreferences.getLong("last_date_pinfo", 0);
        return last_date_pinfo;
    }

    public String getUid(){
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

    public void saveListParents(JSONArray list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("par_inf", list.toString());
        editor.apply();
    }

    public JSONArray loadListParents()  {
        JSONArray list = null;

        String str = sharedPreferences.getString("par_inf", null);

            try {
                list = new JSONArray(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        return list;
    }


}
