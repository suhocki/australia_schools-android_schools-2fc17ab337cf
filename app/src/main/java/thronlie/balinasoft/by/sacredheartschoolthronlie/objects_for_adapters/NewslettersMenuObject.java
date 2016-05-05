package thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_adapters;

/**
 * Created by Ruslan on 30.03.2016.
 */
public class NewslettersMenuObject {

    private String title;

    private String full_title;

    private String was_readed;

    private String date;

    private String pdf_link;

    private String serverId;

    public NewslettersMenuObject(String title, String full_title, String was_readed, String date,
                                 String pdf_link, String serverId){
        this.title = title;
        this.full_title = full_title;
        this.was_readed = was_readed;
        this.date = date;
        this.pdf_link = pdf_link;
        this.serverId = serverId;
    }

    public String getPdf_link(){
        return pdf_link;
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
