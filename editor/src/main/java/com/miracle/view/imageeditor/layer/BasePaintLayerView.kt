package com.miracle.view.imageeditor.layer

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import com.miracle.view.imageeditor.Utils
import com.miracle.view.imageeditor.bean.SaveStateMarker

/**
 * ## Base paintingLayerView  for [ScrawlView] and [MosaicView]
 *  It's hold move path[paintPath] for user's finger move
 *
 * Created by lxw
 */
abstract class BasePaintLayerView<T : SaveStateMarker> : BaseLayerView<T> {
    protected var paintPath: Path? = null
    protected var currentPathValidate = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun checkInterceptedOnTouchEvent(event: MotionEvent): Boolean {
        val action = event.action and MotionEvent.ACTION_MASK
        if (action == MotionEvent.ACTION_DOWN) {
            if (!validateRect.contains(event.x, event.y)) {
                return false
            }
        }
        return super.checkInterceptedOnTouchEvent(event)
    }

    override fun onFingerDown(downX: Float, downY: Float) {
        super.onFingerDown(downX, downY)
        genDisplayCanvas()
        paintPath = Path()
        val result = Utils.mapInvertMatrixPoint(drawMatrix, PointF(downX, downY))
        paintPath?.moveTo(result.x, result.y)
    }

    override fun onDrag(dx: Float, dy: Float, x: Float, y: Float, rootLayer: Boolean) {
        if (!rootLayer) {
            paintPath?.let {
                val result = Utils.mapInvertMatrixPoint(drawMatrix, PointF(x, y))
                if (interceptDrag(x, y)) {
                    return@let
                }
                it.lineTo(result.x, result.y)
                currentPathValidate = true
                drawDragPath(it)
            }
        }
    }

    private fun upOrCancelFinger() {
        paintPath?.let {
            if (currentPathValidate) {
                val result = savePathOnFingerUp(it)
                result?.let {
                    saveStateMap.put(it.id, it)
                }
            }
        }
        paintPath = null
        currentPathValidate = false
    }

    override fun onFingerCancel() {
        super.onFingerCancel()
        upOrCancelFinger()
    }

    override fun onFingerUp(upX: Float, upY: Float) {
        super.onFingerUp(upX, upY)
        upOrCancelFinger()
    }

    override fun revoke() {
        if (saveStateMap.size > 0) {
            saveStateMap.removeAt(saveStateMap.size - 1)
            redrawAllCache()
        }
    }

    protected open fun drawDragPath(paintPath: Path) {

    }

    protected open fun interceptDrag(x: Float, y: Float) = false

    protected abstract fun savePathOnFingerUp(paintPath: Path): T?
}