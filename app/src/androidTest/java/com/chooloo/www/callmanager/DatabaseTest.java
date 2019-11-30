package com.chooloo.www.callmanager;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.chooloo.www.callmanager.database.AppDatabase;
import com.chooloo.www.callmanager.database.dao.CGroupDao;
import com.chooloo.www.callmanager.database.dao.ContactDao;
import com.chooloo.www.callmanager.database.entity.CGroup;
import com.chooloo.www.callmanager.database.entity.Contact;
import com.jraska.livedata.TestObserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private static final String TAG = "TESTING";

    private AppDatabase mDb;
    private ContactDao mContactDao;
    private CGroupDao mCGroupDao;

    private CGroup mList1;
    private Contact mContact1;
    private Contact mContact2;

    @Rule public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        mDb = AppDatabase.getDatabase(context);
        mContactDao = mDb.getContactDao();
        mCGroupDao = mDb.getCGroupDao();
    }

    @Before
    public void initObjects() {
        mList1 = new CGroup("Gods");
        mContact1 = new Contact("Jesus Christ", "0000000000000", null);
        mContact2 = new Contact("Moses Avinu", "0000000000001", null);
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test(timeout = 1000)
    public void simpleInsert() throws Exception {
        //Insert the list
        mCGroupDao.insert(mList1);

        LiveData<List<CGroup>> result1 = mCGroupDao.getCGroupByName(mList1.getName());
        List<CGroup> listDb1 =
                TestObserver.test(result1)
                        .awaitValue()
                        .assertHasValue()
                        .value();
        assertThat(listDb1.size(), greaterThan(0));
        assertThat(listDb1.get(0), is(mList1));
        long listId = listDb1.get(0).getListId();

        //Insert the contact
        mContact1.setListId(listId);
        mContactDao.insert(mContact1);

        LiveData<List<Contact>> result2 = mContactDao.getContactsByPhoneNumber(mContact1.getMainPhoneNumber());
        List<Contact> contactsDb1 = TestObserver.test(result2)
                .awaitValue()
                .assertHasValue()
                .value();
        assertThat(contactsDb1.size(), greaterThan(0));
        assertThat(contactsDb1.get(0), is(mContact1));
    }

    @Test(timeout = 1000)
    public void singleContactInsert() throws Exception {
        boolean foreignKeyConstraintFailed = false;
        try {
            mContactDao.insert(mContact2);
        } catch (SQLiteConstraintException e) {
            foreignKeyConstraintFailed = true;
        }
        assertThat(foreignKeyConstraintFailed, is(true));
    }

    @Test(timeout = 1000)
    public void simpleDeletion() throws Exception {

        //Delete the list
        LiveData<List<CGroup>> result1 = mCGroupDao.getCGroupByName(mList1.getName());
        List<CGroup> listDb1 =
                TestObserver.test(result1)
                        .awaitValue()
                        .assertHasValue()
                        .value();

        if (listDb1.contains(mList1)) {
            int rowsDeleted = mCGroupDao.deleteByName(mList1.getName());
            assertThat(rowsDeleted, greaterThan(0));
            Log.d(TAG, "List rows deleted: " + rowsDeleted);
        }

        //Make sure it deleted all the associated contacts too
        LiveData<List<Contact>> result2 = mContactDao.getContactsByPhoneNumber(mContact1.getMainPhoneNumber());
        List<Contact> contactsDb1 = TestObserver.test(result2)
                .awaitValue()
                .assertHasValue()
                .value();

        assertThat(contactsDb1, not(hasItem(mContact1)));

        mContactDao.deleteByPhoneNumber(mContact2.getMainPhoneNumber());
        LiveData<List<Contact>> result3 = mContactDao.getContactsByPhoneNumber(mContact2.getMainPhoneNumber());
        List<Contact> contactsDb2 = TestObserver.test(result3)
                .awaitValue()
                .assertHasValue()
                .value();

        if (contactsDb2.contains(mContact2)) {
            int rowsDeleted = mContactDao.deleteByPhoneNumber(mContact2.getName());
            assertThat(rowsDeleted, greaterThan(0));
            Log.d(TAG, "Contact2 rows deleted: " + rowsDeleted);
        }
    }
}
