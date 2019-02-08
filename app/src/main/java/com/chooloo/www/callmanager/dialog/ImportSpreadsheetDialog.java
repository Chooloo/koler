package com.chooloo.www.callmanager.dialog;

import android.content.Context;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chooloo.www.callmanager.R;
import com.chooloo.www.callmanager.database.Contact;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportSpreadsheetDialog {

    public static void showDialog(@NotNull Context context) {
        new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_import_spreadsheet, false)
                .title("Import contacts from spreadsheet")
                .positiveText("Import")
                .negativeText("Cancel")
                .show();
    }

    class LoadContactsFromSpreadSheet extends AsyncTask<Void, Void, List<Contact>> {

        private String mFilePath;
        private int mNameColIndex;
        private int mPhoneNumberColIndex;

        public LoadContactsFromSpreadSheet(String filePath, int nameColIndex, int phoneNumberColIndex) {
            mFilePath = filePath;
            mNameColIndex = nameColIndex;
            mPhoneNumberColIndex = phoneNumberColIndex;
        }

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            List<Contact> contacts = new ArrayList<>();
            try {
                Workbook workbook = new HSSFWorkbook(new FileInputStream(mFilePath));
                Sheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.rowIterator();

                while(rowIterator.hasNext()) {
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
