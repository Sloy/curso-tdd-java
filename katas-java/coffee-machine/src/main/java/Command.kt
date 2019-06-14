import kotlin.math.absoluteValue

sealed class Command {
    class ValidOrder(val drink: DrinkType, val sugar: Int) : Command() {
        fun format(): String {
            val drinkCommand: String = when (drink) {
                DrinkType.TEA -> "T"
                DrinkType.COFFEE -> "C"
                DrinkType.CHOCOLATE -> "H"
                DrinkType.UNSELECTED -> ""
            }
            val sugarCommand = sugar.takeIf { it > 0 }?.toString() ?: ""
            val stickCommand = "0".takeIf { sugar > 0 } ?: ""

            return "$drinkCommand:$sugarCommand:$stickCommand"
        }
    }

    class MissingMoneyError(val missingAmount: Double) : Command() {
        fun format(): String {
            val formattedAmount = missingAmount.absoluteValue.toRoundedString()
            return "M:Not enough money inserted. Missing $formattedAmount €"
        }
    }

    class IncompleteOrder() : Command()
}


private fun Double.toRoundedString() = "%.2f".format(this)