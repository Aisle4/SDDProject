package sdd.aisle4android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class ListActivity extends AppCompatActivity
        implements AddItemDialog.Listener, RenameListDialog.Listener {
    private TheApp app;
    private ShopList shopList;

    private Toolbar toolbar;
    private MenuItem timerItem;
    private ArrayAdapter<String> listArrayAdapter;

    private Handler timerHandler;
    private Runnable timerUpdater = new Runnable() {
        @Override
        public void run() {
            String timeStr = secondsToString((int)(app.getShoppingTime() / 1000));
            timerItem.setTitle(timeStr);
            timerHandler.postDelayed(this, 1000);
        }
    };


    // PUBLIC MODIFIERS

    public void onClickBtnAddItem(View v) {
        DialogFragment dialog = new AddItemDialog();
        dialog.show(getSupportFragmentManager(), "Add Item"); // TODO: should this tag be in string res?
    }
    public void onClickBtnShop(View v) {
        if (!app.isShopping()) app.startShopping(shopList);
        else app.endShopping();

        Button btn = (Button)v;
        btn.setText(app.isShopping() ? R.string.btn_stop_shop : R.string.btn_shop);

        if (app.isShopping()) {
            // Update timer every second
            timerHandler.post(timerUpdater);
        }
        else {
            timerHandler.removeCallbacks(timerUpdater);
            toolbar.setTitle(shopList.getName());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_toolbar, menu);

        // Timer
        timerItem = menu.findItem(R.id.list_toolbar_timer);
        timerHandler = new Handler();

        return true;
    }
    @Override
    public void onAddItemDialogConfirm(String itemName) {
        shopList.addItem(new ShopItem(itemName));
        populateList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.list_toolbar_rename:
                openRenameDialog();
                return true;

            case R.id.list_toolbar_delete:
                app.removeShopList(shopList);
                goToHomeScreen();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.list_list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(shopList.getItem(info.position).getName());
            menu.add(Menu.NONE, 0, 0, R.string.delete_item);
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case 0: // Delete
                shopList.removeItem(info.position);
                populateList();
                break;
        }

        return true;
    }
    @Override
    public void onRenameListDialogConfirm(String listName, int index) {
        shopList.rename(listName);
        updateToolbarTitle();
    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        app = (TheApp)getApplicationContext();

        // Get list title from HomeActivity
        Intent intent = getIntent();
        int listIndex = intent.getIntExtra(HomeActivity.MSG_LIST_INDEX, -1);
        if (listIndex < 0 || listIndex > app.getShopLists().size()) {
            // TODO: error handling
            Log.e("error", "Invalid ShopList index on ListActivity Create");
        }
        shopList = app.getShopList(listIndex);

        // Toolbar
        toolbar = (Toolbar)findViewById(R.id.list_toolbar);
        updateToolbarTitle();
        setSupportActionBar(toolbar);

        // List View
        listArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1);
        populateList();
        ListView list = (ListView)findViewById(R.id.list_list);
        list.setAdapter(listArrayAdapter);
        registerForContextMenu(list);
    }
    @Override
    protected void onStart() {
        super.onStart();
        updateToolbarTitle();
        populateList();
    }
    private void populateList() {
        listArrayAdapter.clear();
        for (ShopItem item : shopList.getItems()) {
            listArrayAdapter.add(item.getName());
        }
    }
    private void goToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    private void openRenameDialog() {
        RenameListDialog dialog = new RenameListDialog();
        Bundle info = new Bundle();
        info.putInt("INDEX", -1);

        // TODO: have rename dialog actually change the model?
        // It's weird to pass an idex to the dialog as the dialog doesnt need it,
        // and in this case, neither does the callback...

        dialog.setArguments(info);
        dialog.show(getSupportFragmentManager(), "Rename List"); // TODO: should this tag be in string res?
    }
    private void updateToolbarTitle() {
        toolbar.setTitle(shopList.getName());
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60); // TODO: locale
    }
}






