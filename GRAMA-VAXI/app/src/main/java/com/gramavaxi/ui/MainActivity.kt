package com.gramavaxi.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gramavaxi.R
import com.gramavaxi.data.Animal
import com.gramavaxi.data.AppDatabase
import com.gramavaxi.worker.VaccineReminderWorker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getInstance(this)

        val nameInput = findViewById<EditText>(R.id.etName)
        val breedInput = findViewById<EditText>(R.id.etBreed)
        val ageInput = findViewById<EditText>(R.id.etAge)
        val addButton = findViewById<Button>(R.id.btnAddAnimal)
        val listView = findViewById<ListView>(R.id.lvAnimals)

        addButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val breed = breedInput.text.toString().trim()
            val age = ageInput.text.toString().toIntOrNull() ?: 0

            if (name.isNotEmpty() && breed.isNotEmpty() && age > 0) {
                val now = System.currentTimeMillis()
                val nextDate = now + TimeUnit.DAYS.toMillis(180)

                lifecycleScope.launch {
                    db.animalDao().insert(
                        Animal(
                            name = name,
                            breed = breed,
                            age = age,
                            lastVaccinationDateMillis = now,
                            nextVaccinationDateMillis = nextDate
                        )
                    )
                    scheduleReminder(nextDate - TimeUnit.DAYS.toMillis(2))
                }

                nameInput.text.clear()
                breedInput.text.clear()
                ageInput.text.clear()
            }
        }

        lifecycleScope.launch {
            db.animalDao().getAnimals().collectLatest { animals ->
                val rows = animals.map {
                    "${it.name} (${it.breed}) - Next Vaccine: ${java.text.DateFormat.getDateInstance().format(it.nextVaccinationDateMillis)}"
                }
                listView.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, rows)
            }
        }
    }

    private fun scheduleReminder(triggerAtMillis: Long) {
        val delay = (triggerAtMillis - System.currentTimeMillis()).coerceAtLeast(1000)
        val request = OneTimeWorkRequestBuilder<VaccineReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(this).enqueue(request)
    }
}
