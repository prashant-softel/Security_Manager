    package com.example.securitymanager.securitymanager;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Color;
    import android.net.Uri;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.Filter;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;

    import com.squareup.picasso.Callback;
    import com.squareup.picasso.Picasso;

    import java.util.ArrayList;


    public class VisitorListAdapter extends ArrayAdapter<VisitorExitList> {

        ArrayList<VisitorExitList> VisitorExitLists;
        ArrayList<VisitorExitList> orig;
        Context context;
        int resource;


        public VisitorListAdapter(Context context, int resource, ArrayList<VisitorExitList> VisitorExitLists) {
            super(context, resource, VisitorExitLists);
            this.context=context;
            this.resource=resource;
            this.VisitorExitLists=VisitorExitLists;
        }

        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    constraint.toString().toLowerCase();
                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<VisitorExitList> results = new ArrayList<VisitorExitList>();
                    if (orig == null)
                        orig = VisitorExitLists;
                    if (constraint != null) {
                        if (orig != null && orig.size() > 0) {
                            for (final VisitorExitList g : orig) {
                                if (g.getName().toLowerCase()
                                        .contains(constraint.toString().toLowerCase()) || g.getUnitNo().toLowerCase()
                                        .contains(constraint.toString().toLowerCase()) || g.getVehicle().toLowerCase().contains(constraint.toString().toLowerCase()))
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
                    VisitorExitLists = (ArrayList<VisitorExitList>) results.values;
                    notifyDataSetChanged();
                }
            };
        }

        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return VisitorExitLists.size();
        }

        @Override
        public VisitorExitList getItem(int position) {
            return VisitorExitLists.get(position);
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
                convertView=layoutInflater.inflate(R.layout.visitorexitlistview,null,true);
            }
            final VisitorExitList visitor=getItem(position);
            TextView txtName1 = (TextView) convertView.findViewById(R.id.txtname);

            TextView txtName = (TextView) convertView.findViewById(R.id.name);
            TextView txtunit = (TextView) convertView.findViewById(R.id.unit);
            TextView txtdate = (TextView) convertView.findViewById(R.id.indate);
            TextView txtin = (TextView) convertView.findViewById(R.id.intime);
            TextView txtgate = (TextView) convertView.findViewById(R.id.gateno);
            TextView txtcontact = (TextView) convertView.findViewById(R.id.txtcontact);
            final TextView contactno = (TextView) convertView.findViewById(R.id.contactno);
            final ImageView image1 = (ImageView) convertView.findViewById(R.id.image1);
            TextView time_limit = (TextView) convertView.findViewById(R.id.timelimit);
            TextView txttime_limit = (TextView) convertView.findViewById(R.id.txtTimelimit);
            LinearLayout nameL = (LinearLayout) convertView.findViewById(R.id.nameL);
            Picasso.get().load(visitor.getImg()).into(image1, new Callback()
            {
                @Override
                public void onSuccess()
                {
                }
                @Override
                public void onError(Exception e)
                {
                    image1.setImageResource(R.drawable.noimage);
                }
            });
            Log.e("visitorName",visitor.getName());
            Log.e("visitorName2222",visitor.getUnitNo());
            txtName.setText(visitor.getName());
            txtunit.setText(visitor.getUnitNo());
            txtdate.setText(visitor.getDate());
            txtin.setText(visitor.getInTime());
            txtgate.setText(visitor.getEntryGate());
            contactno.setText(visitor.getContact());
            if(!visitor.getEntrywith().equals("1"))
            {
                contactno.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        new PhotoFullPopupWindow(getContext(), R.layout.popup_photo_full, v, visitor.getDoc_Img(), null);
                    }
                });
            }
            if (visitor.getEntrywith().equals("2"))
            {
                txtcontact.setText(context.getString(R.string.aadhar));
            }
            if (visitor.getEntrywith().equals("3"))
            {
                txtcontact.setText(context.getString(R.string.pancard));
            }
            if (visitor.getEntrywith().equals("4"))
            {
                txtcontact.setText(context.getString(R.string.dl));
            }
            if (visitor.getEntrywith().equals("5"))
            {
                txtcontact.setText(context.getString(R.string.other));
            }
            if(visitor.getEntrywith().equals("1"))
            {
                txtcontact.setText(context.getString(R.string.contact_no));
                contactno.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        try
                        {
                            callIntent.setData(Uri.parse("tel:" + visitor.getContact()));
                            context.startActivity(callIntent);
                        }
                        catch (Exception e)
                        {

                        }
                    }
                });
            }

            Log.e("kk",visitor.getTimeStatus()+"   "+visitor.getUnitNo());
            if(visitor.getTimeStatus().equals("1") && (!(visitor.getUnitNo().equals(""))))
            {
                    txttime_limit.setTextColor(Color.argb(255,255,88,72));
                    time_limit.setText(context.getString(R.string.time_limit_exceeded));
                    time_limit.setTextColor(Color.argb(255,255,88,72));
                    nameL.setBackgroundColor(Color.argb(255,255,88,72));


            }
            if(!(visitor.getTimeStatus().equals("1")) && (!(visitor.getUnitNo().equals(""))))
            {
                    txttime_limit.setTextColor(Color.argb(255,72,138,255));
                    time_limit.setTextColor(Color.argb(255,72,138,255));
                    time_limit.setText(context.getString(R.string.within_time_limit));
                    nameL.setBackgroundColor(Color.argb(255,72,138,255));
            }

            return convertView;
        }
    }
