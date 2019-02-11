package com.chooloo.www.callmanager.dialog;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.BaseDialogFragment;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.Contact;
import com.chooloo.www.callmanager.util.Utilities;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImportSpreadsheetDialog extends BaseDialogFragment<ImportSpreadsheetDialog.Builder> implements FileChooserDialog.FileCallback {

    public static final String TAG = "import_spreadsheet";

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

        return new MaterialDialog.Builder(getContext())
                .customView(customView, false)
                .title("Import contacts from spreadsheet")
                .positiveText("Import")
                .negativeText("Cancel")
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
        if (Utilities.checkPermissionGranted(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Utilities.askForPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});

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

    public static class Builder extends BaseDialogFragment.Builder {

        public Builder(FragmentManager fragmentManager) {
            super(fragmentManager);
            tag = TAG;
        }
    }

    class LoadContactsFromSpreadSheet extends AsyncTask<Void, Void, List<Contact>> {

        private File mExcelFile;
        private int mNameColIndex;
        private int mPhoneNumberColIndex;

        public LoadContactsFromSpreadSheet(File excelFile, int nameColIndex, int phoneNumberColIndex) {
            mExcelFile = excelFile;
            mNameColIndex = nameColIndex;
            mPhoneNumberColIndex = phoneNumberColIndex;
        }

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            List<Contact> contacts = new ArrayList<>();
            try {
                Workbook workbook = new HSSFWorkbook(new FileInputStream(mExcelFile));
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    String name = row.getCell(mNameColIndex).getStringCellValue();
                    String phoneNumber = row.getCell(mPhoneNumberColIndex).getStringCellValue();
                    Contact contact = new Contact(name, phoneNumber, null);
                    contacts.add(contact);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contacts;
        }
    }
}
