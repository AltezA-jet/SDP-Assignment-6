package com.alteza.inspection.model;

import com.alteza.inspection.visitor.ProjectVisitor;

public final class TestFile implements ProjectElement {
    private final String name; private final int linesOfCode; private final String targetModuleName; private final int testCaseCount;
    public TestFile(String name, int linesOfCode, String targetModuleName, int testCaseCount) {
        this.name = name; this.linesOfCode = linesOfCode; this.targetModuleName = targetModuleName; this.testCaseCount = testCaseCount;
    }
    public String getName() { return name; }
    public int getLinesOfCode() { return linesOfCode; }
    public String getTargetModuleName() { return targetModuleName; }
    public int getTestCaseCount() { return testCaseCount; }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
