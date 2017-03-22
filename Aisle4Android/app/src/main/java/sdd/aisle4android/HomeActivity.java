package sdd.aisle4android;

import android.app.Activity;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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


public class HomeActivity extends AppCompatActivity implements NewListDialog.Listener {
    public static final String MSG_LIST_INDEX = "MsgListIndex";

    private TheApp app;
    private List<String> shopListNames = new ArrayList<>();


    // PUBLIC MODIFIERS

    public void onClickBtnNewList(View v) {
        DialogFragment dialog = new NewListDialog();
        dialog.show(getSupportFragmentManager(), "New List"); // TODO: should this tag be in string res?
    }
    public void onClickBtnPin(View v) {

    }
    public void onClickBtnList(int listIndex) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra(MSG_LIST_INDEX, listIndex);
        startActivity(intent);
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override public void onNewListDialogConfirm(String listName) {
        shopListNames.add(listName);
        app.addShopList(new ShopList(listName));
    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        app = (TheApp)getApplicationContext();

        // Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        // Get Shopping List Names
        for (ShopList list : app.getShopLists()) {
            shopListNames.add(list.getName());
        }

        // List View
        final ListView list = (ListView)findViewById(R.id.home_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, shopListNames);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickBtnList(position);
            }
        });
    }
}






