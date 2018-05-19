package com.termproject.misaka.timesecretary.part;

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

import static com.termproject.misaka.timesecretary.part.DateDividerItemDecoration.getDateDividerGap;

public class EntityDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "EntityDivider";
    private EntityDividerCallback mCallback;
    private TextPaint mTextPaint;
    private Paint mPaint;
    private int mGap;
    private int mTextBottom;
    private int mTextLeft;

    public EntityDividerItemDecoration(Context context, EntityDividerCallback callback) {
        mCallback = callback;
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(context, R.color.white));
        mPaint.setAlpha(100);
        mGap = context.getResources().getDimensionPixelSize(R.dimen.entity_divider_item_decoration_height);
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
        String groupName = mCallback.getEntityName(pos);
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
        String preEntityName, currentEntityName = null;
        String preDateString, currentDateString = null;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            preEntityName = currentEntityName;
            currentEntityName = mCallback.getEntityName(position);
            preDateString = currentDateString;
            currentDateString = mCallback.getDateString(position);
            if (currentEntityName == null || (currentEntityName.equals(preEntityName) && currentDateString.equals(preDateString))) {
                continue;
            }
            String textLine = mCallback.getEntityName(position);
            if (TextUtils.isEmpty(textLine)) {
                continue;
            }
            int childBottom = child.getBottom();
            float bottom = Math.max(mGap + getDateDividerGap(), child.getTop());
            if (position + 1 < itemCount) {
                String nextEntityName = mCallback.getEntityName(position + 1);
                if (!nextEntityName.equals(currentEntityName) && childBottom < bottom) {
                    bottom = childBottom;
                }
            }
            c.drawRect(left, bottom - mGap, right, bottom, mPaint);
            c.drawText(textLine, left + mTextLeft, bottom - mTextBottom, mTextPaint);
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            String prevEntityName = mCallback.getEntityName(pos - 1);
            String nowEntityName = mCallback.getEntityName(pos);
            String preDateString = mCallback.getDateString(pos - 1);
            String nowDateString = mCallback.getDateString(pos);
            return !(prevEntityName.equals(nowEntityName) && preDateString.equals(nowDateString));
        }
    }

    public interface EntityDividerCallback {
        String getEntityName(int position);

        String getDateString(int position);
    }
}
