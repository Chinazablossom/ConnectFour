package connectfour


import kotlin.system.exitProcess

fun main() {
    ConnectFour()
}

class ConnectFour {
    var row = 6
    var column = 7
    lateinit var userInput: String
    val valid = "\\s*[5-9]\\s*[xX]\\s*[5-9]\\s*".toRegex()
    val digit = "[1-9][0-9]?".toRegex()
    lateinit var userDigit: String
    var board = MutableList(row) { MutableList(column) { " " } }
    val vertLine = '\u2551'
    val lastLineLeft = '\u255A'
    val straightLine = '\u2550'
    val sepLine = '\u2569'
    val lastLineRight = '\u255D'
    var firstPlayersName: String
    var secondPlayersName: String
    lateinit var input: String
    var userDigitToInt = 0
    var game = 1
    var aWin = 0
    var bWin = 0


    init {
        println("Connect Four")
        println("First player's name:")
        firstPlayersName = readln()
        println("Second player's name:")
        secondPlayersName = readln()
        setBoard()

    }

    fun MutableList<MutableList<String>>.displayBoard() {
        for (i in 1..column) {
            print(" $i")
        }
        println()
        this.forEachIndexed { _, row ->
            print("$vertLine")
            row.forEach { print("$it$vertLine") }
            print("\n")
        }
        print("$lastLineLeft")
        (0 until column - 1).forEach { print("$straightLine$sepLine") }
        print("$straightLine$lastLineRight")


    }

    fun setBoard() {
        do {
            println("\nSet the board dimensions (Rows x Columns)")
            println("Press Enter for default (6 x 7)")
            userInput = readln().trim()
            val returnX = { userInput: String -> String
                var x = ""
                for (i in userInput) {
                    if (i == 'x' || i == 'X')
                        x += i
                }
                x
            }

            if (userInput.isEmpty()) {
                loop@ do {
                    println("Do you want to play single or multiple games?\n" +
                            "For a single game, input 1 or press Enter\n" +
                            "Input a number of games:")
                    userDigit = readln()
                    if (!digit.matches(userDigit)){
                        if (userDigit.isEmpty()) {
                            println("$firstPlayersName VS $secondPlayersName\n6 X 7 board")
                            println("Single game")
                            board.displayBoard()
                            board.firstPlayerTurn()
                        }
                        println("Invalid input\n")
                        continue@loop
                    }
                    if (digit.matches(userDigit)) {
                        println("$firstPlayersName VS $secondPlayersName\n6 X 7 board")
                        userDigitToInt = userDigit.toInt()
                        if (userDigit == "1") {
                            println("Single game")
                            board.displayBoard()
                            board.firstPlayerTurn()
                        }
                        if (userDigit > "1") {
                            println("Total $userDigit games")
                            println("Game #$game")
                            game++
                            board.displayBoard()
                            board.firstPlayerTurn()
                        }

                    }

                }while (true)
            }

            if (valid.matches(userInput)) {
                row = userInput.first().digitToInt()
                column = userInput.last().digitToInt()
                MutableList(row) { MutableList(column) { " " } }.also { board = it }

                loop@  do {
                    println("Do you want to play single or multiple games?\n" +
                            "For a single game, input 1 or press Enter\n" +
                            "Input a number of games:")
                    userDigit = readln()

                    if (!digit.matches(userDigit)){
                        if (userDigit.isEmpty()) {
                            println("$firstPlayersName VS $secondPlayersName\n$row X $column board")
                            println("Single game")
                            board.displayBoard()
                            board.firstPlayerTurn()
                        }
                        println("Invalid input\n")
                        continue@loop
                    }
                    if (digit.matches(userDigit)) {
                        println("$firstPlayersName VS $secondPlayersName\n$row X $column board")
                        userDigitToInt = userDigit.toInt()
                        if (userDigit == "1") {
                            println("Single game")
                            board.displayBoard()
                            board.firstPlayerTurn()
                        }
                        if (userDigit > "1") {
                            println("Total $userDigit games")
                            println("Game #$game")
                            game++
                            board.displayBoard()
                            board.firstPlayerTurn()
                        }

                    }

                }while (true)

            } else if (userInput.isNotEmpty()) {
                if (!returnX(userInput).equals("x", true)) {
                    println("Invalid input")
                    return setBoard()
                }
                if (!userInput.first().isDigit()) {
                    println("Invalid input")
                    return setBoard()
                }
                if (!userInput.last().isDigit()) {
                    println("Invalid input")
                    return setBoard()
                }
                if (userInput.first().digitToInt() !in 5..9) {
                    println("Board rows should be from 5 to 9")
                    return setBoard()
                }
                if (userInput.last().digitToInt() !in 5..9) {
                    println("Board columns should be from 5 to 9")
                    return setBoard()
                }
            }

        } while (true)

    }


