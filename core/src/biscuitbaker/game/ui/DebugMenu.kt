package biscuitbaker.game.ui

import biscuitbaker.game.Game
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable

class DebugMenu(game: Game, ui: Ui, skin: Skin) {
    public var table: VisTable = VisTable()
        private set

    internal var biscuitsEarned: Label = Label("", skin)
    internal var nextEvent: Label = Label("", skin)

    init {
        val debugLabel = Label("Debug", skin)
        table.add(debugLabel)

        table.row()

        // Add button to add/set biscuit count
        val biscuitModRow = HorizontalGroup()
        val biscuitModLabel = TextField("100", skin)
        biscuitModRow.addActor(biscuitModLabel)
        val biscuitAddButton = TextButton("Add", skin)
        biscuitAddButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val amount = biscuitModLabel.text.toDouble()
                if (amount >= 0) {
                    game.biscuits += amount
                }
            }
        })
        biscuitModRow.addActor(biscuitAddButton)
        val biscuitSetButton = TextButton("Set", skin)
        biscuitSetButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val amount = biscuitModLabel.text.toDouble()
                if (amount >= 0) {
                    game.biscuits = amount
                }
            }
        })
        biscuitModRow.addActor(biscuitSetButton)
        table.add(biscuitModRow)

        table.row()

        table.add(biscuitsEarned)

        table.row()

        table.add(nextEvent)

        table.row()

        val activateEventButton = TextButton("Activate Event", skin)
        activateEventButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.eventManager.tryActivateEvent(game, ui)
            }
        })
        table.add(activateEventButton)

        table.row()

        val levelRow = HorizontalGroup()

        val expAddField = TextField("100", skin)
        levelRow.addActor(expAddField)
        val expAddButton = TextButton("Add", skin)
        expAddButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val amount = expAddField.text.toInt()
                if (amount >= 0) {
                    game.addExp(amount)
                }
            }
        })
        levelRow.addActor(expAddButton)

        val levelUpButton = TextButton("Level Up", skin)
        levelUpButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.levelUp()
            }
        })
        levelRow.addActor(levelUpButton)
        table.add(levelRow)
    }

    public fun render(dt: Float, game: Game) {
        biscuitsEarned.setText("Total biscuits earned: %.1f".format(game.biscuitsEarned))
        nextEvent.setText("Next event: %.1f".format(game.eventManager.eventTimer))
    }
}
