package com.pynexia.week2.contacts;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends BaseActivity implements EntryAdapter.Actions {
    private EntryStore store;
    private EntryAdapter adapter;
    private EditText search;
    private RadioGroup filters;
    private List<Entry> entries = new ArrayList<>();
    private int request;
    private boolean loaded;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state); showLayout(R.layout.activity_main);
        store = EntryStore.get(this);
        search = findViewById(R.id.search); filters = findViewById(R.id.filters);
        filters.setVisibility(BuildConfig.KIND.equals("todo") ? View.VISIBLE : View.GONE);
        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EntryAdapter(this); list.setAdapter(adapter);
        adapter.setStateRestorationPolicy(RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY);
        findViewById(R.id.add).setOnClickListener(v -> edit(new Entry(0, "", "", "", false)));
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { render(); }
            public void afterTextChanged(Editable e) {}
        });
        filters.setOnCheckedChangeListener((group, checked) -> render());
    }
    @Override protected void onResume() { super.onResume(); refresh(); }
    private void refresh() {
        int token = ++request;
        store.submit(() -> store.database.all(BuildConfig.KIND.equals("contacts")), (items, error) -> {
            if (isDestroyed() || isFinishing() || token != request) return;
            if (error != null) { Toast.makeText(this, R.string.storage_error, Toast.LENGTH_LONG).show(); return; }
            entries = items; loaded = true; render();
        });
    }
    private void render() {
        List<Entry> visible = new ArrayList<>();
        int completed = 0;
        for (Entry entry : entries) {
            if (entry.done) completed++;
            boolean status = !BuildConfig.KIND.equals("todo") || filters.getCheckedRadioButtonId() == R.id.all || (filters.getCheckedRadioButtonId() == R.id.completed) == entry.done;
            if (status && EntryRules.matches(entry, search.getText().toString())) visible.add(entry);
        }
        adapter.submitList(visible);
        findViewById(R.id.empty).setVisibility(loaded && visible.isEmpty() ? View.VISIBLE : View.GONE);
        ((TextView)findViewById(R.id.empty_title)).setText(entries.isEmpty() ? R.string.empty_title : R.string.no_matches);
        ((TextView)findViewById(R.id.empty_body)).setText(entries.isEmpty() ? R.string.empty_body : R.string.change_filter);
        ((TextView)findViewById(R.id.stats)).setText(BuildConfig.KIND.equals("todo") ? getString(R.string.task_count, completed, entries.size()) : getString(R.string.entry_count, visible.size(), entries.size()));
    }
    @Override public void edit(Entry entry) {
        startActivity(new Intent(this, EditorActivity.class).putExtra("id", entry.id).putExtra("title", entry.title).putExtra("body", entry.body).putExtra("extra", entry.extra).putExtra("done", entry.done));
    }
    @Override public void open(Entry entry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(entry.title).setMessage(entry.body + (entry.extra.isEmpty() ? "" : "\n\n" + entry.extra)).setNegativeButton(R.string.close, null).setPositiveButton(R.string.edit, (d, w) -> edit(entry));
        if (BuildConfig.KIND.equals("contacts")) builder.setNeutralButton(R.string.dial, (d, w) -> {
            try { startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", entry.body, null))); }
            catch (ActivityNotFoundException error) { Toast.makeText(this, R.string.no_dialer, Toast.LENGTH_LONG).show(); }
        });
        builder.show();
    }
    @Override public void delete(Entry entry) {
        new AlertDialog.Builder(this).setTitle(R.string.delete_title).setMessage(getString(R.string.delete_message, entry.title)).setNegativeButton(R.string.cancel, null).setPositiveButton(R.string.delete, (d, w) ->
            store.submit(() -> { store.database.delete(entry.id); return true; }, (value, error) -> changed(error))).show();
    }
    @Override public void toggle(Entry entry, boolean done) {
        store.submit(() -> { store.database.setDone(entry.id, done); return true; }, (value, error) -> changed(error));
    }
    private void changed(Exception error) {
        if (isDestroyed() || isFinishing()) return;
        if (error != null) Toast.makeText(this, R.string.storage_error, Toast.LENGTH_LONG).show();
        refresh();
    }
}
