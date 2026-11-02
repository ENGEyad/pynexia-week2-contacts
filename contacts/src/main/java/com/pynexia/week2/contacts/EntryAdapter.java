package com.pynexia.week2.contacts;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.CheckBox;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
public final class EntryAdapter extends ListAdapter<Entry, EntryAdapter.Holder> {
    public interface Actions { void open(Entry entry); void edit(Entry entry); void delete(Entry entry); void toggle(Entry entry, boolean done); }
    private final Actions actions;
    public EntryAdapter(Actions actions) {
        super(new DiffUtil.ItemCallback<Entry>() {
            public boolean areItemsTheSame(Entry a, Entry b) { return a.id == b.id; }
            public boolean areContentsTheSame(Entry a, Entry b) { return a.sameContent(b); }
        });
        this.actions = actions;
    }
    @Override public Holder onCreateViewHolder(ViewGroup parent, int type) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_entry, parent, false));
    }
    @Override public void onBindViewHolder(Holder holder, int position) {
        Entry entry = getItem(position);
        holder.title.setText(entry.title);
        holder.body.setText(entry.body.isEmpty() ? holder.itemView.getContext().getString(R.string.no_details) : entry.body);
        holder.done.setOnCheckedChangeListener(null);
        holder.done.setVisibility(BuildConfig.KIND.equals("todo") ? View.VISIBLE : View.GONE);
        holder.done.setChecked(entry.done);
        holder.done.setContentDescription(holder.itemView.getContext().getString(R.string.complete_task, entry.title));
        holder.title.setPaintFlags(entry.done ? holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG : holder.title.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        holder.done.setOnCheckedChangeListener((button, checked) -> actions.toggle(entry, checked));
        holder.itemView.findViewById(R.id.row_text).setOnClickListener(v -> actions.open(entry));
        holder.itemView.findViewById(R.id.edit).setOnClickListener(v -> actions.edit(entry));
        holder.itemView.findViewById(R.id.delete).setOnClickListener(v -> actions.delete(entry));
        holder.itemView.findViewById(R.id.edit).setContentDescription(holder.itemView.getContext().getString(R.string.edit_named, entry.title));
        holder.itemView.findViewById(R.id.delete).setContentDescription(holder.itemView.getContext().getString(R.string.delete_named, entry.title));
    }
    static final class Holder extends RecyclerView.ViewHolder {
        final TextView title, body; final CheckBox done;
        Holder(View view) { super(view); title = view.findViewById(R.id.row_title); body = view.findViewById(R.id.row_body); done = view.findViewById(R.id.done); }
    }
}
