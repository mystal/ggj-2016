package biscuitbaker.game

// TODO: Rename to reflect use in Products and Upgrades
class ProductRequirements() {
    public var biscuits: Long? = null
    //var upgrades: ArrayList<String>

    public fun isSatisfied(game: Game): Boolean {
        var satisfied = true
        biscuits?.let { biscuits ->
            satisfied = satisfied && (game.biscuits >= biscuits)
        }
        return satisfied
    }
}
