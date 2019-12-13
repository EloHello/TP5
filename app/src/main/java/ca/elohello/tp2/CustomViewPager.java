package ca.elohello.tp2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import org.json.JSONException;

import java.lang.reflect.Field;

/**
 * Custom view pager.
 */
public class CustomViewPager extends ViewPager {

    /**
     * Constructor
     * @param context
     */
    public CustomViewPager(Context context) {
        super(context);
        setMyScroller();
    }

    /**
     * Constructor with arguments
     * @param context
     * @param attrs
     */
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            if(!PrefManager.getInstance().getSettings().getBoolean("betafunctions"))
            {
                return false;
            }
        }
        catch (JSONException ex)
        {
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if(!PrefManager.getInstance().getSettings().getBoolean("betafunctions"))
            {
                return false;
            }
        }
        catch (JSONException ex)
        {
        }
        return super.onTouchEvent(event);
    }

    //down one is added for smooth scrolling

    /**
     * Set the scroller
     */
    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Viewpager scroller. (Speed / durations)
     */
    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }
}