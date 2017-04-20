package sdd.aisle4android;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by cameron on 3/8/17.
 */

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;


public class NewListDialog extends DialogFragment {
    private EditText editText;
    private Button posBtn;

    public interface Listener {
        void onNewListDialogConfirm(String listName);
    }

    @Override @NonNull public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_new_list, null);

        editText = (EditText)view.findViewById(R.id.dialog_new_list_input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                onListTitleUpdated(s.toString());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setMessage(R.string.btn_new_list);

        builder.setPositiveButton(R.string.btn_new_list_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String listTitle = editText.getText().toString();
                Listener listener = (Listener)getActivity();
                listener.onNewListDialogConfirm(listTitle);
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
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
        }
    }
    private void onListTitleUpdated(String title) {
        if (title.equals("")) {
            // Invalid Title
            if (posBtn.isEnabled()) posBtn.setEnabled(false);
        }
        else {
            // Valid Title
            if (!posBtn.isEnabled()) posBtn.setEnabled(true);
        }
    }
}