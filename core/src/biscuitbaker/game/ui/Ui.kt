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

    internal var leftColumn: VisTable
    internal var centerColumn: VisTable
    internal var rightColumn: VisTable

    // Biscuits
    internal var biscuitsOwned: Label
    internal var biscuitsPerSecond: Label

    // Eclairs
    internal var eclairsOwned: Label
    internal var eclairsPerSecond: Label

    // Cupcakes
    internal var cupcakesOwned: Label
    internal var cupcakesPerSecond: Label

    // Pies
    internal var piesOwned: Label
    internal var piesPerSecond: Label

    // Level and Exp
    internal var level: Label
    internal var expToNextLevel: Label

    // Tabs
    internal var mainPane: TabbedPane

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
        eventCards = EventCards(this)

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

        eclairsOwned = Label("%.0f eclairs".format(game.eclairs), skin)
        eclairsPerSecond = Label("%.1f eclairs per second".format(game.eps), skin)

        cupcakesOwned = Label("%.0f cupcakes".format(game.cupcakes), skin)
        cupcakesPerSecond = Label("%.1f cupcakes per second".format(game.cps), skin)

        piesOwned = Label("%.0f pies".format(game.pies), skin)
        piesPerSecond = Label("%.1f pies per second".format(game.pps), skin)

        level = Label("Level: %d".format(game.level), skin)
        expToNextLevel = Label("%d to next level".format(game.expToNextLevel), skin)

        // Left Column
        //leftColumn.add(bakeryName)
        //leftColumn.row()

        leftColumn.add(biscuitsOwned)
        leftColumn.row()
        leftColumn.add(biscuitsPerSecond)
        leftColumn.row()
        leftColumn.add(biscuitButton)
        leftColumn.row()
        leftColumn.addSeparator()
        leftColumn.add(eclairsOwned)
        leftColumn.row()
        leftColumn.add(eclairsPerSecond)
        leftColumn.row()
        leftColumn.add(cupcakesOwned)
        leftColumn.row()
        leftColumn.add(cupcakesPerSecond)
        leftColumn.row()
        leftColumn.add(piesOwned)
        leftColumn.row()
        leftColumn.add(piesPerSecond)
        leftColumn.row()
        leftColumn.addSeparator()
        leftColumn.add(level)
        leftColumn.row()
        leftColumn.add(expToNextLevel)

        // Center Column
        mainPane = TabbedPane()
        centerColumn.add(mainPane.table).expandX().fillX()

        centerColumn.row()

        val contentPane = ScrollPane(null)
        centerColumn.add(contentPane).expand().fill()

        mainPane.addListener(object : TabbedPaneAdapter() {
            override fun switchedTab(tab: Tab) {
                contentPane.widget = tab.contentTable
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
        rightColumn.add(eventCards.cardGroup)
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

    fun addEvent(event: Event, index: Int) {
        eventCards.addEvent(event, index)
    }

    fun removeEvent(event: Event) {
        eventCards.removeEvent(event)
    }

    fun update(dt: Float) {
        stage.act(dt)
    }

    fun render(dt: Float, game: Game) {
        biscuitsOwned.setText("%.0f biscuits".format(game.biscuits))
        biscuitsPerSecond.setText("%.1f biscuits per second".format(game.bps))

        eclairsOwned.setText("%.0f eclairs".format(game.eclairs))
        eclairsPerSecond.setText("%.1f eclairs per second".format(game.eps))

        cupcakesOwned.setText("%.0f cupcakes".format(game.cupcakes))
        cupcakesPerSecond.setText("%.1f cupcakes per second".format(game.cps))

        piesOwned.setText("%.0f pies".format(game.pies))
        piesPerSecond.setText("%.1f pies per second".format(game.pps))

        level.setText("Level: %d".format(game.level))
        expToNextLevel.setText("%d to next level".format(game.expToNextLevel))

        storeTab.render(dt, game)

        eventCards.update(dt)

        debugMenu?.render(dt, game)

        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }
}
