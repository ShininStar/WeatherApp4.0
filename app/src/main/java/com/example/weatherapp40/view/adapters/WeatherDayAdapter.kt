package com.example.weatherapp40.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp40.R
import com.example.weatherapp40.data.model.Forecatday
import com.example.weatherapp40.databinding.ListDaysHoursBinding
import com.squareup.picasso.Picasso

//адаптер для заполнения погоды по дням на втором экране

class WeatherDayAdapter: ListAdapter<Forecatday, WeatherDayAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ListDaysHoursBinding.bind(view)
        fun bind(item: Forecatday) = with(binding){
            val minMaxTemp = "${item.day.mintemp_c.toInt()}°C..." +
                    "${item.day.maxtemp_c.toInt()}°C"
            tvDHDate.text = item.date
            tvDHCondition.text = item.day.condition.text
            tvDHTemp.text =  minMaxTemp
            Picasso.get().load("https:" + item.day.condition.icon).into(imDH)
        }
    }

    class Comparator : DiffUtil.ItemCallback<Forecatday>() {
        override fun areItemsTheSame(oldItem: Forecatday, newItem: Forecatday): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Forecatday, newItem: Forecatday): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_days_hours, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}