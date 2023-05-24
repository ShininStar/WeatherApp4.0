package com.example.weatherapp40.view


import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.weatherapp40.Constants.DATA
import com.example.weatherapp40.R
import com.example.weatherapp40.data.model.WeatherModel
import com.example.weatherapp40.view.fragments.SecondActivityFragment
import com.example.weatherapp40.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        //получаем данные из интент и кладем во viewModel
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(DATA, WeatherModel::class.java)
        else
            intent.getParcelableExtra(DATA)
        data?.let { model.putWeather(it) }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentHolder, SecondActivityFragment.newInstance())
            .commit()
    }
}