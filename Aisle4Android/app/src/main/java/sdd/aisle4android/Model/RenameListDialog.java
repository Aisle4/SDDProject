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
import android.widget.Button;
import android.widget.EditText;

import sdd.aisle4android.R;


public class RenameListDialog extends DialogFragment {
    private EditText editText;
    private Button posBtn;
    private Button negBtn;
    private int index;

    public interface Listener {
        void onRenameListDialogConfirm(String itemName, int index);
    }

    /**
     * Create the rename list dialogue and attach corresponding buttons with a user input fields
     * @param savedInstanceState
     * @return
     */
    @Override @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_rename_list, null);

        editText = (EditText)view.findViewById(R.id.dialog_rename_list_input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) { onListTitleUpdated(s.toString()); }
        });

        index = getArguments().getInt("INDEX");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setMessage(R.string.dialog_rename_list_heading);

        builder.setPositiveButton(R.string.btn_rename_list_positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String newListTitle = editText.getText().toString();
                Listener listener = (Listener)getActivity();
                listener.onRenameListDialogConfirm(newListTitle, index);
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

    /**
     * enables the "rename" button in the rename list dialogue when the input field is not empty
     * if it is empty the button is disabled (grayed out)
     * @param title
     */
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
