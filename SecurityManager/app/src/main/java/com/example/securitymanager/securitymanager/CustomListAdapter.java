package com.example.securitymanager.securitymanager;

import com.squareup.picasso.Callback;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import android.text.Html;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<StaffList> {

    ArrayList<StaffList> stafflists;
    ArrayList<StaffList> orig;

    Context context;
    int resource;


    public CustomListAdapter(Context context, int resource, ArrayList<StaffList> stafflists) {
        super(context, resource, stafflists);
        this.context=context;
        this.resource=resource;
        this.stafflists=stafflists;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<StaffList> results = new ArrayList<StaffList>();
                if (orig == null)
                    orig = stafflists;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final StaffList g : orig) {
                            if (g.getName().toLowerCase()
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
                stafflists = (ArrayList<StaffList>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return stafflists.size();
    }

    @Override
    public StaffList getItem(int position) {
        return stafflists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            LayoutInflater layoutInflater=(LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.view_staff,null,true);
        }
        StaffList staff=getItem(position);
        final ImageView imageView=(ImageView) convertView.findViewById(R.id.image1);
        //System.out.println(staff.getImage());

            //Picasso.get().load("https://way2society.com/" + staff.getImage()).into(imageView);
       Picasso.get().load(staff.getImage()).into(imageView, new Callback() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onError(Exception e) {
            imageView.setImageResource(R.drawable.noimage);
            }
        });
        TextView tx=(TextView) convertView.findViewById(R.id.textname);
        String name = "<b>"+context.getString(R.string.name)+"</b> ";
        tx.setText(Html.fromHtml(name));
        TextView tx1=(TextView) convertView.findViewById(R.id.textprofile);
        String profile="<b>"+context.getString(R.string.job_profile)+"</b>";
        tx1.setText(Html.fromHtml(profile));

        TextView txtName=(TextView) convertView.findViewById(R.id.text1);
        txtName.setText(staff.getName());


        TextView txtprofile=(TextView) convertView.findViewById(R.id.text2);
        txtprofile.setText(staff.getJobProfile());


        return convertView;
    }


}
