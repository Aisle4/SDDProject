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



public class RenameListDialog extends DialogFragment {
    private EditText editText;
    private Button posBtn;
    private Button negBtn;
    private int index;

    public interface Listener {
        void onRenameListDialogConfirm(String itemName, int index);
    }

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
            public void afterTextChanged(Editable s) { }
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

    }

}