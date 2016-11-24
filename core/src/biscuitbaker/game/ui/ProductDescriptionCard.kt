package biscuitbaker.game.ui

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable


class ProductDescriptionCard() {
    val table: VisTable = VisTable()

    val nameLabel: VisLabel = VisLabel()
    val descriptionLabel: VisLabel = VisLabel()
    val flavorLabel: VisLabel = VisLabel()

    init {
        //table.debug = true
        nameLabel.setWrap(true)
        nameLabel.setAlignment(Align.center)
        descriptionLabel.setWrap(true)
        flavorLabel.setWrap(true)

        table.add(nameLabel).fillX().expandX()
        table.row()
        table.add(descriptionLabel).fillX()
        table.row()
        table.add(flavorLabel).fillX()
    }

    fun setName(text: String) {
        nameLabel.setText(text)
    }

    fun setDescription(text: String) {
        descriptionLabel.setText(text)
    }

    fun setFlavor(text: String) {
        flavorLabel.setText("\"$text\"")
    }
}
