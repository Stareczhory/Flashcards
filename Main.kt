import java.io.File

    /* To help you practise words/phrases and their respective meanings. */

fun TempForAskFunction(answer: String, map: Map<String, String>): String? {

    /* part of the AskFunction function. */

    for (i in map.keys) {
        if (map[i] == answer) {
            return i
        }
    }
    return null
}

fun theMenu(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /*
    * Enables the user to choose an action based on the word typed.
    * If the user does not type one of the action words then they will be prompted with the same question.
    * Typing exit will stop the application
    * */

    printLogs(log, "Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
    val action = readLogs(log)
    if (action != "exit") {
        callFunction(action, map, log, tracker)
    }
    else {
        return  // will return to main.
    }
}

fun callFunction(action: String, map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /*
    * selects the relevant action depending on the word typed into the console*/

    if (action == "add") addFunction(map, log, tracker)
    else if (action == "remove") removeFunction(map, log, tracker)
    else if (action == "import") importFunction(map, log, tracker)
    else if (action == "export") exportFunction(map, log, tracker)
    else if (action == "ask") askFunction(map, log, tracker)
    else if (action == "log") logFunction(map, log, tracker)
    else if (action == "hardest card") hardestCardFun(map, log, tracker)
    else if (action == "reset stats") resetStatsFun(map, log, tracker)
    else return theMenu(map, log, tracker) // calls theMenu
}

fun resetStatsFun(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /* wipes the card statistics */

    for (k in tracker.keys) {
        tracker[k] = 0
    }
    printLogs(log, "Card statistics have been reset.")
    return theMenu(map, log, tracker) // calls theMenu
}

fun hardestCardSubFunction(tracker: MutableMap<String, Int>): Int {

    /* part of the hardestCardFun function */

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
    return m // returns the number of cards with the highest amount of errors.
}

fun hardestCardFun(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /* determines what the hardest card is */

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
    return theMenu(map, log, tracker) // calls theMenu
}

fun trackerAddFunction(term: String, tracker: MutableMap<String, Int>) {

    /* adds a term to the tracker */

    tracker[term] = 0
}

fun trackerRemoveFunction(term: String, tracker: MutableMap<String, Int>) {

    /* removes a term from the tracker */

    tracker.remove(term)
}

fun logFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /* Creates a log file */

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
    return theMenu(map, log, tracker) // calls theMenu

}

fun askFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /* Asks the user for a definition to a random term in the collection of flashcards */

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

    /* Saves the terms and their definitions to a text file */

    printLogs(log, "File name:")
    val myFile = readLogs(log)
    // val separator = File.separator
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
    return theMenu(map, log, tracker) // calls theMenu
}

fun importFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /* Imports a text file with terms and definitions into the program */

    printLogs(log, "File name: ")
    val importFileName = readln()
    // val separator = File.separator
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
        }
        tracker.putAll(tempMap)
        printLogs(log, "$n cards have been loaded.")
    } else printLogs(log, "File not found")
    return theMenu(map, log, tracker) // calls theMenu
}

fun removeFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /* Removes a specified term and its definition from the collection as well as the tracker */

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

    /* The final function in main(), exits the application.
    * Calls the exportLast function if the "-export" followed by a "name.txt" file are present in the program arguments
    * */

    if (list.size > 0) {
        exportLast(list, map, log, tracker)
    }
    return printLogs(log, "Bye bye!")
}

fun addFunction(map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /* Prompts the user to enter a term followed by its definition */

    printLogs(log, "The card:")
    val term = readLogs(log)
    while (map.containsKey(term)) {
        printLogs(log, "The card \"$term\" already exists.")
        return theMenu(map, log, tracker)
        // term = readln() To keep the user guessing until they enter a unique term
    }
    printLogs(log, "The definition of the card:")
    val definition = readLogs(log)
    while (map.containsValue(definition)) {
        printLogs(log, "The definition \"$definition\" already exists.")
        return theMenu(map, log, tracker)
        // definition = readln() To keep the user guessing until they enter a unique definition
    }
    map[term] = definition
    trackerAddFunction(term, tracker)
    printLogs(log, "The pair (\"${term}\":\"${definition}\") has been added")
    return theMenu(map, log, tracker)
}

fun printLogs(log: MutableList<String>, text: String) {

    /* Saves any text pushed by the console */

    log.add(text)
    println(text)
}

fun printLogsNoNewLine(log: MutableList<String>, text: String) {

    /* does not print a new line to the log file, unlike the printLogs function */

    log.add(text)
    print(text)
}

fun readLogs(log: MutableList<String>): String {

    /* Saves any text inputted into the console by the user */

    val temp = readln()
    log.add(temp)
    return temp
}

fun checkForImportAndExport(list: MutableList<String>, args: Array<String>, map: MutableMap<String, String>, log: MutableList<String>, tracker: MutableMap<String, Int>) {

    /* Checks for the "-import" and "-export" arguments */

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

    /* Exports the terms and definitions to a .txt file if the "-export" argument was passed to the program */

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

    /* Imports the terms and definitions to of a .txt file if the "-import" argument was passed to the program */

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

    val newMap = mutableMapOf<String, String>() // to store all the terms and definitions
    val logList = mutableListOf<String>() // to store the logs
    val wrongAnswerTracker = mutableMapOf<String, Int>() // to store the wrong answers
    val listToExport = mutableListOf<String>() // name of the .txt file that exportLast function will export to

    checkForImportAndExport(listToExport, args, newMap, logList, wrongAnswerTracker)

    theMenu(newMap, logList, wrongAnswerTracker)

    exitFunction(newMap, wrongAnswerTracker, listToExport, logList)
}
