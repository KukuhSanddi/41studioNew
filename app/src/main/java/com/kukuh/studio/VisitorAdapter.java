package com.kukuh.studio;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class VisitorAdapter extends ArrayAdapter<Visitor> {
    private Context context;
    private int resource;
    private ArrayList<Visitor> vis;
    private ArrayList<Visitor> visAll;
    private ArrayList<Visitor> suggestions;

    public VisitorAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Visitor> vis) {
        super(context,resource,vis);
        this.vis = vis;
        this.context = context;
        this.resource = resource;
        this.visAll = (ArrayList<Visitor>) vis.clone();
        this.suggestions = new ArrayList<Visitor>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        try {
            if (convertView == null){
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                view = inflater.inflate(resource,parent,false);
            }
            Visitor visitor = vis.get(position);
            TextView email = view.findViewById(R.id.emailVis);
            email.setText(visitor.getEmail());
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }


    @Nullable
    @Override
    public Visitor getItem(int pos){
        return vis.get(pos);
    }

    @Override
    public Filter getFilter() {
        return emailFilter;
    }

    Filter emailFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Visitor)(resultValue)).getEmail();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Visitor vis : visAll) {
                    if(vis.getNama().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(vis);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Visitor> filteredList = (ArrayList<Visitor>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Visitor v : filteredList) {
                    add(v);
                }
                notifyDataSetChanged();
            }
        }
    };


}
