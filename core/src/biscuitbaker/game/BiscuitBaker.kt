package biscuitbaker.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

class BiscuitBaker : ApplicationAdapter() {
    //val fontName: String = "Northwood High"

    internal lateinit var batch: SpriteBatch
    internal lateinit var img: Texture

    //internal lateinit var ttfFont: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        img = Texture("badlogic.jpg")

        //var generator = FreeTypeFontGenerator(Gdx.files.internal(fontName + ".ttf"))
        //var parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        //parameter.size = 32
        //ttfFont = generator.generateFont(parameter)
        //generator.dispose()
    }

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        //ttfFont.draw(batch, "Hello ttf world!", 350f, 100f)
        batch.draw(img, 0f, 0f)
        batch.end()
    }
}
