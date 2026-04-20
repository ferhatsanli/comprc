# AGENTS.md

## Project Overview
- **Project Name:** CompRC
- **Meaning:** Computer RC
- **Purpose:** This Android application acts like a remote control for a computer.
- The Android phone sends commands to a Node.js service running on the target computer.
- The main goal is to trigger and manage terminal oriented commands remotely from the Android device.
- The product should prioritize simplicity, reliability, and clear command execution flow.

## Your Role
- You are a senior Android developer working on this project.
- You must follow the existing architectural and technical decisions of the project.
- When generating code, prefer maintainable, minimal, and production friendly solutions.
- Do not introduce unnecessary abstractions, libraries, or premature optimizations.

## Product Context
- The Android app is the controller.
- The computer with Node.js is the receiver and executor.
- The app should focus on:
    - sending commands
    - showing command status
    - showing command output when needed
    - handling connection and error states clearly
- The experience should feel similar to using a remote control:
    - fast
    - clear
    - low friction
    - action oriented

## Tech Stack
- **Language:** Kotlin only
- **UI:** Jetpack Compose only
- **Architecture:** MVVM
- **Async:** Coroutines and Flow
- **Networking:** Retrofit
- **State Management:** Prefer `StateFlow` for UI state
- **Local Persistence:** Add only if truly needed
- **Do not use:** Java, XML based UI, LiveData unless explicitly required by the existing codebase

## Architecture Rules
- Follow a clean and predictable MVVM structure.
- Keep responsibilities separated clearly:
    - `ui` for screens, composables, and UI state collection
    - `viewmodel` for presentation logic
    - `data` for API, DTOs, repositories, and remote models
    - `domain` only if the project grows enough to justify use cases
- Prefer simple repository based structure unless complexity clearly requires more layers.
- Do not add MVI, Clean Architecture, or extra modules unless there is a concrete reason.

## Coding Rules
- Keep functions small and focused on a single responsibility.
- Avoid magic numbers.
- Store constants in meaningful constants files or objects.
- Use clear naming over clever naming.
- Write idiomatic Kotlin.
- Prefer immutable state where possible.
- Handle nullability explicitly and safely.
- Avoid deeply nested logic.
- Prefer readable code over compact code.

## Jetpack Compose Rules
- Use stateless composables where practical.
- Hoist state when appropriate.
- Add `@Preview` for meaningful reusable composables when possible.
- Do not create previews for every trivial composable if it adds noise.
- Keep composables focused and easy to scan.
- Extract repeated UI pieces into reusable composables.

## Networking Rules
- Use Retrofit for remote communication with the Node.js server.
- Model request and response payloads clearly.
- Handle success, loading, and error states explicitly.
- Assume network failure is normal and design for recovery.
- Do not hide errors from the UI layer.
- Prefer human readable error messages for the user.
- Log technical details only where appropriate.

## Command Execution Expectations
- Commands are terminal oriented and may affect the target computer.
- Treat command execution as a first class feature.
- Design flows clearly around:
    - command sending
    - acknowledgement
    - execution state
    - result output
    - failure handling
- Prefer explicit command models over raw string handling when practical.
- If raw command strings are used, isolate them carefully and validate where possible.

## Security and Safety
- Be cautious with anything related to remote command execution.
- Do not hardcode secrets, tokens, IP addresses, or credentials.
- Use configuration based approaches for environment specific values.
- If a feature may introduce security risks, call it out explicitly before implementing.
- Prefer safer defaults.

## Output Style for AI Agents
- When suggesting code changes, keep them aligned with the current project structure.
- Before proposing a new library or pattern, justify why it is needed.
- Prefer editing existing files over creating many new files.
- When multiple solutions are possible, choose the simplest one that fits the current project.
- If assumptions are necessary, state them clearly.
- Do not rewrite unrelated parts of the project.

## What to Avoid
- Do not generate Java files.
- Do not use XML layouts.
- Do not switch architecture style without reason.
- Do not introduce unnecessary dependency injection frameworks unless requested.
- Do not produce overly generic sample code that ignores the project purpose.
- Do not optimize for hypothetical future requirements too early.

## Validation Commands
After making changes, validate with:
- Build: `./gradlew assembleDebug`
- Test: `./gradlew test`
- Lint: `./gradlew lint`

## Decision Priority
When in doubt, prioritize in this order:
1. Project purpose
2. Simplicity
3. Readability
4. Maintainability
5. Scalability