package thronlie.balinasoft.by.sacredheartschoolthronlie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import thronlie.balinasoft.by.sacredheartschoolthronlie.R;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_adapters.EventsMenuHeaderObject;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_adapters.EventsMenuObject;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.DateHelper;


public class EventsMenuAdapter extends BaseAdapter {

    Context ctx;

    private ArrayList list;

    private LayoutInflater layoutInflater;

    public EventsMenuAdapter(Context ctx, ArrayList<EventsMenuObject> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getViewTypeCount() {
        return 2; // The number of distinct view types the getView() will return.
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof EventsMenuObject){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent) {
        Object item = getItem(position);
        View view = currentView;
        DateHelper dateHelper = new DateHelper();

        if (item instanceof EventsMenuObject) {

            if (view == null) {

                view = layoutInflater.inflate(R.layout.item_eventsmenu_adapter, parent, false);
           }
            EventsMenuObject emd = getEventsMenuObj(position);
            ((TextView) view.findViewById(R.id.tvTitle)).setText(emd.getTitle());
            ((TextView) view.findViewById(R.id.tvDay))
                    .setText(dateHelper.convertSecondsToDate(emd.getDate(), "dd"));

        } else if (item instanceof EventsMenuHeaderObject) {

            if (view == null) {
                view = layoutInflater.inflate(R.layout.item_eventsmenuheader_adapter, parent, false);

            }
            EventsMenuHeaderObject emdh = getEventsMenuHeaderObj(position);
            ((TextView) view.findViewById(R.id.tvMonthTitle))
                    .setText(dateHelper.convertSecondsToDate(emdh.getDate(), ("MMMM yyyy")));
        }
        return view;
    }

    EventsMenuObject getEventsMenuObj(int position){
        return ((EventsMenuObject)getItem(position));
    }

    EventsMenuHeaderObject getEventsMenuHeaderObj(int position){
        return ((EventsMenuHeaderObject)getItem(position));
    }
}
