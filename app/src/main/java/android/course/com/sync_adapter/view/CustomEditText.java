package android.course.com.sync_adapter.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by nongdenchet on 5/27/15.
 */
public class CustomEditText extends EditText {
    private boolean isFocus;
    private Paint mPaint;
    private Rect mRect;
    private int widthSize, heightSize;

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initStyle();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initStyle();
    }

    public CustomEditText(Context context) {
        super(context);
        initStyle();
    }

    private void initStyle() {
        setBackground(null);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#B3B3B3"));
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (isFocus) {
            mPaint.setStrokeWidth(3.0f);
            mPaint.setColor(Color.parseColor("#ffffff"));
        } else {
            mPaint.setStrokeWidth(1.5f);
            mPaint.setColor(Color.parseColor("#90ffffff"));
        }
        for (int i = 0; i < getLineCount(); i++) {
            int baseline = getLineBounds(i, mRect);
            canvas.drawLine(mRect.left, baseline + 20, mRect.right, baseline, mPaint);
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        isFocus = focused;
    }
}