    fun MutableList<MutableList<String>>.firstPlayerTurn() {
        try {
            println()
            println("$firstPlayersName's turn:")
            input = readln()
            exit@ do {
                if (input.equals("end", true)) {
                    println("Game over!")
                    exitProcess(0)
                }
                if (input.toInt() !in 1..column) {
                    println("The column number is out of range (1 - $column)")
                    return firstPlayerTurn()
                } else {
                    if (this[0][input.toInt() - 1] != " ") {
                        println("Column $input is full")
                        return firstPlayerTurn()
                    } else {
                        for (i in this.size - 1 downTo 0) {
                            if (this[i][input.toInt() - 1] == " ") {
                                this[i][input.toInt() - 1] = "o"
                                this.displayBoard()
                                var sBoard = ""
                                board.forEach { sBoard += it.joinToString("") }

                                if (Regex("o{4}").containsMatchIn(sBoard) || checkWinningCondition(board, "o")) {
                                    aWin += 2
                                    println("\nPlayer $firstPlayersName won")
                                    if (userDigit > "1") {
                                        println("Score\n$firstPlayersName: $aWin $secondPlayersName: $bWin")
                                        board = MutableList(row) { MutableList(column) { " " } }
                                        break@exit
                                    }
                                    println("\nGame over!")
                                    exitProcess(0)

                                } else if (!sBoard.contains(" ")) {
                                    aWin++
                                    bWin++
                                    println("\nIt is a draw")
                                    if (userDigit > "1") {
                                        println("Score\n$firstPlayersName: $aWin $secondPlayersName: $bWin")
                                        board = MutableList(row) { MutableList(column) { " " } }
                                        break@exit
                                    }
                                    println("\nGame Over!")
                                    exitProcess(0)
                                }
                                secondPlayersTurn()
                            }
                        }
                    }
                }
            } while (true)
            multi("o")
        } catch (e: Exception) {
            println("\nIncorrect column number")
            return firstPlayerTurn()
        }

    }

    fun MutableList<MutableList<String>>.secondPlayersTurn() {
        try {
            println()
            println("$secondPlayersName's turn:")
            input = readln()
            exit@ do {
                if (input.equals("end", true)) {
                    println("Game over!")
                    exitProcess(0)
                } else {
                    if (input.toInt() !in 1..column) {
                        println("The column number is out of range (1 - $column)")
                        return secondPlayersTurn()
                    } else {
                        if (this[0][input.toInt() - 1] != " ") {
                            println("Column $input is full")
                            return secondPlayersTurn()
                        } else {
                            for (i in this.size - 1 downTo 0) {
                                if (this[i][input.toInt() - 1] == " ") {
                                    this[i][input.toInt() - 1] = "*"
                                    this.displayBoard()
                                    var sBoard = ""
                                    board.forEach { sBoard += it.joinToString("") }
                                    if (Regex("\\*{4}").containsMatchIn(sBoard) || checkWinningCondition(board, "*")  ) {
                                        bWin += 2
                                        println("\nPlayer $secondPlayersName won")
                                        if (userDigit > "1") {
                                            println("Score\n$firstPlayersName: $aWin $secondPlayersName: $bWin")
                                            board = MutableList(row) { MutableList(column) { " " } }
                                            break@exit
                                        }
                                        println("\nGame over!")
                                        exitProcess(0)
                                    } else if (!sBoard.contains(" ")) {
                                        aWin++
                                        bWin++
                                        println("\nIt is a draw")
                                        if (userDigit > "1") {
                                            println("Score\n$firstPlayersName: $aWin $secondPlayersName: $bWin")
                                            board = MutableList(row) { MutableList(column) { " " } }
                                            break@exit
                                        }
                                        println("\nGame Over!")
                                        exitProcess(0)
                                    }
                                    firstPlayerTurn()
                                }

                            }
                        }
                    }
                }
            } while (true)
            multi("*")
        } catch (e: Exception) {
            println("\nIncorrect column number")
            return secondPlayersTurn()
        }

    }
    fun multi(player:String) {
        if (userDigitToInt == 1) {
            println("Game over!")
            exitProcess(0)
        } else {
            do {
                userDigitToInt--
                println("Game #$game")
                game++
                board.displayBoard()
                if (player == "o") board.secondPlayersTurn() else board.firstPlayerTurn()
            } while (true)
        }
    }

    fun checkWinningCondition(board: MutableList<MutableList<String>>, player: String): Boolean {
        for (rows in 0 until row - 3) {
            for (columns in 0 until column) {
                if (board[rows][columns] == player && board[rows+1][columns] == player &&
                    board[rows+2][columns] == player && board[rows+3][columns] == player) {
                    return true
                }
            }
        }

        for (rows in 0 until row - 3) {
            for (columns in 0 until column - 3) {
                if (board[rows][columns] == player && board[rows+1][columns+1] == player &&
                    board[rows+2][columns+2] == player && board[rows+3][columns+3] == player) {
                    return true
                }
            }
        }

        for (rows in 0 until row - 3) {
            for (columns in 3 until column) {
                if (board[rows][columns] == player && board[rows+1][columns-1] == player &&
                    board[rows+2][columns-2] == player && board[rows+3][columns-3] == player) {
                    return true
                }
            }
        }

        return false
    }





}