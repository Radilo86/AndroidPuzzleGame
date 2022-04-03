package fpuoc.puzzleandroide;

import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class ControlTactil implements View.OnTouchListener{
    private float xDelta;
    private float yDelta;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getRawX();
        float y = motionEvent.getRawY();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)view.getLayoutParams();
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                xDelta = x - layoutParams.leftMargin;
                yDelta = y - layoutParams.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams.leftMargin = (int)(x - xDelta);
                layoutParams.topMargin = (int)(y - yDelta);
                view.setLayoutParams(layoutParams);
                break;
        }
        return true;
    }
}
