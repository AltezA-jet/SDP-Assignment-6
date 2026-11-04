package com.alteza.inspection.model;

import com.alteza.inspection.visitor.ProjectVisitor;
import java.util.List;
import java.util.Objects;

public final class DependencyFile implements ProjectElement {
    private final String name; private final int linesOfCode; private final List<DependencyInfo> dependencies;
    public DependencyFile(String name, int linesOfCode, List<DependencyInfo> dependencies) {
        this.name = name; this.linesOfCode = linesOfCode;
        this.dependencies = List.copyOf(Objects.requireNonNull(dependencies));
    }
    public String getName() { return name; }
    public int getLinesOfCode() { return linesOfCode; }
    public List<DependencyInfo> getDependencies() { return dependencies; }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
