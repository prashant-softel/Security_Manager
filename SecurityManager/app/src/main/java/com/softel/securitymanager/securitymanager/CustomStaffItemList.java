package com.softel.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomStaffItemList extends ArrayAdapter<staffitemlist> {
    ArrayList<staffitemlist> staffitemlists;
    Context context;
    int resource;
    public CustomStaffItemList(Context context, int resource, ArrayList<staffitemlist> staffitemlists) {
        super(context, resource, staffitemlists);
        this.context=context;
        this.resource=resource;
        this.staffitemlists=staffitemlists;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.staffdesclist, null, true);
        }

        System.out.println(position);
        final staffitemlist stafflist = getItem(position);
        TextView unitno = (TextView) convertView.findViewById(R.id.unitnoitem);
        TextView ownername = (TextView) convertView.findViewById(R.id.ownernameitem);
        TextView desc = (TextView) convertView.findViewById(R.id.Vcheckoutdescitem);

        unitno.setText(stafflist.getUnit_no());
        ownername.setText(stafflist.getOwner_name());
        desc.setText(stafflist.getStaff_note());
        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getContext(), R.layout.popup_photo_full, v,stafflist.getImagedesc(), null);
            }
        });
        return convertView;


    }


}
