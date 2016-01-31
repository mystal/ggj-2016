package biscuitbaker.game.ui

import biscuitbaker.game.Config
import biscuitbaker.game.Event
import biscuitbaker.game.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter

class Ui(game: Game) {
    internal val WIDTH: Float = Config.SCREEN_WIDTH.toFloat()
    internal val HEIGHT: Float = Config.SCREEN_HEIGHT.toFloat()

    internal var skin: Skin
    internal val stage: Stage = Stage(FitViewport(WIDTH, HEIGHT))

    internal lateinit var leftColumn: VisTable
    internal lateinit var centerColumn: VisTable
    internal lateinit var rightColumn: VisTable

    internal var biscuitsOwned: Label
    internal var biscuitsPerSecond: Label

    internal var storeTab: StoreTab
    internal var eventsTab: EventsTab

    internal lateinit var eventCards: EventCards

    internal var debugMenu: DebugMenu? = null

    init {
        Gdx.input.inputProcessor = stage

        VisUI.load()
        skin = VisUI.getSkin()

        leftColumn = VisTable()
        centerColumn = VisTable()
        rightColumn = VisTable()
        eventCards = EventCards()

        val table = VisTable()
        table.setFillParent(true)
        if (game.debug) {
            table.debug = true
        }

        val bakeryName = TextField("My Bakery", skin)
        bakeryName.setAlignment(Align.center)
        biscuitsOwned = Label("%.0f biscuits".format(game.biscuits), skin)
        biscuitsPerSecond = Label("%.1f biscuits per second".format(game.bps), skin)
        val biscuitButton = TextButton("Biscuit Get", skin)
        biscuitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.click()
            }
        })

        // Left Column
        leftColumn.add(bakeryName)
        leftColumn.row()
        leftColumn.add(biscuitsOwned)
        leftColumn.row()
        leftColumn.add(biscuitsPerSecond)
        leftColumn.row()
        leftColumn.add(biscuitButton)

        // Center Column
        val mainPane = TabbedPane()
        centerColumn.add(mainPane.table).expandX().fillX()

        centerColumn.row()

        // TODO: Make contentTable scrollable
        val contentTable = VisTable()
        centerColumn.add(contentTable).expand().fill()

        mainPane.addListener(object : TabbedPaneAdapter() {
            override fun switchedTab(tab: Tab) {
                contentTable.clearChildren()
                contentTable.add(tab.contentTable).expand().fillX().top()
            }
        })
        storeTab = StoreTab(game, skin)
        eventsTab = EventsTab(game, skin)
        mainPane.add(storeTab)
        mainPane.add(eventsTab)

        mainPane.switchTab(0)

        // Right Column
        // Events overview
        val eventsLabel = Label("Events", skin)
        rightColumn.add(eventsLabel)
        rightColumn.row()
        rightColumn.addSeparator()

        // TODO: Store event cards in scrollable widget
        rightColumn.add(eventCards.table)
        // TODO: Event cards!

        rightColumn.row()

        // Add a debug menu!
        if (game.debug) {
            // TOOD: Add spacer

            rightColumn.addSeparator()

            val newDebugMenu = DebugMenu(game, this, skin)
            rightColumn.add(newDebugMenu.table)

            debugMenu = newDebugMenu
        }

        table.add(leftColumn).width(250f).top()
        table.addSeparator(true)
        table.add(centerColumn).expand().fill()
        table.addSeparator(true)
        table.add(rightColumn).width(250f).top()

        stage.addActor(table)
    }

    fun updateEvents(events: List<Event>) {
        eventCards.clear()

        for (event in events) {
            eventCards.addEvent(event)
        }
    }

    //fun addEvent(event: Event) {
    //    // TODO: Implement
    //}

    //fun removeEvent(event: Event) {
    //    // TODO: Implement
    //}

    fun update(dt: Float) {
        stage.act(dt)
    }

    fun render(dt: Float, game: Game) {
        biscuitsOwned.setText("%.0f biscuits".format(game.biscuits))
        biscuitsPerSecond.setText("%.1f biscuits per second".format(game.bps))

        storeTab.render(dt, game)

        eventCards.update(dt)

        debugMenu?.render(dt, game)

        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }
}
