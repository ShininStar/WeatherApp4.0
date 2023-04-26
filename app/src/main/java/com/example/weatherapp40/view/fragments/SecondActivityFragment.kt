package com.example.weatherapp40.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.example.weatherapp40.R
import com.example.weatherapp40.databinding.FragmentSecondActivityBinding
import com.example.weatherapp40.view.adapters.VpAdapter
import com.example.weatherapp40.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class SecondActivityFragment : Fragment() {


    private val fragmentList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private val titleList = listOf(
        "Hours",
        "Days"
    )

    private lateinit var binding: FragmentSecondActivityBinding
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondActivityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        updateCurrenCard()
    }

    //подключаем vpAdapter и кнопку поделиться
    private fun init() = with(binding) {
        val adapter = VpAdapter(activity as FragmentActivity, fragmentList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp) { tab, pos ->
            tab.text = titleList[pos]
        }.attach()

        ibFrShare.setOnClickListener {
            shareContent()
        }
    }

    //кнопка поделиться погодой
    private fun shareContent() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Смотри какая сегодня погода!")
        intent.putExtra(
            Intent.EXTRA_TEXT, "Привет! Сегодня в ${model.liveDataCurrent.value?.location?.name} ${model.liveDataCurrent.value?.current?.temp_c}°C " +
                "${model.liveDataCurrent.value?.current?.condition?.text} минимум ${model.liveDataCurrent.value?.forecast!!.forecastday[0].day.mintemp_c}°C " +
                "максимум ${model.liveDataCurrent.value?.forecast!!.forecastday[0].day.maxtemp_c}°C")
        startActivity(Intent.createChooser(intent, "Share"))
    }

    //обновление главной карточки второго экрана
    private fun updateCurrenCard() = with(binding) {
        model.liveDataCurrent.observe(viewLifecycleOwner) {
            val minMaxTemp = "${it.forecast.forecastday[0].day.mintemp_c.toInt()}°C..." +
                    "${it.forecast.forecastday[0].day.maxtemp_c.toInt()}°C"
            val currentTemp = "${it.current.temp_c.toInt()}°C"
            tvFrDate.text = it.forecast.forecastday[0].date
            tvFrCity.text = it.location.name
            tvFrCurrentTemp.text = currentTemp.ifEmpty { minMaxTemp }
            tvFrCondition.text = it.current.condition.text
            tvFrMinMax.text = if (currentTemp.isEmpty()) "" else minMaxTemp
            Picasso.get().load("https:" + it.current.condition.icon).into(imFrIcon)
            humidityView.setHumidity(it.current.humidity)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondActivityFragment()
    }
}