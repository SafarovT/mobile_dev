package com.example.calculator

import java.util.Stack

class Calculator {
    private fun isOperation(ch: Char): Boolean {
        return ch == '+' || ch == '-' || ch == '×' || ch == '÷'
    }

    private fun hasPriority(op1: Char, op2: Char): Boolean {
        if ((op1 == '×' || op1 == '÷') && (op2 == '+' || op2 == '-')) return false
        return true
    }

    private fun calculateOperation(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '×' -> a * b
            '÷' -> {
                if (b == 0.0) throw ArithmeticException("Деление на ноль")
                a / b
            }
            else -> throw IllegalArgumentException("Недопустимая операция: $op")
        }
    }

    fun calculateExpression(expression: String): Double {
        if (expression.isEmpty()) {
            return 0.0
        }
        val formattedExpression = expression.replace(',', '.')

        val values = Stack<Double>()
        val operators = Stack<Char>()

        var i = 0
        while (i < formattedExpression.length) {
            val ch = formattedExpression[i]

            when {
                ch == ' ' -> i++

                ch.isDigit() || ch == '.' -> {
                    val sb = StringBuilder()
                    while (i < formattedExpression.length && (formattedExpression[i].isDigit() || formattedExpression[i] == '.')) {
                        sb.append(formattedExpression[i++])
                    }
                    values.push(sb.toString().toDouble())
                }

                isOperation(ch) -> {
                    while (operators.isNotEmpty() && hasPriority(ch, operators.peek())) {
                        val operator = operators.pop()
                        val value1 = values.pop()
                        val value2 = values.pop()
                        values.push(calculateOperation(operator, value1, value2))
                    }
                    operators.push(ch)
                    i++
                }

                else -> throw IllegalArgumentException("Недопустимый символ в выражении: $ch")
            }
        }

        while (operators.isNotEmpty()) {
            if (values.size < 2) {
                break
            }
            values.push(calculateOperation(operators.pop(), values.pop(), values.pop()))
        }

        return values.pop()
    }
}