package com.pynexia.week2.contacts;
import java.util.Locale;
public final class EntryRules {
    private EntryRules() {}
    public static boolean validTitle(String text) { return text != null && !text.trim().isEmpty() && text.trim().length() <= 120; }
    public static boolean validPhone(String text) {
        if (text == null || !text.trim().matches("[+0-9() .-]+")) return false;
        String digits = text.replaceAll("[^0-9]", "");
        return digits.length() >= 3 && digits.length() <= 20 && text.length() <= 40;
    }
    public static boolean validEmail(String text) {
        return text != null && (text.trim().isEmpty() || (text.length() <= 254 && text.trim().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")));
    }
    public static boolean matches(Entry entry, String query) {
        String term = query.trim().toLowerCase(Locale.ROOT);
        return (entry.title + " " + entry.body + " " + entry.extra).toLowerCase(Locale.ROOT).contains(term);
    }
}
