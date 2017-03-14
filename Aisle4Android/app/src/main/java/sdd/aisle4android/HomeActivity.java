package sdd.aisle4android;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity implements NewListDialog.Listener {
    private List<String> shopLists = new ArrayList<>();

    // PUBLIC MODIFIERS

    public void onClickBtnNewList(View v) {
        DialogFragment dialog = new NewListDialog();
        dialog.show(getSupportFragmentManager(), "New List");
    }
    public void onClickBtnPin(View v) {

    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override public void onNewListDialogConfirm(String listName) {
        shopLists.add(listName);
    }


    // PRIVATE / PROTECTED MODIFIERS

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        // List
        ListView list = (ListView)findViewById(R.id.home_list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, shopLists);
        list.setAdapter(arrayAdapter);

        // Lower Panel
        // Button btnNewList = (Button)findViewById(R.id.home_btn_new_list);
        // Button btnPin = (Button)findViewById(R.id.home_btn_pin);
    }
}






