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
}

class EventInfos() {
    public var events: ArrayList<EventInfo>? = null
}
