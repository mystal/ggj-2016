package biscuitbaker.game.ui

import biscuitbaker.game.Game
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import java.util.*

class StoreTab(game: Game, skin: Skin): Tab() {
    internal val content: VisTable = VisTable()

    internal var productStatuses: ArrayList<Label> = ArrayList()
    internal var productButtons: ArrayList<TextButton> = ArrayList()

    internal var upgradeStatuses: ArrayList<Label> = ArrayList()
    internal var upgradeButtons: ArrayList<TextButton> = ArrayList()

    init {
        val storeLabel = Label("Store", skin)
        content.add(storeLabel)

        content.row()

        val upgradesLabel = Label("Upgrades", skin)
        content.add(upgradesLabel)

        content.row()

        content.addSeparator()

        game.upgrades.forEachIndexed { i, upgrade ->
            val purchasedStatus = if (upgrade.purchased) "purchased" else "available"
            val upgradeStatus = Label("%s: %s".format(upgrade.name, purchasedStatus), skin)
            val upgradeButton = TextButton("Buy: %d biscuits".format(upgrade.price), skin)
            upgradeButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buyUpgrade(i)
                }
            })

            content.add(upgradeStatus)
            content.row()
            content.add(upgradeButton)
            content.row()
            upgradeStatuses.add(upgradeStatus)
            upgradeButtons.add(upgradeButton)
        }

        content.row()

        content.addSeparator()

        val productsLabel = Label("Products", skin)
        content.add(productsLabel)

        content.row()

        game.products.forEachIndexed { i, product ->
            val productStatus = Label("%s: %d\nBpS: %.1f\nTotal BpS: %.1f".format(
                    product.name, product.owned, product.bps, product.totalBps), skin)
            val productButton = TextButton("Buy: %d biscuits".format(product.price), skin)
            productButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buyProduct(i)
                }
            })

            content.add(productStatus)
            content.row()
            content.add(productButton)
            content.row()
            productStatuses.add(productStatus)
            productButtons.add(productButton)
        }
    }

    override fun isCloseableByUser(): Boolean {
        return false
    }

    override fun getTabTitle(): String? {
        return "Store"
    }

    override fun getContentTable(): Table? {
        return content
    }

    fun render(dt: Float, game: Game) {
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
    }
}
