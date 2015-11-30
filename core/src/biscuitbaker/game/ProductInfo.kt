package biscuitbaker.game

//data class ProductInfo(val name: String, val price: Long, val bps: Double)
// NOTE: Can't use a data class since JsonBeans expects a default constructor
class ProductInfo() {
    public var name: String = ""
    public var price: Long = 0
    public var bps: Double = 0.0
    public var requirements: Requirements? = null
}

class Requirements() {
    public var biscuits: Long? = null
    //var upgrades: ArrayList<String>

    public fun isSatisfied(game: Game): Boolean {
        var satisfied = true
        if (biscuits != null) {
            // TODO: Any cleaner way to handle the potential null?
            val biscuits = biscuits as Long
            satisfied = satisfied && (game.biscuits >= biscuits)
        }
        return satisfied
    }
}
