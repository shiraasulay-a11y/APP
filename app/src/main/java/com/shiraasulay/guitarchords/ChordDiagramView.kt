package com.shiraasulay.guitarchords

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import kotlin.math.max
import kotlin.math.min

class ChordDiagramView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var shape = ChordShape(intArrayOf(), intArrayOf(), "")
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    fun setShape(value: ChordShape) {
        shape = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (shape.frets.size != 6) return

        val left = 32f
        val right = width - 32f
        val top = 42f
        val bottom = height - 36f
        val sx = (right - left) / 5f
        val sy = (bottom - top) / 5f
        val start = displayStartFret()

        gridPaint.color = 0xFFE2E8F0.toInt()
        gridPaint.strokeWidth = 2.5f

        for (i in 0..5) {
            val x = left + sx * i
            canvas.drawLine(x, top, x, bottom, gridPaint)
            val y = top + sy * i
            canvas.drawLine(left, y, right, y, gridPaint)
        }

        if (start == 1) {
            gridPaint.strokeWidth = 7f
            canvas.drawLine(left, top, right, top, gridPaint)
        } else {
            labelPaint.color = 0xFFCBD5E1.toInt()
            labelPaint.textSize = 13f
            canvas.drawText(start.toString(), left - 17f, top + 5f, labelPaint)
        }

        for (i in 0 until 6) {
            val x = left + sx * i
            val fret = shape.frets[i]
            when {
                fret < 0 -> {
                    labelPaint.color = 0xFFF87171.toInt()
                    labelPaint.textSize = 20f
                    canvas.drawText("×", x, top - 17f, labelPaint)
                }
                fret == 0 -> {
                    labelPaint.color = 0xFF86EFAC.toInt()
                    labelPaint.textSize = 18f
                    canvas.drawText("○", x, top - 17f, labelPaint)
                }
                else -> {
                    val row = fret - start
                    if (row in 0..4) {
                        val y = top + sy * row + sy / 2f
                        dotPaint.color = 0xFF7C5CFF.toInt()
                        canvas.drawCircle(x, y, 15f, dotPaint)
                        val finger = shape.fingers.getOrNull(i) ?: 0
                        if (finger > 0) {
                            labelPaint.color = 0xFFFFFFFF.toInt()
                            labelPaint.textSize = 12f
                            canvas.drawText(finger.toString(), x, y + 4f, labelPaint)
                        }
                    }
                }
            }
        }

        labelPaint.color = 0xFF94A3B8.toInt()
        labelPaint.textSize = 11f
        arrayOf("E", "A", "D", "G", "B", "e").forEachIndexed { i, label ->
            canvas.drawText(label, left + sx * i, bottom + 24f, labelPaint)
        }
    }

    private fun displayStartFret(): Int {
        val pressed = shape.frets.filter { it > 0 }
        if (pressed.isEmpty()) return 1
        val lowest = pressed.minOrNull() ?: 1
        return if (lowest <= 4) 1 else lowest
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasure).coerceAtLeast(220)
        val preferred = (width * 0.72f).toInt().coerceAtLeast(220)
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(heightMeasureSpec)
            MeasureSpec.AT_MOST -> min(preferred, MeasureSpec.getSize(heightMeasureSpec))
            else -> preferred
        }
        setMeasuredDimension(width, max(200, height))
    }
}
