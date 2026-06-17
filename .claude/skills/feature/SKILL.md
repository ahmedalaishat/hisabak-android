---
name: feature
description: Automated SDLC pipeline for Hisabak. Turn a high-level requirement into a reviewed, tested, documented PR against develop. Use when the user types /feature "<requirement>" or asks to build/ship a feature end-to-end. Runs spec → design → branch → code+tests → QA → docs → PR, then stops for one review gate ("ship it").
user-invocable: true
---

# Hisabak feature pipeline

Turn one high-level requirement into a reviewed, tested, documented PR with a **single
human gate at the end**. This is a playbook you (Claude) execute in the current session —
not background automation. Compose the existing tooling; don't reinvent it.

**Input:** the requirement in `$ARGUMENTS` (e.g. `/feature "let users archive a category"`).
If `$ARGUMENTS` is empty, ask the user what to build, then proceed.

**Golden rule:** autonomous from intake through the open PR; then **stop and wait**. Never
merge until the user says **"ship it"**. Never enable auto-merge. (See the
`automate-the-dev-workflow` memory.)

---

## 1. Intake & clarify

Read `$ARGUMENTS`. Ask **1–2 clarifying questions only if it's too ambiguous to spec
safely** (unclear scope, multiple plausible behaviors, a data/UX decision you can't default).
Otherwise proceed and capture any guesses under **Assumptions** in the spec. Keep upfront
questions minimal — the end review is the real gate.

Derive a short kebab `<slug>` from the requirement (e.g. `archive-category`).

## 2. Requirements spec + 3. Design note

Write both to `docs/features/<slug>.md` (committed on the branch, summarized later in the
PR body). Keep it tight:

```markdown
# <Feature title>

## Requirement
<the user's one-liner, verbatim>

## Spec
- **Goal:** …
- **In scope:** …
- **Out of scope:** …
- **Acceptance criteria:** bullet list, each verifiable
- **Edge cases:** …
- **Assumptions:** … (any guess you made instead of asking)

## Design
- **Domain/model changes:** entities, use cases, repos touched
- **Files to add/change:** representative paths
- **Test strategy:** which unit tests prove the acceptance criteria
- **Trade-offs / decisions:** …
```

Follow the codebase's clean-architecture-per-feature layout and `CLAUDE.md` conventions
(domain/data/presentation, Koin DI, MVI `BaseViewModel`, the design system). For any UI,
use the **`hisabak-design`** skill and match existing screens — don't invent new flows.

## 4. Branch

Per **`git-workflow`**: branch `feat/<slug>` off `develop`.
```bash
git checkout develop && git pull --ff-only origin develop
git checkout -b feat/<slug>
```

## 5. Implement (code + tests together)

Build the feature **and its tests in the same change** — this is a hard `CLAUDE.md` rule.
- Reuse the `com.hisabak.testutil` harness (`TestClock`, `MainDispatcherRule`, `Fake*`
  repositories, `TestData`); prefer fakes over a mocking framework. Full guide:
  `docs/testing.md`.
- Cover each acceptance criterion with a test. New logic/use-case/ViewModel → new tests.
- Only pull in the `testing-setup` skill if genuinely new test infrastructure is required.

## 6. QA

- Run `./gradlew testDebugUnitTest` until green (the Stop hook also enforces this).
- Run a self **`code-review`** pass over the diff and fix real findings.
- CI (`.github/workflows/test.yml`) re-verifies on the PR.
- No real-device step for now (out of scope; add later via `verify`/`run` when an emulator
  is available).

## 7. Docs + changelog (when user-visible)

If the change is user-facing: update the relevant `README.md`/`docs/`, and add an entry to
`CHANGELOG.md` under an `## [Unreleased]` heading (Keep a Changelog: Added/Changed/Fixed).
Skip for purely internal changes — say so in the PR instead.

## 8. Open the PR

Commit on the branch (clear messages, Co-Authored-By trailer), push, then:
```bash
gh pr create --base develop --fill
```
PR body must include: a one-paragraph summary, the spec + design (or a link to
`docs/features/<slug>.md`), what tests prove it, and the changelog line. End the body with
the `🤖 Generated with Claude Code` footer.

## 9. Gate — stop for review

Present to the user: the PR link, the acceptance criteria with how each is met, the test
additions, CI status, and any assumptions made. Then **stop.**

On **"ship it"** (and only then):
```bash
gh pr merge --merge          # merge commit, matches repo history
git checkout develop && git pull --ff-only origin develop
git branch -d feat/<slug>
```
Report the merge and that local `develop` is synced. Releases (`develop → main` + version
tag) stay a separate, deliberate step via the `git-workflow` skill — never bundle them here.

---

## Checklist (every run)

- [ ] Spec + design in `docs/features/<slug>.md`, assumptions recorded
- [ ] `feat/<slug>` off `develop`
- [ ] Code **and** tests for every acceptance criterion
- [ ] `./gradlew testDebugUnitTest` green + self code-review done
- [ ] Docs/changelog updated (or explicitly noted N/A)
- [ ] PR to `develop` with structured body
- [ ] Stopped for review — merged only on "ship it", never auto-merge
