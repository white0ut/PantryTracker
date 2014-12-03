package com.whiteout.pantrytracker.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.fragments.AddIngredientFragment;

public class AddIngredientActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);


        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentById(R.id.fragmentContainer);

        if(frag == null){
            Fragment weatherFrag = new AddIngredientFragment();
            FragmentTransaction transac = manager.beginTransaction();
            transac.add(R.id.fragmentContainer , weatherFrag);
            transac.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_ingredient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("AddIngredientActivity", "onOptionsItemSelected running");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
