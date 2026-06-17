# Continuous delivery

How Hisabak builds are distributed. Two environments with separate package names so they
coexist on one device:

| Variant | applicationId | Channel |
|---------|--------------|---------|
| `staging` | `com.hisabak.staging` ("Hisabak STG") | Firebase App Distribution → testers |
| `prod` | `com.hisabak` ("Hisabak") | Google Play *(planned)* |

## Staging → Firebase App Distribution (live)

`.github/workflows/distribute.yml` runs on every **push to `develop`** (and on demand via
*Run workflow*). It builds `assembleStagingRelease` and uploads the APK to the **`qa`** testers
group via the [Firebase Distribution GitHub Action](https://github.com/wzieba/Firebase-Distribution-Github-Action).

> The Firebase App Distribution **Gradle plugin** is not used — it requires the legacy AGP
> `AppExtension` that AGP 9 removed. The GitHub Action uploads the built APK directly instead.

**Required GitHub secret**
- `FIREBASE_SERVICE_ACCOUNT_JSON` — a Google Cloud service account (role: **Firebase App
  Distribution Admin**) JSON key, for `hisabak-finance-tracking`. Create it under
  *IAM & Admin → Service Accounts*, then add the JSON as a repo Actions secret.

The staging Firebase **App ID** (`1:469916556187:android:bb6a22afa66dde1b80df3e`) is not a
secret and lives inline in the workflow.

**Signing:** staging release builds use the debug key (the release signing config falls back to
debug when no keystore is configured) — fine for tester installs. Real release signing is set up
for the Play (prod) path.

## Production → Google Play internal (live)

`.github/workflows/release.yml` runs on a **`v*` release tag**. It decodes the release
keystore, builds a **signed `bundleProdRelease`** AAB, and publishes it to the Play
**internal** track via [r0adkll/upload-google-play](https://github.com/r0adkll/upload-google-play).
Promotion **internal → production** stays a manual step in the Play Console.

> Like Firebase, the Gradle Play Publisher **plugin** is not used (AGP 9 incompatibility). The
> Action uploads the built AAB directly.

**Required GitHub secrets**
- `RELEASE_KEYSTORE_BASE64` — base64 of the upload keystore (`base64 -i hisabak-keystore.jks`).
- `RELEASE_KEYSTORE_PASSWORD`, `RELEASE_KEY_ALIAS`, `RELEASE_KEY_PASSWORD` — keystore credentials.
- `PLAY_SERVICE_ACCOUNT_JSON` — a Google Cloud service account JSON with Play access (granted in
  the Play Console under *Users & permissions*).

**One-time Play Console setup**
1. Create the app in the Play Console and enable **Play App Signing**.
2. **Upload the first signed AAB manually** to the internal track — the API can't create the
   app or seed the first release. CI publishes every release after that.

**Cutting a release** (the `git-workflow` skill's "ship it"): bump `versionName`/`versionCode`,
merge `develop`→`main`, then tag `vX.Y.Z` and push — the tag triggers this workflow.
