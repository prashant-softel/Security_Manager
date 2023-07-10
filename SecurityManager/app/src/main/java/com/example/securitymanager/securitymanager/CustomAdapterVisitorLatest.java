package com.example.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

class CustomAdapterVisitorLatest extends ArrayAdapter<visitorlatestlist> {

        ArrayList<visitorlatestlist> visitorlatestlists;
        Context context;
        int resource;
    public CustomAdapterVisitorLatest(Context context,int resource,ArrayList<visitorlatestlist> visitorlatestlists){
        super(context,resource,visitorlatestlists);
        this.context=context;
        this.resource=resource;
        this.visitorlatestlists=visitorlatestlists;

        }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null)
        {
        LayoutInflater layoutInflater=(LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.view_visitorlatest,null,true);
        }
        int DateCount=0;
        visitorlatestlist visitlatest=getItem(position);
        System.out.println("In adapter");
        TextView txtflat=(TextView) convertView.findViewById(R.id.flat);
        txtflat.setText(visitlatest.getUnitno());

        TextView txtdate=(TextView) convertView.findViewById(R.id.date);
        txtdate.setText(visitlatest.getIndate());

        TextView txtin=(TextView) convertView.findViewById(R.id.ingate);
        txtin.setText(visitlatest.getIntime() + "\n ( " + visitlatest.getEntrygate()+" )"+"\n");

        String[] stdate=visitlatest.getIndate().split("-");
        String sday=stdate[0];
        String smonth=stdate[1];
        String syear=stdate[2];
        int smon=0;
        String months[] = {"Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug", "Sept",
                "Oct", "Nov", "Dec"};
        for(int i=0;i<months.length;i++)
        {
            if(smonth.equals(months[i]))
            {smon=(i+1);}
        }
        String jstartdate=sday+"/"+smon+"/"+syear;

        String[] endate=visitlatest.getOutdate().split("-");
        String eday=endate[0];
        String emonth=endate[1];
        String eyear=endate[2];
        int emon=0;
        String monthse[] = {"Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug", "Sept",
                "Oct", "Nov", "Dec"};
        for(int i=0;i<monthse.length;i++)
        {
            if(emonth.equals(monthse[i]))
            {emon=(i+1);}
        }
        String jenddate=eday+"/"+emon+"/"+eyear;

        try {

            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(jstartdate);

            Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(jenddate);


            long diff = (date2.getTime() - date1.getTime())/1000;
            //System.out.println(diff);
            DateCount = (int)TimeUnit.SECONDS.toDays(diff);
            //System.out.println("diff days : "+day);
        }
        catch (Exception e)
        {}



        TextView txtout=(TextView) convertView.findViewById(R.id.outgate);
        if(visitlatest.getOuttime().equals("00:00"))
        {
            txtout.setTextColor(Color.RED);
            txtout.setText("Still Inside");
        }
        else
        {
            if(DateCount==0 && (!(visitlatest.getCheckout().equals("1"))))
            {
                txtout.setTextColor(Color.rgb(0,120,26));
                txtout.setText(visitlatest.getOuttime());

                //txtout.setText(visitlatest.getOuttime() + " " + DateCount + " days" + "\n ( "+visitlatest.getExitgate() + " )");
            }
            if(DateCount!=0 && (!(visitlatest.getCheckout().equals("1"))))
            {
                txtout.setTextColor(Color.rgb(0,120,26));
                txtout.setText(visitlatest.getOuttime() + " +" + DateCount + " d" + "\n ( "+visitlatest.getExitgate() + " )");
            }
            if(DateCount==0 && (visitlatest.getCheckout().equals("1")))
            {
                txtout.setTextColor(Color.RED);
                //txtout.setText(visitlatest.getOuttime());

                txtout.setText(visitlatest.getOuttime() +"\n ( "+visitlatest.getExitgate() + " )");
            }
            if(DateCount!=0 && (visitlatest.getCheckout().equals("1")))
            {
                txtout.setTextColor(Color.RED);
                txtout.setText(visitlatest.getOuttime() + " +" + DateCount + " d" + "\n ( "+visitlatest.getExitgate() + " )");
            }
        }




        return convertView;
        }
}