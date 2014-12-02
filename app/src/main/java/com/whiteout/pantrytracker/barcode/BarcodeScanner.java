package com.whiteout.pantrytracker.barcode;


import android.app.Activity;
import android.content.Intent;

public class BarcodeScanner {

    /**
     * Creates an intent and launches the barcode scanner. Results will call
     * the activity's onActivityResult().
     */
    public void startBarcodeScan(Activity activity){
        IntentIntegrator scanIntegrator = new IntentIntegrator(activity);
        scanIntegrator.initiateScan();
    }

    public String parseScanResults(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String result;

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat  = scanningResult.getFormatName();

            formatTxt.setText("FORMAT: " + scanFormat);
            contentTxt.setText("CONTENT: " + scanContent);

            OutpanFetcher fetcher = new OutpanFetcher();
            new FetchItemTask().execute(scanContent);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

}
