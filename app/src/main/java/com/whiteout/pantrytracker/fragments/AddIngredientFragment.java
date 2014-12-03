package com.whiteout.pantrytracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.barcode.BarcodeScanner;

import butterknife.InjectView;

public class AddIngredientFragment extends Fragment{

    @InjectView(R.id.btn_add_submit) Button mSubmitButton;
    @InjectView(R.id.dp_add_expiration) DatePicker mDPExpiration;
    @InjectView(R.id.et_add_name) EditText mETName;
    @InjectView(R.id.et_add_unit) EditText mETUnit;
    private BarcodeScanner scanner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_ingredient, container,false);
        ButterKnife.inject(this, view);

        scanner = new BarcodeScanner();



        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("AddIngredientFragment", "onOptionsItemSelected running");
        switch (item.getItemId()) {
            case R.id.menu_barcode_scan:
                Toast.makeText(getActivity(), "Barcode clicked", Toast.LENGTH_LONG).show();
                scanner.startBarcodeScan(AddIngredientFragment.this);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_add_submit)
    public void clicked() {
        // SUBMIT STUFF GOES HERE
        Toast.makeText(getActivity(), "SUBMIT BUTTON CLICKED", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("AddIngredientFragment", "onActivityResult running");

        String result = scanner.parseScanResults(requestCode, resultCode, intent);

        if(result != null){
            String name = scanner.getProductNameFromBarcode(result);
            if(name != null){
                mETName.setText(name);
                Toast.makeText(this.getActivity(), name, Toast.LENGTH_LONG).show();
            }
        }
    }
}
