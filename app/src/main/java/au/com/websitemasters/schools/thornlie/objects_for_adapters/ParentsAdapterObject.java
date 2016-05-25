package au.com.websitemasters.schools.thornlie.objects_for_adapters;

/**
 * Created by Ruslan on 30.03.2016.
 */
public class ParentsAdapterObject {

    public String title;

    public String text;

    public String date;

    public String id;

    public ParentsAdapterObject(String id, String title, String date, String text){
        this.title = title;
        this.date = date;
        this.text = text;
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public String getText(){
        return text;
    }

    public String getId(){
        return id;
    }

    public String getDate(){
        return date;
    }

}
