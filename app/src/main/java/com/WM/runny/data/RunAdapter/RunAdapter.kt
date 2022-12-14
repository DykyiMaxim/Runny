package com.WM.runny.data.RunAdapter

import android.graphics.Color
import android.opengl.Visibility
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.WM.runny.R
import com.WM.runny.common.TrackingUtility
import com.WM.runny.data.repository.MainRepository
import com.WM.runny.domain.run.Run
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_run.view.*
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter : RecyclerView.Adapter<RunAdapter.RunViewHolder>() {

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    val diffCallBack = object : DiffUtil.ItemCallback<Run>() {
        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    fun submitList(list: List<Run>) = differ.submitList(list)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_run,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(run.img).into(ivRunImage)

            val callendar = Calendar.getInstance().apply {
                timeInMillis = run.timestampInMills
            }
            val datFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = datFormat.format(callendar.time)

            val avgSpeed = "${run.avgSpeed}km/h"
            tvAvgSpeed.text = avgSpeed

            val distanceKm = "${run.distanceInMeters / 1000f}km"
            tvDistance.text = distanceKm

            tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMills)

            val caloriesBurned = "${run.caloriesBurned}kcal"
            tvCalories.text = caloriesBurned
        }

    }
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}





