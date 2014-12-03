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

import com.whiteout.pantrytracker.activities.AddIngredientActivity;
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
    private BarcodeScanner scanner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_list_fragment, container, false);
        ButterKnife.inject(this, view);

        scanner = new BarcodeScanner();

        return view;
    }

    @OnClick(R.id.qr_code_button)
    public void clicked() {
        Intent intent = new Intent(this.getActivity(), AddIngredientActivity.class);
        startActivity(intent);
        //scanner.startBarcodeScan(ItemListFragment.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("ItemListFragment", "onActivityResult running");

        String result = scanner.parseScanResults(requestCode, resultCode, intent);
        if(result != null){
            String name = scanner.getProductNameFromBarcode(result);
            tv.setText(name);
            Toast.makeText(this.getActivity(), name, Toast.LENGTH_LONG).show();
        }
    }
}
