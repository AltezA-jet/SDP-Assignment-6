package com.alteza.inspection.visitor;

import com.alteza.inspection.model.*;

public final class ProjectStatisticsVisitor extends AbstractRecursiveProjectVisitor {
    private int totalFiles;
    private int totalLinesOfCode;
    private int sourceFiles;
    private int testFiles;
    private int totalDependencies;
    private int complexitySum;
    @Override public void visit(SourceCodeFile file) { totalFiles++; totalLinesOfCode += file.getLinesOfCode(); sourceFiles++; complexitySum += file.getComplexity(); }
    @Override public void visit(TestFile file) { totalFiles++; totalLinesOfCode += file.getLinesOfCode(); testFiles++; }
    @Override public void visit(ConfigurationFile file) { totalFiles++; totalLinesOfCode += file.getLinesOfCode(); }
    @Override public void visit(DependencyFile file) { totalFiles++; totalLinesOfCode += file.getLinesOfCode(); totalDependencies += file.getDependencies().size(); }
    public int getTotalFiles() { return totalFiles; }
    public int getTotalLinesOfCode() { return totalLinesOfCode; }
    public int getSourceFiles() { return sourceFiles; }
    public int getTestFiles() { return testFiles; }
    public int getTotalDependencies() { return totalDependencies; }
    public double getAverageComplexity() { return sourceFiles == 0 ? 0.0 : (double) complexitySum / sourceFiles; }
    public double getTestSourceRatio() { return sourceFiles == 0 ? 0.0 : (double) testFiles / sourceFiles; }
    public String getReport() {
        return "========== PROJECT STATISTICS ==========\n" + "Total files: " + totalFiles + "\n" +
                "Total LOC: " + totalLinesOfCode + "\n" + "Source files: " + sourceFiles + "\n" +
                "Test files: " + testFiles + "\n" + "Dependencies: " + totalDependencies + "\n" +
                "Average complexity: " + getAverageComplexity() + "\n" + "Test/source ratio: " + getTestSourceRatio();
    }
}
