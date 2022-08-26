import java.io.File

fun main() {
    generateNames()
}

val vowels = listOf('a', 'e', 'i', 'o', 'u', 'y', 'å', 'ä', 'ö')
val consonants =
    listOf('b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'z', 'x')
val maxVowelsOrConsonantsInARow = 3
val maxLength = 40

fun generateNames(order: Int = 3, fileName: String = "sv.txt", nameCount: Int = 100) {
    val input = buildList {
        File(fileName).forEachLine {
            add(it)
        }
    }

    val ngrams = HashMap<String, MutableList<Char>>()
    val startVales = HashSet<String>()

    input
        .map { "$it." }
        .forEach { line ->
            startVales.addAll(line.split(" "))

            line.windowed(order + 1) { window ->
                val ngram = window.take(order).toString()
                val next = window.last()
                ngrams.computeIfAbsent(ngram) {
                    ArrayList()
                }
                ngrams[ngram]!!.add(next)
            }
        }

    println("Generating names: ")
    val names = buildList {
        while (size < nameCount) {
            var name = startVales.random().removeSuffix(".")
            while (true) {
                var next = ngrams[name.takeLast(order)]?.random() ?: ('a'..'z').random()
                val lastChars = (name.takeLast(maxVowelsOrConsonantsInARow) + next).toList()
                val lastWord = name.split(" ").last()
                when {
                    next == '.' && !name.contains(" ") -> next = ' '
                    next == '.' -> {
                        if (!input.contains(name) && !contains(name)) {
                            print("=")
                            add(name.split(" ").joinToString(separator = " ") { it.capitalize() })
                        }
                        break
                    }
                    name.length > 40 -> break
                    consonants.containsAll(lastChars) || vowels.containsAll(lastChars) -> continue
                    lastWord.length >= 20 -> break
                }
                name += next
            }
        }
    }

    println()
    println("Generated names:")
    names.forEach { println(it) }
}
