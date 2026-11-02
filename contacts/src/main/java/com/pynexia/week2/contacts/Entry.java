package com.pynexia.week2.contacts;
public final class Entry {
    public final long id;
    public final String title, body, extra;
    public final boolean done;
    public Entry(long id, String title, String body, String extra, boolean done) {
        this.id = id; this.title = title; this.body = body; this.extra = extra; this.done = done;
    }
    public boolean sameContent(Entry other) {
        return title.equals(other.title) && body.equals(other.body) && extra.equals(other.extra) && done == other.done;
    }
}
