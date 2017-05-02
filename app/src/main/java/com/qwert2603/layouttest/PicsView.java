package com.qwert2603.layouttest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

public class PicsView extends View {

    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    public PicsView(Context context) {
        super(context);

        paint.setAntiAlias(true);

        circle = Bitmap.createBitmap(600, 600, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circle);
        Path path = new Path();
        path.quadTo(600, 0, 600, 600);
        path.cubicTo(300, 300, 0, 600, 0, 0);
        canvas.drawPath(path, paint);

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public PicsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PicsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Drawable klimt1918 = ResourcesCompat.getDrawable(getResources(), R.drawable.klimt1918, null);
    private Bitmap circle;
    private Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        klimt1918.setBounds(100, 100, 700, 700);
        klimt1918.draw(canvas);

        paint.setXfermode(xfermode);

        canvas.drawBitmap(circle, 100, 100, paint);
    }
}
