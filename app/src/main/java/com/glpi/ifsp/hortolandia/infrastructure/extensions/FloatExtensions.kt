import android.content.Context
import android.util.TypedValue

fun Float.toDp(context: Context): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this, context.resources.displayMetrics
)