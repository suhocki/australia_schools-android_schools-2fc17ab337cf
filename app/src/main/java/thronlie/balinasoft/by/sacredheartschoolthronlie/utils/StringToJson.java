package thronlie.balinasoft.by.sacredheartschoolthronlie.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ruslan on 05.04.2016.
 */
public class StringToJson {

    public String parseIdFromResponse(String res) {

        JSONObject res_json = null;
        String id = "";
        try {
            res_json = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            id = res_json.get("device_id").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

}
