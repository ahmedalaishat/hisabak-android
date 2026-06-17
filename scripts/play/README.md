# Play listing push

Pushes the Hisabak **store listing** (title, descriptions, app icon, feature graphic, screenshots)
to Google Play via the Android Publisher API. It does **not** touch content rating, Data safety,
app access, or the first AAB upload — those are Console-only (see `../../play/CONSOLE-CHECKLIST.md`).

## One-time setup

1. **Service account** with the *Google Play Android Publisher API* enabled in its Google Cloud
   project. Download its JSON key.
2. In **Play Console → Users & permissions**, invite that service account (release/admin).
3. The app `com.hisabak` must already exist in the Console.
4. Install deps: `cd scripts/play && npm install`.

## Run

```bash
cd scripts/play
node push-listing.mjs --key /absolute/path/to/service-account.json
# or: GOOGLE_APPLICATION_CREDENTIALS=/abs/path.json node push-listing.mjs
```

It reads:
- text from `play/listing/en-US/{title,short-description,full-description}.txt`
- `play/listing/en-US/graphics/icon.png` (512×512) and `feature-graphic.png` (1024×500)
- `play/listing/en-US/phone-screenshots/*.png` (2–8, ≥320px, 16:9 or 9:16)

Missing graphics are skipped, so you can run it for text first and re-run after adding images.

> **Never commit `service-account.json`.** Keep it outside the repo or in a gitignored path.
