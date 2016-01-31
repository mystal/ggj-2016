package biscuitbaker.game

import biscuitbaker.game.ui.Ui
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

class BiscuitBaker(val debug: Boolean) : ApplicationAdapter() {
    internal lateinit var game: Game
    internal lateinit var ui: Ui

    override fun create() {
        game = Game(debug)
        ui = Ui(game)
    }

    override fun resize(width: Int, height: Int) {
        ui.resize(width, height)
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val dt = Gdx.graphics.deltaTime
        ui.update(dt)
        game.update(dt, ui)
        ui.render(dt, game)
    }
}
