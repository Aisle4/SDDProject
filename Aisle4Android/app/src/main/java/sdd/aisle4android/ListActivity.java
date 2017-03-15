package sdd.aisle4android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ListActivity extends AppCompatActivity implements NewListDialog.Listener {
    private TheApp app;
    private List<String> shopItemNames = new ArrayList<>();
    private ShopList shopList;


    // PUBLIC MODIFIERS

    public void onClickBtnAddItem(View v) {
        DialogFragment dialog = new NewListDialog();
        dialog.show(getSupportFragmentManager(), "Add Item"); // TODO: should this tag be in string res?
    }
    public void onClickBtnShop(View v) {

    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override public void onNewListDialogConfirm(String listName) {
//        shopItemNames.add(listName);
    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override protected void onCreate(Bundle savedInstanceState) {
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
        Toolbar toolbar = (Toolbar)findViewById(R.id.list_toolbar);
        toolbar.setTitle(shopList.getName());
        setSupportActionBar(toolbar);

        // List
        ListView list = (ListView)findViewById(R.id.list_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, shopItemNames);
        list.setAdapter(arrayAdapter);
    }
}






