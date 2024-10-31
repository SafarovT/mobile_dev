package com.example.lab01

sealed interface Transport

object Scooter : Transport

data class Bicycle(
    val brand: String,
    val frontWheel: BicycleWheel,
    val backWheel: BicycleWheel,
    val frame: Frame
) : Transport

data class BicycleWheel(val diameter: Int)

enum class Frame {
    STEEL,
    CARBON_FIBER,
    TITANIUM
}

sealed interface Car : Transport

data class ICECar(
    val engine: Engine,
    val wheels: List<CarWheel>,
    val steeringWheel: String
) : Car

data class Engine(
    val capacity: Double,
    val fuelType: FuelType
)

enum class FuelType {
    DIESEL,
    OCTANE_92,
    OCTANE_95,
    OCTANE_98,
    OCTANE_100
}

data class ElectricCar(
    val electricMotor: Motor,
    val wheels: List<CarWheel>,
    val autopilot: Autopilot
) : Car

data class CarWheel(
    val diameter: Int,
    val firm: String,
    val disk: Disk
)

enum class Disk {
    CAST,
    FORGED,
    STAMPED
}

data class Motor(
    val power: Int
)

enum class Autopilot {
    YANDEX,
    TESLA
}

fun main() {}