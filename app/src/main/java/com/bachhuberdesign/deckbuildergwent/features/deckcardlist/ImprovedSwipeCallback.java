package com.bachhuberdesign.deckbuildergwent.features.deckcardlist;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.mikepenz.fastadapter.ISwipeable;
import com.mikepenz.fastadapter_extensions.swipe.SimpleSwipeCallback;

import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;

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
        // Disable right swiping
        if (super.getSwipeDirs(recyclerView, viewHolder) != LEFT) {
            return 0;
        }

        if (recyclerView == null || viewHolder == null)
            return super.getSwipeDirs(recyclerView, viewHolder);
        if (viewHolder.itemView.getTag() instanceof ISwipeable) {
            return super.getSwipeDirs(recyclerView, viewHolder);
        } else {
            return 0;
        }
    }

}
