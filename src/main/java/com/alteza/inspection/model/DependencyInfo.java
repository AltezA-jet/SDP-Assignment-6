package com.alteza.inspection.model;

public record DependencyInfo(String name, String version, boolean vulnerable, boolean prohibited) { }
