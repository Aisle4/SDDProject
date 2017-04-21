package sdd.aisle4android.Model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import sdd.aisle4android.R;

/**
 * Created by cameron on 3/8/17.
 */


public class AddItemDialog extends DialogFragment {
    //text box for user input which allows for autocomplete menus
    private AutoCompleteTextView editText;
    private Button posBtn;

    public interface Listener {
        void onAddItemDialogConfirm(String itemName);
    }

    //pull array from FoodNameManager which contains item name suggestions for the dropdown menu (autocomplete)
    private static final String[] FOODS = new FoodNameManager().getFoodNames();

    /**
     * Create the add item dialogue and attach corresponding buttons with a user input fields
     * @param savedInstanceState
     * @return
     */
    @Override @NonNull public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_item, null);

        editText = (AutoCompleteTextView)view.findViewById(R.id.dialog_add_item_input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                onItemNameUpdated(s.toString());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setMessage(R.string.dialog_add_item_heading);

        //must pass the FOODS array (suggestions) to get the auto complete drop down menu
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, FOODS);
        editText.setAdapter(arrayAdapter);
        editText.setThreshold(1);

        //create add button
        builder.setPositiveButton(R.string.btn_add_item_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        //create done button
        builder.setNegativeButton(R.string.btn_add_item_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        return builder.create();
    }
    @Override public void onStart() {
        super.onStart();
        final AlertDialog dialog = (AlertDialog)getDialog();
        if(dialog != null) {
            posBtn = (Button)dialog.getButton(Dialog.BUTTON_POSITIVE);
            posBtn.setEnabled(false);
            posBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itemName = editText.getText().toString().trim();
                    Listener listener = (Listener)getActivity();
                    listener.onAddItemDialogConfirm(itemName);
                    editText.setText("");
                }
            });
        }
    }

    /**
     * enables the "add" button in the add item dialogue when the input field is not empty
     * if it is empty the button is disabled (grayed out)
     * @param name
     */
    private void onItemNameUpdated(String name) {
        if (name.equals("")) {
            // Invalid Name
            if (posBtn.isEnabled()) posBtn.setEnabled(false);
        }
        else {
            // Valid Name
            if (!posBtn.isEnabled()) posBtn.setEnabled(true);
        }
    }
}