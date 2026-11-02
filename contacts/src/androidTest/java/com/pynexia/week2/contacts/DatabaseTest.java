package com.pynexia.week2.contacts;
import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    @Test public void crudPersistsAcrossReopenAndUsesStableIds() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String name = "test-" + System.nanoTime() + ".db";
        try {
            long first;
            try (EntryDatabase db = new EntryDatabase(context, name)) {
                first = db.save(new Entry(0, "O'Neil", "100% local", "", false));
                long second = db.save(new Entry(0, "O'Neil", "Other record", "", false));
                assertNotEquals(first, second);
                db.save(new Entry(first, "Edited", "New body", "alex@example.com", false));
                db.setDone(first, true); db.delete(second);
            }
            try (EntryDatabase reopened = new EntryDatabase(context, name)) {
                assertEquals(1, reopened.all(false).size());
                Entry entry = reopened.all(false).get(0);
                assertEquals(first, entry.id); assertEquals("Edited", entry.title); assertEquals("New body", entry.body); assertTrue(entry.done);
                reopened.delete(first); assertTrue(reopened.all(false).isEmpty());
            }
        } finally { context.deleteDatabase(name); }
    }
}