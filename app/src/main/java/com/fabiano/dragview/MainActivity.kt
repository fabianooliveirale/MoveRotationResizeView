package com.fabiano.dragview

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var listViews: ArrayList<View>? = null
    var mode = "MOVE"
    var rotationRefX = 0
    var rotationRefY = 0
    var resizeRefX = 0
    var resizeRefY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listViews = arrayListOf(ImageView(this), ImageView(this), ImageView(this))

        listViews?.mapIndexed { index, view ->
            initConnectView(view, index)
            initButtons(view)
            initValue(view)
        }

        initButton()
    }

    private fun initButton() {
        button.setOnClickListener {
            mode = when (mode) {
                "MOVE" -> "RESIZE"
                "RESIZE" -> "ROTATE"
                "ROTATE" -> "MOVE"
                else -> "MOVE"
            }
        }
    }

    private fun initConnectView(view: View, index: Int) {
        val set = ConstraintSet()

        view.id = View.generateViewId()
        constraintMain.addView(view, 0)
        set.clone(constraintMain)
        set.connect(view.id, ConstraintSet.TOP, constraintMain.id, ConstraintSet.TOP, (index * 200))
        set.connect(
            view.id,
            ConstraintSet.LEFT,
            constraintMain.id,
            ConstraintSet.LEFT,
            (index * 200)
        )
        set.applyTo(constraintMain)
    }

    private fun initButtons(view: View) {
        view.setOnTouchListener { v, event ->
            val set = ConstraintSet()
            set.clone(constraintMain)

            val rawX = event.rawX
            val rawY = event.rawY

            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    when (mode) {
                        "MOVE" -> {
                            set.connect(
                                view.id,
                                ConstraintSet.LEFT,
                                constraintMain.id,
                                ConstraintSet.LEFT,
                                event.rawX.toInt() - 150
                            )
                            set.connect(
                                view.id,
                                ConstraintSet.TOP,
                                constraintMain.id,
                                ConstraintSet.TOP,
                                event.rawY.toInt() - 400
                            )
                            set.applyTo(constraintMain)
                        }
                        "RESIZE" -> {
                            val params = view.layoutParams
//                            if(resizeRefX > event.rawX.toInt()){
//                                params.width = params.width - 5
//                            }else{
//                                params.width = params.width + 5
//                            }

                            if(resizeRefY > event.rawY.toInt()){
                                params.height = params.width - 5
                                params.width = params.width - 5
                            }else{
                                params.height = params.width + 5
                                params.width = params.width + 5
                            }

                            resizeRefX = event.rawX.toInt()
                            resizeRefY = event.rawY.toInt()

                            view.layoutParams = params
                        }
                        "ROTATE" -> {
//                            if(rotationRefX > event.rawX.toInt()){
//                                view.rotation = view.rotation - 2
//                            }else{
//                                view.rotation = view.rotation + 2
//                            }

                            if(rotationRefY > event.rawY.toInt()){
                                d("teste", "MAIOR")
                                view.rotation = view.rotation - 5
                            }else{
                                d("teste", "MENOR")
                                view.rotation = view.rotation + 3
                            }

                            rotationRefX = event.rawX.toInt()
                            rotationRefY = event.rawY.toInt()

                            view.rotation = view.rotation + 1
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    rotationRefX = 0
                    rotationRefY = 0
                    resizeRefX = 0
                    resizeRefY = 0
                }
            }
            true
        }
    }

    private fun initValue(view: View) {

        view.setBackgroundColor(Color.GRAY)


        val params = view.layoutParams
        params.width = 200
        params.height = 200
        view.layoutParams = params

        if (view is TextView)
            view.text = "${view.id} OLÁ MUNDO"

        if (view is Button)
            view.text = "${view.id} OLÁ MUNDO"

        if (view is ImageView)
            view.setImageResource(R.drawable.ic_beach_access)

        if (view is TouchImageView)
            view.setImageResource(R.drawable.ic_beach_access)
    }
}

