

package com.facebook.common.file;

import java.io.File;

// Referenced classes of package com.facebook.common.file:
//            FileTreeVisitor

public class FileTree
{

    public FileTree()
    {
    }

    public static boolean deleteContents(File file)
    {
        File files[] = file.listFiles();
        boolean flag1 = true;
        boolean flag = true;
        if(files != null)
        {
            int j = files.length;
            int i = 0;
            do
            {
                flag1 = flag;
                if(i >= j)
                    break;
                flag &= deleteRecursively(files[i]);
                i++;
            } while(true);
        }
        return flag1;
    }

    public static boolean deleteRecursively(File file)
    {
        if(file.isDirectory())
            deleteContents(file);
        return file.delete();
    }

    public static void walkFileTree(File file, FileTreeVisitor filetreevisitor)
    {
        filetreevisitor.preVisitDirectory(file);
        File afile[] = file.listFiles();
        if(afile != null)
        {
            int j = afile.length;
            int i = 0;
            while(i < j)
            {
                File file1 = afile[i];
                if(file1.isDirectory())
                    walkFileTree(file1, filetreevisitor);
                else
                    filetreevisitor.visitFile(file1);
                i++;
            }
        }
        filetreevisitor.postVisitDirectory(file);
    }
}
