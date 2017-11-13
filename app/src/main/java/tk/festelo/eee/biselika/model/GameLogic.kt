package tk.festelo.eee.biselika.model

/**
 * Created by eee on 13.11.2017.
 */

class GameLogic(val word : String, val maxAttemptsCount : Int) {
    var hiddenWord: String
    val usedWords = mutableListOf<String>()
    init {
        val i = word.length-1
        hiddenWord = word[0] + "*".repeat(word.length-2) + word[i]
        usedWords.add(word[0].toString())
        usedWords.add(word[i].toString())
    }

    var attemptsCount = 0
    val isClosed
        get() = maxAttemptsCount == attemptsCount || hiddenWord == word
    val isWon
        get() = hiddenWord == word

    fun openSymbols(symbols: String): Int{
        if (isClosed) throw GameException("Game Closed")
        val symbolsUpper = symbols.toUpperCase()
        if (symbolsUpper in usedWords) throw GameException("Symbols has been already opened")
        usedWords.add(symbolsUpper)
        val count = openSymbols(symbols, 0)
        if(count == 0) attemptsCount++
        return count
    }

    private fun openSymbols(symbols: String, offset: Int) : Int{
        val index = word.indexOf(symbols, offset, true)
        if (index == -1) return 0
        hiddenWord = hiddenWord.replaceRange(index, symbols.length + index, word.subSequence(index, symbols.length + index))
        return 1 + openSymbols(symbols, index + symbols.length)
    }
}