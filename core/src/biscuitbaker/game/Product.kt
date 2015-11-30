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

    public var displayCondition: Long = 0
}