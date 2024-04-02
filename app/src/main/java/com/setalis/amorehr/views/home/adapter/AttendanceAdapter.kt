package com.setalis.amorehr.views.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.setalis.amorehr.R
import com.setalis.amorehr.base.AmItemViewHolder
import com.setalis.amorehr.base.AmRecyclerAdapter
import com.setalis.amorehr.compareTimes
import com.setalis.amorehr.convertTimeFormat
import com.setalis.amorehr.data.models.Attendance
import com.setalis.amorehr.databinding.ItemAttendanceBinding
import com.setalis.components.extensions.getValueOrEmpty
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AttendanceAdapter(
    context: Context,
    data: MutableList<Attendance> = mutableListOf(),
    val radius: Int = 0,
    val itemCreateClickListener:  ((Attendance) -> Unit)? = null,
    val itemClickListener:  ((Attendance) -> Unit)? = null
) : AmRecyclerAdapter<Attendance, AttendanceAdapter.AmViewHolder>(context, data) {

    inner class AmViewHolder(
        context: Context?,
        private val binding: ItemAttendanceBinding,
        itemClickListener: OnItemClickListener?,
        longItemClickListener: OnLongItemClickListener?
    ) : AmItemViewHolder<Attendance>(context, binding, itemClickListener, longItemClickListener) {

        override fun bind(data: Attendance) {
            with(itemView){

                if(data.clock_in != null) {
                    binding.labelTitleClockIn.text = convertTimeFormat(data.clock_in.getValueOrEmpty())
                    binding.labelTitleRadiusCheckIn.text = "%.1f".format(data.distance_in) + " Km"

                    val colorIn = when {
                        (data.distance_in ?: 0.0) <= radius * 0.5 -> R.color.yellow
                        (data.distance_in ?: 0.0) >= radius * 1.5 -> R.color.red
                        else -> R.color.green
                    }

                    binding.labelTitleRadiusCheckIn.setTextColor(ContextCompat.getColor(context, colorIn))

                    binding.labelTitleClockIn.setTextColor(ContextCompat.getColor(context, if(compareTimes(data.clock_in, data.shift_clock_in)) R.color.red else R.color.green))
                }

                if(data.clock_out != null) {
                    binding.labelTitleClockOut.text = convertTimeFormat(data.clock_out.getValueOrEmpty())
                    binding.labelTitleRadiusCheckOut.text = "%.1f".format(data.distance_out) + " Km"

                    val colorOut = when {
                        (data.distance_out ?: 0.0) <= radius * 0.5 -> R.color.yellow
                        (data.distance_out ?: 0.0) >= radius * 1.5 -> R.color.red
                        else -> R.color.green
                    }

                    binding.labelTitleRadiusCheckOut.setTextColor(ContextCompat.getColor(context, colorOut))
                    binding.labelTitleClockOut.setTextColor(ContextCompat.getColor(context, if(compareTimes(data.clock_out, data.shift_clock_out)) R.color.green else R.color.red))
                }
                val formatter = DateTimeFormatter.ofPattern("MMM-dd")
                    .withZone(ZoneId.systemDefault())

                val instant = Instant.parse(data.date)
                val formattedInstant = formatter.format(instant)
                val dates = formattedInstant.split("-")

                binding.labelTitleDate.text = dates.lastOrNull()
                binding.labelTitleMonth.text = dates.firstOrNull()
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmViewHolder {
        val binding = ItemAttendanceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AmViewHolder(
                mContext,
                binding,
                mItemClickListener,
                mLongItemClickListener
        )
    }


}