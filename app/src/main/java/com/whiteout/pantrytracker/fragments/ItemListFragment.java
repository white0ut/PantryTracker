package com.whiteout.pantrytracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.whiteout.pantrytracker.R;

import com.whiteout.pantrytracker.barcode.*;

import java.lang.reflect.Array;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Author:  Kendrick Cline
 * Date:    10/29/14
 * Email:   kdecline@gmail.com
 */
public class ItemListFragment extends Fragment {

    @InjectView(R.id.qr_code_button) Button button;
    @InjectView(R.id.textView) TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_list_fragment, container, false);
        ButterKnife.inject(this, view);

        return view;
    }


    @OnClick(R.id.qr_code_button)
    public void clicked() {
        Toast.makeText(getActivity().getApplicationContext(), "heyy", Toast.LENGTH_SHORT).show();
        IntentIntegrator scanIntegrator = new IntentIntegrator(this.getActivity());
        scanIntegrator.initiateScan();
    }

    public static void onReceiveBarcodeResults(){
        Log.d("ItemListFragment", "onReceiveBarcodeResults running");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Toast.makeText(getActivity(), "onActivityResult Running", Toast.LENGTH_LONG);

        Log.d("ItemListFragment", "onActivityResult running");
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat  = scanningResult.getFormatName();

            tv.setText(scanContent);
            Toast.makeText(getActivity(),scanContent, Toast.LENGTH_LONG);

        }
        else{
            Toast.makeText(getActivity(),
                    "No scan data received!", Toast.LENGTH_SHORT).show();
        }
    }
}
