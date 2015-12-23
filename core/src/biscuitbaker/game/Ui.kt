package biscuitbaker.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import java.util.*

class Ui(game: Game) {
    internal val WIDTH: Float = 800f
    internal val HEIGHT: Float = 600f

    internal val skin: Skin = Skin(Gdx.files.internal("ui/uiskin.json"))
    internal val stage: Stage = Stage(FitViewport(WIDTH, HEIGHT))

    internal var biscuitsOwned: Label
    internal var biscuitsPerSecond: Label

    internal var productStatuses: ArrayList<Label> = ArrayList()
    internal var productButtons: ArrayList<TextButton> = ArrayList()

    internal var upgradeStatuses: ArrayList<Label> = ArrayList()
    internal var upgradeButtons: ArrayList<TextButton> = ArrayList()

    init {
        Gdx.input.inputProcessor = stage

        val table = Table()
        table.setFillParent(true)
        table.debug = true

        val bakeryName = TextField("My Bakery", skin)
        bakeryName.setAlignment(Align.center)
        biscuitsOwned = Label("%.0f biscuits".format(game.biscuits), skin)
        biscuitsPerSecond = Label("%.0f biscuits per second".format(game.bps), skin)
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

        val centerColumn = VerticalGroup()

        // TODO: Show producer tiles

        val storeColumn = VerticalGroup()

        val storeLabel = Label("Store", skin)
        storeColumn.addActor(storeLabel)

        val upgradesLabel = Label("Upgrades", skin)
        storeColumn.addActor(upgradesLabel)

        // TODO: Add separator

        game.upgrades.forEachIndexed { i, upgrade ->
            val purchasedStatus = if (upgrade.purchased) "purchased" else "available"
            val upgradeStatus = Label("%s: %s".format(upgrade.name, purchasedStatus), skin)
            val upgradeButton = TextButton("Buy %s: %d biscuits".format(upgrade.name, upgrade.price), skin)
            upgradeButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buyUpgrade(i)
                }
            })

            storeColumn.addActor(upgradeStatus)
            storeColumn.addActor(upgradeButton)
            upgradeStatuses.add(upgradeStatus)
            upgradeButtons.add(upgradeButton)
        }

        // TODO: Add separator

        val productsLabel = Label("Products", skin)
        storeColumn.addActor(productsLabel)

        game.products.forEachIndexed { i, product ->
            val productStatus = Label("%s: %d".format(product.name, product.owned), skin)
            val productButton = TextButton("Buy %s: %d biscuits".format(product.name, product.price), skin)
            productButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buyProduct(i)
                }
            })

            storeColumn.addActor(productStatus)
            storeColumn.addActor(productButton)
            productStatuses.add(productStatus)
            productButtons.add(productButton)
        }

        table.add(biscuitColumn).width(250f).expandY().top()
        table.add(centerColumn).width(350f).expandY().top()
        table.add(storeColumn).width(300f).expandY().top()

        stage.addActor(table)
    }

    fun update(dt: Float) {
        stage.act(dt)
    }

    fun render(dt: Float, game: Game) {
        biscuitsOwned.setText("%.0f biscuits".format(game.biscuits))
        biscuitsPerSecond.setText("%.1f biscuits per second".format(game.bps))

        game.upgrades.forEachIndexed { i, upgrade ->
            val upgradeStatus = upgradeStatuses[i]
            val upgradeButton = upgradeButtons[i]
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
            if (product.isVisible(game)) {
                productStatus.isVisible = true
                //productStatus.setLayoutEnabled(false)
                productStatus.setText("%s: %d".format(product.name, product.owned))
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