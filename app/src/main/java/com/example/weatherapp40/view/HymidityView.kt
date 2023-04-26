package com.example.weatherapp40.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

//кастомный элемент view в виде круговой диаграммы, рисуем круг в зависимости от параметр влажности
//показывается в главной карточке при переходе на второй экран, внутри показания в цифрах

class HymidityView(context: Context, attrs: AttributeSet?): View(context, attrs) {
    private var humidity = 0

    fun setHumidity(humidity: Int) {
        this.humidity = humidity
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = Color.BLUE
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE

        val angle = humidity.toFloat() / 100 * 360
        val rectF = RectF(50f, 50f, 150f, 150f)

        canvas?.drawArc(rectF, 270f, angle, false, paint)

        val text = "$humidity%"
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 45f
            textAlign = Paint.Align.CENTER
        }
        val textX = width / 2f
        val textY = height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f
        canvas?.drawText(text, textX, textY, textPaint)
    }
}