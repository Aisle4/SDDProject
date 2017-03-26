package sdd.aisle4android;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import java.util.List;
import android.graphics.Paint;



/**
 * Created by cameron on 3/26/17.
 */

public class ItemArrayAdapter extends ArrayAdapter<ShopItem> {

    public ItemArrayAdapter(Context context, List<ShopItem> ShopItemList) {
        super(context, 0, ShopItemList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopItem shopitem = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final TextView itemName = (TextView) convertView.findViewById(R.id.itemName);

        itemName.setText(shopitem.getName());
        if(shopitem.getCollected()){
            itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else{
            itemName.setPaintFlags(0);
        }

        return convertView;

    }


}
