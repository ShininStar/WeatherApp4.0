package com.example.weatherapp40.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.weatherapp40.Constants
import com.example.weatherapp40.data.WeatherModel
import com.example.weatherapp40.databinding.ActivityMainBinding
import com.example.weatherapp40.view.adapters.ItemTouchHelperCallback
import com.example.weatherapp40.view.adapters.WeatherAdapter
import com.example.weatherapp40.viewmodel.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.weatherapp40.Constants.startCities

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var permissonLauncher: ActivityResultLauncher<String>
    private lateinit var adapter: WeatherAdapter
    private val model: MainViewModel by viewModels()
    private lateinit var fLocationClient: FusedLocationProviderClient
    private var listToSecondActivity = ArrayList<WeatherModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Проверям резрешения
        checkPermission()
        //Провермя разрешения на геопозицию, если оно есть то получаем координаты и отправляем запрос на сервер
        checkLocation()
        init()
        //делаем канал уведомления
        model.createNotificationChannel(this)
        //уведомления о погоде в текущем городе будут показываться каждый час
        model.scheduleNotificationUpdate(this)
        //инициализируем базу данных
        model.initDataBase(this)
    }

    override fun onResume() {
        super.onResume()
        updateRcView()
    }

    //здесь добавляем адаптер, настраиваем перетаскивание элементов и кнопки
    private fun init() = with(binding) {
        rcMainView.layoutManager = GridLayoutManager(this@MainActivity, 2)
        adapter = WeatherAdapter(model)
        rcMainView.adapter = adapter
        val callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(rcMainView)

        //по нажатию на карточку переходим во второй экран, где отображается погода нажатого города по часам и дням
        adapter.setOnItemClickListener(object : WeatherAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                intent.putExtra(Constants.DATA, listToSecondActivity[position])
                startActivity(intent)
            }
        })

        //кнопка обновления данных
        ibMainSync.setOnClickListener {
            startCities.removeAt(startCities.lastIndex)
            getLocation()
        }

        //кнопка поиска другого города, если написать неправильно, то запрос с сервера придет с ошибкой
        //новый город добавится в список и отобразится на главной карточке
        ibMainSearch.setOnClickListener {
            DialogManager.searchByNameDialog(this@MainActivity, object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    name?.let { it1 -> startCities.add(it1) }
                    CoroutineScope(Dispatchers.IO).launch {
                        model.getWeather()
                    }
                }
            })
        }
    }

    private fun updateRcView() {
        model.liveDataMain.observe(this@MainActivity) {
            adapter.submitList(it)
            //элементы этого списка передаются во вторую активити
            listToSecondActivity = it as ArrayList<WeatherModel>
        }
    }

    private fun updateCurrenCard() = with(binding) {
        model.liveDataMain.observe(this@MainActivity) {
            val minMaxTemp =
                "${it[it.lastIndex].forecast.forecastday[0].day.mintemp_c.toInt()}°C...${it[it.lastIndex].forecast.forecastday[0].day.maxtemp_c.toInt()}°C"
            val currentTemp = "${it[it.lastIndex].current.temp_c.toInt()}°C"
            tvMainDate.text = it[it.lastIndex].forecast.forecastday[0].date
            tvMainCity.text = it[it.lastIndex].location.name
            tvMainCurrentTemp.text = currentTemp
            tvMainCondition.text = it[it.lastIndex].current.condition.text
            tvMainMinMax.text = minMaxTemp
            Picasso.get().load("https:" + it[it.lastIndex].current.condition.icon).into(imMainIcon)
        }
    }

    private fun checkLocation() {
        if (isLocationEnabled()) {
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(this, object : DialogManager.Listener {
                override fun onClick(name: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    }

    private fun isLocationEnabled(): Boolean {
        val lm = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun getLocation() {
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        fLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                //Если разрешение получено добавляем координаты в стартовый список городов и обновляем
                //главную карточку и recyclerVieew
                val currentCityLocation = "${it.result.latitude},${it.result.longitude}"
                startCities.add(currentCityLocation)
                CoroutineScope(Dispatchers.IO).launch {
                    model.getWeather()
                }
                updateCurrenCard()
            }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissonListener()
            permissonLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else if (!isPermissionGranted(Manifest.permission.FOREGROUND_SERVICE)) {
            permissonListener()
            permissonLauncher.launch(Manifest.permission.FOREGROUND_SERVICE)
        }
    }

    private fun permissonListener() {
        permissonLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(this, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
}