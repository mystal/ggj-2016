package biscuitbaker.game.ui

import biscuitbaker.game.Event
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable
import java.util.*

class EventCard(val event: Event) {
    val table: VisTable = VisTable()

    val nameLabel: VisLabel = VisLabel(event.name)
    val timeRemainingLabel: VisLabel = VisLabel("")

    init {
        table.add(nameLabel)
        table.row()
        table.add(timeRemainingLabel)
    }

    fun update(dt: Float) {
        timeRemainingLabel.setText("Time Remaining: %.1f".format(event.timeRemaining))
    }
}

class EventCards() {
    public val table: VisTable = VisTable()

    public val eventCards: ArrayList<EventCard> = ArrayList()

    fun addEvent(event: Event) {
        val card = EventCard(event)
        eventCards.add(card)
        table.add(card.table)
        table.row()
    }

    fun clear() {
        table.clearChildren()
        eventCards.clear()
    }

    fun update(dt: Float) {
        for (card in eventCards) {
            card.update(dt)
        }
    }
}