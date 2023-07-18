package com.softel.securitymanager.securitymanager;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

public class About extends AppCompatActivity {
    Session session;
    private TextView mWarVesion;
    private TextView mCurrentAPKVersion;
    private TextView mNewAPKVerision;
    private LinearLayout nContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About Us");
        /* --    back button code ---- */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /* --    back button code ---- */

        session=new Session(getApplicationContext());
        String WarVersion = session.getWarversion();
        mWarVesion=(TextView) findViewById(R.id.warVesion);
        nContent =(LinearLayout) findViewById(R.id.ContentLay);
        mWarVesion.setText(WarVersion);
        String CurrentAPKVersion = session.getAPKVersion_old();
        mCurrentAPKVersion=(TextView) findViewById(R.id.currentApk);
        mCurrentAPKVersion.setText(CurrentAPKVersion);

        String NewAPKVersion =session.getAPKVersion_New();
        String apk="<u>"+NewAPKVersion+"</u>";
      //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ionicframework.way2society869487&rdid=com.ionicframework.way2society869487"));
        mNewAPKVerision=(TextView) findViewById(R.id.NewAPK);
        mNewAPKVerision.append(Html.fromHtml(apk));
        if(mCurrentAPKVersion.getText().toString().equals(NewAPKVersion))
        {
            System.out.println("version");
            //mNewAPKVerision.setVisibility(View.INVISIBLE);
            nContent.removeView(mNewAPKVerision);
        }

        System.out.println("version" + WarVersion);
    }
}
