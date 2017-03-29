package sdd.aisle4android;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.Menu;


public class HomeActivity extends AppCompatActivity
        implements NewListDialog.Listener, RenameListDialog.Listener {
    public static final String MSG_LIST_INDEX = "MsgListIndex";

    private TheApp app;
    private Shopper shopper;
    private ListArrayAdapter listArrayAdapter;


    // PUBLIC MODIFIERS

    public void onClickBtnNewList(View v) {
        DialogFragment dialog = new NewListDialog();
        dialog.show(getSupportFragmentManager(), "New List"); // TODO: should this tag be in string res?
    }
    public void onClickBtnPin(View v) {

    }
    public void onClickBtnList(int listIndex) {
        goToListScreen(listIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public void onNewListDialogConfirm(String listName) {
        shopper.addShopList(new ShopList(listName));
        listArrayAdapter.notifyDataSetChanged();
        goToListScreen(shopper.getShopLists().size()-1);
    }
    public void onRenameListDialogConfirm(String listName, int index) {
        shopper.getShopList(index).rename(listName);
        listArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.home_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(shopper.getShopList(info.position).getName());
            menu.add(Menu.NONE, 0, 0, R.string.rename_list);
            menu.add(Menu.NONE, 1, 1, R.string.delete_list);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case 0: // Rename
                openRenameDialog(info.position);
                break;
            case 1: // Delete
                shopper.removeShopList(info.position);
                listArrayAdapter.notifyDataSetChanged();
                break;
        }

        return true;
    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        app = (TheApp)getApplicationContext();
        shopper = app.getShopper();

        // Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        // List View
        listArrayAdapter = new ListArrayAdapter(this, shopper.getShopLists());

        ListView list = (ListView)findViewById(R.id.home_list);
        list.setAdapter(listArrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickBtnList(position);
            }
        });
        registerForContextMenu(list);
    }
    @Override
    protected void onStart() {
        super.onStart();
        listArrayAdapter.notifyDataSetChanged();
        //populateList();
    }
    private void goToListScreen(int listIndex) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(MSG_LIST_INDEX, listIndex);
        startActivity(intent);
    }
    private void openRenameDialog(int listIndex) {
        RenameListDialog dialog = new RenameListDialog();
        Bundle info = new Bundle();
        info.putInt("INDEX", listIndex); // TODO: can we not just pass to the constructor of RenameListDialog?
        dialog.setArguments(info);
        dialog.show(getSupportFragmentManager(), "Rename List"); // TODO: should this tag be in string res?
    }

}






