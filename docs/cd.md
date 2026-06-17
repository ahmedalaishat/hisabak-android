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

## Production → Google Play (planned)

Not wired yet. Will publish a signed **AAB** of the `prod` variant to the Play **internal**
track on a `v*` release tag, gated to production manually. Needs a release keystore (Play App
Signing) and a Play service-account secret.
