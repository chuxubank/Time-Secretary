package com.termproject.misaka.timesecretary.part;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.termproject.misaka.timesecretary.R;

public class DateDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "DateDivider";
    private static int mGap;
    private TextPaint mTextPaint;
    private Paint mPaint;
    private final Rect mBounds = new Rect();
    private int mTextBottom;
    private int mTextLeft;
    private DateDividerCallback mCallback;

    public DateDividerItemDecoration(Context context, DateDividerCallback dateDividerCallback) {
        mCallback = dateDividerCallback;
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.white));
        mPaint.setAlpha(180);
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

    public static int getDateDividerGap() {
        return mGap;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildAdapterPosition(view);
        String groupName = mCallback.getDateString(pos);
        if (groupName == null) {
            return;
        }
        if (isFirstInGroup(pos)) {
            outRect.top = mGap;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        final int itemCount = state.getItemCount();
        final int childCount = parent.getChildCount();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        String preDateString, currentDateString = null;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            int position = parent.getChildAdapterPosition(child);
            int oldGap = child.getTop() - mBounds.top;
            if (isFirstInGroup(position)) {
                oldGap -= mGap;
            }
            preDateString = currentDateString;
            currentDateString = mCallback.getDateString(position);
            if (currentDateString == null || currentDateString.equals(preDateString)) {
                continue;
            }
            String textLine = mCallback.getDateString(position).toUpperCase();
            if (TextUtils.isEmpty(textLine)) {
                continue;
            }
            int childBottom = child.getBottom();
            float bottom = Math.max(mGap + oldGap, child.getTop());
            if (position + 1 < itemCount) {
                String nextGroupName = mCallback.getDateString(position + 1);
                if (!nextGroupName.equals(currentDateString) && childBottom < bottom) {
                    bottom = childBottom;
                }
            }
            c.drawRect(left, bottom - mGap - oldGap, right, bottom - oldGap, mPaint);
            c.drawText(textLine, left + mTextLeft, bottom - mTextBottom - oldGap, mTextPaint);
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            String prevGroupName = mCallback.getDateString(pos - 1);
            String nowGroupName = mCallback.getDateString(pos);
            return !prevGroupName.equals(nowGroupName);
        }
    }

    public interface DateDividerCallback {
        String getDateString(int position);
    }
}
