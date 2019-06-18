package com.chooloo.www.callmanager;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class ExcelReadTest {

    private static final String TAG = "TESTING";

    private Map<String, Double> mDoc1;
    private Workbook mWorkbook1;

    @Before
    public void initApache() throws Exception {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        AssetManager assetManager = context.getAssets();
        mWorkbook1 = new HSSFWorkbook((assetManager.open("holy_book.xls")));
    }

    @Before
    public void initDocs() {
        if (mDoc1 == null) {
            mDoc1 = new HashMap<>();
            mDoc1.put("Jesus1", 2.565465455E9);
            mDoc1.put("Jesus2", 1.54987654235466E14);
        }
    }

    @Test
    public void simpleExcelTest() throws Exception {
        assertThat(mWorkbook1.getNumberOfSheets(), is(1));
        Sheet sheet1 = mWorkbook1.getSheetAt(0);

        int rowsCount = 0;
        Iterator<Row> iterable1 = sheet1.rowIterator();
        while ((iterable1).hasNext()) {
            rowsCount++;
            Row row = iterable1.next();
            String jesus = row.getCell(1).getStringCellValue();
            double num = row.getCell(2).getNumericCellValue();
            assertThat(mDoc1.containsKey(jesus), is(true));
            assertThat(num, is(mDoc1.get(jesus)));
        }
        assertThat(rowsCount, is(2));
    }
}
