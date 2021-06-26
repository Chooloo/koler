package com.chooloo.www.koler.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.chooloo.www.koler.R
import com.stedi.randomimagegenerator.ImageParams
import com.stedi.randomimagegenerator.Rig
import com.stedi.randomimagegenerator.RigPalette
import com.stedi.randomimagegenerator.callbacks.GenerateCallback
import com.stedi.randomimagegenerator.generators.ColoredCirclesGenerator

fun Context.generateRandomImage(onBitmapListener: (Bitmap) -> Unit) =
    Rig.Builder()
        .setGenerator(ColoredCirclesGenerator(2))
        .setFixedSize(1920, 1920)
        .setPalette(RigPalette.fromColor(ViewManager(this).getAttrColor(R.attr.colorSecondary)))
        .setCallback(object : GenerateCallback {
            override fun onGenerated(imageParams: ImageParams, bitmap: Bitmap) {
                onBitmapListener.invoke(bitmap)
            }

            override fun onFailedToGenerate(imageParams: ImageParams, e: Exception) {
                TODO("Not yet implemented")
            }
        })
        .build()
        .generate()

fun generateInitials(initials: String, height: Int, width: Int) =
    Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
        Canvas(it).apply {
            drawText(initials, (width / 2).toFloat(), (height / 2).toFloat(), Paint())
        }
    }
