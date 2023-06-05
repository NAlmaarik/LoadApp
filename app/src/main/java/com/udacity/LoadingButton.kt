package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    // colors
    private var buttonColor = ContextCompat.getColor(context,R.color.colorPrimary)
    private var buttonLoadingColor = ContextCompat.getColor(context,R.color.colorPrimaryDark)
    private var circleColor = ContextCompat.getColor(context,R.color.colorAccent)

    // button text
    private var buttonTextWidth = 0f
    private var buttonTextSize = resources.getDimension(R.dimen.default_text_size)
    private var buttonText = resources.getText(R.string.download)
    private var buttonTextColor = ContextCompat.getColor(context,R.color.white)

    // circle radios
    private var radios = buttonTextSize/2

    // progress
    private var buttonLoadingProgress = 0f
    private var circleLoadingProgress = 0f
    private var duration = 3000

    private val paint = Paint()
    private val valueAnimator = ValueAnimator.ofFloat(0f,1f)

     var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Clicked -> {

            }
            ButtonState.Loading -> {
                buttonText = resources.getString(R.string.loading)
                valueAnimator.duration = duration.toLong()
                valueAnimator.addUpdateListener { updatedAnimation ->
                    buttonLoadingProgress = widthSize * updatedAnimation.animatedValue as Float
                    circleLoadingProgress = 360 * updatedAnimation.animatedValue as Float
                    invalidate()
                }
                valueAnimator.addListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator) {
                        buttonLoadingProgress = 0f
                        circleLoadingProgress = 0f
                        if(buttonState == ButtonState.Loading){
                            buttonState = ButtonState.Loading
                        }
                    }
                })
                valueAnimator.start()
            }
            ButtonState.Completed -> {
                buttonLoadingProgress = 0f
                circleLoadingProgress = 0f
                valueAnimator.cancel()
                buttonText = resources.getString(R.string.download)
                invalidate()
            }
        }
    }


    init {
        paint.apply {
            isAntiAlias = true
            textSize = resources.getDimension(R.dimen.default_text_size)
        }
        context.withStyledAttributes(attrs,R.styleable.LoadingButton){
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor,0)
            buttonLoadingColor = getColor(R.styleable.LoadingButton_buttonLoadingColor,0)
            circleColor = getColor(R.styleable.LoadingButton_circleColor,0)
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawButton(canvas)
        drawButtonProgress(canvas)
        drawButtonText(canvas)
        drawCircleProgress(canvas)
    }

    private fun drawButton(canvas: Canvas?){
        paint.color = buttonColor
        canvas?.drawRect(0f,0f,widthSize.toFloat(),heightSize.toFloat(),paint)
    }

    private fun drawButtonText(canvas: Canvas?){
        paint.color = buttonTextColor
        buttonTextWidth = paint.measureText(buttonText.toString())
        canvas?.drawText(buttonText.toString(),(widthSize/2 - buttonTextWidth/2),(heightSize/2 - (paint.descent() + paint.ascent()) / 2),paint)
    }

    private fun drawButtonProgress(canvas: Canvas?){
        paint.color = buttonLoadingColor
        canvas?.drawRect(0f,0f,buttonLoadingProgress,heightSize.toFloat(),paint)
    }

    private fun drawCircleProgress(canvas: Canvas?){
        paint.color = circleColor
        canvas?.translate(widthSize/2 + buttonTextWidth/2 + radios,heightSize/2 - buttonTextSize/2)
        canvas?.drawArc(RectF(0f,0f,radios*2f,radios*2f),0f,circleLoadingProgress,true,paint)

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}