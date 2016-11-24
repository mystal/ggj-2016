package biscuitbaker.game.ui

import biscuitbaker.game.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab
import java.util.*


internal const val MAX_COLUMNS: Int = 4


class StoreTab(game: Game, ui: Ui, skin: Skin): Tab() {
    internal val content: VisTable = VisTable()

    internal var productImages: ArrayList<Image> = ArrayList()
    internal var productStatuses: ArrayList<Label> = ArrayList()
    internal var productButtons: ArrayList<TextButton> = ArrayList()

    internal var upgradeImages: ArrayList<Image> = ArrayList()
    internal var upgradeStatuses: ArrayList<Label> = ArrayList()
    internal var upgradeButtons: ArrayList<TextButton> = ArrayList()

    internal var defaultTexture: Texture

    init {
        defaultTexture = Texture(Gdx.files.internal("img/bread_boy.png"))

        val upgradesLabel = Label("Upgrades", skin)
        content.add(upgradesLabel).colspan(MAX_COLUMNS)

        content.row()

        // TODO: Use a HorizontalFlowGroup.
        content.addSeparator().colspan(MAX_COLUMNS)

        var col = 0
        game.upgrades.forEachIndexed { i, upgrade ->
            val upgradeImage = getImage(upgrade.info.image)

            val buttonText = if (upgrade.purchased) "Purchased" else "Buy: %d biscuits".format(upgrade.price)
            val upgradeStatus = Label(upgrade.name, skin)
            val upgradeButton = TextButton(buttonText, skin)
            upgradeButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buyUpgrade(i)
                }
            })

            val t = VisTable()
            val nt = VisTable()
            t.add(nt)
            nt.add(upgradeImage).expandX()
            nt.add(upgradeStatus).expandX()
            t.row()
            t.add(upgradeButton)
            t.row()

            t.addListener(object : ClickListener() {
                override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                    if (pointer != -1) {
                        return
                    }

                    ui.descriptionCard.setName(upgrade.name)
                    ui.descriptionCard.setDescription(upgrade.strings.description)
                    ui.descriptionCard.setFlavor(upgrade.strings.flavor)
                }
            })

            content.add(t)
            col += 1
            if (col == MAX_COLUMNS) {
                content.row()
                col = 0
            }

            upgradeImages.add(upgradeImage)
            upgradeStatuses.add(upgradeStatus)
            upgradeButtons.add(upgradeButton)
        }

        content.row()

        content.addSeparator().colspan(MAX_COLUMNS)

        val productsLabel = Label("Products", skin)
        content.add(productsLabel).colspan(MAX_COLUMNS)

        content.row()

        content.addSeparator().colspan(MAX_COLUMNS)

        col = 0
        game.products.forEachIndexed { i, product ->
            //val productStatus = Label("%s: %d\nBpS: %.1f\nTotal BpS: %.1f".format(
              //      product.name, product.owned, product.bps, product.totalBps), skin)

            val productStatus = Label("%s: %d\nBPS: %.1f".format(product.name, product.owned, product.bps), skin)

            val productButton = TextButton("Buy: %d biscuits".format(product.price), skin)
            productButton.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    game.buyProduct(i)
                }
            })

            val productImage = getImage(product.info.image)

            val t = VisTable()
            val nt = VisTable()
            t.add(nt)
            nt.add(productImage).expandX()
            nt.add(productStatus).expandX()
            t.row()
            t.add(productButton)
            t.row()

            t.addListener(object : ClickListener() {
                override fun enter(event: InputEvent?, x: Float, y: Float, pointer: Int, fromActor: Actor?) {
                    if (pointer != -1) {
                        return
                    }

                    ui.descriptionCard.setName(product.name)
                    ui.descriptionCard.setDescription(product.strings.description)
                    ui.descriptionCard.setFlavor(product.strings.flavor)
                }
            })

            content.add(t)
            col += 1
            if (col == MAX_COLUMNS) {
                content.row()
                col = 0
            }

            productImages.add(productImage)
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
            val upgradeImage = upgradeImages[i]
            val upgradeStatus = upgradeStatuses[i]
            val upgradeButton = upgradeButtons[i]

            // TODO: Don't update visibility every frame, it can be expensive
            if (upgrade.isVisible(game)) {
                upgradeImage.isVisible = true
                val buttonText = if (upgrade.purchased) "Purchased" else "Buy: %d biscuits".format(upgrade.price)
                upgradeStatus.isVisible = true
                //upgradeStatus.setLayoutEnabled(false)
                upgradeButton.isVisible = true
                //upgradeButton.setLayoutEnabled(false)
                upgradeButton.setText(buttonText)
                upgradeButton.isDisabled = upgrade.purchased || (game.biscuits < upgrade.price)
            } else {
                upgradeImage.isVisible = false
                upgradeStatus.isVisible = false
                //upgradeStatus.setLayoutEnabled(true)
                upgradeButton.isVisible = false
                //upgradeButton.setLayoutEnabled(true)
            }
        }

        game.products.forEachIndexed { i, product ->
            val productStatus = productStatuses[i]
            val productButton = productButtons[i]
            val productImage = productImages[i]

            // TODO: Don't update visibility every frame, it can be expensive
            if (product.isVisible(game)) {
                productImage.isVisible = true
                productStatus.isVisible = true
                //productStatus.setLayoutEnabled(false)
               // productStatus.setText("%s: %d\nBpS: %.1f\nTotal BpS: %.1f".format(
                 //       product.name, product.owned, product.bps, product.totalBps))
                productStatus.setText("%s: %d\nBPS: %.1f".format(product.name, product.owned, product.bps))

                productButton.isVisible = true
                //productButton.setLayoutEnabled(false)
                productButton.isDisabled = game.biscuits < product.price
            } else {
                productImage.isVisible = false
                productStatus.isVisible = false
                //productStatus.setLayoutEnabled(true)
                productButton.isVisible = false
                //productButton.setLayoutEnabled(true)
            }
        }
    }

    fun getImage(path: String): Image {
        val texture = getTexture(path)

        val image = Image(texture)
        image.scaleBy(-0.35f)
        return image
    }

    fun getTexture(path: String): Texture {
        if (path == "") {
            return defaultTexture
        }

        val imageHandle = Gdx.files.internal("img/$path")
        if (!imageHandle.exists()) {
            return defaultTexture
        }

        return Texture(imageHandle)
    }
}
