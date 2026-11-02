package com.pynexia.week2.contacts;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;
public final class EditorModel extends ViewModel {
    public enum State { READY, SAVING, SAVED, ERROR }
    public final MutableLiveData<State> state = new MutableLiveData<>(State.READY);
    public void save(EntryStore store, Entry entry) {
        if (state.getValue() == State.SAVING || state.getValue() == State.SAVED) return;
        state.setValue(State.SAVING);
        store.submit(() -> store.database.save(entry), (id, error) -> state.setValue(error == null ? State.SAVED : State.ERROR));
    }
}
