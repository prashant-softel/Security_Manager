package com.softel.securitymanager.securitymanager;

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

public class CustomAdapterVisitReport extends ArrayAdapter<allvisitlist> {
    ArrayList<allvisitlist> allvisit;
    Context context;
    int resource;
    public CustomAdapterVisitReport(Context context, int resource, ArrayList<allvisitlist> allvisit) {
        super(context, resource, allvisit);
        this.context=context;
        this.resource=resource;
        this.allvisit=allvisit;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        System.out.println("position" +position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.visitlistreport, null, true);
        }
        int DateCount = 0;
        allvisitlist visit = getItem(position);

        TextView txtname=(TextView) convertView.findViewById(R.id.name);
        txtname.setText(visit.getName());
        TextView txtflat=(TextView) convertView.findViewById(R.id.flatvisit);
        txtflat.setText(visit.getUnitno());

        TextView txtdate=(TextView) convertView.findViewById(R.id.date);
        txtdate.setText(visit.getIndate());


        TextView txtin=(TextView) convertView.findViewById(R.id.in);
        txtin.setText(visit.getIntime() );
        String[] stdate=visit.getIndate().split("-");
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

        String[] endate=visit.getOutdate().split("-");
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


        TextView txtout=(TextView) convertView.findViewById(R.id.out);
        if(visit.getOuttime().equals("00:00") && visit.getCheck().equals(""))
        {
            txtout.setTextColor(Color.RED);
            txtout.setText(context.getString(R.string.still_inside));
        }
        else if(visit.getOuttime().equals("00:00") && visit.getCheck().equals("data"))
        {
            txtout.setTextColor(Color.RED);
            txtout.setText(context.getText(R.string.denied));
        }
        else
        {
            if(DateCount==0 && (!(visit.getCheckout().equals("1"))))
            {
                txtout.setTextColor(Color.rgb(0,120,26));
                txtout.setText(visit.getOuttime());

                //txtout.setText(visitlatest.getOuttime() + " " + DateCount + " days" + "\n ( "+visitlatest.getExitgate() + " )");
            }
            if(DateCount!=0 && (!(visit.getCheckout().equals("1"))))
            {
                txtout.setTextColor(Color.rgb(0,120,26));
                txtout.setText(visit.getOuttime() + "\n+" + DateCount + " days" );
            }
            if(DateCount==0 && (visit.getCheckout().equals("1")))
            {
                txtout.setTextColor(Color.RED);
                //txtout.setText(visitlatest.getOuttime());

                txtout.setText(visit.getOuttime());
            }
            if(DateCount!=0 && (visit.getCheckout().equals("1")))
            {
                txtout.setTextColor(Color.RED);
                txtout.setText(visit.getOuttime() + " \n+" + DateCount + " days");
            }
        }





        return convertView;
    }
    }
