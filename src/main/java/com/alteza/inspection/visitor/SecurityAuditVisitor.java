package com.alteza.inspection.visitor;

import com.alteza.inspection.model.*;
import java.util.ArrayList;
import java.util.List;

public final class SecurityAuditVisitor extends AbstractRecursiveProjectVisitor {
    private final List<SecurityIssue> issues = new ArrayList<>();
    @Override public void visit(SourceCodeFile file) {
        if (file.hasHardcodedCredentials()) add(file.getName(), "Hardcoded credentials", SecuritySeverity.CRITICAL);
        if (file.isHighRisk()) add(file.getName(), "High-risk source file", SecuritySeverity.HIGH);
    }
    @Override public void visit(ConfigurationFile file) {
        if (file.hasExposedCredentials()) add(file.getName(), "Exposed credentials", SecuritySeverity.CRITICAL);
        if (file.hasInsecureSettings()) add(file.getName(), "Insecure configuration", SecuritySeverity.MEDIUM);
    }
    @Override public void visit(DependencyFile file) {
        for (DependencyInfo dependency : file.getDependencies())
            if (dependency.vulnerable()) add(file.getName() + ":" + dependency.name(), "Vulnerable dependency", SecuritySeverity.HIGH);
    }
    @Override public void visit(TestFile file) { }
    public List<SecurityIssue> getIssues() { return List.copyOf(issues); }
    public long count(SecuritySeverity severity) { return issues.stream().filter(i -> i.severity() == severity).count(); }
    public int getSecurityScore() {
        int score = 100 - (int) count(SecuritySeverity.CRITICAL) * 30 - (int) count(SecuritySeverity.HIGH) * 20
                - (int) count(SecuritySeverity.MEDIUM) * 10;
        return Math.max(0, score);
    }
    public String getReport() {
        StringBuilder report = new StringBuilder("========== SECURITY AUDIT ==========\n");
        issues.forEach(i -> report.append(i.fileName()).append(" | ").append(i.problem()).append(" | ").append(i.severity()).append('\n'));
        return report.append("Security Score: ").append(getSecurityScore()).append("/100").toString();
    }
    private void add(String file, String problem, SecuritySeverity severity) { issues.add(new SecurityIssue(file, problem, severity)); }
}
