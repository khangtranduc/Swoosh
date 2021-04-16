package com.example.swoosh.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/* Adapted from it.gilvegliach.android:transparent-text-textview:1.0.3
*  Under the Apache License
* */

@Suppress("DEPRECATION")
class CutoutTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    private var maskBitmap: Bitmap? = null
    private var maskCanvas: Canvas? = null
    private var paint: Paint? = null
    private var maskBackground: Drawable? = null
    private var backgroundBitmap: Bitmap? = null
    private var backgroundCanvas: Canvas? = null

    init {
        paint = Paint()
        paint?.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        super.setTextColor(Color.BLACK)
        super.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onDraw(canvas: Canvas?) {
        if (isNothingToDraw()){
            return
        }
        drawMask()
        drawBackground()
        canvas?.drawBitmap(backgroundBitmap!!, 0f, 0f, null)
    }

    @SuppressLint("WrongCall")
    private fun drawMask(){
        maskCanvas?.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
        super.onDraw(maskCanvas)
    }

    private fun drawBackground(){
        clear(backgroundCanvas!!)
        maskBackground?.draw(backgroundCanvas!!)
        backgroundCanvas?.drawBitmap(maskBitmap!!, 0f, 0f, paint)
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        if (maskBackground == background){
            return
        }

        maskBackground = background

        val w = width
        val h = height
        if (maskBackground != null && w != 0 && h != 0){
            maskBackground!!.setBounds(0, 0, w, h)
        }
    }

    override fun setBackgroundColor(color: Int) {
        setBackgroundDrawable(ColorDrawable(color))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0 || h == 0){
            freeBitmap()
            return
        }

        createBitmaps(w, h)
        if (maskBackground != null){
            maskBackground?.setBounds(0, 0, w, h)
        }
    }

    private fun createBitmaps(w: Int, h: Int){
        backgroundBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        backgroundCanvas = Canvas(backgroundBitmap!!)
        maskBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8)
        maskCanvas = Canvas(maskBitmap!!)
    }

    private fun freeBitmap(){
        backgroundBitmap = null
        backgroundCanvas = null
        maskBitmap = null
        maskCanvas = null
    }

    private fun clear(canvas: Canvas){
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.CLEAR)
    }

    private fun isNothingToDraw(): Boolean = maskBackground == null || width == 0 || height == 0
}