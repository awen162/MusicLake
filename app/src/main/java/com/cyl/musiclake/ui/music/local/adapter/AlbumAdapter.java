package com.cyl.musiclake.ui.music.local.adapter;

import android.os.Build;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cyl.musiclake.R;
import com.cyl.musiclake.bean.Album;
import com.cyl.musiclake.common.Constants;
import com.cyl.musiclake.utils.CoverLoader;

import java.util.List;

public class AlbumAdapter extends BaseQuickAdapter<Album, BaseViewHolder> {

    public AlbumAdapter(List<Album> albumList) {
        super(R.layout.item_playlist_grid, albumList);
    }

    @Override
    protected void convert(BaseViewHolder helper, Album album) {
        helper.setText(R.id.name, album.getName());
        helper.setText(R.id.artist, album.getArtistName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            helper.getView(R.id.album).setTransitionName(Constants.TRANSTITION_ALBUM);
        }
        CoverLoader.loadImageViewById(mContext, album.getId(), helper.getView(R.id.album));
    }
}
