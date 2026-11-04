package com.alteza.inspection.model;

import com.alteza.inspection.visitor.ProjectVisitor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Directory implements ProjectElement {
    private final String name; private final List<ProjectElement> elements = new ArrayList<>();
    public Directory(String name) { this.name = Objects.requireNonNull(name); }
    public String getName() { return name; }
    public void addElement(ProjectElement element) { elements.add(Objects.requireNonNull(element)); }
    public List<ProjectElement> getElements() { return List.copyOf(elements); }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
