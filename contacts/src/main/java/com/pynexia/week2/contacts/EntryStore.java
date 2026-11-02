package com.pynexia.week2.contacts;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
public final class EntryStore {
    private static EntryStore instance;
    public final EntryDatabase database;
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());
    private final AtomicInteger pending = new AtomicInteger();
    private EntryStore(Context context) { database = new EntryDatabase(context, "entries.db"); }
    public static synchronized EntryStore get(Context context) {
        if (instance == null) instance = new EntryStore(context.getApplicationContext());
        return instance;
    }
    public boolean isIdle() { return pending.get() == 0; }
    public interface Callback<T> { void complete(T result, Exception error); }
    public <T> void submit(Callable<T> operation, Callback<T> callback) {
        pending.incrementAndGet();
        worker.execute(() -> {
            T value = null; Exception failure = null;
            try { value = operation.call(); } catch (Exception error) { failure = error; }
            T result = value; Exception error = failure;
            main.post(() -> { try { callback.complete(result, error); } finally { pending.decrementAndGet(); } });
        });
    }
}
