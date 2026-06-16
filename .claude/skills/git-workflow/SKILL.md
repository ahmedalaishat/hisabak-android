---
name: git-workflow
description: Hisabak's branching and release workflow (Git-Flow-lite). Use when starting a new feature, finishing/merging a feature, or cutting a versioned release (bump version + merge to main + tag). Covers the main/develop/feat-* model, semantic versioning, and the release checklist.
user-invocable: true
---

# Hisabak git workflow

A lightweight Git-Flow: `main` is the released line, `develop` is integration,
each change gets a `feat/*` branch. **No `release/*` or `hotfix/*` branches.**

| Branch | Role |
|--------|------|
| `main` | Released, always-buildable. One tagged commit per version (`vX.Y.Z`). Never commit directly. |
| `develop` | Integration line. Features merge here. The default working branch. |
| `feat/<name>` | One per enhancement, branched from `develop`. Short-lived. |

The repo is currently **local-only (no remote)**. Where steps mention pushing,
do it only if a remote (e.g. `origin`) exists.

When invoked, figure out which of the three operations the user wants (start /
finish / release) and run the matching steps. Ask only if it's ambiguous.

---

## 1. Start a feature

```bash
git checkout develop
# git pull --ff-only            # only if a remote exists
git checkout -b feat/<short-kebab-name>
```
Pick a concise `feat/<name>` (e.g. `feat/budget-rollover`). Then implement;
commit on the feat branch as you go.

## 2. Finish a feature

Merge back into `develop`, keeping the feature as one grouped unit, then delete
the branch:
```bash
git checkout develop
git merge --no-ff feat/<name>
git branch -d feat/<name>
```
Don't tag or touch `main` here — features accumulate on `develop` until a release.

## 3. Cut a release

Do this from `develop` once it holds everything for the version.

1. **Decide the version** (semver `MAJOR.MINOR.PATCH`):
   - PATCH — bug fixes only.
   - MINOR — new, backward-compatible features (the usual bump here).
   - MAJOR — breaking changes / big reworks.

2. **Bump the version** in `app/build.gradle.kts` → `defaultConfig`:
   - `versionName = "X.Y.Z"`
   - `versionCode` += 1 (must increase every release).

3. **Database migrations** — if the Room schema changed since the last release
   (`HisabakDatabase` in `core/data/local/`), **add a real `Migration`** and bump
   `version`; do **not** rely on the destructive `fallbackToDestructiveMigration`
   for released versions (it wipes user data). A fresh schema reset is only OK
   pre-1.0.

4. **Commit on `develop`**, then merge to `main` and tag:
   ```bash
   git commit -am "Cut vX.Y.Z: <one-line summary>"
   git checkout main
   git merge --no-ff develop -m "Release vX.Y.Z"
   git tag -a vX.Y.Z -m "Hisabak vX.Y.Z"
   git checkout develop          # go back to integration line
   # git push origin main develop --follow-tags   # only if a remote exists
   ```

5. **Verify**: `git tag` shows the new tag; `git log --oneline main -1` is the
   release; `./gradlew :app:assembleDebug` builds; optionally install and smoke-test.

---

## Conventions
- End every commit message with the footer:
  `Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>`
- Use `git branch -d` (safe) not `-D` when deleting merged branches.
- Keep `main` linear-ish and clean; never force-push it once a remote exists.
- One concern per `feat/*` branch; if scope grows, branch again from `develop`.
