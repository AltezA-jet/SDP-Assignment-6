package com.alteza.inspection.visitor;

public record SecurityIssue(String fileName, String problem, SecuritySeverity severity) { }
