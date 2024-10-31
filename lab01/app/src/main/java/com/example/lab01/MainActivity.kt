package com.example.lab01

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

fun types() {
    // Пример
    val a: Int = 1000

    // Далее добавьте типы
    val b: String = "A"
    val c: String = "log message"
    val d: Double = 3.14
    val e: Int = 3.14.toInt()
    val f: Double = .1
    val g: Char = ' '
    val h: Char = 'c'
    val i: Long = 100_000_000_000_000
    val j: Boolean = false
    val k: Char = '\n'
    val l: String = 123.toString()
}

fun sum(a: Int, b: Int): Int {
    return a + b
}

fun subtract(a: Int, b: Int): Int {
    return a - b
}

fun divide(a: Int, b: Int): Int {
    return a / b
}

fun multiply(a: Int, b: Int): Int {
    return a * b
}

fun pow(a: Int, b: Int): Int {
    return a.toDouble().pow(b).toInt()
}

fun max(numbers: List<Int>): Int {
    return numbers.maxOrNull() ?: throw IllegalArgumentException("List is empty")
}

fun min(numbers: List<Int>): Int {
    return numbers.minOrNull() ?: throw IllegalArgumentException("List is empty")
}

fun printAbout(name: String, age: Int): String {
    return "Привет, меня зовут $name, мне $age лет, через 5 лет мне будет ${age + 5} лет."
}

fun processCommand(command: String): Boolean {
    val parts = command.split(" ")

    when (parts[0]) {
        "sum" -> {
            val a = parts[1].toInt()
            val b = parts[2].toInt()
            val res = sum(a, b)
            println("$a + $b = $res")
        }
        "subtract" -> {
            val a = parts[1].toInt()
            val b = parts[2].toInt()
            val res = subtract(a, b)
            println("$a - $b = $res")
        }
        "divide" -> {
            val a = parts[1].toInt()
            val b = parts[2].toInt()
            val res = divide(a, b)
            println("$a / $b = $res")
        }
        "multiply" -> {
            val a = parts[1].toInt()
            val b = parts[2].toInt()
            val res = multiply(a, b)
            println("$a * $b = $res")
        }
        "pow" -> {
            val a = parts[1].toInt()
            val b = parts[2].toInt()
            val res = pow(a, b)
            println("$a ^ $b = $res")
        }
        "max" -> {
            val numbers = parts.drop(1).map { it.toInt() }
            val res = max(numbers)
            println("Максимальное число = $res")
        }
        "min" -> {
            val numbers = parts.drop(1).map { it.toInt() }
            val res = min(numbers)
            println("Минимальное число = $res")
        }
        "print_list" -> {
            val numbers = parts.drop(1).map { it.toInt() }
            val str = numbers.sorted().joinToString(" < ")
            println("Сортированный список: $str")
        }
        "print_about" -> {
            val name = parts[1]
            val age = parts[2].toInt()
            println(printAbout(name, age))
        }
        "exit" -> {
            println("Программа завершена.")
            return false
        }
        else -> {
            println("Неизвестная команда.")
        }
    }
    return true
}

fun main() {
    var commandLine = ""
    do {
        commandLine = readln()
        processCommand(commandLine)
    } while (commandLine != "exit")
}

