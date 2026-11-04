# Smart Code Inspection & Compliance Engine

An educational Java 17 project that applies the Visitor pattern to a fintech project's source tree. It performs security, statistics, quality-gate, compliance, and technical-debt analysis without placing analysis logic inside model elements.

## Technologies and structure

- Java 17, Maven, JUnit 5, PlantUML source diagrams.
- `model` contains `ProjectElement` and five concrete elements: source, test, configuration, dependency, and directory.
- `visitor` contains the `ProjectVisitor` double-dispatch API, recursive traversal, and analysis visitors.
- `Main` builds a real nested fintech project tree and prints reports computed from its data.

## Visitor architecture

Each element implements `accept(ProjectVisitor)` and calls the overload `visitor.visit(this)`. This is double dispatch: Java selects the visitor operation using both the visitor and concrete element types. `Directory` delegates to every child, so visitors work at any nesting depth.

The four required visitors are `SecurityAuditVisitor`, `ProjectStatisticsVisitor`, `QualityGateVisitor`, and `ComplianceVisitor`. `TechnicalDebtVisitor` is an independent extension. Adding a visitor is localized, while adding a new element requires extending `ProjectVisitor` and every visitor, which is the main Visitor trade-off.

## Run

```bash
mvn clean test
mvn -q exec:java -Dexec.mainClass=com.alteza.inspection.Main
```

The second command may also be replaced by running `com.alteza.inspection.Main` from an IDE. The demonstration prints security score, project statistics, quality-gate status, compliance status, and technical debt.

## Why Visitor?

Visitor keeps operations such as security auditing separate from stable project data classes. This follows single responsibility and open/closed principles for new operations. Strategy would select one interchangeable algorithm for one responsibility; Visitor provides type-specific operations across a heterogeneous object structure. Visitor is a poor fit when element types change frequently or the object graph is very small.

If `DatabaseMigrationFile` is added, it implements `ProjectElement` and calls `visitor.visit(this)`. `ProjectVisitor` and all concrete visitors must gain that overload, plus tests and the demo must be updated. Existing elements need not change, but every visitor interface implementation does.

See [DEFENSE_NOTES.md](DEFENSE_NOTES.md) for defense questions, live-modification examples, and metric formulas. PlantUML sources are in `docs/`.
