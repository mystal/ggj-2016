package biscuitbaker.game.ui

import biscuitbaker.game.Config
import biscuitbaker.game.Event
import biscuitbaker.game.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
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
    internal lateinit var biscuitsOwned: Label
    internal lateinit var biscuitsPerSecond: Label

    // Eclairs
    internal lateinit var eclairsOwned: Label
    internal lateinit var eclairsPerSecond: Label

    // Cupcakes
    internal lateinit var cupcakesOwned: Label
    internal lateinit var cupcakesPerSecond: Label

    // Pies
    internal lateinit var piesOwned: Label
    internal lateinit var piesPerSecond: Label

    // Rank and Exp
    internal lateinit var rank: Label
    internal lateinit var expToNextRank: Label

    // Tabs
    internal var mainPane: TabbedPane

    internal var storeTab: StoreTab
    internal var eventsTab: EventsTab

    internal var debugTab: DebugTab? = null

    internal var eventCards: EventCards

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

        // Left Column
        createLeftColumn(game)

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
        eventsTab = EventsTab(game, skin, eventCards)
        mainPane.add(storeTab)
        mainPane.add(eventsTab)

        // Add a debug menu in a separate tab.
        if (game.debug) {
            val newDebugTab = DebugTab(game, this, skin)
            mainPane.add(newDebugTab)

            debugTab = newDebugTab
        }

        mainPane.switchTab(0)

        // Right Column
        createRightColumn(game)

        // Add Columns
        table.add(leftColumn).width(250f).top()
        table.addSeparator(true)
        table.add(centerColumn).expand().fill()
        table.addSeparator(true)
        table.add(rightColumn).width(250f).top()

        stage.addActor(table)
    }

    fun createRightColumn(game: Game) {
        // Events overview
        val eventsLabel = Label("Events", skin)
        rightColumn.add(eventsLabel)
        rightColumn.row()
        rightColumn.addSeparator()

        // TODO: Store event cards in scrollable widget
        rightColumn.add(eventCards.cardGroup)
        // TODO: Event cards!

        rightColumn.row()


        // label to fill space
        val fillLabel = Label("Assistant Angie", skin)
        rightColumn.add(fillLabel).expandY()

        // add assistant image
        rightColumn.row()
        val assistantImage = Image(Texture(Gdx.files.internal("img/assistant.png")))
        rightColumn.add(assistantImage).expandY()
        rightColumn.row()
        val quoteLabel = Label("\"Bake those biscuits!\"", skin)
        rightColumn.add(quoteLabel)
    }

    fun createLeftColumn(game: Game) {
        val logoImage = Image(Texture(Gdx.files.internal("img/logo.png")))

        biscuitsOwned = Label("%.0f  ".format(game.biscuits), skin)
        biscuitsPerSecond = Label("%.1f biscuits per second".format(game.bps), skin)
        val biscuitButton = TextButton("Bake Biscuits!", skin)
        biscuitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.click()
            }
        })
        val biscuitsImage = Image(Texture(Gdx.files.internal("img/biscuit.png")))

        eclairsOwned = Label("%.0f".format(game.eclairs), skin)
        eclairsPerSecond = Label("%.1f eclairs per second".format(game.eps), skin)
        val eclairsImage = Image(Texture(Gdx.files.internal("img/eclair.png")))

        cupcakesOwned = Label("%.0f cupcakes".format(game.cupcakes), skin)
        cupcakesPerSecond = Label("%.1f cupcakes per second".format(game.cps), skin)
        val cupcakesImage = Image(Texture(Gdx.files.internal("img/chocolate_cupcake.png")))
        cupcakesImage.scaleBy(-0.25f)

        piesOwned = Label("%.0f pies".format(game.pies), skin)
        piesPerSecond = Label("%.1f pies per second".format(game.pps), skin)
        val piesImage = Image(Texture(Gdx.files.internal("img/pie.png")))

        rank = Label("Rank: %d".format(game.rank), skin)
        expToNextRank = Label("%d to next rank".format(game.expToNextRank), skin)

        // Logo
        leftColumn.add(logoImage).expandX()
        leftColumn.row()
        leftColumn.addSeparator()

        // Biscuits
        addCounterToLeft(biscuitsOwned, biscuitsPerSecond, biscuitsImage)
        leftColumn.add(biscuitButton)
        leftColumn.row()
        leftColumn.addSeparator()

        // Eclairs
        addCounterToLeft(eclairsOwned, eclairsPerSecond, eclairsImage)

        // Cupcakes
        addCounterToLeft(cupcakesOwned, cupcakesPerSecond, cupcakesImage)

        // Pies
        addCounterToLeft(piesOwned, piesPerSecond, piesImage)

        //Add Rank info
        leftColumn.addSeparator()
        leftColumn.add(rank)
        leftColumn.row()
        leftColumn.add(expToNextRank)
    }

    fun addCounterToLeft(owned: Label, perSec: Label, image: Image){
        val t = VisTable()
        leftColumn.add(t)
        t.add(owned).expandX()
        t.add(image).expandX()
        leftColumn.row()
        leftColumn.add(perSec)
        leftColumn.row()
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
        biscuitsOwned.setText("%.0f  ".format(game.biscuits))
        biscuitsPerSecond.setText("%.1f biscuits per second".format(game.bps))

        eclairsOwned.setText("%.0f  ".format(game.eclairs))
        eclairsPerSecond.setText("%.1f eclairs per second".format(game.eps))

        cupcakesOwned.setText("%.0f  ".format(game.cupcakes))
        cupcakesPerSecond.setText("%.1f cupcakes per second".format(game.cps))

        piesOwned.setText("%.0f  ".format(game.pies))
        piesPerSecond.setText("%.1f pies per second".format(game.pps))

        rank.setText("Rank: %d".format(game.rank))
        expToNextRank.setText("%d to next rank".format(game.expToNextRank))

        storeTab.render(dt, game)
        eventsTab.render(dt, game)

        debugTab?.render(dt, game)

        eventCards.update(dt)

        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }
}
