package biscuitbaker.game

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
import java.util.*

class Ui(game: Game) {
    internal val WIDTH: Float = 800f
    internal val HEIGHT: Float = 600f

    internal var skin: Skin
    internal val stage: Stage = Stage(FitViewport(WIDTH, HEIGHT))

    internal var biscuitsOwned: Label
    internal var biscuitsPerSecond: Label

    internal var productStatuses: ArrayList<Label> = ArrayList()
    internal var productButtons: ArrayList<TextButton> = ArrayList()

    internal var upgradeStatuses: ArrayList<Label> = ArrayList()
    internal var upgradeButtons: ArrayList<TextButton> = ArrayList()

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

        val biscuitColumn = VerticalGroup()
        //biscuitColumn.align(Align.center)
        biscuitColumn.addActor(bakeryName)
        biscuitColumn.addActor(biscuitsOwned)
        biscuitColumn.addActor(biscuitsPerSecond)
        biscuitColumn.addActor(biscuitButton)

        val centerColumn = VisTable()

        // Add a debug menu!
        if (game.debug) {
            val debugLabel = Label("Debug", skin)
            centerColumn.add(debugLabel)

            centerColumn.row()

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
            centerColumn.add(biscuitModRow)

            centerColumn.row()

            biscuitsEarned = Label("", skin)
            centerColumn.add(biscuitsEarned)

            centerColumn.row()

            centerColumn.addSeparator()
        }

        // TODO: Add producer tiles

        val storeColumn = VisTable()

        val storeLabel = Label("Store", skin)
        storeColumn.add(storeLabel)

        storeColumn.row()

        val upgradesLabel = Label("Upgrades", skin)
        storeColumn.add(upgradesLabel)

        storeColumn.row()

        storeColumn.addSeparator()

        storeColumn.row()

        game.upgrades.forEachIndexed { i, upgrade ->
            val purchasedStatus = if (upgrade.purchased) "purchased" else "available"
            val upgradeStatus = Label("%s: %s".format(upgrade.name, purchasedStatus), skin)
            val upgradeButton = TextButton("Buy: %d biscuits".format(upgrade.price), skin)
            upgradeButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buyUpgrade(i)
                }
            })

            storeColumn.add(upgradeStatus)
            storeColumn.row()
            storeColumn.add(upgradeButton)
            storeColumn.row()
            upgradeStatuses.add(upgradeStatus)
            upgradeButtons.add(upgradeButton)
        }

        storeColumn.row()

        storeColumn.addSeparator()

        val productsLabel = Label("Products", skin)
        storeColumn.add(productsLabel)

        storeColumn.row()

        game.products.forEachIndexed { i, product ->
            val productStatus = Label("%s: %d\nBpS: %.1f\nTotal BpS: %.1f".format(
                    product.name, product.owned, product.bps, product.totalBps), skin)
            val productButton = TextButton("Buy: %d biscuits".format(product.price), skin)
            productButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buyProduct(i)
                }
            })

            storeColumn.add(productStatus)
            storeColumn.row()
            storeColumn.add(productButton)
            storeColumn.row()
            productStatuses.add(productStatus)
            productButtons.add(productButton)
        }

        table.add(biscuitColumn).width(250f).top()
        table.addSeparator(true)
        table.add(centerColumn).expandX().top()
        table.addSeparator(true)
        table.add(storeColumn).width(250f).top()

        stage.addActor(table)
    }

    fun update(dt: Float) {
        stage.act(dt)
    }

    fun render(dt: Float, game: Game) {
        biscuitsOwned.setText("%.0f biscuits".format(game.biscuits))
        biscuitsPerSecond.setText("%.1f biscuits per second".format(game.bps))

        if (game.debug) {
            biscuitsEarned.setText("Total biscuits earned: %.1f".format(game.biscuitsEarned))
        }

        game.upgrades.forEachIndexed { i, upgrade ->
            val upgradeStatus = upgradeStatuses[i]
            val upgradeButton = upgradeButtons[i]

            // TODO: Don't update visibility every frame, it can be expensive
            if (upgrade.isVisible(game)) {
                val purchasedStatus = if (upgrade.purchased) "purchased" else "available"
                upgradeStatus.isVisible = true
                //upgradeStatus.setLayoutEnabled(false)
                upgradeStatus.setText("%s: %s".format(upgrade.name, purchasedStatus))
                upgradeButton.isVisible = !upgrade.purchased
                //upgradeButton.setLayoutEnabled(false)
                upgradeButton.isDisabled = game.biscuits < upgrade.price
            } else {
                upgradeStatus.isVisible = false
                //upgradeStatus.setLayoutEnabled(true)
                upgradeButton.isVisible = false
                //upgradeButton.setLayoutEnabled(true)
            }
        }

        game.products.forEachIndexed { i, product ->
            val productStatus = productStatuses[i]
            val productButton = productButtons[i]

            // TODO: Don't update visibility every frame, it can be expensive
            if (product.isVisible(game)) {
                productStatus.isVisible = true
                //productStatus.setLayoutEnabled(false)
                productStatus.setText("%s: %d\nBpS: %.1f\nTotal BpS: %.1f".format(
                        product.name, product.owned, product.bps, product.totalBps))
                productButton.isVisible = true
                //productButton.setLayoutEnabled(false)
                productButton.isDisabled = game.biscuits < product.price
            } else {
                productStatus.isVisible = false
                //productStatus.setLayoutEnabled(true)
                productButton.isVisible = false
                //productButton.setLayoutEnabled(true)
            }
        }

        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }
}