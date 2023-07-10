package com.example.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapterExpectedVisitor  extends ArrayAdapter<allExpectedVisitor> {
    ArrayList<allExpectedVisitor> allExpectedVisitor;
    ArrayList<allExpectedVisitor> orig;

    Context context;
    int resource;

    public CustomAdapterExpectedVisitor(Context context, int resource, ArrayList<allExpectedVisitor> allExpectedVisitors) {
        super(context, resource, allExpectedVisitors);
        this.context = context;
        this.resource = resource;
        this.allExpectedVisitor = allExpectedVisitors;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<allExpectedVisitor> results = new ArrayList<allExpectedVisitor>();
                if (orig == null)
                    orig = allExpectedVisitor;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final allExpectedVisitor g : orig) {
                            if (g.getFname().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                allExpectedVisitor = (ArrayList<allExpectedVisitor>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return allExpectedVisitor.size();
    }

    @Override
    public allExpectedVisitor getItem(int position) {
        return allExpectedVisitor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.allexpected, null, true);
        }
        allExpectedVisitor expVisitor=getItem(position);
            TextView txtfName = (TextView) convertView.findViewById(R.id.txtFName);
            txtfName.setText(expVisitor.getFname());

            TextView txtLName = (TextView) convertView.findViewById(R.id.txtLName);
            txtLName.setText(expVisitor.getLname());

            TextView txtContact = (TextView) convertView.findViewById(R.id.txtMobile);
            txtContact.setText(expVisitor.getMobile());

            TextView txtExpectedDate = (TextView) convertView.findViewById(R.id.txtExpDate);
            String[] date1 = expVisitor.getExpectedDate().split("-");
            String sday = date1[2];
            String smonth = date1[1];
            String syear = date1[0];
            String smon = "";
            String months[] = {"Jan", "Feb", "Mar", "Apr",
                    "May", "Jun", "Jul", "Aug", "Sept",
                    "Oct", "Nov", "Dec"};

            int iMonth = Integer.parseInt(smonth);
            smon = months[iMonth-1];

            txtExpectedDate.setText(sday + "-" + smon + "-" + syear);

            TextView txtTime = (TextView) convertView.findViewById(R.id.txtExpTime);
            txtTime.setText(expVisitor.getExpectedTime());

            TextView txtPurpose = (TextView) convertView.findViewById(R.id.txtExpPurpose);
            txtPurpose.setText(expVisitor.getPurposeName());

            TextView txtUnit = (TextView) convertView.findViewById(R.id.txtUnit);
            txtUnit.setText(expVisitor.getUnitNo());

            TextView txtOwner = (TextView) convertView.findViewById(R.id.txtOwner);
            txtOwner.setText(expVisitor.getOwnerName());

        return convertView;
    }
}
