# Contact App

Maintain a private contact directory with create, view, edit, delete and search, a custom RecyclerView adapter and local SQLite storage.

Pynexia Android Development / Week 2.

[Hosted showcase](https://engeyad.github.io/pynexia-week2-contacts/) · [PDF report](https://engeyad.github.io/pynexia-week2-contacts/downloads/Contact-App-Report.pdf) · [APK](https://engeyad.github.io/pynexia-week2-contacts/downloads/Contact-App.apk) · [Submission ZIP](https://engeyad.github.io/pynexia-week2-contacts/downloads/Contact-App.zip)

## Implementation

### Contact model and validation

A contact has a required name and phone, with optional email. Names allow 1-120 characters; phone numbers need 3-20 digits with basic formatting, up to 40 characters.

### Directory and CRUD

SQLite persists entries. A custom ListAdapter displays names and phones in alphabetical order. Search matches name, phone or email without treating SQL wildcards specially.

### Viewing and dialer

Selecting a row shows full contact details. Open dialer uses ACTION_DIAL with the selected number and handles missing dialer apps. The app requests no contacts or call permission.

## Run

Open the repository root in Android Studio. Install SDK 35 and Build Tools 35.0.0, use JDK 17 or 21 and sync Gradle. Run `contacts` on Android 8.0/API 26 or newer. AGP 8.9.1; Gradle wrapper 8.11.1. Initial sync needs internet.

```powershell
.\gradlew.bat :contacts:assembleDebug :contacts:testDebugUnitTest :contacts:lintDebug
.\gradlew.bat :contacts:connectedDebugAndroidTest
```

The second command requires an emulator/device. On macOS/Linux use `chmod +x gradlew` and `./gradlew`.

## Walkthrough

1. Tap New contact and enter a name, phone and optional email.
2. Save, then search by name, phone digits or email.
3. Open the contact details and select Open dialer to inspect the number.
4. Edit the entry, reopen the app, and test cancelling/confirming deletion.

## Verification

4 JVM tests and 4 device tests passed with no failures, errors or skips. Lint: 0 errors and 18 warnings without suppression. Tested 2 November 2026 on Pixel 8 emulator, Android 17/API 37 (37.1 image), x86_64. No physical-device or API 26 run. See `verification/` for results with local paths redacted.

Name/phone/email validation and literal search, SQLite CRUD/reopen, UI add/edit/delete, editor recreation, full details and intercepted ACTION_DIAL Intent.

## Scope

Does not read or modify the system address book. No automatic calls, cloud sync or encrypted storage. Clearing app data/uninstalling removes contacts. Phone validation is a basic input check, not number verification.

APKs are debug signed for evaluation. Hosted pages show actual screenshots and downloads, not Android emulation. No Pynexia form is submitted automatically.

## شرح عربي

يحفظ التطبيق جهات الاتصال في دليل محلي مستقل عن جهات اتصال الهاتف. يمكن إضافة الاسم ورقم الهاتف والبريد الاختياري وعرض البيانات وتعديلها وحذفها. يبحث في الحقول الثلاثة ويرتب الأسماء، ويفتح برنامج الاتصال بالرقم المحدد دون إجراء مكالمة تلقائيًا.

## Submission

Copy `SUBMISSION-NOTES.txt` into the form. Attach the ZIP or PDF. APK is inside the ZIP because the earlier form does not list direct APK uploads. Verify the current Week 2 form accepts the same formats and limits.
