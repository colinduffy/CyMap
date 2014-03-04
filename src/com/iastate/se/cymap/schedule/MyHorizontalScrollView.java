package com.iastate.se.cymap.schedule;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {

	/**
	 * Multiple constructors for the Scroll View 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyHorizontalScrollView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Helps initialize the scroll views behaviors
	 * @param context
	 */
	void init(Context context) {
		/* Sets the fading of the HSV */
		setHorizontalFadingEdgeEnabled(false);
		setVerticalFadingEdgeEnabled(false);
	}
	
	/**
	 * 
	 * @param children - The children to add to the parent
	 * @param scrollToViewId - The index of the View to scroll to after initialization
	 * @param sizeCallback - A SizeCallback to interact with the HSC
	 */
	public void initView(View[] children, int scrollToViewId, SizeCallback sizeCallback) {
		
		ViewGroup parent = (ViewGroup) getChildAt(0);
		
		/* Add all the children, but keeps them invisible */
		for(int i = 0; i < children.length; i++) {
			children[i].setVisibility(View.INVISIBLE);
			parent.addView(children[i]);
		}
		
		/* Add layout listener */
		OnGlobalLayoutListener listener = new MyOnGlobalLayoutListener(parent, children, scrollToViewId, sizeCallback);
		getViewTreeObserver().addOnGlobalLayoutListener(listener);
	}

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
    	/* Doesn't allow touch right now */
    	return false;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	/* Doesn't allow touch right now */
        return false;
    }
    
    
    class MyOnGlobalLayoutListener implements OnGlobalLayoutListener {

    	ViewGroup parent;
    	View[] children;
    	int scrollToViewId;
    	int scrollToViewPos = 0;
    	SizeCallback sizeCallback;
    	
        public MyOnGlobalLayoutListener(ViewGroup parent, View[] children, int scrollToViewId, SizeCallback sizeCallback) {
        	this.parent = parent;
        	this.children = children;
        	this.scrollToViewId = scrollToViewId;
        	this.sizeCallback = sizeCallback;
        }
        
		public void onGlobalLayout() {
			
			final HorizontalScrollView scrollView = MyHorizontalScrollView.this;
			
			/* The listener will remove itself as a layout listener to the HSV */
			scrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
			
			/* Allow the SizeCallBack to 'see' the Views before we remove them and re-add them */
			/* This lets the SizeCallBack prepare View sizes, ahead of calls to SizeCallback.getviewSize() */
			sizeCallback.onGlobalLayout();
			
			parent.removeViewsInLayout(0, children.length);
			
			final int width = scrollView.getMeasuredWidth();
			final int height = scrollView.getMeasuredHeight();
			
			System.out.println("Width: " + width + ", Height: " + height);
			
			/* Adds each view in turn, and applies the width and height returned by the SizeCallback */
			int[] dims = new int[2];
			scrollToViewPos = 0;
			for (int i = 0; i < children.length; i++) {
				sizeCallback.getViewSize(i, width, height, dims);
				children[i].setVisibility(View.VISIBLE);
				parent.addView(children[i], dims[0], dims[1]);
				if( i < scrollToViewId) {
					scrollToViewPos += dims[0];
				}
			}
			
			new Handler().post(new Runnable() {
				
				public void run() {
					scrollView.scrollBy(scrollToViewPos, 0);
				}
			});
		}
    	
    }

    /**
     * Callback interface to interact with the HSV.
     */
    public interface SizeCallback {
        /**
         * Used to allow clients to measure Views before re-adding them.
         */
        public void onGlobalLayout();

        /**
         * Used by clients to specify the View dimensions.
         * 
         * @param idx
         *            Index of the View.
         * @param w
         *            Width of the parent View.
         * @param h
         *            Height of the parent View.
         * @param dims
         *            dims[0] should be set to View width. dims[1] should be set to View height.
         */
        public void getViewSize(int idx, int w, int h, int[] dims);
    }
}
