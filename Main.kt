package flashcards
import java.io.File
import kotlin.math.log

fun TempForAskFunction(answer: String, map: Map<String, String>): String? {

    for (i in map.keys) {
        if (map[i] == answer) {
            return i
        }
    }
    return null
}

fun theMenu(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    printLogs(log, "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
    val action = readLogs(log)
    if (action != "exit") {
        callFunction(action, map, log, tracker)
    }
    else {
        return
        // exitFunction(log)
    }
}

fun callFunction(action: String, map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    if (action == "add") addFunction(map, log, tracker)
    else if (action == "remove") removeFunction(map, log, tracker)
    else if (action == "import") importFunction(map, log, tracker)
    else if (action == "export") exportFunction(map, log, tracker)
    else if (action == "ask") askFunction(map, log, tracker)
    else if (action == "log") logFunction(map, log, tracker)
    else if (action == "hardest card") hardestCardFun(map, log, tracker)
    else if (action == "reset stats") resetStatsFun(map, log, tracker)
    else return theMenu(map, log, tracker)
}

fun resetStatsFun(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    for (k in tracker.keys) {
        tracker[k] = 0
    }
    printLogs(log, "Card statistics have been reset.")
    return theMenu(map, log, tracker)
}

fun hardestCardSubFunction(tracker: MutableMap<String, Int>): Int {
    var n = 0
    var m = 0
    for ((k, v) in tracker) {
        if (v > n) {
            n = v
        }

    }
    for ((k, v) in tracker) {
        if (v == n && v != 0) {
            m++
        }
    }
    return m
}

fun hardestCardFun(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    if (hardestCardSubFunction(tracker) == 0) {
        printLogs(log, "There are no cards with errors.")
    }
    else if (hardestCardSubFunction(tracker) == 1) {
        var n = 0
        var m = ""
        for ((k, v) in tracker) {
            if (v > n) {
                n = v
                m = k
            }
        }
        printLogs(log, "The hardest card is \"${m}\". You have $n errors answering it.")
    } else {
        var n = 0
        var m = ""
        for ((k, v) in tracker) {
            if (v > n) {
                n = v
            }
        }
        val hardestTerms = tracker.filterValues { it == n }
        printLogs(log, "The hardest cards are")
        for (i in hardestTerms.keys){
            printLogsNoNewLine(log, " \"$i\"")
            if (i != hardestTerms.keys.last()) {
                printLogsNoNewLine(log, ",")
            } else printLogsNoNewLine(log, ".")
        }
        printLogs(log, "You have $n errors answering them.")
    }
    return theMenu(map, log, tracker)
}

fun trackerAddFunction(term: String, tracker: MutableMap<String, Int>) {
    tracker[term] = 0
}

fun trackerRemoveFunction(term: String, tracker: MutableMap<String, Int>) {
    tracker.remove(term)
}

fun logFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    printLogs(log, "File name: ")
    val fileName = readLogs(log)
    val file = File(fileName)
    if (file.exists()) {
        file.writeText("")
        for (line in log) {
            file.appendText("$line\n")
        }
    } else {
        for (line in log) {
            file.appendText("$line\n")
        }
    }
    println("The log has been saved")
    file.appendText("The log has been saved")
    return theMenu(map, log, tracker)

}

fun askFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    printLogs(log, "How many times to ask?")
    val numberOfCards = readLogs(log).toInt()
    repeat(numberOfCards) {
        val i = map.keys.random()
        printLogs(log, "Print the definition of \"${i}\":")
        val answer = readLogs(log)
        if (map[i] == answer) {
            printLogs(log, "Correct!")
        } else if (map.containsValue(answer)) {
            val temp = TempForAskFunction(answer, map)
            tracker[i] = tracker[i]!! + 1
            printLogs(log, "Wrong. The right answer is \"${map.getValue(i)}\", but your definition is correct for \"$temp\".")
        } else {
            tracker[i] = tracker[i]!! + 1
            printLogs(log, "Wrong. The right answer is \"${map.getValue(i)}\"")
        }
    }
    return theMenu(map, log, tracker)
}

fun exportFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    printLogs(log, "File name:")
    val myFile = readLogs(log)
    // val separator = File.separator
    // val filePath = "src${separator}flashcards${separator}${myFile}"
    // val filePath = "C:${separator}Users${separator}Jakub${separator}IdeaProjects${separator}Flashcards${separator}Flashcards${separator}task${separator}src${separator}flashcards${separator}${myFile}"
    val file = File(myFile)
    val orderedList = mutableListOf<String>()
    var n = 0
    for ((key, value) in map) {
        orderedList.add(key)
        orderedList.add(value)
        if (tracker.getValue(key) != 0) orderedList.add(tracker.getValue(key).toString())
        n++
    }
    if (file.exists()) {
        for (line in orderedList) {
            if (line == orderedList.first()) file.writeText("${line}\n")
            else {
                if (line == orderedList.last()) file.appendText(line) else file.appendText("${line}\n")
            }
        }
    } else {
        for (line in orderedList) {
            file.appendText(line)
            if (line != orderedList.last()) file.appendText("\n")
        }
    }
    printLogs(log, "$n cards have been saved.")
    return theMenu(map, log, tracker)
}

