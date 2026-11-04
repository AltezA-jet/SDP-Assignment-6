package com.alteza.inspection.visitor;

import com.alteza.inspection.model.*;
import java.util.ArrayList;
import java.util.List;

public final class TechnicalDebtVisitor extends AbstractRecursiveProjectVisitor {
    private int points;
    private final List<String> findings = new ArrayList<>();
    private final List<String> productionModules = new ArrayList<>();
    private final List<String> testedModules = new ArrayList<>();
    @Override public void visit(SourceCodeFile file) {
        if (file.getComplexity() > 10) { points += 2; findings.add(file.getName() + ": complexity above 10 (+2)"); }
        if (file.getLinesOfCode() > 200) { points++; findings.add(file.getName() + ": long file (+1)"); }
        if (file.isProductionModule()) productionModules.add(file.getModuleName());
    }
    @Override public void visit(TestFile file) { testedModules.add(file.getTargetModuleName()); }
    @Override public void visit(ConfigurationFile file) { if (file.hasInsecureSettings()) { points++; findings.add(file.getName() + ": insecure settings (+1)"); } }
    @Override public void visit(DependencyFile file) {
        for (DependencyInfo dependency : file.getDependencies()) {
            if (dependency.vulnerable()) { points += 3; findings.add(dependency.name() + ": vulnerable dependency (+3)"); }
            if (dependency.prohibited()) { points += 2; findings.add(dependency.name() + ": prohibited dependency (+2)"); }
        }
    }
    public int getDebtPoints() {
        int missingTests = 0;
        for (String module : productionModules) if (!testedModules.contains(module)) missingTests++;
        return points + missingTests * 2;
    }
    public List<String> getFindings() { return List.copyOf(findings); }
    public String getReport() { return "========== TECHNICAL DEBT ==========\nPoints: " + getDebtPoints() + "\n" + String.join("\n", findings); }
}
