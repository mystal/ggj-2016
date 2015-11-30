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

    internal var skin: Skin = Skin(Gdx.files.internal("ui/uiskin.json"))
    internal var stage: Stage = Stage(FitViewport(WIDTH, HEIGHT))

    internal var biscuitsOwned: Label
    internal var biscuitsPerSecond: Label

    internal var productStatuses: ArrayList<Label> = ArrayList()
    internal var productButtons: ArrayList<TextButton> = ArrayList()

    init {
        Gdx.input.inputProcessor = stage

        var table = Table()
        table.setFillParent(true)
        table.debug()

        var bakeryName = TextField("My Bakery", skin)
        bakeryName.setAlignment(Align.center)
        biscuitsOwned = Label("%.0f biscuits".format(game.biscuits), skin)
        biscuitsPerSecond = Label("%.0f biscuits per second".format(game.bps), skin)
        var biscuitButton = TextButton("Biscuit Get", skin)
        biscuitButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.click()
            }
        })

        var biscuitColumn = VerticalGroup()
        //biscuitColumn.align(Align.center)
        biscuitColumn.addActor(bakeryName)
        biscuitColumn.addActor(biscuitsOwned)
        biscuitColumn.addActor(biscuitsPerSecond)
        biscuitColumn.addActor(biscuitButton)

        var centerColumn = VerticalGroup()

        var storeColumn = VerticalGroup()

        game.products.forEachIndexed { i, product ->
            var productStatus = Label("%s: %d".format(product.name, product.owned), skin)
            var productButton = TextButton("Buy %s: %d biscuits".format(product.name, product.price), skin)
            productButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buy(i)
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

        game.products.forEachIndexed { i, product ->
            var productStatus = productStatuses[i]
            var productButton = productButtons[i]
            if (product.isVisible(game)) {
                productStatus.isVisible = true
                productStatus.setText("%s: %d".format(product.name, product.owned))
                productButton.isVisible = true
                productButton.isDisabled = game.biscuits < product.price
            } else {
                productStatus.isVisible = false
                productButton.isVisible = false
            }
        }
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height)
    }
}