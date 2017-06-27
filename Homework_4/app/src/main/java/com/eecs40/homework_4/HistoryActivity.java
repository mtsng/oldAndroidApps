package com.eecs40.homework_4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Michael on 6/2/2015.
 */
public class HistoryActivity extends ActionBarActivity implements ActionMode.Callback{
    private ArrayList<PlacebookEntry> mPlacebookEntries;
    private ListView mListview ;
    protected Object mActionMode ;
    public static int selectedItem = -1;
    public static final int RESULT_EDIT = 2;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_history);
        mPlacebookEntries = intent.getParcelableArrayListExtra(MainActivity.VIEW_ALL_KEY);
        mListview = (ListView) findViewById(R.id.listview);

        PlacebookAdapter adapter = new PlacebookAdapter(this, mPlacebookEntries);
        mListview.setAdapter(adapter);

        mListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                if (mActionMode != null)
                    return false;
                selectedItem = position;
                mActionMode = HistoryActivity.this.startActionMode(HistoryActivity.this);
                view.setSelected(true);
                return true ;
            }
        });
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu ) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.rowselection, menu);
        return true ;
    }

    @Override
    public boolean onPrepareActionMode (ActionMode mode, Menu menu) {
        return false ;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_place :
                // Delete Item
                mPlacebookEntries.remove(selectedItem);
                MainActivity.deleting = true;
                mode.finish();
                return true ;
            case R.id.action_edit_place:
                Toast.makeText(getApplication(), "Edit", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(MainActivity.VIEW_ALL_KEY, mPlacebookEntries);
                setResult(RESULT_EDIT, intent);
                finish();
                return true;
            default: return false;
        }
    }

    public void onDestroyActionMode (ActionMode mode) {
        mActionMode = null;
        selectedItem = -1;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplication(), "Back", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                //send arraylist back to main onactivityresult
                intent.putParcelableArrayListExtra(MainActivity.VIEW_ALL_KEY, mPlacebookEntries);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            default:
                System.out.println("sh!t");
                return super.onOptionsItemSelected (item);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //System.out.println("obliterated");
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        intent.putParcelableArrayListExtra(MainActivity.VIEW_ALL_KEY, mPlacebookEntries);
        finish();
    }

}
