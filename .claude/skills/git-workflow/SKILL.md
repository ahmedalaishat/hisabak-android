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

The repo has a GitHub remote (`origin`) with CI (`.github/workflows/test.yml`) and
branch protection on `develop`/`main`. Features go in via **pull request**, not local
merges: push the branch, open a PR into `develop`, let CI pass, and merge **only when
the user says "merge it"** (never auto-merge). See the `automate-the-dev-workflow` memory.

**Workflow vocabulary — one phrase per action (don't conflate them):**
- **"merge it"** — merge the reviewed PR into `develop`. Never touches `main`. Routine.
- **"send to testers"** — distribute a staging build via Firebase (mostly automatic on `develop`).
- **"ship it" / "release"** — cut a production release (§3): bump version → merge `develop`→`main`
  → tag `vX.Y.Z` → push (the tag triggers the Play upload). Touches `main`; deliberate —
  confirm the version and that `develop` is release-ready first.

When invoked, figure out which of the three operations the user wants (start /
finish / release) and run the matching steps. Ask only if it's ambiguous.

---

## 1. Start a feature

```bash
git checkout develop
git pull --ff-only origin develop
git checkout -b feat/<short-kebab-name>
```
Pick a concise `feat/<name>` (e.g. `feat/budget-rollover`). Then implement;
commit on the feat branch as you go.

## 2. Finish a feature

Land it through a PR into `develop` so CI and branch protection apply. Push, open the
PR, wait for CI green, then merge **only on the user's "merge it"** and sync:
```bash
git push -u origin feat/<name>
gh pr create --base develop --fill          # then wait for CI + user approval
gh pr merge --merge --delete-branch         # only after "merge it"
git checkout develop && git pull --ff-only origin develop
git branch -d feat/<name>                   # delete the local branch too
```
Don't tag or touch `main` here — features accumulate on `develop` until a release.

## 3. Cut a release

This is what **"ship it" / "release"** means. It touches `main` and produces a public
release, so treat it as deliberate: confirm the version bump and that `develop` is
release-ready before starting. Do this from `develop` once it holds everything for the version.

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
   git push origin main develop --follow-tags
   ```

5. **Verify**: `git tag` shows the new tag; `git log --oneline main -1` is the
   release; `./gradlew :app:assembleProdRelease` builds (the shipped prod artifact);
   optionally install and smoke-test.

---

## Conventions
- End every commit message with the footer:
  `Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>`
- Use `git branch -d` (safe) not `-D` when deleting merged branches.
- Keep `main` linear-ish and clean; never force-push it once a remote exists.
- One concern per `feat/*` branch; if scope grows, branch again from `develop`.
