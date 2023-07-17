package com.softel.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewViewCustomeAdapter extends ArrayAdapter<NewsList>{
    ArrayList<NewsList> NewsLists;
    Context context;
    int resource;
    public NewViewCustomeAdapter(Context context, int resource, ArrayList<NewsList> NewsLists)
    {
        super(context, resource, NewsLists);
        this.context=context;
        this.resource=resource;
        this.NewsLists=NewsLists;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.newslist, null, true);
        }
        System.out.println(position);
        NewsList noticeList = getItem(position);
        if(noticeList.getI()==0)

        {
            System.out.println("In adapter : " + noticeList.getTitle());
            TextView txtnotice = (TextView) convertView.findViewById(R.id.noticeTitle);
            txtnotice.setText(noticeList.getTitle());
            TextView txtnotice1 = (TextView) convertView.findViewById(R.id.noticeDesc);
            txtnotice1.setText("");
            txtnotice1.setVisibility(View.GONE);
        }
     else
        {

            System.out.println("test " + noticeList.getI());
            TextView txtnotice = (TextView) convertView.findViewById(R.id.noticeTitle);
            txtnotice.setText(noticeList.getTitle());
            TextView txtnotice1 = (TextView) convertView.findViewById(R.id.noticeDesc);
            txtnotice1.setText(noticeList.getDesc());
            txtnotice1.setVisibility(View.VISIBLE);

        }
        return convertView;
    }

}
