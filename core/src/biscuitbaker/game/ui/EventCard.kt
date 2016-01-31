package biscuitbaker.game.ui

import biscuitbaker.game.Event
import biscuitbaker.game.GameTime
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisProgressBar
import com.kotcrab.vis.ui.widget.VisTable
import java.util.*

class EventCard(val event: Event, val manager: EventCards) {
    val table: VisTable = VisTable()

    val nameLabel: VisLabel = VisLabel(event.name)
    val timeRemainingBar: VisProgressBar = VisProgressBar(0f, event.duration * GameTime.DAY, 0.5f, false)

    init {
        //table.setBackground(table.skin.getDrawable("default-scroll"))
        table.add(nameLabel)
        table.row()
        timeRemainingBar.setValue(event.timeRemaining)
        table.add(timeRemainingBar)

        val card = this

        table.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                manager.select(card)
            }
        })
    }

    fun update(dt: Float, selected: Boolean) {
        if (selected) {
            nameLabel.color = Color.BLUE
        } else {
            nameLabel.color = Color.WHITE
        }
        timeRemainingBar.setValue(event.timeRemaining)
        //timeRemainingLabel.setText("Time Remaining: %.1f".format(event.timeRemaining))
    }
}

class EventCards(val ui: Ui) {
    public val table: VisTable = VisTable()

    public val eventCards: ArrayList<EventCard> = ArrayList()

    public var selected: EventCard? = null

    fun addEvent(event: Event) {
        val card = EventCard(event, this)
        eventCards.add(card)
        table.add(card.table)
        table.row()
    }

    fun select(card: EventCard) {
        selected = card
        ui.mainPane.switchTab(ui.eventsTab)
    }

    fun deselect() {
        selected = null
    }

    fun clear() {
        deselect()
        table.clearChildren()
        eventCards.clear()
    }

    fun update(dt: Float) {
        for (card in eventCards) {
            card.update(dt, card == selected)
        }
    }
}