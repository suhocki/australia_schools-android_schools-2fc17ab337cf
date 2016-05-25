package au.com.websitemasters.schools.thornlie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.NewslettersMenuObject;



public class NewslettersMenuAdapter extends BaseAdapter {

    Context ctx;

    private ArrayList<NewslettersMenuObject> list;

    private LayoutInflater layoutInflater;

    public NewslettersMenuAdapter(Context ctx, ArrayList<NewslettersMenuObject> list) {
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
            view = layoutInflater.inflate(R.layout.item_newslettersmenu_adapter, parent, false);
        }

        NewslettersMenuObject emd = getNewslettersMenuObj(position);

        ((ImageView) view.findViewById(R.id.ivMessageStatus))
                .setBackgroundResource(definiteMessageDrawable(emd.getWas_readed()));

        //((ImageView) view.findViewById(R.id.ivMessageStatus))
        //        .setBackgroundResource(definiteMessageDrawable(amd.getWas_readed()));

        ((TextView) view.findViewById(R.id.tvTitle)).setText(emd.getTitle());

        //DateStringGetter dateGetter = new DateStringGetter();
        //((TextView) view.findViewById(R.id.tvDay)).setText(dateGetter.getDay(emd.getDate()));

        //((TextView) view.findViewById(R.id.tvFullText)).setText(amd.getFullTitle());

        LinearLayout linMessageStatus = (LinearLayout)view.findViewById(R.id.linMessageStatus);
        if (emd.getWas_readed().equals("NO")){
            linMessageStatus.setVisibility(View.VISIBLE);
        } else {
            linMessageStatus.setVisibility(View.GONE);
        }

        return view;
    }

    NewslettersMenuObject getNewslettersMenuObj(int position){
        return ((NewslettersMenuObject)getItem(position));
    }

    int definiteMessageDrawable(String value){
        int drawable = R.drawable.message_inactive;

        if (value.equals("NO")){
            drawable = R.drawable.message_active;
        }
        return drawable;
    }

}
