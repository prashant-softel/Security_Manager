package com.softel.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapterStaffTotalExit extends ArrayAdapter<StaffTotalExit> {

    ArrayList<StaffTotalExit> staffTotalExits;
    public ArrayList<StaffTotalExit> orig;
    Context context;
    int resource;

    public CustomAdapterStaffTotalExit(Context context, int resource, ArrayList<StaffTotalExit> staffTotalExits) {
        super(context, resource, staffTotalExits);
        this.context = context;
        this.resource = resource;
        this.staffTotalExits = staffTotalExits;

    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<StaffTotalExit> results = new ArrayList<StaffTotalExit>();
                if (orig == null)
                    orig = staffTotalExits;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final StaffTotalExit g : orig) {
                            if (g.getFull_name().toLowerCase()
                                    .contains(constraint.toString().toLowerCase()))
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
                staffTotalExits = (ArrayList<StaffTotalExit>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return staffTotalExits.size();
    }

    @Override
    public StaffTotalExit getItem(int position) {
        return staffTotalExits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.total_staff,null,true);
        }
        final StaffTotalExit stafftotal=getItem(position);


        final ImageView imagestaff=(ImageView) convertView.findViewById(R.id.image1);
       // Picasso.get().load("https://way2society.com/" + stafftotal.getPhoto_thumb()).into(imagestaff);


        Picasso.get().load(stafftotal.getPhoto_thumb()).into(imagestaff, new Callback() {
            @Override
            public void onSuccess() {

                Log.e("SUCCESS","SUCCESS");
            }
            @Override
            public void onError(Exception e) {
                imagestaff.setImageResource(R.drawable.noimage);
                Log.e("UNSUCCESS","UNSUCCESS");

            }
        });

        TextView txtname=(TextView) convertView.findViewById(R.id.name);
        txtname.setText(stafftotal.getFull_name());

        TextView txtcontact=(TextView) convertView.findViewById(R.id.contactno);

        String contactno = "<u>"+stafftotal.getCur_con_1()+"</u>";
        txtcontact.setClickable(true);
        txtcontact.setText(Html.fromHtml(contactno));
        txtcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                try {
                    callIntent.setData(Uri.parse("tel:" + stafftotal.getCur_con_1()));
                    context.startActivity(callIntent);
                }
                catch (Exception e)
                {}
            }
        });

        txtcontact.setText(stafftotal.getCur_con_1());

        TextView txtcat=(TextView) convertView.findViewById(R.id.category);
        txtcat.setText(stafftotal.getCat());

        TextView txtstaff=(TextView) convertView.findViewById(R.id.staffid);
        txtstaff.setText(stafftotal.getSocService_prd_reg_id());

        TextView txtsince=(TextView) convertView.findViewById(R.id.workingsince);
        txtsince.setText(stafftotal.getSince());

        TextView txtrating=(TextView) convertView.findViewById(R.id.rating);


        TextView txtrat=(TextView) convertView.findViewById(R.id.txtrating);
        if(stafftotal.getRating().equals(""))
        {

            txtrat.setText("");
            txtrating.setText("");
        }
        else
        {

            double rating=Double.valueOf(stafftotal.getRating());
            txtrat.setText(context.getString(R.string.rating));
            if(rating == 1 || rating > 1 && rating < 2)
            {

                txtrating.setText(String.valueOf(rating));
                txtrating.append(context.getString(R.string.poor));
                txtrating.setTextColor(Color.argb(255,255,97,97));

            }
            if(rating == 2 || rating > 2  && rating < 3)
            {
                txtrating.setText(String.valueOf(rating));
                txtrating.append(context.getString(R.string.average));
                txtrating.setTextColor(Color.argb(255,255,159,0));

            }
            if(rating == 3 || (rating > 3  && rating < 4))
            {

                txtrating.setText(rating+"");
                txtrating.append(context.getString(R.string.good));
                txtrating.setTextColor(Color.argb(255,56,142,60));

            }
            if(rating == 4 || rating > 4  && rating < 5)
            {
                txtrating.setText(String.valueOf(rating));
                txtrating.append(context.getString(R.string.very_good));
                txtrating.setTextColor(Color.argb(255,56,142,60));

            }
            if(rating==5)
            {
                txtrating.setText(String.valueOf(rating));
                txtrating.append(context.getString(R.string.excellent));
                txtrating.setTextColor(Color.argb(255,56,142,60));

            }
        }
        //txtrating.setText(stafftotal.getRating());
        return convertView;
    }



}
