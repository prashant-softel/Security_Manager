package com.softel.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

class CustomAdapterStaffreport extends ArrayAdapter<allstafflist> {

    ArrayList<allstafflist> allstafflists;
    Context context;
    int resource;
    public CustomAdapterStaffreport(Context context, int resource, ArrayList<allstafflist> allstafflists)
    {
        super(context, resource, allstafflists);
        this.context=context;
        this.resource=resource;
        this.allstafflists=allstafflists;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.view_allstaffreport,null,true);
        }
        allstafflist staffall=getItem(position);
        int DateCount=0;

        TextView tdate=(TextView) convertView.findViewById(R.id.indateall);
        tdate.setText(staffall.getInDate());

        TextView tstatus=(TextView) convertView.findViewById(R.id.attendanceall);
        if(staffall.getAttendance().equals("A"))
        {
            String att="<b> A </b>";
           tstatus.setTextColor(Color.RED);
            tstatus.setText(Html.fromHtml(att));
        }
        else
        {

            String att="<b> P </b>";
            tstatus.setTextColor(Color.rgb(0,120,26));
            tstatus.setText(Html.fromHtml(att));
        }


        TextView tin=(TextView) convertView.findViewById(R.id.intimeall);
        tin.setText(staffall.getInTime());

        TextView tout=(TextView) convertView.findViewById(R.id.outimeall);

        if(staffall.getOutDate().equals("00-00-00"))
        {
            DateCount=0;
        }
        else
        {

            String[] stdate=staffall.getInDate().split("-");
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

            String[] endate=staffall.getOutDate().split("-");
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
        }

        if(staffall.getOutTime().equals("00:00"))
        {
            tout.setTextColor(Color.RED);
            tout.setText(context.getString(R.string.still_inside));
        }
        if(!(staffall.getOutTime().equals("00:00")) && DateCount==0)
        {
            tout.setTextColor(Color.rgb(0,120,26));
            tout.setText(staffall.getOutTime());
        }
        if(!(staffall.getOutTime().equals("00:00")) && DateCount!=0 && (!(staffall.getOutTime().equals("--"))))
        {
            tout.setTextColor(Color.rgb(0,120,26));
            tout.setText(staffall.getOutTime() + "\n+" +DateCount +"d\n");
        }
        TextView thours=(TextView) convertView.findViewById(R.id.hoursall);

        thours.setText(staffall.getTotalHour());
        return convertView;
    }
}
