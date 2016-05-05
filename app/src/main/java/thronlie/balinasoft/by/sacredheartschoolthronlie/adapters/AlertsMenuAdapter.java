package thronlie.balinasoft.by.sacredheartschoolthronlie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import thronlie.balinasoft.by.sacredheartschoolthronlie.R;
import thronlie.balinasoft.by.sacredheartschoolthronlie.objects_for_adapters.AlertsMenuObject;
import thronlie.balinasoft.by.sacredheartschoolthronlie.utils.DateHelper;


public class AlertsMenuAdapter extends BaseAdapter {

    Context ctx;

    private ArrayList<AlertsMenuObject> list;

    private LayoutInflater layoutInflater;

    public AlertsMenuAdapter(Context ctx, ArrayList<AlertsMenuObject> list) {
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
            view = layoutInflater.inflate(R.layout.item_alertsmenu_adapter, parent, false);
        }

        AlertsMenuObject amd = getAlertMenuObj(position);

        ((ImageView) view.findViewById(R.id.ivMessageStatus))
                .setBackgroundResource(definiteMessageDrawable(amd.getWas_readed()));

        ((TextView) view.findViewById(R.id.tvTitle)).setText(amd.getTitle());

        DateHelper dateHelper = new DateHelper();
        ((TextView) view.findViewById(R.id.tvDate))
                .setText(dateHelper.convertSecondsToDate(amd.getDate(), "dd.MM.yyyy"));

        ((TextView) view.findViewById(R.id.tvFullText)).setText(cutTextIfLong(amd.getFullTitle()));

        return view;
    }

    AlertsMenuObject getAlertMenuObj(int position){
        return ((AlertsMenuObject)getItem(position));
    }

    private int definiteMessageDrawable(String value){
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
