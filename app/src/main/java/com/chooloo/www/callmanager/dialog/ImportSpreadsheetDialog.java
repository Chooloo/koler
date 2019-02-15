package com.chooloo.www.callmanager.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.BaseDialogFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.util.Utilities;
import com.chooloo.www.callmanager.util.validation.Validator;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.chooloo.www.callmanager.util.Utilities.askForPermissions;
import static com.chooloo.www.callmanager.util.Utilities.checkPermissionGranted;

public class ImportSpreadsheetDialog extends BaseDialogFragment<ImportSpreadsheetDialog.Builder> implements FileChooserDialog.FileCallback {

    public static final String TAG = "import_spreadsheet";

    @BindView(R.id.edit_name) TextInputEditText mEditName;
    @BindView(R.id.edit_path) TextInputEditText mEditPath;
    @BindView(R.id.edit_name_index) TextInputEditText mEditNameIndex;
    @BindView(R.id.edit_number_index) TextInputEditText mEditNumberIndex;

    @BindView(R.id.button_choose_file) ImageView mChooseFileButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        //Let's create the custom view
        View customView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_import_spreadsheet, null, false);
        ButterKnife.bind(this, customView);

        MaterialDialog.SingleButtonCallback onPositive = (dialog, which) -> {

            if (!Validator.validateName(mEditName.getText().toString()) ||
                    !Validator.validateColumnIndex(mEditNameIndex.getText().toString()) ||
                    !Validator.validateColumnIndex(mEditNumberIndex.getText().toString())) {
                Utilities.vibrate(getContext(), Utilities.LONG_VIBRATE_LENGTH);
                return;
            }

            if (mBuilder.onImportListener != null) {
                String name = mEditName.getText().toString();
                File excelFile = new File(mEditPath.getText().toString());
                int nameIndex = Integer.parseInt(mEditNameIndex.getText().toString());
                int numberIndex = Integer.parseInt(mEditNumberIndex.getText().toString());

                mBuilder.onImportListener.onImport(
                        new CGroup(name),
                        excelFile,
                        nameIndex,
                        numberIndex);
            }
            dismiss();
        };

        return new MaterialDialog.Builder(getContext())
                .customView(customView, false)
                .title("Import contacts from spreadsheet")
                .positiveText("Import")
                .negativeText("Cancel")
                .autoDismiss(false)
                .onPositive(onPositive)
                .build();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Utilities.PERMISSION_RC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Handler handler = new Handler();
                handler.postDelayed(() -> mChooseFileButton.performClick(), 1000);
            } else {
                Toast.makeText(
                        getContext(),
                        "The folder or file chooser will not work without "
                                + "permission to read external storage.",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mEditPath.getText().toString().isEmpty()) {
            showFileChooser();
        }
    }

    @OnClick(R.id.button_choose_file)
    public void chooseFile(View view) {
        showFileChooser();
    }

    private void showFileChooser() {
        if (!checkPermissionGranted(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            askForPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            return;
        }

        new FileChooserDialog.Builder(getFragmentManager())
                .onFileSelected(this)
                .extensionsFilter(".xls")
                .goUpLabel("...")
                .show(new FileChooserDialog());
    }

    @Override
    public void onFileSelection(FileChooserDialog dialog, File file) {
        mEditPath.setText(file.getPath());
    }

    @Override
    public void onFileChooserDismissed(FileChooserDialog dialog) {

    }

    @OnTextChanged(value = R.id.edit_name, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onEditField(Editable editable) {
        if (!Validator.validateName(editable.toString())) {
            mEditName.setError(getString(R.string.error_name));
        }
    }

    @OnTextChanged(value = R.id.edit_number_index, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onEditNumberIndex(Editable editable) {
        validateColumnIndex(editable, mEditNumberIndex);
    }

    @OnTextChanged(value = R.id.edit_name_index, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onEditNameIndex(Editable editable) {
        validateColumnIndex(editable, mEditNameIndex);
    }

    private void validateColumnIndex(Editable editable, TextInputEditText view) {
        if (!Validator.validateColumnIndex(editable.toString())) {
            view.setError(getString(R.string.error_column_index));
        }
    }

    public interface OnImportListener {
        void onImport(CGroup list, File excelFile, int nameColIndex, int numberColIndex);
    }

    public static class Builder extends BaseDialogFragment.Builder {

        protected OnImportListener onImportListener;

        public Builder(FragmentManager fragmentManager) {
            super(fragmentManager);
            tag = TAG;
        }

        public Builder onImportListener(OnImportListener listener) {
            onImportListener = listener;
            return this;
        }
    }
}
