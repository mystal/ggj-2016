package biscuitbaker.game

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Json


class ProfileManager {
    var lastPlayedProfile: Int = 0

    private val json: Json = Json()

    fun getProfileData(slot: Int): GameSaveData? {
        val saveDataFile = getProfileHandle(slot)
        if (!saveDataFile.exists()) {
            return null
        }

        val saveDataString = saveDataFile.readString()
        return json.fromJson(GameSaveData::class.java, saveDataString)
    }

    fun setProfileData(slot: Int, saveData: GameSaveData) {
        val saveDataFile = getProfileHandle(slot)
        val saveDataString = json.prettyPrint(saveData)
        saveDataFile.writeString(saveDataString, false)
    }

    fun getProfileHandle(slot: Int): FileHandle {
        return when (Gdx.app.type) {
            // On Android, put files in app private storage.
            ApplicationType.Android -> Gdx.files.local("profiles/profile$slot.json")
            // On Desktop, put files in the user's home directory.
            ApplicationType.Desktop -> Gdx.files.external("profiles/profile$slot.json")
            else -> throw RuntimeException("Unsupported platform!")
        }
    }
}
