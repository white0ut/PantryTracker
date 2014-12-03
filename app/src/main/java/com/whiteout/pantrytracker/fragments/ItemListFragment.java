package com.whiteout.pantrytracker.fragments;

import android.app.Activity;
import android.content.Intent;
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
import com.whiteout.pantrytracker.activities.MainActivity;
import com.whiteout.pantrytracker.adapters.ItemListAdapter;
import com.whiteout.pantrytracker.data.PantryDataSource;
import com.whiteout.pantrytracker.data.model.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.whiteout.pantrytracker.activities.AddIngredientActivity;


/**
 * Author:  Kendrick Cline
 * Date:    10/29/14
 * Email:   kdecline@gmail.com
 */
public class ItemListFragment extends Fragment implements ItemListAdapter.ItemListAdapterCallbacks {


    ListView mListView;
    ItemListAdapter mAdapter;

    PantryDataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Give it an options menu! (they resist sometimes)
        setHasOptionsMenu(true);
        setRetainInstance(true);
        dataSource = ((MainActivity)getActivity()).getDataSource();
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

        mAdapter.attachCallbacks(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        mAdapter.detachCallbacks();
        super.onDestroyView();
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
                    for (int i = adapter.getCount() - 1; i >= 0; i--) {
                        if (mListView.isItemChecked(i)) {
                            // Edit item
                            Item editItem = (Item) mListView.getItemAtPosition(i);
                            Intent intent = itemToIntent(editItem);
                            intent.putExtra(AddIngredientFragment.KEY_INDEX, i);
                            intent.putExtra(AddIngredientFragment.KEY_ID, editItem.getId());
                            Log.d("IDProblem", "ItemListFrag: " + editItem.getId());

                            intent.putExtra(AddIngredientFragment.KEY_REQUESTCODE, AddIngredientFragment.REQUEST_CODE_EXISTING);
                            intent.setClass(getActivity(), AddIngredientActivity.class);
                            startActivityForResult(intent, AddIngredientFragment.REQUEST_CODE_EXISTING);
                            break;
                        }
                    }
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

        /**
         * Put all elements of Item as extras in an Intent
         */
        private Intent itemToIntent(Item item){
            Intent intent = new Intent();
            intent.putExtra(AddIngredientFragment.KEY_NAME, item.getName());
            intent.putExtra(AddIngredientFragment.KEY_QUANTITY, item.getQuantity());
            intent.putExtra(AddIngredientFragment.KEY_DATE, item.getExpiration());
            intent.putExtra(AddIngredientFragment.KEY_UNIT, item.getUnit());
            intent.putExtra(AddIngredientFragment.KEY_ID, item.getId());
            return intent;
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
                Intent intent = new Intent(this.getActivity(), AddIngredientActivity.class);
                intent.putExtra(AddIngredientFragment.KEY_REQUESTCODE, AddIngredientFragment.REQUEST_CODE_NEW);
                startActivityForResult(intent, AddIngredientFragment.REQUEST_CODE_NEW);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            Item item = new Item();
            item.setName(data.getStringExtra(AddIngredientFragment.KEY_NAME));
            item.setExpiration(data.getLongExtra(AddIngredientFragment.KEY_DATE, 0));
            item.setQuantity(data.getFloatExtra(AddIngredientFragment.KEY_QUANTITY, 0));
            item.setUnit(data.getStringExtra(AddIngredientFragment.KEY_UNIT));


            if(requestCode == AddIngredientFragment.REQUEST_CODE_EXISTING){
                Log.d("ItemListFragment","edit running");

                //TODO update record in database
                try {
                    item.setId(data.getLongExtra(AddIngredientFragment.KEY_ID,5));
                    item.setId(data.getLongExtra(AddIngredientFragment.KEY_ID,1));
                    mAdapter.edit(data.getIntExtra(AddIngredientFragment.KEY_INDEX, 0), item);
                    dataSource.open();
                    dataSource.editItem(item);
                    dataSource.close();
                    mAdapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {
                    mAdapter.add(item);
                    mAdapter.notifyDataSetChanged();
                    dataSource.open();
                    dataSource.addItem(item);
                    dataSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(getActivity(), item.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.item_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemQuantityChanged(Item item) {
        // Asynchronously update the DB
        new UpdateItemTask().execute(item);
    }

    private class UpdateItemTask extends AsyncTask<Item, Void, Item> {

        @Override
        protected Item doInBackground(Item... params) {
            try {
                dataSource.open();
                for (Item param : params) {
                    dataSource.editItem(param);
                }
            } catch (SQLException e) {
                Toast.makeText(getActivity(), "Error editing values", Toast.LENGTH_SHORT).show();
            } finally {
                dataSource.close();
            }
            return null;
        }
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
