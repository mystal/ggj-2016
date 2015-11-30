package biscuitbaker.game

class Product(info: ProductInfo) {
    public var info: ProductInfo = info
        private set

    public var owned: Int = 0

    public val name: String
        get() = info.name

    public val price: Long
        get() = info.price

    // TODO: Make this a Long
    public val bps: Double
        get() = info.bps

    // Whether to display this product in the store. Once visible, it
    // will always be visible.
    private var visible: Boolean = false

    // TODO: Add several levels of visibility
    // e.g. show shadowed item first, then full item once it is affordable
    public fun isVisible(game: Game): Boolean {
        if (!visible) {
            visible = info.requirements?.isSatisfied(game) ?: true
        }
        return visible
    }
}