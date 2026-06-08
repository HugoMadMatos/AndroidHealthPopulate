package com.example.healthseedapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.*
import androidx.health.connect.client.records.metadata.Metadata
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Mass
import androidx.lifecycle.lifecycleScope
import com.example.healthseedapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.*
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var healthConnectClient: HealthConnectClient

    private val permissions = setOf(
        HealthPermission.getWritePermission(StepsRecord::class),
        HealthPermission.getWritePermission(HeartRateRecord::class),
        HealthPermission.getWritePermission(SleepSessionRecord::class),
        HealthPermission.getWritePermission(TotalCaloriesBurnedRecord::class),
        HealthPermission.getWritePermission(WeightRecord::class),
        HealthPermission.getWritePermission(ExerciseSessionRecord::class)
    )

    private val requestPermissionLauncher = registerForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(permissions)) {
            log("✅ All permissions granted!")
        } else {
            val missing = permissions.filter { it !in granted }
            log("❌ Missing permissions: ${missing.size} pending.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sdkStatus = HealthConnectClient.getSdkStatus(this)
        if (sdkStatus == HealthConnectClient.SDK_AVAILABLE) {
            healthConnectClient = HealthConnectClient.getOrCreate(this)
        } else {
            log("Health Connect not available. Status: $sdkStatus")
            disableAllButtons()
            return
        }

        // Individual Seeding Buttons
        binding.btnSeedSteps.setOnClickListener { checkAndSeed { seedSteps() } }
        binding.btnSeedHeartRate.setOnClickListener { checkAndSeed { seedHeartRate() } }
        binding.btnSeedSleep.setOnClickListener { checkAndSeed { seedSleep() } }
        binding.btnSeedCalories.setOnClickListener { checkAndSeed { seedCalories() } }
        binding.btnSeedWeight.setOnClickListener { checkAndSeed { seedWeight() } }
        binding.btnSeedExercise.setOnClickListener { checkAndSeed { seedExercise() } }

        // Bulk Seed All
        binding.btnSeedData.setOnClickListener { checkAndSeed { seedAll() } }

        binding.btnClear.setOnClickListener { clearData() }
    }

    private fun checkAndSeed(seedAction: suspend () -> Unit) {
        lifecycleScope.launch {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            if (granted.containsAll(permissions)) {
                seedAction()
            } else {
                log("Requesting permissions...")
                requestPermissionLauncher.launch(permissions)
            }
        }
    }

    private fun disableAllButtons() {
        binding.btnSeedSteps.isEnabled = false
        binding.btnSeedHeartRate.isEnabled = false
        binding.btnSeedSleep.isEnabled = false
        binding.btnSeedCalories.isEnabled = false
        binding.btnSeedWeight.isEnabled = false
        binding.btnSeedExercise.isEnabled = false
        binding.btnSeedData.isEnabled = false
        binding.btnClear.isEnabled = false
    }

    private fun log(message: String) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.tvLog.append("$message\n")
        }
    }

    private suspend fun seedSteps() = performSeed("Steps") { generateSteps() }
    private suspend fun seedHeartRate() = performSeed("Heart Rate") { generateHeartRate() }
    private suspend fun seedSleep() = performSeed("Sleep") { generateSleep() }
    private suspend fun seedCalories() = performSeed("Calories") { generateCalories() }
    private suspend fun seedWeight() = performSeed("Weight") { generateWeight() }
    private suspend fun seedExercise() = performSeed("Exercise") { generateExercise() }

    private suspend fun seedAll() {
        performSeed("All (Bulk)") {
            generateSteps() + generateHeartRate() + generateSleep() + 
            generateCalories() + generateWeight() + generateExercise()
        }
    }

    private suspend fun performSeed(label: String, generator: () -> List<Record>) {
        withContext(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSeedData.isEnabled = false
        }
        try {
            log("Seeding $label...")
            val records = withContext(Dispatchers.IO) { generator() }
            healthConnectClient.insertRecords(records)
            log("✅ $label: ${records.size} records inserted.")
        } catch (e: Exception) {
            log("❌ Error in $label: ${e.message}")
        } finally {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                binding.btnSeedData.isEnabled = true
            }
        }
    }

    // Generators
    private fun generateSteps(): List<Record> {
        val records = mutableListOf<Record>()
        val startDate = Instant.now().minus(90, ChronoUnit.DAYS)
        for (i in 0 until 90) {
            val dayStart = startDate.plus(i.toLong(), ChronoUnit.DAYS)
            records.add(StepsRecord(
                count = Random.nextLong(4000, 14001),
                startTime = dayStart.plus(8, ChronoUnit.HOURS),
                startZoneOffset = ZoneOffset.UTC,
                endTime = dayStart.plus(20, ChronoUnit.HOURS),
                endZoneOffset = ZoneOffset.UTC,
                metadata = Metadata.manualEntry()
            ))
        }
        return records
    }

    private fun generateHeartRate(): List<Record> {
        val records = mutableListOf<Record>()
        val startDate = Instant.now().minus(90, ChronoUnit.DAYS)
        for (i in 0 until 90) {
            val dayStart = startDate.plus(i.toLong(), ChronoUnit.DAYS)
            for (hour in 0..22 step 2) {
                val time = dayStart.plus(hour.toLong(), ChronoUnit.HOURS)
                records.add(HeartRateRecord(
                    samples = listOf(HeartRateRecord.Sample(time, Random.nextLong(58, 111))),
                    startTime = time, startZoneOffset = ZoneOffset.UTC,
                    endTime = time, endZoneOffset = ZoneOffset.UTC,
                    metadata = Metadata.manualEntry()
                ))
            }
        }
        return records
    }

    private fun generateSleep(): List<Record> {
        val records = mutableListOf<Record>()
        val startDate = Instant.now().minus(90, ChronoUnit.DAYS)
        for (i in 0 until 90) {
            val dayStart = startDate.plus(i.toLong(), ChronoUnit.DAYS)
            val sleepStart = dayStart.plus((21 + Random.nextInt(0, 3)).toLong(), ChronoUnit.HOURS).plus(Random.nextInt(0, 60).toLong(), ChronoUnit.MINUTES)
            val sleepDuration = Duration.ofMinutes(Random.nextLong(330, 511))
            records.add(SleepSessionRecord(
                startTime = sleepStart, startZoneOffset = ZoneOffset.UTC,
                endTime = sleepStart.plus(sleepDuration), endZoneOffset = ZoneOffset.UTC,
                metadata = Metadata.manualEntry()
            ))
        }
        return records
    }

    private fun generateCalories(): List<Record> {
        val records = mutableListOf<Record>()
        val startDate = Instant.now().minus(90, ChronoUnit.DAYS)
        for (i in 0 until 90) {
            val dayStart = startDate.plus(i.toLong(), ChronoUnit.DAYS)
            records.add(TotalCaloriesBurnedRecord(
                energy = Energy.calories(Random.nextDouble(1800.0, 3200.0)),
                startTime = dayStart, startZoneOffset = ZoneOffset.UTC,
                endTime = dayStart.plus(1, ChronoUnit.DAYS), endZoneOffset = ZoneOffset.UTC,
                metadata = Metadata.manualEntry()
            ))
        }
        return records
    }

    private fun generateWeight(): List<Record> {
        val records = mutableListOf<Record>()
        val startDate = Instant.now().minus(90, ChronoUnit.DAYS)
        for (i in 0 until 90) {
            val dayStart = startDate.plus(i.toLong(), ChronoUnit.DAYS)
            records.add(WeightRecord(
                time = dayStart.plus(12, ChronoUnit.HOURS), zoneOffset = ZoneOffset.UTC,
                weight = Mass.kilograms(Random.nextDouble(68.0, 75.0)),
                metadata = Metadata.manualEntry()
            ))
        }
        return records
    }

    private fun generateExercise(): List<Record> {
        val records = mutableListOf<Record>()
        val startDate = Instant.now().minus(90, ChronoUnit.DAYS)
        for (i in 0 until 90) {
            val dayStart = startDate.plus(i.toLong(), ChronoUnit.DAYS)
            val dayOfWeek = dayStart.atZone(ZoneId.systemDefault()).dayOfWeek
            if (dayOfWeek in listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY)) {
                val duration = Duration.ofMinutes(Random.nextLong(30, 61))
                val type = if (Random.nextBoolean()) ExerciseSessionRecord.EXERCISE_TYPE_RUNNING else ExerciseSessionRecord.EXERCISE_TYPE_STRENGTH_TRAINING
                records.add(ExerciseSessionRecord(
                    exerciseType = type,
                    startTime = dayStart.plus(17, ChronoUnit.HOURS), startZoneOffset = ZoneOffset.UTC,
                    endTime = dayStart.plus(17, ChronoUnit.HOURS).plus(duration), endZoneOffset = ZoneOffset.UTC,
                    metadata = Metadata.manualEntry()
                ))
            }
        }
        return records
    }

    private fun clearData() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                log("Clearing data from the last 90 days...")
                val filter = TimeRangeFilter.between(Instant.now().minus(90, ChronoUnit.DAYS), Instant.now())
                val types = listOf(StepsRecord::class, HeartRateRecord::class, SleepSessionRecord::class, TotalCaloriesBurnedRecord::class, WeightRecord::class, ExerciseSessionRecord::class)
                types.forEach { healthConnectClient.deleteRecords(it, filter) }
                log("✅ Data cleared.")
            } catch (e: Exception) {
                log("❌ Error clearing data: ${e.message}")
            }
        }
    }
}
