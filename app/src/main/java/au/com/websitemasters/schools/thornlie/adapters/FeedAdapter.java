package au.com.websitemasters.schools.thornlie.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.AlertsMenuObject;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.EventsMenuObject;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.NewsMenuObject;
import au.com.websitemasters.schools.thornlie.utils.DateHelper;


public class FeedAdapter extends BaseAdapter {

    Context ctx;

    private ArrayList list;

    private LayoutInflater layoutInflater;

    public FeedAdapter(Context ctx, ArrayList list) {
        this. ctx = ctx;
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
        DateHelper dateHelper = new DateHelper();

        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_feed_adapter, parent, false);
        }

         Object obj = getFeedObj(position);

        if (obj instanceof AlertsMenuObject){

            AlertsMenuObject amd = (AlertsMenuObject) obj;

            ((LinearLayout) view.findViewById(R.id.linColor))
                    .setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_dark_blue));
            ((ImageView) view.findViewById(R.id.ivMessageStatus))
                    .setBackgroundResource(definiteMessageDrawable(amd.getWas_readed()));
            ((TextView) view.findViewById(R.id.tvTitle)).setText(amd.getTitle());
            ((TextView) view.findViewById(R.id.tvDate))
                    .setText(dateHelper.convertSecondsToDate(amd.getDate(), "dd.MM.yyyy"));
            ((TextView) view.findViewById(R.id.tvFullText)).setText(cutTextIfLong(amd.getFullTitle()));
            ((TextView) view.findViewById(R.id.tvFullText)).setVisibility(View.VISIBLE);
        }

        if (obj instanceof NewsMenuObject){

            NewsMenuObject nmd = (NewsMenuObject) obj;

            ((LinearLayout) view.findViewById(R.id.linColor))
                    .setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_green));
            ((ImageView) view.findViewById(R.id.ivMessageStatus))
                    .setBackgroundResource(definiteMessageDrawable(nmd.getWas_readed()));
            ((TextView) view.findViewById(R.id.tvTitle)).setText(nmd.getTitle());
            ((TextView) view.findViewById(R.id.tvDate))
                    .setText(dateHelper.convertSecondsToDate(nmd.getDate(), "dd.MM.yyyy"));
            ((TextView) view.findViewById(R.id.tvFullText)).setText(cutTextIfLong(nmd.getUrlOfFullText()));
            ((TextView) view.findViewById(R.id.tvFullText)).setVisibility(View.VISIBLE);
        }

        if (obj instanceof EventsMenuObject){

            EventsMenuObject emd = (EventsMenuObject) obj;
            ((LinearLayout) view.findViewById(R.id.linColor))
                    .setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_blue));
            ((ImageView) view.findViewById(R.id.ivMessageStatus))
                    .setBackgroundResource(definiteMessageDrawable(emd.getWas_readed()));
            ((TextView) view.findViewById(R.id.tvTitle)).setText(emd.getTitle());
            ((TextView) view.findViewById(R.id.tvDate))
                    .setText(dateHelper.convertSecondsToDate(emd.getDate(), "dd.MM.yyyy"));
            ((TextView) view.findViewById(R.id.tvFullText)).setVisibility(View.GONE);
        }

        return view;
    }

    Object getFeedObj(int position){
        return ((Object)getItem(position));
    }


    int definiteMessageDrawable(String value){
        int drawable = R.drawable.message_inactive;

        if (value.equals("NO")){
            drawable = R.drawable.message_active;
        }
        return drawable;
    }

    private String cutTextIfLong(String text){
        String result = text;
        if (text.length() > 105){
            result = text.substring(0, 105) + "...";
        }
        return result;
    }

}
