package com.fluentbuild.pcas.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import com.fluentbuild.pcas.R
import com.fluentbuild.pcas.adapters.EngineStatusAdapter
import com.fluentbuild.pcas.models.EngineStatusModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EngineButtonWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): FloatingActionButton(context, attrs, defStyle), Widget<EngineStatusModel> {

    private val buttonAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_repeating)

    init {
        WidgetDelegate(this)
    }

    override val adapter = EngineStatusAdapter(context)

    override fun update(model: EngineStatusModel) {
        setImageDrawable(model.icon)
        backgroundTintList = model.background
        setOnClickListener { model.clickAction.perform(context) }

        if(model.shouldAnimate) {
            startAnimation(buttonAnimation)
        } else {
            clearAnimation()
        }
    }
}