package pl.droidsonroids.gif;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class GifTextView extends View {
    public GifTextView(Context context) {
        this(context, null);
    }

    public GifTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
