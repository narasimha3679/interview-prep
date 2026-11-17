import { Plugin } from "@opencode/plugin"
import { execFile } from "node:child_process"
import { promisify } from "node:util"

const run = promisify(execFile)
const MESSAGE = "practice: auto-save session progress"

async function git(cwd: string, args: string[]) {
  return run("git", args, { cwd })
}

/**
 * Durability for the practice log.
 *
 * When a session goes idle (a turn finished), commit anything under practice/.
 * If the next turn never happens, the graded answers and the skill matrix are
 * already in git. This is a backstop: the agent still writes in the same reply.
 */
export default Plugin.define({
  id: "practice-autosave",
  async setup(ctx) {
    const controller = new AbortController()
    const cwd = ctx.location.directory

    const save = async () => {
      try {
        const status = await git(cwd, ["status", "--porcelain", "--", "practice"])
        if (!status.stdout.trim()) return
        await git(cwd, ["add", "--", "practice"])
        await git(cwd, ["commit", "-m", MESSAGE, "--", "practice"])
      } catch {
        // not a git repo, git missing, or nothing to commit: never break the turn
      }
    }

    void (async () => {
      for await (const event of ctx.event.subscribe({
        types: ["session.idle"],
        signal: controller.signal,
      })) {
        if (event.type === "session.idle") await save()
      }
    })()

    return () => controller.abort()
  },
})
