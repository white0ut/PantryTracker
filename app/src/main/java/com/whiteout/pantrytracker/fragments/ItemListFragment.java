package com.whiteout.pantrytracker.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.whiteout.pantrytracker.R;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_list_fragment, container, false);
        ButterKnife.inject(this, view);


        return view;
    }

    @OnClick(R.id.qr_code_button)
    public void clicked() {
        Toast.makeText(getActivity().getApplicationContext(), "heyy", Toast.LENGTH_SHORT).show();
    }
}
