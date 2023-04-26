package com.example.weatherapp40.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp40.R
import com.example.weatherapp40.data.Hour
import com.example.weatherapp40.databinding.ListDaysHoursBinding
import com.squareup.picasso.Picasso

//адаптер для заполнения recyclerView второго экрана по часам

class WeatherHourAdapter : ListAdapter<Hour, WeatherHourAdapter.Holder>(Comparator()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ListDaysHoursBinding.bind(view)
        fun bind(item: Hour) = with(binding){
            val temp = "${item.temp_c.toInt()}°C"
            tvDHDate.text = item.time.substringAfter(' ')
            tvDHCondition.text = item.condition.text
            tvDHTemp.text =  temp
            Picasso.get().load("https:" + item.condition.icon).into(imDH)
        }
    }

    class Comparator : DiffUtil.ItemCallback<Hour>() {
        override fun areItemsTheSame(oldItem: Hour, newItem: Hour): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour): Boolean {
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