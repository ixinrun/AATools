package com.example.lib_aatools.tools.file;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.lib_aatools.R;

import java.util.List;

/**
 * @author toperc
 * @data 2020/9/27
 */
public class FileViewAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_LEVER_100 = 100;
    public static final int TYPE_LEVER_101 = 101;

    FileViewAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVER_100, R.layout.file_view_folder_item);
        addItemType(TYPE_LEVER_101, R.layout.file_view_file_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_LEVER_100:
                final FileFolderItem folderItem = (FileFolderItem) item;
                helper.setText(R.id.folder_name_tv, folderItem.getFolderName());
                helper.setText(R.id.folder_path_tv, folderItem.getFolderPath());
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
                        if (folderItem.isExpanded()) {
                            collapse(pos);
                        } else {
                            expand(pos);
                        }
                    }
                });
                break;

            case TYPE_LEVER_101:
                final FileItem fileItem = (FileItem) item;
                TextView emptyView = helper.getView(R.id.empty_view);
                RelativeLayout itemContent = helper.getView(R.id.item_content);

                if (fileItem.isNull()) {
                    emptyView.setVisibility(View.VISIBLE);
                    itemContent.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    itemContent.setVisibility(View.VISIBLE);
                    helper.setText(R.id.file_name_tv, fileItem.getFileName());
                    helper.setText(R.id.file_path_tv, fileItem.getFilePath());
                    helper.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FileUtil.openFile(mContext, fileItem.getFilePath());
                        }
                    });
                }
                break;

            default:
                break;
        }

    }
}
