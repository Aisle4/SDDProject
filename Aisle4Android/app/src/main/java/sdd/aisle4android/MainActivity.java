package sdd.aisle4android;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;



public class MainActivity extends AppCompatActivity implements NewListDialog.NewListDialogListener{

    String[] arrayOfLists = {"List One","List Two","List Three","List Four","List Five","List Six","List Seven"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Shopping lists");
        setSupportActionBar(toolbar);



        //Setup buttons
        Button newList = (Button)findViewById(R.id.newList);
        Button anotherButton = (Button)findViewById(R.id.anotherButton);
        newList.setOnClickListener(clickListener);
        anotherButton.setOnClickListener(clickListener);


        //Setup ListView

        ListView lv = (ListView)findViewById(R.id.lists);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayOfLists);
        lv.setAdapter(arrayAdapter);
        //lv.setTextFilterEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View button) {
            switch(button.getId()){
                case R.id.newList:
                    newList();
                    break;
                case R.id.anotherButton:
                    anotherButton();
                    break;
            }
        }
    };



    private void newList(){
        DialogFragment newList = new NewListDialog();
        newList.show(getSupportFragmentManager(), "New List");

    }

    private void anotherButton(){
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText("anotherButton");
    }

    @Override
    public void onDialogPositiveClick(String listName) {
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText(listName);
    }

//    @Override
//    public void onDialogNegativeClick(DialogFragment dialog) {
//
//    }





}






