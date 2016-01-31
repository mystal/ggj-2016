package biscuitbaker.game.ui

import biscuitbaker.game.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.Separator
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener
import java.util.*

class Ui(game: Game) {
    internal val WIDTH: Float = 1280f
    internal val HEIGHT: Float = 720f

    internal var skin: Skin
    internal val stage: Stage = Stage(FitViewport(WIDTH, HEIGHT))

    internal var biscuitsOwned: Label
    internal var biscuitsPerSecond: Label

    internal var storeTab: StoreTab

    // Debug UI

    internal lateinit var biscuitsEarned: Label

    init {
        Gdx.input.inputProcessor = stage

        VisUI.load()
        skin = VisUI.getSkin()

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
        val leftColumn = VerticalGroup()
        //leftColumn.align(Align.center)
        leftColumn.addActor(bakeryName)
        leftColumn.addActor(biscuitsOwned)
        leftColumn.addActor(biscuitsPerSecond)
        leftColumn.addActor(biscuitButton)

        // Center Column
        val centerColumn = VisTable()

        val mainPane = TabbedPane()
        centerColumn.add(mainPane.table).expandX().fillX()

        centerColumn.row()

        val contentTable = VisTable()
        centerColumn.add(contentTable).expand().fill()

        mainPane.addListener(object : TabbedPaneAdapter() {
            override fun switchedTab(tab: Tab) {
                print("Switched tabs!")
                contentTable.clearChildren()
                contentTable.add(tab.contentTable).expandX().fillX()
            }
        })
        storeTab = StoreTab(game, skin)
        mainPane.add(storeTab)

        // Right Column
        val rightColumn = VisTable()

        // TODO: Events overview
        // TODO: Add scrollable

        // Add a debug menu!
        if (game.debug) {
            // TOOD: Add expander

            rightColumn.addSeparator()

            val debugLabel = Label("Debug", skin)
            rightColumn.add(debugLabel)

            rightColumn.row()

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
            rightColumn.add(biscuitModRow)

            rightColumn.row()

            biscuitsEarned = Label("", skin)
            rightColumn.add(biscuitsEarned)
        }

        table.add(leftColumn).width(250f).top()
        table.addSeparator(true)
        table.add(centerColumn).expandX().fillX().top()
        table.addSeparator(true)
        table.add(rightColumn).width(250f).top()

        stage.addActor(table)
    }

    fun update(dt: Float) {
        stage.act(dt)
    }

    fun render(dt: Float, game: Game) {
        biscuitsOwned.setText("%.0f biscuits".format(game.biscuits))
        biscuitsPerSecond.setText("%.1f biscuits per second".format(game.bps))

        //storeTab.render(dt, game)

        if (game.debug) {
            biscuitsEarned.setText("Total biscuits earned: %.1f".format(game.biscuitsEarned))
        }

        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }
}