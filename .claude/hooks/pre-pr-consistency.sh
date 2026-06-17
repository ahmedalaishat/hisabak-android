#!/usr/bin/env bash
# PreToolUse(Bash) hook: when the command is `gh pr create`, inject the pre-PR consistency
# checklist so docs/skills are reviewed against the diff before a PR goes out. For any other
# Bash command, stay silent. Self-gates on the command (the settings.json `if` filter is an
# additional guard but not relied upon). Non-blocking — surfaces the checklist as context.
input="$(cat)"
cmd="$(printf '%s' "$input" | jq -r '.tool_input.command // empty' 2>/dev/null)"

case "$cmd" in
  *"gh pr create"*) ;;   # fall through and emit the checklist
  *) exit 0 ;;           # not a PR creation — say nothing
esac

cat <<'JSON'
{"hookSpecificOutput":{"hookEventName":"PreToolUse","additionalContext":"PRE-PR CONSISTENCY CHECK — before/with this PR, confirm the diff didn't leave any of these stale, and update whatever it touched in the SAME PR: (1) CLAUDE.md — stack/architecture/storage facts, Gradle commands, conventions; (2) README.md — build/run commands, features, badges, tech stack; (3) docs/ — testing.md (test command + coverage), cd.md; (4) .claude/skills/ — git-workflow (Gradle task names, release steps), feature (pipeline commands), hisabak-design/compose-bridge.md (tokens/components vs the app theme); (5) .claude/hooks/run-tests.sh and .github/workflows/*.yml — task names/triggers; (6) CHANGELOG.md — add an entry for user-visible changes. Common triggers: renamed Gradle tasks/variants, changed applicationId/package, changed test/build/run commands, storage/architecture changes, design token/component changes, new dependencies, user-visible behavior. If a follow-up edit is needed, make it and amend the PR before asking to merge."}}
JSON
