package com.whiteout.pantrytracker.fragments;

import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.whiteout.pantrytracker.R;
import com.whiteout.pantrytracker.adapters.ItemListAdapter;
import com.whiteout.pantrytracker.data.PantryDataSource;
import com.whiteout.pantrytracker.data.model.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.whiteout.pantrytracker.activities.AddIngredientActivity;
import com.whiteout.pantrytracker.barcode.*;



/**
 * Author:  Kendrick Cline
 * Date:    10/29/14
 * Email:   kdecline@gmail.com
 */
public class ItemListFragment extends Fragment {

    ListView mListView;
    ItemListAdapter mAdapter;

    PantryDataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Give it an options menu! (they resist sometimes)
        setHasOptionsMenu(true);
        setRetainInstance(true);
        dataSource = new PantryDataSource(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.item_list_fragment, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null) {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mListView = (ListView) view.findViewById(R.id.listView);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(mListView);
        } else {
            mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            mListView.setMultiChoiceModeListener(actionModeListener);
        }

        mAdapter = new ItemListAdapter(getActivity(), R.layout.item_item, new ArrayList<Item>());
        new FetchItemsTask().execute();
        mListView.setAdapter(mAdapter);


        return view;
    }


    AbsListView.MultiChoiceModeListener actionModeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) { }

        @Override
        public boolean onCreateActionMode(ActionMode  mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.item_list_action_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            ItemListAdapter adapter = (ItemListAdapter) mListView.getAdapter();

            switch (item.getItemId()) {

                case R.id.action_new:
                    Toast.makeText(getActivity().getApplicationContext(), "Hey!", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                case R.id.action_delete:
                    for (int i = adapter.getCount() - 1; i >= 0; i--) {
                        if (mListView.isItemChecked(i)) {
                            // Remove element from list
                            // Asynchronously delete from Database
                            adapter.deletePosition(i);
                            new DeleteItemTask().execute(Float.valueOf(String.valueOf(i)));
                            break;
                        }
                    }
                    mode.finish();
                    return true;
                case R.id.action_edit:
                    // TODO JOSH
                    mode.finish();
                    return true;
                case R.id.action_refresh:
                    // Asynchronously re-load Items and return
                    Log.d("Kenny", "Refreshing...");
                    new FetchItemsTask().execute();
                    mode.finish();
                    return true;
                case R.id.action_delete_all:
                    // TODO make warning
                    // Update UI then asynchronously wipe the database
                    adapter.clear();
                    adapter.notifyDataSetChanged();
                    new DeleteAllItemsTask().execute();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.item_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;
            case R.id.action_new:
                return true;
            case R.id.action_refresh:
                // Asynchronously re-load Items and return
                Log.d("Kenny", "Refreshing...");
                new FetchItemsTask().execute();
                return true;
            case R.id.action_delete_all:
                // TODO make warning
                Log.d("Kenny", "Deleting all...");
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
                new DeleteAllItemsTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... params) {
            List<Item> items = null;
            try {
                dataSource.open();
                items = dataSource.getAllItems();

            } catch (SQLException e) {
                Toast.makeText(getActivity(), "Error loading your Pantry Items", Toast.LENGTH_LONG).show();
            } finally {
                dataSource.close();
            }
            return items;
        }

        @Override
        protected void onPostExecute(List<Item> items) {
            super.onPostExecute(items);
            Log.d("Kenny", "Finished loading " + items.size() + " items");
            mAdapter.addAll(items);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class DeleteItemTask extends AsyncTask<Float, Void, Void> {

        @Override
        protected Void doInBackground(Float... params) {
            if (params.length > 0) {
                try {
                    dataSource.open();
                    for (Float param : params) {
                        dataSource.deleteItem(param);
                    }
                } catch (SQLException e) {
                    Toast.makeText(getActivity(), "Error removing item from DB", Toast.LENGTH_LONG).show();
                } finally {
                    dataSource.close();
                }
            }
            return null;
        }
    }

    private class DeleteAllItemsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                dataSource.open();
                Log.d("Kenny", "Delete all items...");
                dataSource.deleteAllItems();

            } catch (SQLException e) {
                Toast.makeText(getActivity(), "Error opening database", Toast.LENGTH_LONG).show();
            } finally {
                dataSource.close();
            }
            return null;
        }
    }

}
