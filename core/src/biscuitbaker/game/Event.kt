package biscuitbaker.game

import java.util.*

class EventInfo() {
    public var name: String = ""
    public var faction: String = ""
    public var duration: Int = 0
    public var repeatable: Boolean = false
    public var prereqs: Prerequisites? = null
    public var costs: Costs? = null
    public var rewards: Rewards? = null
}

class Event(info: EventInfo) {
    public var info: EventInfo = info
        private set

    public val name: String
        get() = info.name

    public val repeatable: Boolean
        get() = info.repeatable

    public val duration: Int
        get() = info.duration

    public var timeRemaining: Float = 0f
        private set

    public var completedOnce: Boolean = false
        private set

    // Whether this event can pop up. Once visible, it
    // will always be visible.
    private var visible: Boolean = false

    public fun tickTimer(dt: Float) {
        timeRemaining -= dt
    }

    public fun activate() {
        timeRemaining = duration * GameTime.DAY
    }

    public fun isAvailable(game: Game): Boolean {
        return isVisible(game) && (!completedOnce || repeatable) && timeRemaining <= 0f
    }

    // TODO: Add several levels of visibility
    public fun isVisible(game: Game): Boolean {
        // Once visible, always visible
        if (!visible) {
            visible = info.prereqs?.isSatisfied(game) ?: true
        }
        return visible
    }
}

class EventInfos() {
    public var events: ArrayList<EventInfo>? = null
}
