package biscuitbaker.game

//data class ProductInfo(val name: String, val price: Long, val bps: Double)
// NOTE: Can't use a data class since JsonBeans expects a default constructor
class ProductInfo() {
    public var name: String = ""
    public var price: Long = 0
    public var bps: Double = 0.0
    //public var requirements: Requirements = Requirement()
}

//class Requirements() {
//    public var previouslySatisfied: Boolean = false
//    public var biscuitCount: Long? = null
//    //var upgrades: ArrayList<String>
//
//    fun isSatisfied(game: Game): Boolean {
//        if (previouslySatisfied) {
//            return true
//        }
//
//        var satisfied = true
//        if (biscuitCount != null) {
//            val biscuitCount = biscuitCount as Long
//            satisfied = satisfied && (game.biscuits >= biscuitCount?.toDouble())
//        }
//        return satisfied
//    }
//}
