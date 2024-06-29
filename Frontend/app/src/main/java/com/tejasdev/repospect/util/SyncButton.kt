import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import com.tejasdev.repospect.R

@SuppressLint("ObjectAnimatorBinding")
class SyncButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val syncIcon = AppCompatResources.getDrawable(context, R.drawable.ic_scan)
    private var rotationAnimator: ObjectAnimator? = null

    init {
        // Set padding to accommodate the icon
        val padding = 12
        compoundDrawablePadding = padding
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, syncIcon, null)

        // Set up rotation animation
        rotationAnimator = ObjectAnimator.ofFloat(syncIcon, "rotation", 0f, 360f).apply {
            duration = 1000 // Set rotation duration (milliseconds)
            repeatCount = ObjectAnimator.INFINITE // Repeat indefinitely by default
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    fun setRotation() {
        rotationAnimator?.let {
            if (!it.isRunning) {
                it.start()
            }
        }
    }

    fun unsetRotation() {
        rotationAnimator?.let {
            if (it.isRunning) {
                it.end()
            }
        }
    }
}
