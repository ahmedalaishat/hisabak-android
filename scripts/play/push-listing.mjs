// Pushes the Hisabak store listing (text + graphics) to Google Play via the Android Publisher API.
//
// Prereqs (one-time):
//   1. A Google Cloud service account JSON with the "Android Publisher" API enabled.
//   2. That service account invited in Play Console → Users & permissions (release/admin).
//   3. The app (com.hisabak) already created in Play Console.
//   4. `npm install` in this folder (installs googleapis).
//
// Run:
//   GOOGLE_APPLICATION_CREDENTIALS=/abs/path/service-account.json node push-listing.mjs
//   (or pass --key /abs/path/service-account.json)
//
// It only edits the store *listing* — content rating, data safety, app access, and the first AAB
// upload remain Console-only (see play/CONSOLE-CHECKLIST.md).

import { google } from 'googleapis';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const PACKAGE = 'com.hisabak';
const LANG = 'en-US';
const CONTACT_EMAIL = 'ahmedalaishat@gmail.com';
const CONTACT_WEBSITE = 'https://ahmedalaishat.github.io/hisabak-android/';

const here = path.dirname(fileURLToPath(import.meta.url));
const repoRoot = path.resolve(here, '..', '..');
const listingDir = path.join(repoRoot, 'play', 'listing', LANG);

function read(rel) {
  return fs.readFileSync(path.join(listingDir, rel), 'utf8').trim();
}
function keyFile() {
  const flagIdx = process.argv.indexOf('--key');
  if (flagIdx !== -1) return process.argv[flagIdx + 1];
  if (process.env.GOOGLE_APPLICATION_CREDENTIALS) return process.env.GOOGLE_APPLICATION_CREDENTIALS;
  throw new Error('Provide the service account JSON via --key <path> or GOOGLE_APPLICATION_CREDENTIALS.');
}
function pngFiles(dir) {
  if (!fs.existsSync(dir)) return [];
  return fs.readdirSync(dir).filter((f) => f.toLowerCase().endsWith('.png')).sort()
    .map((f) => path.join(dir, f));
}

async function main() {
  const auth = new google.auth.GoogleAuth({
    keyFile: keyFile(),
    scopes: ['https://www.googleapis.com/auth/androidpublisher'],
  });
  const publisher = google.androidpublisher({ version: 'v3', auth });

  const { data: edit } = await publisher.edits.insert({ packageName: PACKAGE });
  const editId = edit.id;
  console.log(`edit ${editId} opened`);

  await publisher.edits.details.update({
    packageName: PACKAGE, editId,
    requestBody: { defaultLanguage: LANG, contactEmail: CONTACT_EMAIL, contactWebsite: CONTACT_WEBSITE },
  });
  console.log('details updated');

  await publisher.edits.listings.update({
    packageName: PACKAGE, editId, language: LANG,
    requestBody: {
      language: LANG,
      title: read('title.txt'),
      shortDescription: read('short-description.txt'),
      fullDescription: read('full-description.txt'),
    },
  });
  console.log('listing text updated');

  const single = {
    icon: path.join(listingDir, 'graphics', 'icon.png'),
    featureGraphic: path.join(listingDir, 'graphics', 'feature-graphic.png'),
  };
  for (const [imageType, file] of Object.entries(single)) {
    if (!fs.existsSync(file)) { console.log(`skip ${imageType} (missing ${path.relative(repoRoot, file)})`); continue; }
    await publisher.edits.images.deleteall({ packageName: PACKAGE, editId, language: LANG, imageType });
    await publisher.edits.images.upload({
      packageName: PACKAGE, editId, language: LANG, imageType,
      media: { mimeType: 'image/png', body: fs.createReadStream(file) },
    });
    console.log(`uploaded ${imageType}`);
  }

  const shots = pngFiles(path.join(listingDir, 'phone-screenshots'));
  if (shots.length) {
    await publisher.edits.images.deleteall({ packageName: PACKAGE, editId, language: LANG, imageType: 'phoneScreenshots' });
    for (const file of shots) {
      await publisher.edits.images.upload({
        packageName: PACKAGE, editId, language: LANG, imageType: 'phoneScreenshots',
        media: { mimeType: 'image/png', body: fs.createReadStream(file) },
      });
      console.log(`uploaded screenshot ${path.basename(file)}`);
    }
  } else {
    console.log('skip phoneScreenshots (none found)');
  }

  await publisher.edits.commit({ packageName: PACKAGE, editId });
  console.log('committed — listing is live in Play Console.');
}

main().catch((err) => {
  console.error('Push failed:', err?.errors ?? err?.message ?? err);
  process.exit(1);
});
