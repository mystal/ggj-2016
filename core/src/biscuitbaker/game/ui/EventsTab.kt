package biscuitbaker.game.ui

import biscuitbaker.game.Game
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.tabbedpane.Tab

class EventsTab(game: Game, skin: Skin): Tab() {
    internal val content: VisTable = VisTable()

    override fun isCloseableByUser(): Boolean {
        return false
    }

    override fun getContentTable(): Table? {
        return content
    }

    override fun getTabTitle(): String? {
        return "Events"
    }
}
