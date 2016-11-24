package biscuitbaker.game.ui

import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable


class ProductDescriptionCard() {
    val table: VisTable = VisTable()

    val descriptionLabel: VisLabel = VisLabel()

    init {
        table.add(descriptionLabel)
    }
}
