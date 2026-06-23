# Google Drive backup — Cloud setup

The encrypted backup/restore feature stores a single file in the user's **Drive App Data Folder**
(hidden, app-private) using the `drive.appdata` OAuth scope. No secret ships in the app — Android
authorizes by the app's **package name + signing SHA-1**. Until the steps below are done, connecting
an account in the app will fail with an auth error; the code is otherwise complete.

## 1. Enable the Drive API
In the [Google Cloud Console](https://console.cloud.google.com/) for the project backing Firebase
(`hisabak-finance-tracking`): **APIs & Services → Library → Google Drive API → Enable**.

## 2. OAuth consent screen
**APIs & Services → OAuth consent screen**:
- User type **External**; fill app name, support email, developer contact.
- Add the scope `https://www.googleapis.com/auth/drive.appdata` (App Data Folder — **sensitive**,
  not restricted).
- While unverified, add yourself + testers under **Test users** (works without full verification).
- For a public Play release, submit for **OAuth verification** (sensitive-scope review). The
  appdata scope does **not** require the restricted-scope CASA assessment.

## 3. Android OAuth client(s)
**APIs & Services → Credentials → Create credentials → OAuth client ID → Android** for each variant:
- **prod:** package `com.hisabak` + the release signing SHA-1 (and the debug SHA-1 for local runs).
- **staging:** package `com.hisabak.staging` + its signing SHA-1.

Get SHA-1s with:
```bash
./gradlew :app:signingReport     # lists debug + release SHA-1 per variant
```
Add both debug and release SHA-1s so debug builds and released builds both authorize.

## 4. (If using google-services.json) refresh it
Adding OAuth clients doesn't require app code changes, but if you regenerate
`app/google-services.json` from Firebase, keep the existing Firebase config intact.

## Verify
Build a debug variant on a device/emulator signed with a registered SHA-1, sign in with a **test
user** account, enable backup → connect account → consent → **Back up now** should succeed; the file
appears in that account's Drive App Data (not visible in the normal Drive UI).

## Notes
- Tokens are short-lived and fetched on demand; the app stores only the chosen account email
  (the encryption passphrase is stored separately, Keystore-encrypted).
- Revoking access in the Google account settings invalidates backups' authorization; the app will
  prompt to reconnect.
