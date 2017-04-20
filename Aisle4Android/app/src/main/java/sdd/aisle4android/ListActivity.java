package sdd.aisle4android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


public class ListActivity extends AppCompatActivity
        implements AddItemDialog.Listener, RenameListDialog.Listener, ShopList.IEarOrderChanged,
        DataCollector.IEarDataRecorded {
    private TheApp app;
    private Shopper shopper;
    private ShopList shopList;

    private Toolbar toolbar;
    private MenuItem timerItem;
    private ItemArrayAdapter listArrayAdapter;

    private Handler timerHandler;
    private Runnable timerUpdater = new Runnable() {
        @Override
        public void run() {
            String timeStr = secondsToString((int)(shopper.getShoppingTime() / 1000));
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
        if (!shopper.isShopping()) shopper.startShopping(shopList);
        else shopper.endShopping();

        Button btn = (Button)v;
        btn.setText(shopper.isShopping() ? R.string.btn_stop_shop : R.string.btn_shop);

        if (shopper.isShopping()) {
            // Started shopping
            timerHandler.post(timerUpdater); // Update timer every second
        }
        else {
            // Ended shopping
            timerHandler.removeCallbacks(timerUpdater);
            toolbar.setTitle(shopList.getName());

            showSaveDataPopup();
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
        shopList.addItem(new ShopItem(itemName, this));
        listArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;

            case R.id.list_toolbar_rename:
                openRenameDialog();
                return true;

            case R.id.list_toolbar_delete:
                shopper.removeShopList(shopList);
                goToHomeScreen();
                return true;

            case R.id.list_sort_name:
                shopList.sortAlphabetical();
                listArrayAdapter.notifyDataSetChanged();
                return true;

            case R.id.list_sort_added:
                shopList.sortChronological();
                listArrayAdapter.notifyDataSetChanged();
                return true;

            case R.id.list_uncheck_all:
                shopList.unmarkAllItems();
                listArrayAdapter.notifyDataSetChanged();
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
            if(shopList.getItem(info.position).isCollected()){
                menu.add(Menu.NONE, 1, 0, R.string.uncheck_item);
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        switch (item.getItemId()) {
            case 0: // Delete
                shopList.removeItem(info.position);
                listArrayAdapter.notifyDataSetChanged();
                break;
            case 1: // Uncollect
                shopList.getItem(info.position).setCollected(false);
                listArrayAdapter.notifyDataSetChanged();
                break;
        }

        return true;
    }
    @Override
    public void onRenameListDialogConfirm(String listName, int index) {
        shopList.rename(listName);
        updateToolbarTitle();
    }
    @Override
    public void onOrderChanged(ShopList list) {
        listArrayAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDataRecorded(ItemToItemData data) {
        showDataCollectedMessage(data);
    }



    // PRIVATE / PROTECTED MODIFIERS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        app = (TheApp)getApplicationContext();
        shopper = app.getShopper();

        // Get list index from HomeActivity
        Intent intent = getIntent();
        int listIndex = intent.getIntExtra(HomeActivity.MSG_LIST_INDEX, -1);
        if (listIndex < 0 || listIndex > shopper.getShopLists().size()) {
            // TODO: error handling
            Log.e("error", "Invalid ShopList index on ListActivity Create");
        }
        shopList = shopper.getShopList(listIndex);

        // Shopping events
        shopList.eventOrderChanged.attach(this);
        app.getDataCollector().eventDataRecorded.attach(this);

        // Toolbar
        toolbar = (Toolbar)findViewById(R.id.list_toolbar);
        updateToolbarTitle();
        setSupportActionBar(toolbar);

        // List View
        listArrayAdapter = new ItemArrayAdapter(this, shopList.getItems());
        ListView list = (ListView)findViewById(R.id.list_list);
        list.setAdapter(listArrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shopList.getItem(position).setCollected(true);
                listArrayAdapter.notifyDataSetChanged();
            }
        });
        registerForContextMenu(list);
    }
    @Override
    protected void onStart() {
        super.onStart();
        updateToolbarTitle();
        listArrayAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        dettachEvents();
        super.onDestroy();
    }

    private void goToHomeScreen() {
        dettachEvents();
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
    private void showDataCollectedMessage(ItemToItemData data) {
        String seconds = String.valueOf(data.timeMs / 1000);
        String fromItem = data.item1Name == null || data.item1Name.equals("") ? "entrance" : data.item1Name;
        String msg = data.steps + " steps   " + seconds + " sec   from " + fromItem;

        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
    }
    private void showSaveDataPopup() {
        // Define response
        View.OnClickListener onYesResponse = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.getDataCollector().saveDataToDataBase();
            }
        };

        // Create popup
        Snackbar.make(findViewById(android.R.id.content), R.string.save_data_question, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.yes, onYesResponse)
                .show();
    }

    private void dettachEvents() {
        shopList.eventOrderChanged.dettach(this);
        app.getDataCollector().eventDataRecorded.dettach(this);
    }

    private String secondsToString(int pTime) {
        return String.format("%02d:%02d", pTime / 60, pTime % 60); // TODO: locale
    }

}






