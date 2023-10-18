package mysp.bpt.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import mysp.bpt.R;

public abstract class TouchListener {
	private static boolean isClickable = true;

	@SuppressLint("ClickableViewAccessibility")
	public static View.OnTouchListener createTouchListener(Context context) {
		return (view, event) -> {
			float onTouchElevation1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics());
			float afterTouchElevation1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, context.getResources().getDisplayMetrics());

			Animation animationOnTouch1 = AnimationUtils.loadAnimation(context, R.anim.btn_touch);
			Animation animationAfterTouch1 = AnimationUtils.loadAnimation(context, R.anim.btn_not_touch);

			if (!isClickable) {
				return true;
			}

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startClickAnimation(view, animationOnTouch1);
					view.setElevation(onTouchElevation1);
					break;

				case MotionEvent.ACTION_MOVE:
					view.setElevation(afterTouchElevation1);
					break;

				case MotionEvent.ACTION_UP:
					startClickAnimation(view, animationAfterTouch1);
					view.setElevation(onTouchElevation1);
					isClickable = false;
					view.postDelayed(() -> isClickable = true, 300);
					break;

				case MotionEvent.ACTION_CANCEL:
					startClickAnimation(view, animationAfterTouch1);
					break;
			}

			view.setElevation(onTouchElevation1);
			return false;
		};
	}

	private static void startClickAnimation(View view, Animation animation) {
		view.startAnimation(animation);
	}
}