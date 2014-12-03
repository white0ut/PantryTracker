package com.whiteout.pantrytracker.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.barcode.BarcodeScanner;

import java.util.Calendar;
import java.util.Date;

public class AddIngredientFragment extends Fragment{
    public static final int REQUEST_CODE_NEW = 0;
    public static final int REQUEST_CODE_EXISTING = 1;
    public static final String KEY_REQUESTCODE = "REQUESTCODE";
    public static final String KEY_ID = "IDOFITEM";
    public static final String KEY_NAME = "ITEMNAME";
    public static final String KEY_DATE = "ITEMEXPIRATIONDATE";
    public static final String KEY_QUANTITY = "ITEMQUANTITY";
    public static final String KEY_UNIT = "ITEMUNIT";
    public static final String KEY_INDEX = "ITEMINDEX";

    @InjectView(R.id.btn_add_submit) Button mSubmitButton;
    @InjectView(R.id.dp_add_expiration) DatePicker mDPExpiration;
    @InjectView(R.id.et_add_quantity) EditText mETQuantity;
    @InjectView(R.id.et_add_name) EditText mETName;
    @InjectView(R.id.et_add_unit) EditText mETUnit;
    private BarcodeScanner scanner;
    private long mCurrentID;
    private int mCurrentIndex;

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


        Intent intent = getActivity().getIntent();

        // If an item was passed to this fragment, fill in pickers with passed data
        if(intent.getExtras().getInt(KEY_REQUESTCODE) == REQUEST_CODE_EXISTING){
            mCurrentID = intent.getLongExtra(KEY_ID, 0);
            mCurrentIndex = intent.getIntExtra(KEY_INDEX, 0);

            Log.d("IDProblem", "AddIngredient id: " + mCurrentID + " " + ", index: " + mCurrentIndex);

            Log.d("fill pickers", intent.getStringExtra(KEY_NAME));
            mETName.setText(intent.getStringExtra(KEY_NAME));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(intent.getLongExtra(KEY_DATE, calendar.getTimeInMillis()));

            mDPExpiration.updateDate(calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            mETQuantity.setText(String.valueOf(intent.getFloatExtra(KEY_QUANTITY,0)));
            mETUnit.setText(intent.getStringExtra(KEY_UNIT));
        }
        else{
            mCurrentID = -1;
            mCurrentIndex = -1;
        }

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

        if(inputDataIsValid()){
            // TODO send data back to other fragment
            Intent intent = new Intent();
            intent.putExtra(KEY_NAME, mETName.getText().toString());
            intent.putExtra(KEY_QUANTITY, Float.valueOf(mETQuantity.getText().toString()));

            int day = mDPExpiration.getDayOfMonth();
            int month = mDPExpiration.getMonth();
            int year = mDPExpiration.getYear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            Long time = calendar.getTime().getTime();
            intent.putExtra(KEY_DATE, time);

            intent.putExtra(KEY_UNIT, mETUnit.getText());

            if(mCurrentIndex >= 0 && mCurrentID >= 0){
                intent.putExtra(KEY_ID, mCurrentID);
                intent.putExtra(KEY_INDEX, mCurrentIndex);
            }

            getActivity().setResult(Activity.RESULT_OK,intent);
            getActivity().finish();
        }
        else{
            Toast.makeText(getActivity(),
                    "Invalid input. Please ensure that all fields are filled in.",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean inputDataIsValid(){
        if(mETName.getText().toString().matches("")) {
            return false;
        }else if (mETQuantity.getText().toString().matches("")){
            return false;
        }else{
            return true;
        }
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
