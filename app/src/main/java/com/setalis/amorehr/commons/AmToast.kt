package com.setalis.amorehr.commons

import android.content.Context
import android.widget.Toast

/**
 * Created by Al
 * on 01/04/23
 */
class AmToast(context: Context, text: String, duration: Int = LENGTH_SHORT) : Toast(context) {

    init {
        makeText(context, text, duration).show()
    }
}