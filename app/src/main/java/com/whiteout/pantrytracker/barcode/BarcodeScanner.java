package com.whiteout.pantrytracker.barcode;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.concurrent.CountDownLatch;

public class BarcodeScanner {

    /**
     * Creates an intent and launches the barcode scanner. Results will call
     * the passed activity's onActivityResult().
     *
     */
    public void startBarcodeScan(Activity activity){
        IntentIntegrator scanIntegrator = new IntentIntegrator(activity);
        scanIntegrator.initiateScan();
    }

    /**
     * Launches an intent to cal the ZXing barcode scanner. Calls the passed fragment's
     * onActivityResult when complete.
     * @param fragment Fragment to return results to
     */
    public void startBarcodeScan(Fragment fragment){
        IntentIntegrator scanIntegrator = new IntentIntegrator(fragment);
        scanIntegrator.initiateScan();
    }


    /**
     * Retrieves a barcode stored as a String from an Intent. Accepts parameters from
     * onActivityResult.
     * @return String containing barcode if one has been scanned. If no barcode has been scanned, returns null.
     */
    public String parseScanResults(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat  = scanningResult.getFormatName();

            OutpanFetcher fetcher = new OutpanFetcher();
            //new FetchItemTask().execute(scanContent);
            return scanContent;
        }
        else{
            return null;
        }
    }

    /**
     * Retrieves product data from Outpan. To be outside of UI thread, creates
     * a separate thread.
     * @param code Barcode to retrieve data for
     * @return String value with name of product.
     */
    public String getProductNameFromBarcode(String code){
        // hacky but it works
        final String[] result = new String[1];
        final String barcode = code;
        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(new Runnable() {
            public void run() {
                OutpanFetcher fetcher = new OutpanFetcher();
                result[0] = fetcher.fetchItems(barcode);
                latch.countDown();
            }
        }).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result[0];
    }

}
