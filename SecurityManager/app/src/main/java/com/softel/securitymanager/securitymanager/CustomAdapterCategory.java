package com.softel.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterCategory extends ArrayAdapter<categorylist> {
    ArrayList<categorylist> categorylists;
    Context context;
    int resource;
    int textview;
    public CustomAdapterCategory(Context context, int resource,int textviewid,ArrayList<categorylist> categorylists) {
        super(context, resource,textviewid, categorylists);
        this.context=context;
        this.resource=resource;
        this.textview=textviewid;
        this.categorylists=categorylists;
    }
    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt)
    {
        return getView(position, cnvtView, prnt);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.categorylist, null, true);
        }

         categorylist CatList = getItem(position);
         TextView categorydata = (TextView) convertView.findViewById(R.id.categorydata);
        categorydata.setText(CatList.getCategoryName());
        return convertView;
    }
}
