package org.mewx.wenku8.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.mewx.wenku8.R;
import org.mewx.wenku8.global.GlobalConfig;
import org.mewx.wenku8.global.api.NovelItemInfo;
import org.mewx.wenku8.global.api.Wenku8API;
import org.mewx.wenku8.listener.MyItemClickListener;
import org.mewx.wenku8.listener.MyItemLongClickListener;
import org.mewx.wenku8.util.LightCache;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MewX on 2015/1/20.
 * Olde Novel Item Adapter.
 */
public class NovelItemAdapter extends RecyclerView.Adapter<NovelItemAdapter.ViewHolder> {

    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;
    private List<NovelItemInfo> mDataset;

    // empty list, then use append method to add list elements
    public NovelItemAdapter() {
        mDataset = new ArrayList<>();
    }

    public NovelItemAdapter(List<NovelItemInfo> dataset) {
        super();
        mDataset = dataset; // reference
    }

    public void RefreshDataset(List<NovelItemInfo> dataset) {
        mDataset = dataset; // reference
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.view_novel_item, null);
        return new ViewHolder(view, mItemClickListener, mItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        // set text
        if(viewHolder.tvNovelTitle != null)
            viewHolder.tvNovelTitle.setText(mDataset.get(i).getTitle());
        if(viewHolder.tvNovelAuthor != null)
            viewHolder.tvNovelAuthor.setText(mDataset.get(i).getAuthor());
        if(viewHolder.tvNovelStatus != null)
            viewHolder.tvNovelStatus.setText(Wenku8API.getStatusBySTATUS(Wenku8API.getSTATUSByInt(mDataset.get(i).getStatus())));
        if(viewHolder.tvNovelUpdate != null)
            viewHolder.tvNovelUpdate.setText(mDataset.get(i).getUpdate());
        if(viewHolder.tvNovelIntro != null)
            viewHolder.tvNovelIntro.setText(mDataset.get(i).getIntroShort());

        // need to solve flicking problem
        if(LightCache.testFileExist(GlobalConfig.getFirstStoragePath() + "imgs" + File.separator + mDataset.get(i).getAid() + ".jpg"))
            ImageLoader.getInstance().displayImage("file://" + GlobalConfig.getFirstStoragePath() + "imgs" + File.separator + mDataset.get(i).getAid() + ".jpg", viewHolder.ivNovelCover);
        else if(LightCache.testFileExist(GlobalConfig.getSecondStoragePath() + "imgs" + File.separator + mDataset.get(i).getAid() + ".jpg"))
            ImageLoader.getInstance().displayImage("file://" + GlobalConfig.getSecondStoragePath() + "imgs" + File.separator + mDataset.get(i).getAid() + ".jpg", viewHolder.ivNovelCover);
        else
            ImageLoader.getInstance().displayImage(Wenku8API.getCoverURL(mDataset.get(i).getAid()), viewHolder.ivNovelCover);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }

    /**
     * View Holder:
     * Called by RecyclerView to display the data at the specified position.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MyItemClickListener mClickListener;
        private MyItemLongClickListener mLongClickListener;
        public int position;
        public boolean isLoading = false;

        private ImageButton ibNovelOption;
        public ImageView ivNovelCover;
        public TextView tvNovelTitle;
        public TextView tvNovelStatus;
        public TextView tvNovelAuthor;
        public TextView tvNovelUpdate;
        public TextView tvNovelIntro;

        public ViewHolder(View itemView, MyItemClickListener clickListener, MyItemLongClickListener longClickListener) {
            super(itemView);
            this.mClickListener = clickListener;
            this.mLongClickListener = longClickListener;
            itemView.findViewById(R.id.item_card).setOnClickListener(this);
            itemView.findViewById(R.id.item_card).setOnLongClickListener(this);

            // get all views
            ibNovelOption = (ImageButton) itemView.findViewById(R.id.novel_option);
            ivNovelCover = (ImageView) itemView.findViewById(R.id.novel_cover);
            tvNovelTitle = (TextView) itemView.findViewById(R.id.novel_title);
            tvNovelAuthor = (TextView) itemView.findViewById(R.id.novel_author);
            tvNovelStatus = (TextView) itemView.findViewById(R.id.novel_status);
            tvNovelUpdate = (TextView) itemView.findViewById(R.id.novel_update);
            tvNovelIntro = (TextView) itemView.findViewById(R.id.novel_intro);

            // test current fragment
            if(!GlobalConfig.testInBookshelf())
                ibNovelOption.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            if(mClickListener != null){
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(mLongClickListener != null){
                mLongClickListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }

}