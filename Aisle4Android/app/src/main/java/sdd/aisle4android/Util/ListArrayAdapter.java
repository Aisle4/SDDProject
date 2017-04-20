package sdd.aisle4android.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sdd.aisle4android.Model.ShopList;
import sdd.aisle4android.R;


/**
 * Created by cameron on 3/26/17.
 **/

public class ListArrayAdapter extends ArrayAdapter<ShopList> {

    public ListArrayAdapter(Context context, List<ShopList> ShopListList) {
        super(context, 0, ShopListList);
    }

    // creates view to display shoplist in list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // get shoplist to be displayed
        ShopList shoplist = getItem(position);

        // checks if view for shoplist already exists, if not creates a new view
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_list, parent, false);
        }

        // get text view to display list name in
        final TextView itemName = (TextView) convertView.findViewById(R.id.listName);

        // set text view to display list name
        itemName.setText(shoplist.getName());


        // get text view to display list creation date
        final TextView itemDate = (TextView) convertView.findViewById(R.id.listDate);

        // set text view to display list creation date
        itemDate.setText(shoplist.getCreationDate());


        // get text view to display list item count
        final TextView itemCount = (TextView) convertView.findViewById(R.id.itemCount);

        // gets shoplist item count and set text view to display value
        itemCount.setText(String.format("%d items", shoplist.getItems().size()));

        // returns view
        return convertView;

    }
}