fun importFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    // val separator = File.separator
    printLogs(log, "File name: ")
    val importFileName = readln()
    // val filePath = importFileName
    // val filePath = "src${separator}flashcards${separator}${importFileName}"
    // val filePath = "C:${separator}Users${separator}Jakub${separator}IdeaProjects${separator}Flashcards${separator}Flashcards${separator}task${separator}src${separator}flashcards${separator}${importFileName}"
    val file = File(importFileName)
    if (file.exists()) {
        val lines = file.readLines()
        val textArray = mutableListOf<String>()
        val newArray = mutableListOf<String>()
        val tempMap = mutableMapOf<String, Int>()
        var n = 0
        for (line in lines) {
            textArray.add(line)
        }
        for (i in 0 until textArray.size) {
            if (textArray[i].toIntOrNull() != null) {
                tempMap[textArray[i - 2]] = textArray[i].toInt()
            } else {
                newArray.add(textArray[i])
            }
        }
        for (i in 0 until newArray.size step 2) {
            map[newArray[i]] = newArray[i + 1]
            tracker[newArray[i]] = 0
            n++
            // tracker[textArray[i]] = textArray[i + 2].toInt()
        }
        tracker.putAll(tempMap)
        printLogs(log, "$n cards have been loaded.")
    } else printLogs(log, "File not found")
    return theMenu(map, log, tracker)
}

fun removeFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    printLogs(log, "Which card?")
    val cardToBeRemoved = readLogs(log)
    if (map.containsKey(cardToBeRemoved)) {
        map.remove(cardToBeRemoved)
        trackerRemoveFunction(cardToBeRemoved, tracker)
        printLogs(log, "The card has been removed")
    } else printLogs(log, "Can't remove \"${cardToBeRemoved}\": there is no such card.")
    return theMenu(map, log, tracker)
}

fun exitFunction(map: MutableMap<String, String>, tracker: MutableMap<String, Int>, list: MutableList<String>, log: MutableList<String>) {
    if (list.size > 0) {
        exportLast(list, map, log, tracker)
    }
    return printLogs(log, "Bye bye!")
}

fun addFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    printLogs(log, "The card:")
    val term = readLogs(log)
    while (map.containsKey(term)) {
        printLogs(log, "The card \"$term\" already exists.")
        return theMenu(map, log, tracker)
        // term = readln() ***** To keep the user guessing until they enter a unique term ****
    }
    printLogs(log, "The definition of the card:")
    val definition = readLogs(log)
    while (map.containsValue(definition)) {
        printLogs(log, "The definition \"$definition\" already exists.")
        return theMenu(map, log, tracker)
        // definition = readln() ***** To keep the user guessing until they enter a unique definition *****
    }
    map[term] = definition
    trackerAddFunction(term, tracker)
    printLogs(log, "The pair (\"${term}\":\"${definition}\") has been added")
    return theMenu(map, log, tracker)
}

fun printLogs(log: MutableList<String>, text: String) {
    log.add(text)
    println(text)
}

fun printLogsNoNewLine(log: MutableList<String>, text: String) {
    log.add(text)
    print(text)
}

fun readLogs(log: MutableList<String>): String {
    val temp = readln()
    log.add(temp)
    return temp
}

fun checkForImportAndExport(list: MutableList<String>, args: Array<String>, map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    var n = 0
    for (i in args) {
        if (i == "-import" || n == 1) {
            if (i != "-import" && i != "-export") importAtFirst(i, map, log, tracker)
            n = 1
        }
        if (i == "-export" || n == 2) {
            if (i != "-export") list.add(i)
            n = 2
        }
    }
}

fun exportLast(list: MutableList<String>, map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    val file = File(list[0])
    val orderedList = mutableListOf<String>()
    var n = 0
    for ((key, value) in map) {
        orderedList.add(key)
        orderedList.add(value)
        if (tracker.getValue(key) != 0) orderedList.add(tracker.getValue(key).toString())
        n++
    }
    if (file.exists()) {
        for (line in orderedList) {
            if (line == orderedList.first()) file.writeText("${line}\n")
            else {
                if (line == orderedList.last()) file.appendText(line) else file.appendText("${line}\n")
            }
        }
    } else {
        for (line in orderedList) {
            file.appendText(line)
            if (line != orderedList.last()) file.appendText("\n")
        }
    }
    printLogs(log, "$n cards have been saved.")

}

fun importAtFirst(arg: String, map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {
    val file = File(arg)
    if (file.exists()) {
        val lines = file.readLines()
        val textArray = mutableListOf<String>()
        val newArray = mutableListOf<String>()
        val tempMap = mutableMapOf<String, Int>()
        var n = 0
        for (line in lines) {
            textArray.add(line)
        }
        for (i in 0 until textArray.size) {
            if (textArray[i].toIntOrNull() != null) {
                tempMap[textArray[i - 2]] = textArray[i].toInt()
            } else {
                newArray.add(textArray[i])
            }
        }
        for (i in 0 until newArray.size step 2) {
            map[newArray[i]] = newArray[i + 1]
            tracker[newArray[i]] = 0
            n++
            // tracker[textArray[i]] = textArray[i + 2].toInt()
        }
        tracker.putAll(tempMap)
        printLogs(log, "$n cards have been loaded.")
    }
}

fun main(args: Array<String>) {

    val newMap = mutableMapOf<String, String>()
    val logList = mutableListOf<String>()
    val wrongAnswerTracker = mutableMapOf<String, Int>()
    val listToExport = mutableListOf<String>()
    checkForImportAndExport(listToExport, args, newMap, logList, wrongAnswerTracker)
    theMenu(newMap, logList, wrongAnswerTracker)
    exitFunction(newMap, wrongAnswerTracker, listToExport, logList)
}

