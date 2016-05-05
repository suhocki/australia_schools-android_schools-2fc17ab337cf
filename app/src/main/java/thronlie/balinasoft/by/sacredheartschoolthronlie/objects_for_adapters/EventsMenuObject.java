package thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_adapters;

/**
 * Created by Ruslan on 30.03.2016.
 */
public class EventsMenuObject {

    private String title;

    private String full_title;

    private String was_readed;

    private String date;

    private String serverId;

    private String url_full_text;

    public EventsMenuObject(String title, String full_title, String url_full_text, String was_readed,
                            String date, String serverId){
        this.title = title;
        this.full_title = full_title;
        this.was_readed = was_readed;
        this.date = date;
        this.url_full_text = url_full_text;
        this.serverId = serverId;
    }

    public String getTitle(){
        return title;
    }

    public String getDateUp(){
        return full_title;
    }

    public String getDateEnd(){
        return url_full_text;
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
