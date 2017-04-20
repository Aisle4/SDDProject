package sdd.aisle4android.Util;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import java.util.List;
import android.graphics.Paint;
import android.graphics.Color;

import sdd.aisle4android.Model.ShopItem;
import sdd.aisle4android.R;



/**
 * Created by cameron on 3/26/17.
 */

public class ItemArrayAdapter extends ArrayAdapter<ShopItem> {


    public ItemArrayAdapter(Context context, List<ShopItem> ShopItemList) {
        super(context, 0, ShopItemList);
    }


    // creates view to display shopitem in list
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get shopitem to be displayed
        ShopItem shopitem = getItem(position);

        // checks if view for shopitem already exists, if not creates a new view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);

        }

        // gets textview to be modified with shopitem information
        final TextView itemName = (TextView) convertView.findViewById(R.id.itemName);

        // set text to be displayed to item name
        itemName.setText(shopitem.getName());

        // if item has been collected strike through text and change color to gray
        if(shopitem.isCollected()){
            itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            itemName.setTextColor(Color.parseColor("#999999"));
        }
        // else remove strike through if exists and change color to black
        else{
            itemName.setPaintFlags(itemName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            itemName.setTextColor(Color.parseColor("#000000"));
        }

        // returns view
        return convertView;

    }

}
