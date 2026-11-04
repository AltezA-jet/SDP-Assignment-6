package com.alteza.inspection.visitor;

import com.alteza.inspection.model.*;

public final class QualityGateVisitor extends AbstractRecursiveProjectVisitor {
    private final ProjectStatisticsVisitor statistics = new ProjectStatisticsVisitor();
    private int criticalIssues;
    @Override public void visit(SourceCodeFile file) { statistics.visit(file); if (file.hasHardcodedCredentials()) criticalIssues++; }
    @Override public void visit(TestFile file) { statistics.visit(file); }
    @Override public void visit(ConfigurationFile file) { statistics.visit(file); if (file.hasExposedCredentials()) criticalIssues++; }
    @Override public void visit(DependencyFile file) { statistics.visit(file); for (DependencyInfo d : file.getDependencies()) if (d.vulnerable()) criticalIssues++; }
    public boolean isComplexityPassed() { return statistics.getAverageComplexity() <= 10; }
    public boolean areTestsPassed() { return statistics.getTestSourceRatio() >= 0.40; }
    public boolean isSecurityPassed() { return criticalIssues == 0; }
    public boolean isPassed() { return isComplexityPassed() && areTestsPassed() && isSecurityPassed(); }
    public String getReport() {
        return "========== QUALITY GATE ==========\n" + "Complexity: " + status(isComplexityPassed()) + "\n" +
                "Tests: " + status(areTestsPassed()) + "\n" + "Security: " + status(isSecurityPassed()) + "\n\n" +
                "FINAL RESULT: " + (isPassed() ? "PASSED" : "FAILED");
    }
    private String status(boolean passed) { return passed ? "PASS" : "FAIL"; }
}
