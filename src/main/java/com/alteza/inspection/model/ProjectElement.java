package com.alteza.inspection.model;

import com.alteza.inspection.visitor.ProjectVisitor;

public interface ProjectElement {
    void accept(ProjectVisitor visitor);
}
