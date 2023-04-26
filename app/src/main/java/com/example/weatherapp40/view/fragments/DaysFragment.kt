package com.example.weatherapp40.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp40.databinding.FragmentDaysBinding
import com.example.weatherapp40.view.adapters.WeatherDayAdapter
import com.example.weatherapp40.viewmodel.MainViewModel


class DaysFragment : Fragment() {

    //заоплняем погоду по дням через WeatherDayAdapter
    private lateinit var binding: FragmentDaysBinding
    private lateinit var adapter: WeatherDayAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observe()
    }

    private fun init() = with(binding) {
        adapter = WeatherDayAdapter()
        rcViewDays.layoutManager = LinearLayoutManager(activity)
        rcViewDays.adapter = adapter
    }

    private fun observe() {
        model.liveDataCurrent.observe(viewLifecycleOwner) {
            adapter.submitList(it.forecast.forecastday.subList(1, it.forecast.forecastday.size))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }
}