package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class Adapter extends BaseAdapter implements Filterable {
    private ArrayList<Contact> data; //nguon du lieu
    private ArrayList<Contact> databackup; // sao luu du lieu
    private Activity context;//doi tuong
    private LayoutInflater inflater; //bo phan tich layout

    public Adapter() {
    }

    public Adapter(ArrayList<Contact> data, Activity activity) {
        this.data = data;
        this.context = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Contact> getData() {
        return data;
    }

    public void setData(ArrayList<Contact> data) {
        this.data = data;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public void setInflater(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return data.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null)
            v = inflater.inflate(R.layout.contactitem, null);
        ImageView imgprofile = v.findViewById(R.id.imageView);
        TextView tvname = v.findViewById(R.id.tvName);
        tvname.setText(data.get(i).getName());
        TextView tvphone = v.findViewById(R.id.tvPhone);
        tvphone.setText(data.get(i).getPhone());
        return v;
    }

    @Override
    public Filter getFilter() {
        Filter f = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults fr = new FilterResults();
                //Backup du lieu, luu vao data backup
                if (databackup == null) {
                    databackup = new ArrayList<>(data);
                    //Neu chuoi de filter la trong thi khoi phuc du lieu
                }
                if (charSequence == null || charSequence.length() == 0) {
                    fr.count = databackup.size();
                    fr.values = databackup;
                } else {
                    ArrayList<Contact> newdata = new ArrayList<>();
                    for (Contact c : databackup) {
                        if (c.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            newdata.add(c);
                        }
                        fr.count = newdata.size();
                        fr.values = newdata;
                    }
                }
                return fr;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                data = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return f;
    }
}
