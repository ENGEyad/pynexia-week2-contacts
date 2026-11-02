package com.pynexia.week2.contacts;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.text.InputType;
import androidx.lifecycle.ViewModelProvider;
public class EditorActivity extends BaseActivity {
    private EditText title, body, extra;
    private EditorModel model;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state); showLayout(R.layout.activity_editor);
        title = findViewById(R.id.title_input); body = findViewById(R.id.body_input); extra = findViewById(R.id.extra_input);
        for (EditText field : new EditText[]{title, body, extra}) {
            field.addTextChangedListener(new android.text.TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) { field.setError(null); }
                public void afterTextChanged(android.text.Editable value) {}
            });
        }
        boolean contact = BuildConfig.KIND.equals("contacts");
        findViewById(R.id.extra_group).setVisibility(contact ? View.VISIBLE : View.GONE);
        if (contact) { body.setInputType(InputType.TYPE_CLASS_PHONE); body.setSingleLine(true); }
        if (state == null) {
            title.setText(getIntent().getStringExtra("title")); body.setText(getIntent().getStringExtra("body")); extra.setText(getIntent().getStringExtra("extra"));
        }
        ((TextView)findViewById(R.id.editor_heading)).setText(getIntent().getLongExtra("id", 0) == 0 ? R.string.add : R.string.edit_heading);
        findViewById(R.id.cancel).setOnClickListener(v -> finish());
        Button save = findViewById(R.id.save);
        model = new ViewModelProvider(this).get(EditorModel.class);
        model.state.observe(this, status -> {
            boolean saving = status == EditorModel.State.SAVING;
            save.setEnabled(!saving); title.setEnabled(!saving); body.setEnabled(!saving); extra.setEnabled(!saving);
            findViewById(R.id.cancel).setEnabled(!saving);
            save.setText(saving ? R.string.saving : R.string.save);
            if (status == EditorModel.State.SAVED) finish();
            if (status == EditorModel.State.ERROR) ((TextView)findViewById(R.id.error)).setText(R.string.storage_error);
        });
        save.setOnClickListener(v -> save());
    }
    private void save() {
        String name = title.getText().toString().trim(), content = body.getText().toString().trim(), email = extra.getText().toString().trim();
        boolean valid = true;
        if (!EntryRules.validTitle(name)) { title.setError(getString(R.string.title_error)); valid = false; }
        if (BuildConfig.KIND.equals("notes") && content.isEmpty()) { body.setError(getString(R.string.body_error)); valid = false; }
        if (content.length() > 10000) { body.setError(getString(R.string.length_error)); valid = false; }
        if (BuildConfig.KIND.equals("contacts")) {
            if (!EntryRules.validPhone(content)) { body.setError(getString(R.string.phone_error)); valid = false; }
            if (!EntryRules.validEmail(email)) { extra.setError(getString(R.string.email_error)); valid = false; }
        }
        if (valid) model.save(EntryStore.get(this), new Entry(getIntent().getLongExtra("id", 0), name, content, email, getIntent().getBooleanExtra("done", false)));
    }
}
