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
    public var factionImage: String = ""
}

class Event(info: EventInfo) {
    public var info: EventInfo = info
        private set

    public var strings: FlavorStrings = FlavorStrings()
        private set

    public val name: String
        get() = info.name

    public val repeatable: Boolean
        get() = info.repeatable

    public val duration: Int
        get() = info.duration

    public val exp: Int
        get() = info.rewards?.exp ?: 0

    public val costs: Costs?
        get() = info.costs

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

    public fun fulfill(game: Game) {
        costs?.let { costs ->
            game.spendBiscuits(costs.biscuits.toDouble())
            game.spendEclairs(costs.eclairs.toDouble())
            game.spendCupcakes(costs.cupcakes.toDouble())
            game.spendPies(costs.pies.toDouble())
        }

        game.addExp(exp)
        game.eventManager.deactivateEvent(this)

        completedOnce = true
    }

    public fun canBeFulfilled(game: Game): Boolean {
        val costs = costs
        if (costs == null) {
            return true
        } else {
            return game.biscuits >= costs.biscuits &&
                   game.eclairs >= costs.eclairs &&
                   game.cupcakes >= costs.cupcakes &&
                   game.pies >= costs.pies
        }
    }
}

class EventInfos() {
    public var events: ArrayList<EventInfo>? = null
}
