package com.termproject.misaka.timesecretary.part;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.termproject.misaka.timesecretary.R;

public class DateDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "DateDividerItemDecoration";
    private ItemDecorationCallback mCallback;
    private TextPaint mTextPaint;
    private Paint mPaint;
    private int mGap;
    private int mTextBottom;
    private int mTextLeft;

    public DateDividerItemDecoration(Context context, ItemDecorationCallback itemDecorationCallback) {
        mCallback = itemDecorationCallback;

        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.divider));
        mPaint.setAlpha(100);
        mGap = context.getResources().getDimensionPixelSize(R.dimen.date_divider_item_decoration_height);
        mTextBottom = 24;
        mTextLeft = mTextBottom;
        mTextPaint = new TextPaint();
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(40);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        String groupName = mCallback.getGroupName(pos);
        if (groupName == null) {
            return;
        }
        if (isFirstInGroup(pos)) {
            outRect.top = mGap;
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int itemCount = state.getItemCount();
        final int childCount = parent.getChildCount();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        String preGroupName, currentGroupName = null;
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(view);
            preGroupName = currentGroupName;
            currentGroupName = mCallback.getGroupName(position);
            if (currentGroupName == null || currentGroupName.equals(preGroupName)) {
                continue;
            }
            String textLine = mCallback.getGroupName(position).toUpperCase();
//            textLine = textLine.substring(0, textLine.length() - 6);
            if (TextUtils.isEmpty(textLine)) {
                continue;
            }
            int viewBottom = view.getBottom();
            float top = Math.max(mGap, view.getTop());
            if (position + 1 < itemCount) {
                String nextGroupName = mCallback.getGroupName(position + 1);
                if (!nextGroupName.equals(currentGroupName) && viewBottom < top) {
                    top = viewBottom;
                }
            }
            c.drawRect(left, top - mGap, right, top, mPaint);
            c.drawText(textLine, left + mTextLeft, top - mTextBottom, mTextPaint);

        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            String prevGroupName = mCallback.getGroupName(pos - 1);
            String nowGroupName = mCallback.getGroupName(pos);
            return !prevGroupName.equals(nowGroupName);
        }
    }

    public interface ItemDecorationCallback {
        String getGroupName(int position);
    }
}
