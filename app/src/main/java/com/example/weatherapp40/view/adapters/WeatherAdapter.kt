package com.example.weatherapp40.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp40.R
import com.example.weatherapp40.data.model.WeatherModel
import com.example.weatherapp40.databinding.ListItemBinding
import com.example.weatherapp40.viewmodel.MainViewModel
import com.squareup.picasso.Picasso

//адаптер для заполнения recyclerView стартового экрана

class WeatherAdapter(private val model: MainViewModel): ListAdapter<WeatherModel, WeatherAdapter.Holder>(Comparator()) {

    private var onItemClickListener: OnItemClickListener? = null

    class Holder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ListItemBinding.bind(view)
        fun bind(item: WeatherModel) = with(binding){
            val minMaxTemp = "${item.forecast.forecastday[0].day.mintemp_c.toInt()}°C...${item.forecast.forecastday[0].day.maxtemp_c.toInt()}°C"
            val currentTemp = "${item.current.temp_c.toFloat()?.toInt().toString()}°C"
            tvCity.text = item.location.name
            tvCondition.text = item.current.condition.text
            tvCurrentTemp.text = currentTemp
            tvMinMax.text = minMaxTemp
            Picasso.get().load("https:" + item.current.condition.icon).into(imWeather)
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    class Comparator : DiffUtil.ItemCallback<WeatherModel>() {
        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))

        holder.binding.imMain.setOnClickListener {
            model.putWeather(getItem(position))
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}