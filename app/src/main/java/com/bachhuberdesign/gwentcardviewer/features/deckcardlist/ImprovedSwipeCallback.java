package com.bachhuberdesign.gwentcardviewer.features.deckcardlist;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter.ISwipeable;
import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeCallback;

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
public class ImprovedSwipeCallback extends SimpleSwipeCallback {

    public ImprovedSwipeCallback(ItemSwipeCallback itemSwipeCallback, Drawable leaveBehindDrawableLeft) {
        super(itemSwipeCallback, leaveBehindDrawableLeft);
    }

    public ImprovedSwipeCallback(ItemSwipeCallback itemSwipeCallback, Drawable leaveBehindDrawableLeft, int swipeDirs) {
        super(itemSwipeCallback, leaveBehindDrawableLeft, swipeDirs);
    }

    public ImprovedSwipeCallback(ItemSwipeCallback itemSwipeCallback, Drawable leaveBehindDrawableLeft, int swipeDirs, int bgColor) {
        super(itemSwipeCallback, leaveBehindDrawableLeft, swipeDirs, bgColor);
    }

    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (recyclerView == null || viewHolder == null)
            return super.getSwipeDirs(recyclerView, viewHolder);
        if (viewHolder.itemView.getTag() instanceof ISwipeable) {
            return super.getSwipeDirs(recyclerView, viewHolder);
        } else {
            return 0;
        }
    }

}
