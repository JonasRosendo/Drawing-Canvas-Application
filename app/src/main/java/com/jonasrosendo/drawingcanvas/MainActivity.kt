package com.jonasrosendo.drawingcanvas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children

class MainActivity : AppCompatActivity() {

    companion object {
        const val FIRST_COLOR_INDEX = 0
    }

    private lateinit var drawingView: DrawingView
    private lateinit var colorsLayout: LinearLayout
    private lateinit var selectedColor: String
    private var isEraserSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawingView = findViewById(R.id.drawingView)
        colorsLayout = findViewById(R.id.colorsLayout)

        val initialPaintViewSelected = colorsLayout.getChildAt(FIRST_COLOR_INDEX) as ImageButton
        selectedColor = initialPaintViewSelected.tag.toString()
        initialPaintViewSelected.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.paint_selected
            )
        )
        drawingView.setColor(selectedColor)

        findViewById<ImageButton>(R.id.saveImageButton).setOnClickListener {
            onSaveButtonClicked()
        }

        findViewById<ImageButton>(R.id.newSheetImageButton).setOnClickListener {
            generateNewSheet()
        }

        findViewById<ImageButton>(R.id.eraserImageButton).setOnClickListener {
            selectEraser()
        }

        findViewById<ImageButton>(R.id.brushImageButton).setOnClickListener {
            selectBrush()
        }
    }

    private fun selectBrush() {
        isEraserSelected = false
        drawingView.setColor(selectedColor)
    }

    private fun selectEraser() {
        isEraserSelected = true
        drawingView.setColor("#FFFFFFFF")
    }

    private fun generateNewSheet() {
        drawingView.clearCanvas()
    }

    private fun onSaveButtonClicked() {
        val bitmap = drawingView.getBitMapFromView()
        val writer = BitmapStorageWriter(this, bitmap)
        writer.saveBitmapOnExternalStorage()
    }

    fun onPaintClicked(view: View) {
        colorsLayout.children.forEach {
            val child = it as ImageButton
            child.setImageDrawable(null)
        }

        val selectedView = (view as ImageButton)
        selectedView.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.paint_selected
            )
        )

        selectedColor = selectedView.tag.toString()

        if (isEraserSelected.not()) {
            drawingView.setColor(selectedColor)
        }
    }
}