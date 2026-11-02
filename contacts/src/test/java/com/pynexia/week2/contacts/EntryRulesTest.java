package com.pynexia.week2.contacts;
import org.junit.Test;
import static org.junit.Assert.*;
public class EntryRulesTest {
    @Test public void titleRejectsBlankAndOversizedInput() {
        assertFalse(EntryRules.validTitle("   ")); assertFalse(EntryRules.validTitle(null));
        assertTrue(EntryRules.validTitle(" A ")); assertTrue(EntryRules.validTitle("a".repeat(120)));
        assertFalse(EntryRules.validTitle("a".repeat(121)));
    }
    @Test public void phoneAllowsFormattingButRejectsNonNumbers() {
        assertTrue(EntryRules.validPhone("+1 (202) 555-0100")); assertFalse(EntryRules.validPhone("12"));
        assertFalse(EntryRules.validPhone("letters")); assertFalse(EntryRules.validPhone("1".repeat(21)));
    }
    @Test public void optionalEmailStillChecksNonemptyAddresses() {
        assertTrue(EntryRules.validEmail("")); assertTrue(EntryRules.validEmail("alex@example.com"));
        assertFalse(EntryRules.validEmail("alex@")); assertFalse(EntryRules.validEmail("a b@example.com"));
    }
    @Test public void searchMatchesNameBodyAndEmailLiterally() {
        Entry item = new Entry(1, "Alex O'Neil", "+1 202 555 0100", "alex@example.com", false);
        assertTrue(EntryRules.matches(item, " ALEX ")); assertTrue(EntryRules.matches(item, "202"));
        assertTrue(EntryRules.matches(item, "EXAMPLE.COM")); assertTrue(EntryRules.matches(item, "O'Neil"));
        assertFalse(EntryRules.matches(item, "%")); assertFalse(EntryRules.matches(item, "missing"));
    }
}