package com.example.lib_aatools.tools.file;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.example.lib_aatools.tools.file.FileViewAdapter.TYPE_LEVER_100;

/**
 * 功能描述: 文件夹实体类
 * </p>
 *
 * @author toperc
 * @data 2020/9/27
 */
public class FileFolderItem extends AbstractExpandableItem<FileItem> implements MultiItemEntity {

    private String folderName;
    private String folderPath;

    public FileFolderItem(String name, String path) {
        this.folderName = name;
        this.folderPath = path;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return TYPE_LEVER_100;
    }
}
