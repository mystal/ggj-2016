package biscuitbaker.game.ui

import biscuitbaker.game.Game
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab


class DebugTab(game: Game, ui: Ui, skin: Skin) : Tab() {
    var content: VisTable = VisTable()
        private set

    internal var biscuitsEarned: Label = Label("", skin)
    internal var nextEvent: Label = Label("", skin)

    init {
        // Add button to add/set biscuit count
        val biscuitModRow = HorizontalGroup()
        val biscuitModLabel = TextField("10000", skin)
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
        content.add(biscuitModRow)

        content.row()

        content.add(biscuitsEarned)

        content.row()

        content.add(nextEvent)

        content.row()

        val activateEventButton = TextButton("Activate Event", skin)
        activateEventButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.eventManager.tryActivateEvent(game, ui)
            }
        })
        content.add(activateEventButton)

        content.row()

        val rankRow = HorizontalGroup()

        val expAddField = TextField("1000", skin)
        rankRow.addActor(expAddField)
        val expAddButton = TextButton("Add", skin)
        expAddButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                val amount = expAddField.text.toInt()
                if (amount >= 0) {
                    game.addExp(amount)
                }
            }
        })
        rankRow.addActor(expAddButton)

        val rankUpButton = TextButton("Rank Up", skin)
        rankUpButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.rankUp()
            }
        })
        rankRow.addActor(rankUpButton)
        content.add(rankRow)

        val deselectEventButton = TextButton("Deselect Event", skin)
        deselectEventButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                ui.eventCards.deselect()
            }
        })
        content.row()
        content.add(deselectEventButton)
    }

    override fun isCloseableByUser(): Boolean {
        return false
    }

    override fun getTabTitle(): String? {
        return "Debug"
    }

    override fun getContentTable(): Table? {
        return content
    }

    fun render(dt: Float, game: Game) {
        biscuitsEarned.setText("Total biscuits earned: %.1f".format(game.biscuitsEarned))
        nextEvent.setText("Next event: %.1f".format(game.eventManager.eventTimer))
    }
}
