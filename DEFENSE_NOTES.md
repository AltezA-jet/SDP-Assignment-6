# Заметки к защите

## Основная идея

Visitor отделяет операции над структурой проекта от самих элементов. `ProjectElement` задаёт `accept`, а конкретные элементы передают себя в правильную перегрузку `visit`. Поэтому анализаторы не используют `instanceof`, большие `switch` или рефлексию.

`accept()` — первая половина double dispatch, `visit()` — вторая. Вызов `source.accept(visitor)` приводит к `visitor.visit(SourceCodeFile)`, хотя переменная может иметь тип `ProjectElement`. `Directory` рекурсивно вызывает `accept` всех дочерних элементов.

## Анализаторы

- `SecurityAuditVisitor`: credentials — CRITICAL, vulnerable dependency и high-risk source — HIGH, insecure configuration — MEDIUM. Score начинается со 100 и уменьшается на 30/20/10, но не становится отрицательным.
- `ProjectStatisticsVisitor`: считает файловые элементы, LOC, источники, тесты, зависимости, среднюю сложность и ratio `tests / sources` с защитой от деления на ноль.
- `QualityGateVisitor`: complexity <= 10, tests/source >= 0.40, critical issues = 0. Итог PASS только при прохождении всех условий.
- `ComplianceVisitor`: проверяет credentials, версии и prohibited dependencies, а также наличие теста для каждого production module.
- `TechnicalDebtVisitor`: считает долг по сложности, длинным файлам, отсутствующим тестам и уязвимым зависимостям.

Visitor удобен для добавления новых операций, но неудобен для добавления новых типов элементов: новый тип требует новой перегрузки в интерфейсе и реализации во всех visitors. Например, для `DatabaseMigrationFile` нужно добавить `visit(DatabaseMigrationFile)` и обновить visitors, тесты и demo. Strategy, напротив, выбирает взаимозаменяемый алгоритм для одной задачи; Visitor обходит разные типы узлов. SOLID-примеры: SRP, OCP для новых операций, DIP через интерфейс visitor.

## Примеры live modification

Чтобы проверить новый риск, добавьте `SourceCodeFile` с `highRisk=true` в `Main` и снова запустите Maven. Чтобы проверить отсутствие теста, создайте production source с новым `moduleName` без `TestFile.targetModuleName`. Чтобы добавить visitor, унаследуйте его от `AbstractRecursiveProjectVisitor`, реализуйте file visits и вызовите `project.accept(visitor)`.
