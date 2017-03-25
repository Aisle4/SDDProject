package sdd.aisle4android;

import android.app.Activity;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity
        implements NewListDialog.Listener, EditListDialog.Listener {
    public static final String MSG_LIST_INDEX = "MsgListIndex";

    private TheApp app;
    private ArrayAdapter<String> listArrayAdapter;


    // PUBLIC MODIFIERS

//    @Override
//    public void onEvent(Event e) {
//        if (e == app.eventListsChanged) {
//            populateList();
//        }
//    }

    public void onClickBtnNewList(View v) {
        DialogFragment dialog = new NewListDialog();
        dialog.show(getSupportFragmentManager(), "New List"); // TODO: should this tag be in string res?
    }
    public void onClickBtnPin(View v) {

    }
    public void onClickBtnList(int listIndex) {
        goToListScreen(listIndex);
    }
    public void onLongClickBtnList(int listIndex) {
        DialogFragment editDialog = new EditListDialog();
        Bundle info = new Bundle();
        info.putInt("INDEX", listIndex); // TODO: can we not just pass to the constructor of EditListDialog?
        editDialog.setArguments(info);
        editDialog.show(getSupportFragmentManager(), "Edit List"); // TODO: should this tag be in string res?
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public void onNewListDialogConfirm(String listName) {
        app.addShopList(new ShopList(listName));
        populateList();
        goToListScreen(app.getShopLists().size()-1);
    }
    public void onEditListDialogConfirm(String listName, int index) {
        app.getShopList(index).rename(listName);
        populateList();
    }
    public void onEditListDialogDelete(int index) {
        app.removeShopList(index);
        populateList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.list_delete:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        app = (TheApp)getApplicationContext();

        // Events
//        app.eventListsChanged.attach(this);

        // Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        // List View
        listArrayAdapter = new ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1);
        populateList();

        final ListView list = (ListView)findViewById(R.id.home_list);
        list.setAdapter(listArrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickBtnList(position);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                onLongClickBtnList(position);
                return true;
            }
        });
    }
    private void populateList() {
        listArrayAdapter.clear();
        for (ShopList list : app.getShopLists()) {
            listArrayAdapter.add(list.getName());
        }
    }
    private void goToListScreen(int listIndex) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(MSG_LIST_INDEX, listIndex);
        startActivity(intent);
    }

}






