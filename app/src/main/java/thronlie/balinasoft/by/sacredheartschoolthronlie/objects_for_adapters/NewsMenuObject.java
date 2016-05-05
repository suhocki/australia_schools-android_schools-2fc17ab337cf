package thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_adapters;

/**
 * Created by Ruslan on 30.03.2016.
 */
public class NewsMenuObject {

    private String title;

    private String full_title;

    private String was_readed;

    private String date;

    private String url_full_text;

    private String photo_link;

    private String serverId;

    public NewsMenuObject(String title, String photo_link, String full_title, String was_readed,
                          String date, String url_full_text, String serverId){
        this.title = title;
        this.photo_link = photo_link;
        this.full_title = full_title;
        this.was_readed = was_readed;
        this.date = date;
        this.url_full_text = url_full_text;
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

    public String getPhotolink(){
        return photo_link;
    }

    public String getUrlOfFullText(){
        return url_full_text;
    }

    public String getServerId(){
        return serverId;
    }
}
