package com.toperc.lib_aatools.tools.file;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static com.toperc.lib_aatools.tools.file.FileViewAdapter.TYPE_LEVER_101;

/**
 * 功能描述: 文件实体类
 * </p>
 *
 * @author toperc
 * @data 2020/9/27
 */
public class FileItem implements MultiItemEntity {

    private boolean isNull;
    private String fileName;
    private String filePath;

    public FileItem(String name, String path) {
        this.fileName = name;
        this.filePath = path;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

    public FileItem(boolean isNull) {
        this.isNull = isNull;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public int getItemType() {
        return TYPE_LEVER_101;
    }
}
