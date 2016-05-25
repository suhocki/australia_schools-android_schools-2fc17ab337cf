package au.com.websitemasters.schools.thornlie.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import au.com.websitemasters.schools.thornlie.R;
import au.com.websitemasters.schools.thornlie.activities.ParentsActivity;
import au.com.websitemasters.schools.thornlie.objects_for_adapters.ParentsAdapterObject;


/*
public class ParentsAdapter extends BaseAdapter {

    Context ctx;

    private ArrayList<ParentsAdapterObject> list;

    private LayoutInflater layoutInflater;

    public ParentsAdapter(Context ctx, ArrayList<ParentsAdapterObject> list) {
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
            view = layoutInflater.inflate(R.layout.item_parents_adapter, parent, false);
        }

        ParentsAdapterObject pd =  getParentsObj(position);

        ((TextView) view.findViewById(R.id.tvTitle)).setText(pd.getTitle());

        return view;
    }

    ParentsAdapterObject getParentsObj(int position){
        return ((ParentsAdapterObject)getItem(position));
    }
}*/


public class ParentsAdapter extends RecyclerView.Adapter<ParentsAdapter.PersonViewHolder>{

    public static Context ctx;
    ParentsAdapterObject pd;

    ArrayList<ParentsAdapterObject> listParents;
    public ParentsAdapter(ArrayList<ParentsAdapterObject> listParents, Context ctx){
        this.listParents = listParents;
        this.ctx= ctx;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_parents_adapter,
                viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {

        pd = listParents.get(i);

        personViewHolder.tvTitle.setText(pd.getTitle());
        personViewHolder.pdo = pd;

    }

    @Override
    public int getItemCount() {
        return listParents.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        ParentsAdapterObject pdo;

        PersonViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);

            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("rklogs", tvTitle.getText().toString());

                    Intent intent = new Intent(ctx, ParentsActivity.class);
                    intent.putExtra("title", pdo.getTitle());
                    intent.putExtra("date", pdo.getDate());
                    intent.putExtra("id", pdo.getId());
                    intent.putExtra("text", pdo.getText());
                    ctx.startActivity(intent);
                    Log.d("rklogs", pdo.getTitle());

                }
            });

        }
    }
}
