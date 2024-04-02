package com.setalis.amorehr.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.TransitionRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import androidx.viewbinding.ViewBinding
import com.setalis.amorehr.R
import com.setalis.amorehr.replaceApi
import com.setalis.components.R as ComponentR

abstract class AmFragment<T: ViewBinding, P: AmActivity> : Fragment() {

    protected var speechRecognizerIntent: Intent? = null

    lateinit var binding: T

    internal var amActivity: P? = null

    @TransitionRes protected open val elementTransition: Int = ComponentR.transition.element
    @TransitionRes protected open val viewTransition: Int = ComponentR.transition.element

    abstract fun viewBinding(): T

    protected open fun stateInstance(savedInstanceState: Bundle) {}

    /** 1
     *  Default constructor. to define onCreate, called before binding a view
     */
    protected open fun initialize(context: Context, savedInstanceState: Bundle?) {
        val elementAnim = TransitionInflater.from(context).inflateTransition(elementTransition)
        val viewAnim = TransitionInflater.from(context).inflateTransition(viewTransition)

        sharedElementEnterTransition = elementAnim
        sharedElementReturnTransition = elementAnim

        enterTransition = viewAnim
        exitTransition = viewAnim
    }

    /** 2
     *  Initialize data ViewModel
     */
    protected open fun data() {}

    /** 3
     *  Initialize user interface
     */
    protected open fun userInterface(context: Context) {}

    /** 4
     *  Action or listener for any object
     */
    protected open fun listener() {}

    /** 5
     *  Observe ViewModel
     */
    protected open fun observer() {}

    /** 6
     *  State on rebinding
     */
    protected open fun rebinding() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { initialize(it, savedInstanceState) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this::binding.isInitialized) {
            binding
            rebinding()
        } else {
            binding = viewBinding()
            if(savedInstanceState == null) {
                context?.let {
                    @Suppress("UNCHECKED_CAST")
                    amActivity = activity as P

                    data()
                    userInterface(it)
                    listener()
                }
            }else{
                rebinding()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
    }

    // Navigation controller
    fun navigate() = amActivity?.navController

    // Get current saved stack from navigation controller
    fun savedBackStackEntry() = navigate()?.currentBackStackEntry?.savedStateHandle

    // Cast parent activity
    @Suppress("UNCHECKED_CAST")
    fun <T : BaseActivity<ViewBinding>> activity(): T? {
        amActivity?.let {
            try {
                return it as T
            } catch (e: TypeCastException) {
                it.message(getString(R.string.system_type_cast_error))
            }
        }

        return null
    }

    internal fun labelError(labelInfo: AppCompatTextView?, s: String? = null) {
        labelInfo?.apply {
            this.isVisible = !s.isNullOrEmpty()
            this.text = s.toString().replaceApi()

            amActivity?.error(s)
        }
    }


}
