package com.chooloo.www.callmanager.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.chooloo.www.callmanager.database.AppDatabase;
import com.chooloo.www.callmanager.database.dao.CGroupDao;
import com.chooloo.www.callmanager.database.dao.ContactDao;
import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.chooloo.www.callmanager.util.Utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;

public class AsyncSpreadsheetImport extends AsyncTask<Void, Integer, List<Contact>> {

    public static final int STATUS_SUCCESSFUL = 0;
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_FILE_NOT_FOUND = 2;

    private Context mContext;
    private File mExcelFile;
    private int mNameColIndex;
    private int mNumberColIndex;

    private int status = STATUS_SUCCESSFUL;

    private CGroup mDatabaseList;

    private OnProgressListener mOnProgressListener;
    private OnFinishListener mOnFinishListener;
    private int mRowCount = 0;

    /**
     * Create an instance of {@link AsyncSpreadsheetImport}.
     *
     * @param context        Needed to access the database.
     * @param excelFile      The file to import contacts from.
     * @param nameColIndex   The contact's name column index.
     * @param numberColIndex The contact's number column index.
     * @param databaseList   A list to import the data into. Can be empty (not in the database).
     */
    public AsyncSpreadsheetImport(@NotNull Context context,
                                  @NotNull File excelFile,
                                  int nameColIndex,
                                  int numberColIndex,
                                  @NotNull CGroup databaseList) {
        mContext = context;
        mExcelFile = excelFile;
        mNameColIndex = nameColIndex;
        mNumberColIndex = numberColIndex;
        mDatabaseList = databaseList;
    }

    public void setOnProgressListener(OnProgressListener listener) {
        mOnProgressListener = listener;
    }

    public void setOnFinishListener(OnFinishListener listener) {
        mOnFinishListener = listener;
    }

    @Override
    protected List<Contact> doInBackground(Void... voids) {

        long listId = mDatabaseList.getListId();
        AppDatabase db = AppDatabase.getDatabase(mContext);

        //TODO use a repository
        CGroupDao cgroupDao = db.getCGroupDao();
        if (listId == 0) { //If this list isn't in the database
            listId = cgroupDao.insert(mDatabaseList);
        }

        List<Contact> contacts = fetchContacts(listId);
        if (contacts == null) {
            cgroupDao.deleteById(listId);
            return null;
        }

        ContactDao contactDao = db.getContactDao();
        contactDao.insert(contacts);

        return contacts;
    }

    private List<Contact> fetchContacts(long listId) {
        List<Contact> contacts = new ArrayList<>();
        try {
            Workbook workbook = new HSSFWorkbook(new FileInputStream(mExcelFile));
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            mRowCount = sheet.getPhysicalNumberOfRows();
            int rowsRead = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(mNameColIndex) == null || row.getCell(mNumberColIndex) == null)
                    continue;

                String name = row.getCell(mNameColIndex).getStringCellValue();

                CellType cellType = row.getCell(mNumberColIndex).getCellType();
                String phoneNumber = null;
                switch (cellType) {
                    case NUMERIC:
                        double number = row.getCell(mNumberColIndex).getNumericCellValue();
                        phoneNumber = String.format(Utilities.sLocale, "%.0f\n", number);
                        break;
                    case STRING:
                        phoneNumber = row.getCell(mNumberColIndex).getStringCellValue();
                        break;
                }

                if (phoneNumber == null) continue;
                if (name == null) name = phoneNumber;

                Contact contact = new Contact(name, phoneNumber, null);
                contact.setListId(listId);

                contacts.add(contact);

                rowsRead++;
                publishProgress(rowsRead);
            }
        } catch (FileNotFoundException e) {
            Timber.e(e);
            status = STATUS_FILE_NOT_FOUND;
            return null;
        } catch (IOException e) {
            Timber.e(e);
            status = STATUS_FAILED;
            return null;
        }
        return contacts;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int rowsRead = values[0];
        if (mOnProgressListener != null) mOnProgressListener.onProgress(rowsRead, mRowCount);
    }

    @Override
    protected void onPostExecute(List<Contact> contacts) {
        if (status == STATUS_FAILED) {
            Toast.makeText(mContext, "Couldn't find contacts in the spreadsheet specified", Toast.LENGTH_LONG).show();
            Timber.w("Couldn't find contacts in %s at the columns %d and %d", mExcelFile.getPath(), mNameColIndex, mNumberColIndex);
        } else if (status == STATUS_FILE_NOT_FOUND) {
            Toast.makeText(mContext, "Couldn't find the file specified", Toast.LENGTH_SHORT).show();
        }

        if (mOnFinishListener != null) mOnFinishListener.onFinish(status);
    }

    public interface OnProgressListener {
        void onProgress(int rowsRead, int rowsCount);
    }

    public interface OnFinishListener {
        void onFinish(int callback);
    }
}
