#!/usr/bin/env python3
"""Check relative Markdown links in the repository."""

import re
import sys
import urllib.parse
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
LINK_RE = re.compile(r"\[[^\]]*\]\(([^)]+)\)")
SKIP_DIRS = {".git", "node_modules", "target", "build", "out"}


def is_external(link: str) -> bool:
    low = link.lower()
    return (
        low.startswith("http://")
        or low.startswith("https://")
        or low.startswith("mailto:")
        or low.startswith("tel:")
        or low.startswith("#")
        or low.startswith("/")
    )


def main() -> int:
    errors: list[str] = []
    files = [
        p
        for p in ROOT.rglob("*.md")
        if not any(part in SKIP_DIRS for part in p.parts)
    ]

    for path in files:
        text = path.read_text(encoding="utf-8")
        for match in LINK_RE.finditer(text):
            raw = match.group(1).strip()
            if raw.startswith("<") and raw.endswith(">"):
                raw = raw[1:-1].strip()
            if is_external(raw):
                continue
            target = raw.split("#", 1)[0]
            if not target:
                continue
            target = urllib.parse.unquote(target)
            resolved = (path.parent / target).resolve()
            if not resolved.exists():
                errors.append(f"{path.relative_to(ROOT)} -> {raw}")

    if errors:
        print("Broken relative links:")
        for error in sorted(set(errors)):
            print(f"  {error}")
        return 1

    print(f"Checked {len(files)} Markdown files; all relative links resolve.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
