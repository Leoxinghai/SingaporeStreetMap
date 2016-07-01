

package com.facebook.common.file;

import java.io.File;

public interface FileTreeVisitor
{

    public abstract void postVisitDirectory(File file);

    public abstract void preVisitDirectory(File file);

    public abstract void visitFile(File file);
}
