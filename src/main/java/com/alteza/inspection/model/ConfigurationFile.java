package com.alteza.inspection.model;

import com.alteza.inspection.visitor.ProjectVisitor;

public final class ConfigurationFile implements ProjectElement {
    private final String name; private final int linesOfCode; private final boolean hasExposedCredentials; private final boolean hasInsecureSettings;
    public ConfigurationFile(String name, int linesOfCode, boolean hasExposedCredentials, boolean hasInsecureSettings) {
        this.name = name; this.linesOfCode = linesOfCode; this.hasExposedCredentials = hasExposedCredentials; this.hasInsecureSettings = hasInsecureSettings;
    }
    public String getName() { return name; }
    public int getLinesOfCode() { return linesOfCode; }
    public boolean hasExposedCredentials() { return hasExposedCredentials; }
    public boolean hasInsecureSettings() { return hasInsecureSettings; }
    @Override public void accept(ProjectVisitor visitor) { visitor.visit(this); }
}
