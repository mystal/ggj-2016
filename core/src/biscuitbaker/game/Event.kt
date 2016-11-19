package biscuitbaker.game

import java.util.*

class EventInfo() {
    var name: String = ""
    var faction: String = ""
    var duration: Int = 0
    var repeatable: Boolean = false
    var prereqs: Prerequisites? = null
    var costs: Costs? = null
    var rewards: Rewards? = null
    var factionImage: String = ""
}

class Event(info: EventInfo) {
    var info: EventInfo = info
        private set

    var strings: FlavorStrings = FlavorStrings()
        private set

    val name: String
        get() = info.name

    val repeatable: Boolean
        get() = info.repeatable

    val duration: Int
        get() = info.duration

    val exp: Int
        get() = info.rewards?.exp ?: 0

    val costs: Costs?
        get() = info.costs

    var timeRemaining: Float = 0f
        private set

    var completedOnce: Boolean = false
        private set

    // Whether this event can pop up. Once visible, it
    // will always be visible.
    private var visible: Boolean = false

    fun tickTimer(dt: Float) {
        timeRemaining -= dt
    }

    fun activate() {
        timeRemaining = duration * GameTime.DAY
    }

    fun isAvailable(game: Game): Boolean {
        return isVisible(game) && (!completedOnce || repeatable) && timeRemaining <= 0f
    }

    // TODO: Add several levels of visibility
    fun isVisible(game: Game): Boolean {
        // Once visible, always visible
        if (!visible) {
            visible = info.prereqs?.isSatisfied(game) ?: true
        }
        return visible
    }

    fun fulfill(game: Game) {
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

    fun canBeFulfilled(game: Game): Boolean {
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
    var events: ArrayList<EventInfo>? = null
}
