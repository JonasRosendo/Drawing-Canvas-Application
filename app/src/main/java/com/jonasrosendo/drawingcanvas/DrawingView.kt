package com.jonasrosendo.drawingcanvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val drawPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 20f
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val drawPath = Path()

    private lateinit var canvasBitmap: Bitmap
    private lateinit var drawCanvas: Canvas

    init {
        background = ContextCompat.getDrawable(context, R.color.white)
    }

    fun setColor(color: String) {
        val newColor = Color.parseColor(color)
        drawPaint.color = newColor
        invalidate()
    }

    fun getBitMapFromView(): Bitmap {
        return canvasBitmap
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
        drawCanvas.drawColor(ContextCompat.getColor(context, R.color.white))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawBitmap(canvasBitmap, 0f, 0f, null)
        canvas?.drawPath(drawPath, drawPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                drawPath.lineTo(x, y)
                drawCanvas.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
        }

        invalidate()
        return true
    }

    fun clearCanvas() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR)
        invalidate()
    }
}