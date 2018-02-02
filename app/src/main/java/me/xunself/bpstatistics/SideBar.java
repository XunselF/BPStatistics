package me.xunself.bpstatistics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.net.InterfaceAddress;

/**
 * Created by XunselF on 2018/2/2.
 */

public class SideBar extends View {

    private OnTouchLetterChangedListener onTouchLetterChangedListener;
    //字母
    public static String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q"
    ,"R","S","T","U","V","W","S","Y","Z","#"};

    private int choose = -1;

    private Paint paint = new Paint();


    private TextView textDialog;

    public void setTextView(TextView textDialog){
        this.textDialog = textDialog;
    }

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int singleHeight = height / letters.length; //获取每个字母的高度

        for (int i = 0; i < letters.length; i++){
            paint.setColor(Color.parseColor("#ffffff"));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextSize(35);
            //抗锯齿
            paint.setAntiAlias(true);

            if (choose == i){
                paint.setFakeBoldText(true);
            }

            float xPos = width / 2 - paint.measureText(letters[i]) / 2;
            float yPos = singleHeight * ( i + 1);

            canvas.drawText(letters[i],xPos,yPos,paint);
            paint.reset();
        }

    }

    /**
     * 对触摸事件进行处理
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //触摸动作
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchLetterChangedListener listener = onTouchLetterChangedListener;
        final int c = (int)(y / getHeight() * letters.length);

        switch(action){
            case MotionEvent.ACTION_UP:
                choose = -1;
                invalidate();
                if (textDialog != null){
                    textDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                if (oldChoose != c){
                    if (c >= 0 && c < letters.length){
                        if (listener != null){
                            listener.onTouchingLetterChanged(letters[c]);
                        }
                        if (textDialog != null){
                            textDialog.setText(letters[c]);
                            textDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnTouchLetterChangedListener(OnTouchLetterChangedListener onTouchLetterChangedListener){
        this.onTouchLetterChangedListener = onTouchLetterChangedListener;
    }


    public interface OnTouchLetterChangedListener{
        void onTouchingLetterChanged(String s);
    }

}
