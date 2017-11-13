package tk.festelo.eee.biselika.activities

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import tk.festelo.eee.biselika.R
import tk.festelo.eee.biselika.model.GameException
import tk.festelo.eee.biselika.model.GameLogic
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().setContentView(this)
    }
}

class MainActivityUI : AnkoComponent<MainActivity> {
    private val random = Random()
    private lateinit var game: GameLogic
    private lateinit var words: Array<String>
    private lateinit var wordTextView: TextView
    private lateinit var attemptsTextView: TextView

    override fun createView(ui: AnkoContext<MainActivity>): View = ui.apply {
        words = resources.getStringArray(R.array.words)
        resetGame()

        verticalLayout {
            wordTextView = textView ("Текст"){
                gravity = Gravity.CENTER_HORIZONTAL
                setTypeface( null, Typeface.BOLD)
                textSize = 20F
            }
            val inputEditText = editText {
                singleLine = true
            }
            button("Check") {
                onClick {
                    val text = inputEditText.text.toString()
                    if (!text.isEmpty()){
                        try{
                            game.openSymbols(text)
                        } catch (e: GameException){
                            toast(e.message.orEmpty())
                        }
                        if (game.isClosed){
                            if (game.isWon){
                                toast("Congratulations! You won it! Word: ${game.word}")
                            }
                            else {
                                toast("You lose! Word: ${game.word}")
                            }
                            resetGame()
                        }

                        updateUI()
                    }
                }
            }
            linearLayout {
                textView("Attempts left: ")
                attemptsTextView = textView()
            }
            updateUI()
        }
    }.view

    fun updateUI(){
        wordTextView.text = game.hiddenWord
        attemptsTextView.text = (game.maxAttemptsCount - game.attemptsCount).toString()
    }

    fun resetGame() {
        game = createNewGame()
    }
    private fun createNewGame() : GameLogic {
        return GameLogic(words[random.nextInt(words.size)], 5)
    }
}
