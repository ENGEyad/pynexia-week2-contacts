package com.pynexia.week2.contacts;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
public final class EntryDatabase extends SQLiteOpenHelper {
    public EntryDatabase(Context context, String name) { super(context.getApplicationContext(), name, null, 1); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE entries (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, body TEXT NOT NULL, extra TEXT NOT NULL, done INTEGER NOT NULL DEFAULT 0 CHECK(done IN (0,1)))");
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("No migration is defined for this schema version.");
    }
    public List<Entry> all(boolean alphabetical) {
        List<Entry> items = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().query("entries", null, null, null, null, null, alphabetical ? "title COLLATE NOCASE ASC, id DESC" : "id DESC")) {
            while (cursor.moveToNext()) items.add(new Entry(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4) == 1));
        }
        return items;
    }
    public long save(Entry entry) {
        ContentValues values = new ContentValues();
        values.put("title", entry.title); values.put("body", entry.body); values.put("extra", entry.extra); values.put("done", entry.done ? 1 : 0);
        if (entry.id == 0) return getWritableDatabase().insertOrThrow("entries", null, values);
        if (getWritableDatabase().update("entries", values, "id = ?", new String[]{Long.toString(entry.id)}) != 1) throw new IllegalStateException("Entry no longer exists.");
        return entry.id;
    }
    public void delete(long id) { getWritableDatabase().delete("entries", "id = ?", new String[]{Long.toString(id)}); }
    public void setDone(long id, boolean done) {
        ContentValues values = new ContentValues(); values.put("done", done ? 1 : 0);
        getWritableDatabase().update("entries", values, "id = ?", new String[]{Long.toString(id)});
    }
}
