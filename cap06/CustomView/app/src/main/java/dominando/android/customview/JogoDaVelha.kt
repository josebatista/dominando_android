package dominando.android.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup

class JogoDaVelha @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = 0
) : View(context, attrs, style) {

    private var tamanho: Int = 0
    private var vez: Int = XIS
    private var tabuleiro = Array(3) { IntArray(3) }
    private val rect: RectF = RectF()
    private lateinit var paint: Paint
    private lateinit var imageX: Bitmap
    private lateinit var imageO: Bitmap

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        imageX = BitmapFactory.decodeResource(resources, R.drawable.x_mark)
        imageO = BitmapFactory.decodeResource(resources, R.drawable.o_mark)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        tamanho = when (layoutParams.width) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                (TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 48F,
                    resources.displayMetrics
                ) * 3).toInt()
            }
            ViewGroup.LayoutParams.MATCH_PARENT -> {
                Math.min(
                    View.MeasureSpec.getSize(widthMeasureSpec),
                    View.MeasureSpec.getSize(heightMeasureSpec)
                )
            }
            else -> layoutParams.width
        }
        setMeasuredDimension(tamanho, tamanho)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val quadrante = (tamanho / 3).toFloat()
        val tamanhoF = tamanho.toFloat()

        //desenhando linhas
        paint.color = Color.BLACK
        paint.strokeWidth = 3F

        //verticais
        canvas?.drawLine(quadrante, 0F, quadrante, tamanhoF, paint)
        canvas?.drawLine(quadrante * 2, 0F, quadrante * 2, tamanhoF, paint)

        //horizontais
        canvas?.drawLine(0F, quadrante, tamanhoF, quadrante, paint)
        canvas?.drawLine(0F, quadrante * 2, tamanhoF, quadrante * 2, paint)

        tabuleiro.forEachIndexed { rowIndex, rowValue ->
            rowValue.forEachIndexed { columnIndex, columnValue ->
                val x = (columnIndex * quadrante)
                val y = (rowIndex * quadrante)

                rect.set(x, y, x + quadrante, y + quadrante)
                if (columnValue == XIS) {
                    canvas?.drawBitmap(imageX, null, rect, null)
                } else if (columnValue == BOLA) {
                    canvas?.drawBitmap(imageO, null, rect, null)
                }
            }
        }
    }

    companion object {
        const val XIS = 1
        const val BOLA = 2
    }

}