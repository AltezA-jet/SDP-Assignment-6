package com.alteza.inspection.visitor;

import com.alteza.inspection.model.Directory;
import com.alteza.inspection.model.ProjectElement;

public abstract class AbstractRecursiveProjectVisitor implements ProjectVisitor {
    @Override public void visit(Directory directory) {
        for (ProjectElement element : directory.getElements()) element.accept(this);
    }
}
