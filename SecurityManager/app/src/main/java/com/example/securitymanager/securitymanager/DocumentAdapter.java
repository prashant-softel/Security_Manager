package com.example.securitymanager.securitymanager;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;



public class DocumentAdapter extends ArrayAdapter<Document> {
    ArrayList<Document> documents;
        Context context;
        int resource;
        int textview;
        public DocumentAdapter(Context context, int resource,int textviewid, ArrayList<Document> documents) {
            super(context, resource,textviewid, documents);
            this.context=context;
            this.resource=resource;
            this.textview=textviewid;
            this.documents=documents;

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
                convertView = layoutInflater.inflate(R.layout.unitlayout, null, true);
            }
            Document doc = getItem(position);
            TextView txtdoc = (TextView) convertView.findViewById(R.id.UnitList);
            txtdoc.setText(doc.getDocName());
            return convertView;
        }
    }


