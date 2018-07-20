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

public class EmployeeAdapter extends ArrayAdapter<Employee>{
    private Context context;
    private int resource;
    private ArrayList<Employee> emp;
    private ArrayList<Employee> empAll;
    private ArrayList<Employee> suggestions;

    public EmployeeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Employee> emp) {
        super(context,resource,emp);
        this.emp = emp;
        this.context = context;
        this.resource = resource;
        this.empAll = (ArrayList<Employee>) emp.clone();
        this.suggestions = new ArrayList<Employee>();
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
            Employee employee = emp.get(position);
            TextView name = (TextView) view.findViewById(R.id.namaEmp);
            name .setText(employee.getNama());
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }


    @Nullable
    @Override
    public Employee getItem(int pos){
        return emp.get(pos);
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Employee)(resultValue)).getNama();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Employee emp : empAll) {
                    if(emp.getNama().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(emp);
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
            ArrayList<Employee> filteredList = (ArrayList<Employee>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Employee e : filteredList) {
                    add(e);
                }
                notifyDataSetChanged();
            }
        }
    };


}
//    <!--Code By M. Adli Rachman-->