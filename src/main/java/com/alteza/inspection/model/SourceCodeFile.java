package com.alteza.inspection.model;

import com.alteza.inspection.visitor.ProjectVisitor;

public final class SourceCodeFile implements ProjectElement {
    private final String name;
    private final String moduleName;
    private final int linesOfCode;
    private final int complexity;
    private final boolean hasHardcodedCredentials;
    private final boolean highRisk;
    private final boolean productionModule;

    public SourceCodeFile(String name, String moduleName, int linesOfCode, int complexity,
                          boolean hasHardcodedCredentials, boolean highRisk, boolean productionModule) {
        this.name = name; this.moduleName = moduleName; this.linesOfCode = linesOfCode;
        this.complexity = complexity; this.hasHardcodedCredentials = hasHardcodedCredentials;
        this.highRisk = highRisk; this.productionModule = productionModule;
    }
    public String getName() { return name; }
    public String getModuleName() { return moduleName; }
    public int getLinesOfCode() { return linesOfCode; }
    public int getComplexity() { return complexity; }
    public boolean hasHardcodedCredentials() { return hasHardcodedCredentials; }
    public boolean isHighRisk() { return highRisk; }
    public boolean isProductionModule() { return productionModule; }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
