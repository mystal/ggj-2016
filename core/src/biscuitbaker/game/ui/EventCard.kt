package biscuitbaker.game.ui

import biscuitbaker.game.Event
import biscuitbaker.game.GameTime
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
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
    public val cardGroup: VerticalGroup = VerticalGroup()

    public val eventCards: ArrayList<EventCard> = ArrayList()

    public var selected: EventCard? = null

    fun addEvent(event: Event, index: Int) {
        val card = EventCard(event, this)
        card.table.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                select(card)
            }
        })
        eventCards.add(index, card)
        cardGroup.addActorAt(index, card.table)
    }

    fun removeEvent(event: Event) {
        val card = eventCards.find { card ->
            card.event == event
        }
        if (card != null) {
            cardGroup.removeActor(card.table)
            eventCards.remove(card)

            if (selected == card) {
                deselect()
            }
        } else {
            // TODO: Error
        }
    }

    fun select(card: EventCard) {
        selected = card
        ui.mainPane.switchTab(ui.eventsTab)
    }

    fun deselect() {
        selected = null
    }

    fun update(dt: Float) {
        for (card in eventCards) {
            card.update(dt, card == selected)
        }
    }
}