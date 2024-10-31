package com.example.lab01

data class Word(val value: String)
data class Context(val name: String)
data class Translate(val value: String)

typealias ContextMap = Map<Context, List<Translate>>

class Translator {
    private val dictionary: MutableMap<Word, MutableMap<Context, MutableList<Translate>>> = mutableMapOf()

    fun add(word: Word, context: Context, translate: Translate) {
        val contexts = dictionary.getOrPut(word) { mutableMapOf() }
        val translates = contexts.getOrPut(context) { mutableListOf() }
        if (!translates.contains(translate)) {
            translates.add(translate)
            println("Перевод добавлен.")
        } else {
            println("Такой перевод уже существует.")
        }
    }

    fun remove(word: Word, context: Context, translate: Translate) {
        val contexts = dictionary[word]
        if (contexts != null) {
            val translates = contexts[context]
            if (translates != null) {
                if (translates.remove(translate)) {
                    println("Перевод удален.")
                    if (translates.isEmpty()) {
                        contexts.remove(context)
                        println("Контекст «${context.name}» удален, так как в нем больше нет переводов.")
                    }
                } else {
                    println("Перевод не найден.")
                }
            } else {
                println("Контекст не найден.")
            }
            if (contexts.isEmpty()) {
                dictionary.remove(word)
                println("Слово «${word.value}» удалено из словаря, так как в нем больше нет контекстов.")
            }
        } else {
            println("Слово не найдено.")
        }
    }

    fun getTranslate(word: Word): ContextMap {
        val contexts = dictionary[word]
        return contexts?.mapValues { it.value.toList() } ?: emptyMap()
    }

    fun words(): Map<Word, ContextMap> {
        return dictionary.mapValues { entry ->
            entry.value.mapValues { it.value.toList() }
        }
    }
}

fun processCommand(command: String, translator: Translator) {
    val parts = command.trim().split("\\s+".toRegex())

    if (parts.isEmpty()) {
        println("Пустая команда.")
    }

    when (parts[0].lowercase()) {
        "add" -> {
            if (parts.size < 4) {
                println("Недостаточно аргументов для команды add.")
            }
            val word = Word(parts[1])
            val context = Context(parts[2])
            val translate = Translate(parts[3])
            translator.add(word, context, translate)
        }

        "remove" -> {
            if (parts.size < 4) {
                println("Недостаточно аргументов для команды remove.")
            }
            val word = Word(parts[1])
            val context = Context(parts[2])
            val translate = Translate(parts[3])
            translator.remove(word, context, translate)
        }

        "translate" -> {
            if (parts.size < 2) {
                println("Недостаточно аргументов для команды translate.")
            }
            val word = Word(parts[1])
            val translations = translator.getTranslate(word)
            if (translations.isEmpty()) {
                println("Переводы для слова «${word.value}» не найдены.")
            } else {
                println("Перевод слова «${word.value}»:")
                for ((context, translates) in translations) {
                    val translatesStr = translates.joinToString(", ") { it.value }
                    println("В контексте «${context.name}»: $translatesStr")
                }
            }
        }

        "print" -> {
            val allWords = translator.words()
            if (allWords.isEmpty()) {
                println("Словарь пуст.")
            } else {
                for ((word, contexts) in allWords) {
                    println("Перевод слова «${word.value}»:")
                    for ((context, translates) in contexts) {
                        val translatesStr = translates.joinToString(", ") { it.value }
                        println("В контексте «${context.name}»: $translatesStr")
                    }
                    println()
                }
            }
        }

        "exit" -> {
            println("Программа завершена.")
        }

        else -> {
            println("Неизвестная команда.")
        }
    }
}

fun main() {
    val translator = Translator()
    var command = ""
    do {
        print("Введите команду: ")
        command = readln()
        processCommand(command, translator)
    } while (command != "exit")
}
