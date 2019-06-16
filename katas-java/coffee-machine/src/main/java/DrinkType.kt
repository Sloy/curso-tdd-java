enum class DrinkType(val price: Double, val canServeHot: Boolean) {
    COFFEE(0.6, canServeHot = true),
    TEA(0.4, canServeHot = true),
    CHOCOLATE(0.5, canServeHot = true),
    ORANGE_JUICE(0.6, canServeHot = false),
    UNSELECTED(0.0, canServeHot = false)

}