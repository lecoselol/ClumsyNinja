package lecoselol.clumsyninja.figherie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import lecoselol.clumsyninja.R;

/**
 * Gira tutto!!1! Mi sa che sono sbronzo nero.
 */
public class CosiGirevoli extends View {
    private float mPlaybackPosition = 0f;

    private Paint mPaint;
    private RectF mArcAreas[];
    private float[] mArcStarts;
    private float[] mArcLength;

    public CosiGirevoli(Context context) {
        super(context);
        initCrap();
    }

    public CosiGirevoli(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CosiGirevoli(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCrap();
    }

    private void initCrap() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.ninjaminchia_color));
        mPaint.setStrokeWidth(getResources().getDimension(R.dimen.arc_width));
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mArcAreas = new RectF[3];
        mArcStarts = new float[] {70, 150, 180};
        mArcLength = new float[] {180, 180, 180};
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Just take the biggest square contained in the view bounds
        final float strokeWidth = getResources().getDimension(R.dimen.arc_width);
        float minSize = Math.min(w, h) - strokeWidth;

        mArcAreas[0] = new RectF(w / 2f - minSize / 2f,
                                 h / 2f - minSize / 2f,
                                 w / 2f + minSize / 2f,
                                 h / 2f + minSize / 2f);

        int insetDelta = getResources().getDimensionPixelSize(R.dimen.arc_inset_delta);

        mArcAreas[1] = new RectF(mArcAreas[0]);
        mArcAreas[1].inset(insetDelta, insetDelta);

        mArcAreas[2] = new RectF(mArcAreas[1]);
        mArcAreas[2].inset(insetDelta, insetDelta);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCosettoFico(canvas, 0);
        drawCosettoFico(canvas, 1);
        drawCosettoFico(canvas, 2);
    }

    private void drawCosettoFico(Canvas canvas, int index) {
        // StartPos = (ArcFinalPos - ArcStartPos) * PlayPosition
        // ArcFinalPos = 360 - ArcLength / 2
        final float finalPos = 360 - mArcLength[index] / 2f;
        canvas.drawArc(mArcAreas[index],
                       mArcStarts[index] + (finalPos - mArcStarts[index]) * mPlaybackPosition,
                       mArcLength[index], false, mPaint);
    }

    /**
     * Set the playback position for the rotating stuff.
     * Value is clamped between 0 (beginning) and 1 (end).
     * <p>
     * <strong>Eugenio</strong>: WTF si chiama playback?!
     * </p>
     *
     * @param position The new playback position (0 >= position >= 1).
     */
    public void setPlaybackPosition(float position) {
        if (position < 0f) {
            position = 0f;
        }
        else if (position > 1f) {
            position = 1f;
        }

        mPlaybackPosition = position;
        invalidate();
    }

    /**
     * Returns the current playback position for the
     * rotating stuff.
     * <p>
     * <strong>Eugenio</strong>: WTF si chiama playback?!
     * </p>
     *
     * @return Returns the playback position (0 >= position >= 1).
     */
    public float getPlaybackPosition() {
        return mPlaybackPosition;
    }
}
