package biscuitbaker.game

import biscuitbaker.game.ui.Ui
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import com.moandjiezana.toml.Toml
import java.util.*

const internal val EVENT_CHANCE: Float = 0.30f
const internal val EVENT_FREQUENCY: Float = GameTime.DAY


class EventManager {
    var events: ArrayList<Event> = ArrayList()
        private set

    var activeEvents: ArrayList<Event> = ArrayList()
        private set

    var eventTimer: Float = EVENT_FREQUENCY

    private val rand: Random = Random()

    init {
        loadData()
    }

    private fun loadData() {
        val eventsToml = Toml().read(Gdx.files.internal("text/events.toml").readString());

        val json = Json()

        val eventsFile = Gdx.files.internal("data/events.json")
        val eventJson = eventsFile.readString()
        val eventInfos = json.fromJson(EventInfos::class.java, eventJson)
        for (info in eventInfos.events!!) {
            val event = Event(info)
            val table = eventsToml.getTable("\"${event.name}\"")
            event.strings.flavor = table.getString("flavor")
            event.strings.description = table.getString("description")
            events.add(event)
        }
    }

    // TODO: Don't pass in Ui, instead return new event and deactivated events
    fun update(dt: Float, game: Game, ui: Ui) {
        eventTimer -= dt

        // Check if we should try to activate a new event
        if (eventTimer <= 0f) {
            if (rand.nextFloat() >= EVENT_CHANCE) {
                // Activate an available event
                tryActivateEvent(game, ui)
            }
            eventTimer = EVENT_FREQUENCY
        }

        // Tick active events
        activeEvents.removeIf { event ->
            event.tickTimer(dt)
            if (event.timeRemaining <= 0f) {
                ui.removeEvent(event)
                true
            } else {
                false
            }
        }
    }

    fun tryActivateEvent(game: Game, ui: Ui): Boolean {
        val event = selectEvent(game)
        if (event != null) {
            print("Activating event: $event")
            event.activate()
            activeEvents.add(event)

            activeEvents.sortBy { it.timeRemaining }

            val index = activeEvents.indexOf(event)

            ui.addEvent(event, index)
            return true
        }
        return false
    }

    // TODO: Do something smarter than picking a random available event?
    fun selectEvent(game: Game): Event? {
        val availableEvents = events.filter { event ->
            event.isAvailable(game)
        }

        if (availableEvents.isEmpty()) {
            return null
        }

        val index = rand.nextInt(availableEvents.size)

        return availableEvents[index]
    }

    fun deactivateEvent(event: Event) {
        activeEvents.remove(event)
    }
}
