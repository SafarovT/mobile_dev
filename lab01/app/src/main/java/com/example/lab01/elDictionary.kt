package com.example.lab01

data class Student(
    val id: Int,
    var name: String,
    var age: Int,
    var points: Int
)

class StudentsJournal {
    private val students = mutableListOf<Student>()
    private var nextId = 1

    fun addStudent(name: String, age: Int, points: Int) {
        val student = Student(nextId++, name, age, points)
        students.add(student)
        println("Студент добавлен с ID ${student.id}.")
    }

    fun removeStudent(id: Int) {
        val iterator = students.iterator()
        var removed = false
        while (iterator.hasNext()) {
            val student = iterator.next()
            if (student.id == id) {
                iterator.remove()
                println("Студент с ID $id удалён.")
                removed = true
                break
            }
        }
        if (!removed) {
            println("Студент с ID $id не найден.")
        }
    }

    fun updatePoints(id: Int, newPoints: Int) {
        val student = students.find { it.id == id }
        if (student != null) {
            student.points = newPoints
            println("Баллы студента с ID $id обновлены до $newPoints.")
        } else {
            println("Студент с ID $id не найден.")
        }
    }

    fun renameStudent(id: Int, newName: String) {
        val student = students.find { it.id == id }
        if (student != null) {
            student.name = newName
            println("Имя студента с ID $id изменено на $newName.")
        } else {
            println("Студент с ID $id не найден.")
        }
    }

    fun printSortByPoints() {
        if (students.isEmpty()) {
            println("Список студентов пуст.")
            return
        }
        val sorted = students.sortedByDescending { it.points }
        println("Студенты, отсортированные по баллам:")
        sorted.forEach { student ->
            println(formatStudent(student))
        }
    }

    fun printSortByNames() {
        if (students.isEmpty()) {
            println("Список студентов пуст.")
            return
        }
        val sorted = students.sortedBy { it.name.lowercase() }
        println("Студенты, отсортированные по именам:")
        sorted.forEach { student ->
            println(formatStudent(student))
        }
    }

    private fun formatStudent(student: Student): String {
        return "${student.id} ${student.name} (${student.age} г.) - ${student.points} баллов"
    }

    fun initializeStudents(input: String) {
        val studentInfos = input.split(",").map { it.trim() }
        for (info in studentInfos) {
            val parts = info.split(" ")
            if (parts.size == 3) {
                val name = parts[0]
                val age = parts[1].toInt()
                val points = parts[2].toInt()
                addStudent(name, age, points)
            } else {
                println("Неверный формат информации о студенте: \"$info\". Пропуск.")
            }
        }
    }
}

fun main() {
    val journal = StudentsJournal()

    println("Введите список студентов в формате: <имя> <возраст> <балл>, <имя> <возраст> <балл>, ...")
    val initialInput = readln()
    journal.initializeStudents(initialInput)

    while (true) {
        print("Введите команду: ")
        val commandLine = readln()
        if (commandLine.isEmpty()) continue

        val parts = commandLine.split(" ")
        val command = parts[0].lowercase()

        when (command) {
            "add" -> {
                if (parts.size != 4) {
                    println("Неверное количество аргументов для команды add.")
                    continue
                }
                val name = parts[1]
                val age = parts[2].toInt()
                val points = parts[3].toInt()
                journal.addStudent(name, age, points)
            }

            "remove" -> {
                if (parts.size != 2) {
                    println("Неверное количество аргументов для команды remove.")
                    continue
                }
                val id = parts[1].toInt()
                journal.removeStudent(id)
            }

            "update_points" -> {
                if (parts.size != 3) {
                    println("Неверное количество аргументов для команды update_points.")
                    continue
                }
                val id = parts[1].toInt()
                val newPoints = parts[2].toInt()
                journal.updatePoints(id, newPoints)
            }

            "rename" -> {
                if (parts.size != 3) {
                    println("Неверное количество аргументов для команды rename.")
                    continue
                }
                val id = parts[1].toInt()
                val newName = parts[2]
                journal.renameStudent(id, newName)
            }

            "print_sort_by_points" -> {
                journal.printSortByPoints()
            }

            "print_sort_by_names" -> {
                journal.printSortByNames()
            }

            "exit" -> {
                println("Программа завершена.")
                break
            }

            else -> {
                println("Неизвестная команда.")
            }
        }
    }
}
