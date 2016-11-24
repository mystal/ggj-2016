package biscuitbaker.game.ui

import biscuitbaker.game.Event
import biscuitbaker.game.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab


class EventsTab(game: Game, skin: Skin, val eventCards: EventCards): Tab() {
    internal val content: VisTable = VisTable()

    internal val noEventSelected = Label("No Event Selected", skin)

    internal val eventTable: VisTable = VisTable()

    internal val factionImage: Image = Image(Texture(Gdx.files.internal("img/bread_boy.png")))
    internal val eventName: Label = Label("", skin)
    internal val eventFlavor: Label = Label("", skin)
    internal val eventDescription: Label = Label("", skin)
    internal val expLabel: Label = Label("", skin)
    internal val biscuitsLabel: Label = Label("", skin)
    internal val eclairsLabel: Label = Label("", skin)
    internal val cupcakesLabel: Label = Label("", skin)
    internal val piesLabel: Label = Label("", skin)
    internal val fulfillButton: TextButton = TextButton("Fulfill", skin)

    internal var shownEvent: Event? = null

    init {
        content.add(noEventSelected)

        fulfillButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                shownEvent?.let { event ->
                    if (event.canBeFulfilled(game)) {
                        event.fulfill(game)

                        // TODO: Don't call this directly, register a callback with EventManager
                        eventCards.removeEvent(event)
                    }
                }
            }
        })

        eventTable.add(eventName)
        eventTable.row()
        eventFlavor.setWrap(true)
        eventTable.add(eventFlavor).fillX()
        eventTable.row()
        eventTable.addSeparator()
        eventDescription.setWrap(true)
        eventTable.add(eventDescription).fillX()
        eventTable.row()
        eventTable.addSeparator()
        eventTable.add(expLabel)
        eventTable.row()
        eventTable.addSeparator()

        val orderLabel = Label("Order Requires", skin)
        eventTable.add(orderLabel)

        eventTable.row()
        eventTable.add(biscuitsLabel)
        eventTable.row()
        eventTable.add(eclairsLabel)
        eventTable.row()
        eventTable.add(cupcakesLabel)
        eventTable.row()
        eventTable.add(piesLabel)

        eventTable.row()
        eventTable.add(fulfillButton)
    }

    fun showEvent(event: Event?) {
        shownEvent = event
        content.clearChildren()
        if (event == null) {
            content.add(noEventSelected)
        } else {
            if (event.info.factionImage != "") {
                factionImage.drawable = TextureRegionDrawable(TextureRegion(Texture(Gdx.files.internal(event.info.factionImage))))
            }
            eventName.setText(event.name)
            eventFlavor.setText("\"${event.strings.flavor}\"")
            eventDescription.setText("\"${event.strings.description}\"")
            expLabel.setText("%d exp".format(event.exp))

            content.add(factionImage).expand()
            content.add(eventTable).width(400f).expandY().top()
        }
    }

    fun render(dt: Float, game: Game) {
        shownEvent?.let { event ->
            fulfillButton.isDisabled = !event.canBeFulfilled(game)

            event.costs?.let { costs ->
                biscuitsLabel.setText("%d biscuits".format(costs.biscuits))
                eclairsLabel.setText("%d eclairs".format(costs.eclairs))
                cupcakesLabel.setText("%d cupcakes".format(costs.cupcakes))
                piesLabel.setText("%d pies".format(costs.pies))
            }
        }
    }

    override fun isCloseableByUser(): Boolean {
        return false
    }

    override fun getContentTable(): Table? {
        return content
    }

    override fun getTabTitle(): String? {
        return "Event"
    }
}
