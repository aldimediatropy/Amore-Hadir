package com.setalis.amorehr.views.home

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.setalis.amorehr.MainActivity
import com.setalis.amorehr.R
import com.setalis.amorehr.base.AmFragment
import com.setalis.amorehr.calculateDuration
import com.setalis.amorehr.convertTimeFormat
import com.setalis.amorehr.data.models.Attendance
import com.setalis.amorehr.databinding.FragmentHomeBinding
import com.setalis.amorehr.isToday
import com.setalis.amorehr.now
import com.setalis.amorehr.viewmodels.AttendViewModel
import com.setalis.amorehr.views.home.adapter.AttendanceAdapter
import com.setalis.components.extensions.getValueOrEmpty
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Crafted by Al (ismealdi) on 21/03/24.
 * Setalis Digital
 */
class HomeFragment: AmFragment<FragmentHomeBinding, MainActivity>() {

    override fun viewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    private val attendViewModel: AttendViewModel by viewModel()

    private var attendanceAdapter: AttendanceAdapter? = null

    override fun userInterface(context: Context) {
        super.userInterface(context)
        binding.apply {
            labelDate.text = now()
            adapterAttendance(context, listAttendances)

            attendViewModel.attendances()
        }
    }

    private fun adapterAttendance(context: Context, list: RecyclerView?) {
        if(attendanceAdapter == null) {
            attendanceAdapter = AttendanceAdapter(context, radius = amActivity?.user?.radius ?: 0)

            list?.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)

                adapter = attendanceAdapter
            }

        }
    }

    override fun observer() {
        super.observer()

        amActivity?.userChanged = {
            binding?.apply {
                labelName.text = it.name + " - " + it.position

                labelCheckIn.text = convertTimeFormat(it.clock_in.getValueOrEmpty())
                labelTextCheckIn.text = getString(R.string.text_belum_clock_in)
                labelLocationCheckIn.text = it.company

                labelCheckOut.text = convertTimeFormat(it.clock_out.getValueOrEmpty())
                labelTextCheckOut.text = getString(R.string.text_belum_clock_out)
                labelLocationCheckOut.text = it.company

                labelValueTotalHours.text = calculateDuration(it.clock_in.getValueOrEmpty(), it.clock_out.getValueOrEmpty())
            }
        }

        attendViewModel.apply {
            loading.observe(viewLifecycleOwner) { amActivity?.loader(it.getEventIfNotHandled() == true) }
            attendances.observe(viewLifecycleOwner) {
                it.getEventIfNotHandled()?.let {
                    attendanceAdapter?.addAndClear(it)
                    initLatest(it.firstOrNull())
                }
            }
        }
    }

    private fun initLatest(item: Attendance?) {
        item?.let {
            if (isToday(item.date.getValueOrEmpty())) {
                binding?.apply {
                    item.clock_in?.let {
                        labelCheckIn.text = convertTimeFormat(it.getValueOrEmpty())
                        labelTextCheckIn.text = getString(R.string.text_check_in)
                        labelLocationCheckIn.text = item.location_clock_in
                        labelValueCheckInText.text = "%.1f".format(item.distance_in) + " Km"
                    }

                    item.clock_out?.let {
                        labelCheckOut.text = convertTimeFormat(it.getValueOrEmpty())
                        labelTextCheckOut.text = getString(R.string.text_check_out)
                        labelLocationCheckOut.text = item.location_clock_out
                        labelValueCheckOutText.text = "%.1f".format(item.distance_out) + " Km"
                    }

                    if(item.clock_in != null && item.clock_out != null) {
                        labelValueTotalHours.text = calculateDuration(item.clock_in.getValueOrEmpty(), item.clock_out.getValueOrEmpty())
                    }
                }
            }
        }
    }

    override fun rebinding() {
        super.rebinding()

        attendViewModel.attendances()
    }

}