package com.softel.securitymanager.securitymanager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;

public class RoundQRScanner extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener  {
    private BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_scanner);

        // getting barcode instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);
    }
    @Override
    public void onScanned(final Barcode barcode) {
        Log.e( "onScanned: ","" + barcode.displayValue);
        barcodeReader.playBeep();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
            }
        });

        Intent security_round = new Intent(this, Round.class);
        Bundle Ckpost;
        String ckpId="";
        Ckpost = getIntent().getExtras();
        ckpId = Ckpost.getString("CheckpostID");

        security_round.putExtra("code", barcode.displayValue);
        security_round.putExtra("SendCkpId", ckpId);

        security_round.putExtra("counter", "1");
        startActivity(security_round);
    }
    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.e("onScannedMultiple: ","" + barcodes.size());

        String codes = "";
        for (Barcode barcode : barcodes) {
            codes += barcode.displayValue + ", ";
        }

        final String finalCodes = codes;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getApplicationContext(), "Camera permission denied!", Toast.LENGTH_LONG).show();
        finish();
    }
}
