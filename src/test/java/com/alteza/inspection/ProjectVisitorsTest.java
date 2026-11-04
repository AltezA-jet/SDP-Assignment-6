package com.alteza.inspection;

import com.alteza.inspection.model.*;
import com.alteza.inspection.visitor.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ProjectVisitorsTest {
    @Test void sourceUsesDoubleDispatch() { SourceCodeFile file = new SourceCodeFile("A.java", "a", 10, 2, false, false, true); ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); file.accept(v); assertEquals(1, v.getSourceFiles()); }
    @Test void testUsesDoubleDispatch() { TestFile file = new TestFile("ATest.java", 5, "a", 1); ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); file.accept(v); assertEquals(1, v.getTestFiles()); }
    @Test void detectsHardcodedCredentials() { SecurityAuditVisitor v = new SecurityAuditVisitor(); new SourceCodeFile("A.java", "a", 1, 1, true, false, false).accept(v); assertEquals(1, v.count(SecuritySeverity.CRITICAL)); }
    @Test void detectsVulnerableDependency() { SecurityAuditVisitor v = new SecurityAuditVisitor(); new DependencyFile("pom", 1, List.of(new DependencyInfo("x", "1", true, false))).accept(v); assertEquals(1, v.count(SecuritySeverity.HIGH)); }
    @Test void calculatesSecurityScore() { SecurityAuditVisitor v = new SecurityAuditVisitor(); new ConfigurationFile("a", 1, true, true).accept(v); assertEquals(60, v.getSecurityScore()); }
    @Test void countsFiles() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); Directory d = new Directory("root"); d.addElement(new SourceCodeFile("a", "a", 10, 2, false, false, false)); d.addElement(new TestFile("t", 4, "a", 1)); d.accept(v); assertEquals(2, v.getTotalFiles()); }
    @Test void countsLines() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); new ConfigurationFile("a", 17, false, false).accept(v); assertEquals(17, v.getTotalLinesOfCode()); }
    @Test void calculatesAverageComplexity() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); Directory d = new Directory("r"); d.addElement(new SourceCodeFile("a", "a", 1, 4, false, false, false)); d.addElement(new SourceCodeFile("b", "b", 1, 8, false, false, false)); d.accept(v); assertEquals(6, v.getAverageComplexity()); }
    @Test void calculatesRatio() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); Directory d = new Directory("r"); d.addElement(new SourceCodeFile("a", "a", 1, 1, false, false, false)); d.addElement(new TestFile("t", 1, "a", 1)); d.accept(v); assertEquals(1.0, v.getTestSourceRatio()); }
    @Test void qualityPassesHealthyProject() { QualityGateVisitor v = new QualityGateVisitor(); Directory d = new Directory("r"); d.addElement(new SourceCodeFile("a", "a", 1, 2, false, false, true)); d.addElement(new TestFile("t", 1, "a", 1)); d.accept(v); assertTrue(v.isPassed()); }
    @Test void qualityFailsComplexProject() { QualityGateVisitor v = new QualityGateVisitor(); new SourceCodeFile("a", "a", 1, 11, false, false, false).accept(v); assertFalse(v.isComplexityPassed()); }
    @Test void complianceDetectsMissingVersion() { ComplianceVisitor v = new ComplianceVisitor(); new DependencyFile("pom", 1, List.of(new DependencyInfo("x", "", false, false))).accept(v); assertFalse(v.isCompliant()); }
    @Test void complianceDetectsUntestedProduction() { ComplianceVisitor v = new ComplianceVisitor(); new SourceCodeFile("a", "payments", 1, 1, false, false, true).accept(v); assertTrue(v.getViolations().get(0).contains("without tests")); }
    @Test void nestedDirectoriesAreVisited() { Directory outer = new Directory("o"); Directory inner = new Directory("i"); inner.addElement(new TestFile("t", 2, "a", 1)); outer.addElement(inner); ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); outer.accept(v); assertEquals(1, v.getTotalFiles()); }
    @Test void emptyDirectoryIsSafe() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); new Directory("empty").accept(v); assertEquals(0, v.getTotalFiles()); assertEquals(0, v.getTestSourceRatio()); }
    @Test void dependenciesAreCounted() { ProjectStatisticsVisitor v = new ProjectStatisticsVisitor(); new DependencyFile("pom", 1, List.of(new DependencyInfo("a", "1", false, false), new DependencyInfo("b", "2", false, false))).accept(v); assertEquals(2, v.getTotalDependencies()); }
    @Test void technicalDebtUsesRealMetrics() { TechnicalDebtVisitor v = new TechnicalDebtVisitor(); new SourceCodeFile("complex.java", "risk", 250, 12, false, false, true).accept(v); new DependencyFile("pom", 1, List.of(new DependencyInfo("bad", "1", true, false))).accept(v); assertEquals(8, v.getDebtPoints()); }
}
