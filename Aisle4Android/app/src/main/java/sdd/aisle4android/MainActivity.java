package sdd.aisle4android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.Menu;


public class MainActivity extends AppCompatActivity {

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
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText("newList");
    }

    private void anotherButton(){
        TextView text = (TextView)findViewById(R.id.textView);
        text.setText("anotherButton");
    }

}


