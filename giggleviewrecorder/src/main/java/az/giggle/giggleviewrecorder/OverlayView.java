package az.giggle.giggleviewrecorder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.Locale;

import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static android.text.TextUtils.getLayoutDirectionFromLocale;
import static android.view.ViewGroup.LayoutParams.FILL_PARENT;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

@SuppressLint("ViewConstructor") // Lint, in this case, I am smarter than you.
public final class OverlayView extends FrameLayout {
    private static final int DURATION_ENTER_EXIT = 300;

    public static OverlayView create(Context context) {
        return new OverlayView(context);
    }

    public static WindowManager.LayoutParams createLayoutParams(Context context) {
        Resources res = context.getResources();
        int width = res.getDimensionPixelSize(R.dimen.overlay_width);
        int height = res.getDimensionPixelSize(R.dimen.overlay_height);
        // TODO Remove explicit "M" comparison when M is released.
        if (Build.VERSION.SDK_INT > LOLLIPOP_MR1 || "M".equals(Build.VERSION.RELEASE)) {
            height = res.getDimensionPixelSize(R.dimen.overlay_height_m);
        }

        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(-1, height, TYPE_SYSTEM_ERROR, FLAG_NOT_FOCUSABLE
                        | FLAG_NOT_TOUCH_MODAL
                        | FLAG_LAYOUT_NO_LIMITS
                        | FLAG_LAYOUT_INSET_DECOR
                        | FLAG_LAYOUT_IN_SCREEN, TRANSLUCENT);
        params.gravity = Gravity.TOP | gravityEndLocaleHack();

        return params;
    }

    @SuppressLint("RtlHardcoded") // Gravity.END is not honored by WindowManager for added views.
    private static int gravityEndLocaleHack() {
        int direction = getLayoutDirectionFromLocale(Locale.getDefault());
        return direction == LAYOUT_DIRECTION_RTL ? Gravity.LEFT : Gravity.RIGHT;
    }


    int animationWidth;


    private OverlayView(Context context) {
        super(context);
        animationWidth = (int)context.getResources().getDimension(R.dimen.overlay_width);
        inflate(context, R.layout.overlay_view, this);

        if (getLayoutDirectionFromLocale(Locale.getDefault()) == LAYOUT_DIRECTION_RTL) {
            animationWidth = -animationWidth;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        setTranslationX(animationWidth);
        animate().translationX(0)
                .setDuration(DURATION_ENTER_EXIT)
                .setInterpolator(new DecelerateInterpolator());
    }

}
