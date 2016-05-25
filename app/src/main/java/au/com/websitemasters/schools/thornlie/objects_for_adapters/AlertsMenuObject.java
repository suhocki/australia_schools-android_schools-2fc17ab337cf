package au.com.websitemasters.schools.thornlie.objects_for_adapters;

/**
 * Created by Ruslan on 30.03.2016.
 */
public class AlertsMenuObject {

    private String title;

    private String full_title;

    private String was_readed;

    private String date;

    private String serverId;

    public AlertsMenuObject(String title, String full_title, String was_readed, String date, String serverId){
        this.title = title;
        this.full_title = full_title;
        this.was_readed = was_readed;
        this.date = date;
        this.serverId = serverId;
    }

    public String getTitle(){
        return title;
    }

    public String getFullTitle(){
        return full_title;
    }

    public String getWas_readed(){
        return was_readed;
    }

    public String getDate(){
        return date;
    }

    public String getServerId(){
        return serverId;
    }
}
