package sdd.aisle4android;

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
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by cameron on 3/8/17.
 */


public class AddItemDialog extends DialogFragment {
    private EditText editText;
    private Button posBtn;

    public interface Listener {
        void onAddItemDialogConfirm(String itemName);
    }

    @Override @NonNull public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_item, null);

        editText = (EditText)view.findViewById(R.id.dialog_add_item_input);
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

        builder.setPositiveButton(R.string.btn_add_item_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
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
                    String itemName = editText.getText().toString();
                    Listener listener = (Listener)getActivity();
                    listener.onAddItemDialogConfirm(itemName);
                    editText.setText("");
                }
            });
        }
    }
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