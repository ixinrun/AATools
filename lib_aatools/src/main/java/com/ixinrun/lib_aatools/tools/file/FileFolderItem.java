package com.ixinrun.lib_aatools.tools.file;

import static com.ixinrun.lib_aatools.tools.file.FileViewAdapter.TYPE_LEVER_100;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * 功能描述: 文件夹实体类
 * </p>
 *
 * @author ixinrun
 * @date 2020/9/27
 */
public class FileFolderItem extends AbstractExpandableItem<MultiItemEntity> implements MultiItemEntity {

    private String folderName;
    private String folderPath;
    private int top;
    private int left;

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

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
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
