#!/usr/bin/env python3
"""Stop hook: make practice progress durable.

Runs after the agent finishes a turn. Two jobs:

1. Auto-commit anything under practice/, so a graded answer or a level change
   cannot be lost if the next turn never happens.
2. Warn when SKILLS.md is older than the newest session log, i.e. the matrix
   was not updated alongside the session file.

Prints nothing on stdout: the harness expects JSON or silence.
"""

import subprocess
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
PRACTICE = ROOT / "practice"
COMMIT_MESSAGE = "practice: auto-save session progress"


def git(*args: str) -> subprocess.CompletedProcess[str]:
    return subprocess.run(
        ["git", "-C", str(ROOT), *args],
        capture_output=True,
        text=True,
        check=False,
    )


def main() -> int:
    if not PRACTICE.is_dir():
        return 0

    status = git("status", "--porcelain", "--", "practice")
    if status.stdout.strip():
        git("add", "--", "practice")
        git("commit", "-m", COMMIT_MESSAGE, "--", "practice")

    sessions = list(PRACTICE.rglob("sessions/*.md"))
    matrices = list(PRACTICE.rglob("SKILLS.md"))
    if sessions and matrices:
        newest_session = max(p.stat().st_mtime for p in sessions)
        newest_matrix = max(p.stat().st_mtime for p in matrices)
        if newest_session > newest_matrix:
            sys.stderr.write(
                "practice: SKILLS.md is older than the newest session log — update the matrix\n"
            )

    return 0


if __name__ == "__main__":
    sys.exit(main())
