package thronlie.balinasoft.by.sacredheartschoolthronlie;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.DateHelper;

@ReportsCrashes( // will not be used
        mailTo = "summoner-rk@mail.ru", // my email here
        mode = ReportingInteractionMode.SILENT)

public class SchoolsApplication extends Application {

    private SharedPreferences sharedPreferences;

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

}
