package thronlie.balinasoft.by.sacredheartschoolthronlie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import thronlie.balinasoft.by.sacredheartschoolthronlie.R;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_adapters.CalendarObject;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.DateHelper;


public class CalendarAdapter extends BaseAdapter {

    Context ctx;

    private ArrayList<CalendarObject> list;

    private LayoutInflater layoutInflater;

    public CalendarAdapter(Context ctx, ArrayList<CalendarObject> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View view = currentView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_calendar_adapter, parent, false);
        }

        CalendarObject emd = getCalendarObj(position);

        //((ImageView) view.findViewById(R.id.ivMessageStatus))
        //        .setBackgroundResource(definiteMessageDrawable(amd.getWas_readed()));

        ((TextView) view.findViewById(R.id.tvTitle)).setText(emd.getTitle());

        DateHelper dateHelper = new DateHelper();
        ((TextView) view.findViewById(R.id.tvDate)).setText(dateHelper.convertSecondsToDate(emd.getDate(), "HH.mm"));

        //((TextView) view.findViewById(R.id.tvFullText)).setText(amd.getFullTitle());

        return view;
    }

    CalendarObject getCalendarObj(int position){
        return ((CalendarObject)getItem(position));
    }



}
