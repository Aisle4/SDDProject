package sdd.aisle4android;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cameron on 3/26/17.
 **/

public class ListArrayAdapter extends ArrayAdapter<ShopList> {

    private Context context;

    public ListArrayAdapter(Context context, List<ShopList> ShopListList) {
        super(context, 0, ShopListList);
        context = this.context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShopList shoplist = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_list, parent, false);
        }

        final TextView itemName = (TextView) convertView.findViewById(R.id.listName);
        itemName.setText(shoplist.getName());

        final TextView itemDate = (TextView) convertView.findViewById(R.id.listDate);
        itemDate.setText(shoplist.getCreationDate());


        return convertView;

    }
}
