package com.noplugins.keepfit.userplatform.customize

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.noplugins.keepfit.userplatform.R
import kotlinx.android.synthetic.main.ms_material_spinner.view.*

class PopMaterialSpinner : LinearLayout {

    private var hint: String? = null
    private var hintColor: Int? = null
    private var lineColor: Int? = null
    private var textSize: Int? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.ms_material_spinner, this, true)

        if (attrs == null) throw Exception("Provide hint for the Spinner")

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialSpinner)

        hint = typedArray.getString(R.styleable.MaterialSpinner_ms_hint)
        hintColor = typedArray.getColor(R.styleable.MaterialSpinner_ms_hint_color, ContextCompat.getColor(context, R.color.ms_colorHint))
        lineColor = typedArray.getColor(R.styleable.MaterialSpinner_ms_line_color, ContextCompat.getColor(context, R.color.ms_colorHint))
        textSize = typedArray.getDimensionPixelSize(R.styleable.MaterialSpinner_ms_text_size, context.resources.getDimensionPixelSize(R.dimen.m15))
        typedArray.recycle()

        initData()
    }

    private lateinit var onItemSelect:OnItemSelect

    fun setOnListener(l:OnItemSelect){
        onItemSelect = l
    }

    interface OnItemSelect{
        fun selected(position: Int)
    }

    private fun initData() {
        ms_hint.text = hint
        ms_hint.setTextColor(hintColor!!)
    }

    /**
     * Set hint text
     *
     * @param hint String
     **/
    fun setHint(items: Array<String>) {
        //默认选中最后一项
//        ms_spinner.setSelection(items.size-1,true)
    }

    /**
     * Set Spinner adapter
     *
     * @param adapter A SpinnerAdapter
     **/
    fun setAdapter(adapter: SpinnerAdapter,items: Array<String>) {
        ms_spinner.adapter = adapter
        ms_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
             }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                onItemSelect.selected(p2)
            }

        }



    }

    /**
     * Set Spinner items. Only if items are String Array. For custom items, use setAdapter(...)
     *
     * @param items String Array
     **/
    fun setItems(items: Array<String>) {
        val adapter = object :ArrayAdapter<String>(context,R.layout.spinner_item,items){

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate( R.layout.spinner_item_drop, parent,false)
                val label = view.findViewById<TextView>(R.id.tv_spinner_item)
                label.setText(items.get(position))
                if (ms_spinner.selectedItemPosition==position){
                    label.setTextColor(context.resources.getColor(R.color.btn_text_color))
//                    view.setBackgroundResource(R.color.white)
                }
                return view
            }

            override fun getCount(): Int {
                //返回数据的统计数量，大于0项则减去1项，从而不显示最后一项
                val i = super.getCount()
                return if (i > 0) i - 1 else i
            }
        }
        adapter.setDropDownViewResource(R.layout.spinner_item)
        setAdapter(adapter,items)

    }

    /**
     * Set hint text color
     *
     * @param hintColor A Color
     **/
    fun setHintColor(hintColor: Int) {
        this@PopMaterialSpinner.hintColor = hintColor
        initData()
    }

    /**
     * Set the color of the line below the text
     *
     * @param lineColor A Color
     **/
    fun setLineColor(lineColor: Int) {
        this@PopMaterialSpinner.lineColor = lineColor
        initData()
    }

    /**
     * Returns Spinner object. This can be used to set listeners etc.
     *
     * @return Spinner
     **/
    fun getSpinner(): Spinner = ms_spinner

    /**
     * Returns current selected item in the Spinner
     *
     * @return Object
     **/
    fun getSelectedItem() = ms_spinner.selectedItem

//    private open class StringArrayAdapter(context: Context, items: Array<String>, val textSize: Int) : ArrayAdapter<String>(context, R.layout.spinner_item, items) {
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            val view = super.getView(position, convertView, parent)
//            view.setPadding(context.resources.getDimensionPixelSize(R.dimen.ms_text_margin), 0, 0, 0)
//            (view as TextView).setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//            view.setTextColor(Color.parseColor("#6D7278"))
//            return view
//        }
//    }
}