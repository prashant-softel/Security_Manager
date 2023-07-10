package com.example.securitymanager.securitymanager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomVisitorDetailsUnit extends ArrayAdapter<VisitorDetailsUnit> {
    ArrayList<VisitorDetailsUnit> visitorDetailsUnits;
    Context context;
    int resource;


    public CustomVisitorDetailsUnit(Context context, int resource, ArrayList<VisitorDetailsUnit> visitorDetailsUnits) {
        super(context, resource, visitorDetailsUnits);
        this.context=context;
        this.resource=resource;
        this.visitorDetailsUnits=visitorDetailsUnits;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.visitor_unit, null, true);
        }

        System.out.println(position);
        final VisitorDetailsUnit visit = getItem(position);

        System.out.println(visit.getUnitNo() + " "+ visit.getOwnerName() + " "+visit.getApprovalStatus());
        LinearLayout statusdenied=(LinearLayout) convertView.findViewById(R.id.deniedL);

        LinearLayout approvebyL=(LinearLayout) convertView.findViewById(R.id.l4);
        LinearLayout noteL=(LinearLayout) convertView.findViewById(R.id.noteL);
        LinearLayout checkoutdesc=(LinearLayout) convertView.findViewById(R.id.checkoutdesc);

        TextView txtapproveby = (TextView) convertView.findViewById(R.id.txtapproveby);
        TextView approveby = (TextView) convertView.findViewById(R.id.approveby);
        TextView approvemsg = (TextView) convertView.findViewById(R.id.approvemsgV);
        TextView Vcheckoutdesc = (TextView) convertView.findViewById(R.id.Vcheckoutdesc);


        TextView txtunit=(TextView) convertView.findViewById(R.id.unitno);
        TextView txtowner=(TextView) convertView.findViewById(R.id.ownername);
        TextView txtstatus=(TextView) convertView.findViewById(R.id.status);

        TextView txtunit1=(TextView) convertView.findViewById(R.id.txtunit);
        TextView txtowner1=(TextView) convertView.findViewById(R.id.txtowner);
        TextView txtstatus1=(TextView) convertView.findViewById(R.id.txtstatus);

        TextView txtstatusdenied=(TextView) convertView.findViewById(R.id.txtdeniedstatus);
        if(visit.getApprovalStatus().equals("2"))
        {

            txtunit1.setTextColor(Color.RED);
            txtowner1.setTextColor(Color.RED);
            txtstatus1.setTextColor(Color.RED);
            txtunit.setTextColor(Color.RED);
            txtowner.setTextColor(Color.RED);
            txtstatus.setTextColor(Color.RED);
            txtapproveby.setTextColor(Color.RED);
            approveby.setTextColor(Color.RED);

        }
        txtunit.setText(visit.getUnitNo());
        txtowner.setText(visit.getOwnerName());
        if(visit.getApprovalStatus().equals("1"))
        {
        //Approved
            txtstatus.setText(context.getString(R.string.approved));
            if(visit.getApprovalwith().equals("0"))
            {
                if(!visit.getLoginName().equals("0"))
                {
                    approvebyL.setVisibility(View.VISIBLE);
                    txtapproveby.setText(context.getString(R.string.approved_by));
                    approveby.setText(context.getString(R.string.security_gaurd) + visit.getLoginName());
                }
                else
                {
                    approvebyL.setVisibility(View.GONE);
                }
            }
            else
            {
                if(!visit.getLoginName().equals("0"))
                {
                    approvebyL.setVisibility(View.VISIBLE);
                    txtapproveby.setText(context.getString(R.string.approved_by));
                    approveby.setText(context.getString(R.string.owner_name) + visit.getLoginName());
                }
                else
                {
                    approvebyL.setVisibility(View.GONE);
                }
            }
            if(!visit.getApprovalmsg().equals("") && (!(visit.getApprovalmsg().equals("NULL"))))
            {
                noteL.setVisibility(View.VISIBLE);
                approvemsg.setText(visit.getApprovalmsg());
            }
           if(visit.getApprovalmsg().equals(""))
            {
                noteL.setVisibility(View.GONE);
            }
        }
        if(visit.getApprovalStatus().equals("2"))
        {
        //Denied
            statusdenied.setVisibility(View.VISIBLE);
            txtstatus.setText(context.getString(R.string.denied));
            txtstatusdenied.setTextColor(Color.RED);
            txtstatusdenied.setText(context.getString(R.string.sorry_your_entry) +visit.getUnitNo() +context.getString(R.string.was_denied));
            if(visit.getApprovalwith().equals("0"))
            {
                if(!visit.getLoginName().equals("0"))
                {
                    approvebyL.setVisibility(View.VISIBLE);
                    txtapproveby.setText(context.getString(R.string.denied_by));
                    approveby.setText(context.getString(R.string.security_gaurd) + visit.getLoginName());
                }
                else
                {
                    approvebyL.setVisibility(View.GONE);
                }
            }
            else
            {
                if(!visit.getLoginName().equals("0"))
                {
                    approvebyL.setVisibility(View.VISIBLE);
                    txtapproveby.setText(context.getString(R.string.denied_by));
                    approveby.setText(context.getString(R.string.owner_name) + visit.getLoginName());
                }
                else
                {
                    approvebyL.setVisibility(View.GONE);
                }
            }


        }
        if(visit.getApprovalStatus().equals("3"))
        {
        //WithoutApproval
            txtstatus.setText(context.getString(R.string.without_approval));
        }
        if(visit.getApprovalStatus().equals(""))
        {
            txtstatus1.setVisibility(View.GONE);
            txtstatus.setVisibility(View.GONE);
            statusdenied.setVisibility(View.GONE);
        }

        if(!(visit.getNote().equals("") && visit.getVisitor_item_image().equals("")))
        {
            checkoutdesc.setVisibility(View.VISIBLE);
            Vcheckoutdesc.setText(visit.getNote());
        }
        else
        {
            checkoutdesc.setVisibility(View.GONE);
        }
        Vcheckoutdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PhotoFullPopupWindow(getContext(), R.layout.popup_photo_full, v, visit.getVisitor_item_image(), null);
            }
        });
        return convertView;
    }


    }
