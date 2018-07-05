package com.kukuh.studio;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {

//    int groupid;
//    String[] item_list;
//    ArrayList<String> desc;
//    Context context;


//    public ListViewAdapter(Context context, int vg, int id, String[] item_list){
//        super(context, vg, id, item_list);
//        this.context = context;
//        groupid = vg;
//        this.item_list = item_list;
//    }
//    static class ViewHolder{
//        public TextView textView;
//        public Button button;
//    }
//
//
//    /**
//     * make list view text and button
//     * @param position
//     * @param convertView
//     * @param parent
//     * @return
//     */
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        View rowView = convertView;
//        if (rowView == null){
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            rowView = inflater.inflate(groupid, parent, false);
//            ViewHolder viewHolder = new ViewHolder();
//            viewHolder.textView = rowView.findViewById(R.id.label);
//            viewHolder.button =  rowView.findViewById(R.id.info_button);
//            rowView.setTag(viewHolder);
//        }
//        ViewHolder holder = (ViewHolder)rowView.getTag();
//        holder.textView.setText(item_list[position]); // Name Employee
//        holder.button.setText("INFO");
//        return rowView;
//
//    }
//

    private final Activity context;
    private String[] employee;

    public ListViewAdapter(Activity context, String[] employee){
        super(context, R.layout.activity_list_view, employee);
        this.context = context;
        this.employee = employee;

    }

    public View getView(int position,  View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_list_view, null, true);
        TextView txtTittle = rowView.findViewById(R.id.label);

        ImageView imgView = rowView.findViewById(R.id.img);
        txtTittle.setText(employee[position]);


        return rowView;

    }

//    listNama = dbase.getNamaEmployee();
//        listArr = new String[listNama.size()];
//        for (int i=0; i<listNama.size();i++){
//            listArr[i]=listNama.get(i).getNama();
//            Log.e("Main",listNama.get(i).getNama());
//        }

}