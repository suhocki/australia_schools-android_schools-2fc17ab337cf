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
import au.com.websitemasters.schools.thornlie.objects_for_adapters.NewsMenuObject;
import au.com.websitemasters.schools.thornlie.utils.DateHelper;



public class NewsMenuAdapter extends BaseAdapter {

    Context ctx;

    LinearLayout linPhoto;

    private ArrayList<NewsMenuObject> list;

    private LayoutInflater layoutInflater;

    public NewsMenuAdapter(Context ctx, ArrayList<NewsMenuObject> list) {
        this.list = list;
        this.ctx = ctx;
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
            view = layoutInflater.inflate(R.layout.item_newsmenu_adapter, parent, false);
        }

        NewsMenuObject nmd = getNewsMenuObj(position);

        ((TextView) view.findViewById(R.id.tvTitle)).setText(nmd.getTitle());

        DateHelper dateHelper = new DateHelper();
        ((TextView) view.findViewById(R.id.tvDate))
                .setText(dateHelper.convertSecondsToDate(nmd.getDate(), "dd.MM.yyyy HH.mm"));

        ((TextView) view.findViewById(R.id.tvFullText)).setText(cutTextIfLong(nmd.getUrlOfFullText()));

        linPhoto = (LinearLayout) view.findViewById(R.id.linPhoto);

        ImageView ivPhoto = ((ImageView) view.findViewById(R.id.ivPhoto));
        //setPhotoOrHide(ivPhoto, nmd);

        ImageView ivMess = (ImageView) view.findViewById(R.id.ivMessageStatus);
        ivMess.setBackgroundResource(definiteMessageDrawable(nmd.getWas_readed()));

        LinearLayout linMessageStatus = (LinearLayout)view.findViewById(R.id.linMessageStatus);

        if (nmd.getWas_readed().equals("NO")){
            linMessageStatus.setVisibility(View.VISIBLE);
        } else {
            linMessageStatus.setVisibility(View.GONE);
        }

        return view;
    }

    NewsMenuObject getNewsMenuObj(int position){
        return ((NewsMenuObject)getItem(position));
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
