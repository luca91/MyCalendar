package com.mycalendar.tools;

import java.util.ArrayList;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeDetector implements OnTouchListener {
	 private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());

	    public boolean onTouch(final View view, final MotionEvent motionEvent) {

//	       onTouch(view, motionEvent);
	        return gestureDetector.onTouchEvent(motionEvent);

	    }

	    private final class GestureListener extends SimpleOnGestureListener {

	        private static final int SWIPE_THRESHOLD = 100;
	        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

	        @Override
	        public boolean onDown(MotionEvent e) {
	            return true;
	        }

	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

	            boolean result = false;
	            try {
	                float diffY = e2.getY() - e1.getY();
	                float diffX = e2.getX() - e1.getX();
	                if (Math.abs(diffX) > Math.abs(diffY)) {
	                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
	                        if (diffX > 0) {
	                            onSwipeRight();
	                        } else {
	                            onSwipeLeft();
	                        }
	                    }
	                } else {
	                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
	                        if (diffY > 0) {
	                            onSwipeBottom();
	                        } else {
	                            onSwipeTop();
	                        }
	                    }
	                }
	            } catch (Exception exception) {
	                exception.printStackTrace();
	            }
	            return result;
	        }
	    }
	    public void onSwipeRight() {}
	    public void onSwipeLeft() {}
	    public void onSwipeTop() {}
	    public void onSwipeBottom() {}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	private static final int SWIPE_THRESHOLD = 100;
//    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//    
//    private final GestureDetector gestureDetector;
//    private ArrayList<String> actions;
//    private String performAction;
//    		
//    public SwipeDetector(Context ctx){
//    	super();
//    	gestureDetector = new GestureDetector(ctx, this);
//    	actions = new ArrayList<String>();
//    }
//    
//    
//
//    @Override
//    public boolean onDown(MotionEvent e) {
//        return true;
//    }
//    
//    @Override
//    public boolean onSingleTapConfirmed(MotionEvent e) {
//        onTouch(e);
//        return true;
//    }
//
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        boolean result = false;
//        try {
//            float diffY = e2.getY() - e1.getY();
//            float diffX = e2.getX() - e1.getX();
//            if (Math.abs(diffX) > Math.abs(diffY)) {
//                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                    if (diffX > 0) {
//                        onSwipeRight();
//                    } else {
//                        onSwipeLeft();
//                    }
//                }
//            } else {
//               // onTouch(e);
//            }
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//        return result;
//    }
//    
//	public void onTouch(MotionEvent e) {
//	}
//	
//	public void onSwipeRight() {
//		performAction = actions.get(0);
//	}
//	
//	public void onSwipeLeft() {
//		performAction = actions.get(1);
//	}
//	
//	public void onSwipeTop() {
//	}
//	
//	public void onSwipeBottom() {
//	}
//	
//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		return gestureDetector.onTouchEvent(event);
//	}
//	
//	public String getAction(){
//		return performAction;
//	}
//	
//	public void setActions(String...strings){
//		for(String s: strings)
//			actions.add(s);
//	}
}
