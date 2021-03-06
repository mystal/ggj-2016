package biscuitbaker.game

import biscuitbaker.game.ui.Ui
import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20


class BiscuitBaker(val debug: Boolean) : ApplicationAdapter() {
    internal lateinit var profileManager: ProfileManager
    internal lateinit var game: Game
    internal lateinit var ui: Ui

    override fun create() {
        // TODO: Show a screen to pick which profile to play.

        profileManager = ProfileManager()
        game = Game(debug)
        ui = Ui(game)

        // TODO: Load state when a profile is picked.
        val saveData = profileManager.getProfileData(profileManager.lastPlayedProfile)
        if (saveData != null) {
            game.loadState(saveData, ui)
        }
    }

    override fun resize(width: Int, height: Int) {
        ui.resize(width, height)
    }

    override fun render() {
        // Update UI and game state.
        val dt = Gdx.graphics.deltaTime
        ui.update(dt)
        game.update(dt, ui)

        // Render the game UI.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        ui.render(dt, game)
    }

    override fun pause() {
        // Save out the game state.
        val saveData = game.saveState()
        profileManager.setProfileData(profileManager.lastPlayedProfile, saveData)
    }
}
