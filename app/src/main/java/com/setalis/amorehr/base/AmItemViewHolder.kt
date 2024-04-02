package com.setalis.amorehr.base

import android.content.Context
import android.view.View
import androidx.viewbinding.ViewBinding

abstract class AmItemViewHolder<Data>(
    protected var mContext: Context?,
    itemView: ViewBinding,
    private val mItemClickListener: AmRecyclerAdapter.OnItemClickListener?,
    private val mLongItemClickListener: AmRecyclerAdapter.OnLongItemClickListener?
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView.root), View.OnClickListener,
    View.OnLongClickListener {

    private var isHasHeader = false

    init {
        itemView.root.setOnClickListener(this)
        itemView.root.setOnLongClickListener(this)
    }

    abstract fun bind(data: Data)

    override fun onClick(v: View) {
        mItemClickListener?.onItemClick(
            v,
            if (isHasHeader) adapterPosition - 1 else adapterPosition
        )
    }

    override fun onLongClick(v: View): Boolean {
        if (mLongItemClickListener != null) {
            mLongItemClickListener.onLongItemClick(
                v,
                if (isHasHeader) adapterPosition - 1 else adapterPosition
            )
            return true
        }
        return false
    }
}