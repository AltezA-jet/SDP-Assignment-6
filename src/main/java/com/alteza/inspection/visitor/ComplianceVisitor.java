package com.alteza.inspection.visitor;

import com.alteza.inspection.model.*;
import java.util.ArrayList;
import java.util.List;

public final class ComplianceVisitor extends AbstractRecursiveProjectVisitor {
    private final List<String> violations = new ArrayList<>();
    private final List<String> productionModules = new ArrayList<>();
    private final List<String> testedModules = new ArrayList<>();
    @Override public void visit(SourceCodeFile file) {
        if (file.hasHardcodedCredentials()) violations.add(file.getName() + ": hardcoded credentials");
        if (file.isProductionModule()) productionModules.add(file.getModuleName());
    }
    @Override public void visit(ConfigurationFile file) { if (file.hasExposedCredentials()) violations.add(file.getName() + ": exposed credentials"); }
    @Override public void visit(DependencyFile file) {
        for (DependencyInfo d : file.getDependencies()) {
            if (d.version() == null || d.version().isBlank()) violations.add(file.getName() + ": missing version for " + d.name());
            if (d.prohibited()) violations.add(file.getName() + ": prohibited dependency " + d.name());
        }
    }
    @Override public void visit(TestFile file) { testedModules.add(file.getTargetModuleName()); }
    public List<String> getViolations() {
        List<String> result = new ArrayList<>(violations);
        for (String module : productionModules) if (!testedModules.contains(module)) result.add("Production module without tests: " + module);
        return List.copyOf(result);
    }
    public boolean isCompliant() { return getViolations().isEmpty(); }
    public String getStatus() { return isCompliant() ? "COMPLIANT" : "NON-COMPLIANT"; }
    public String getReport() { return "========== COMPLIANCE ==========\nStatus: " + getStatus() + "\n" + String.join("\n", getViolations()); }
}
