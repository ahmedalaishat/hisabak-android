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

4. **Land the bump on `develop` via PR.** Both `main` and `develop` are protected — no
   direct pushes; every change needs a PR with the green **"JVM unit tests"** check.
   ```bash
   git switch -c chore/release-vX.Y.Z
   git commit -am "Cut vX.Y.Z: <one-line summary>"   # version bump + CHANGELOG date
   git push -u origin chore/release-vX.Y.Z
   gh pr create --base develop --fill                # merge once CI is green
   ```

5. **Open a `develop` → `main` PR and merge it once green** — this updates the release line
   (don't delete `develop`):
   ```bash
   gh pr create --base main --head develop --title "Release vX.Y.Z" --fill
   gh pr merge --merge                               # after the check passes
   ```

6. **Tag the merged `main` commit and push the tag** — pushing the tag (not a branch) is
   what triggers `release.yml`:
   ```bash
   git fetch origin
   git tag -a vX.Y.Z origin/main -m "Hisabak vX.Y.Z"
   git push origin vX.Y.Z
   ```
   ⚠️ Tag **after** `main` is merged — never pre-tag a local merge. The tag must sit on
   `main`'s history, and re-tagging to fix placement would re-trigger `release.yml` and
   fail on a duplicate `versionCode`.

7. **Verify**: `git tag` shows the new tag on `main`. Pushing the tag triggers `release.yml`,
   which builds the **publishable** prod artifact with `-PrequireReleaseSigning` (release-signed;
   the flag makes a missing keystore a hard failure). A local `./gradlew :app:assembleProdRelease`
   is only a smoke build — it's debug-signed unless a release keystore is configured, so don't
   ship it. Optionally install and smoke-test that local build.

---

## Conventions
- End every commit message with the footer:
  `Co-Authored-By: Claude Opus 4.8 <noreply@anthropic.com>`
- Use `git branch -d` (safe) not `-D` when deleting merged branches.
- Keep `main` linear-ish and clean; never force-push it once a remote exists.
- One concern per `feat/*` branch; if scope grows, branch again from `develop`.
