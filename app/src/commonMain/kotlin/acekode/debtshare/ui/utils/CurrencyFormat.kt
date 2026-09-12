package acekode.debtshare.ui.utils

import kotlin.math.abs
import kotlin.math.round

const val MINUS_SIGN = '−'

private const val CENTS_PER_UNIT = 100L
private const val GROUP_SIZE = 3
private const val DECIMAL_DIGITS = 2
private const val EURO_SUFFIX = " €"

fun formatEuros(amount: Double): String {
    val cents = round(abs(amount) * CENTS_PER_UNIT).toLong()
    val units = groupThousands(cents / CENTS_PER_UNIT)
    val decimals = (cents % CENTS_PER_UNIT).toString().padStart(DECIMAL_DIGITS, '0')
    return "$units,$decimals$EURO_SUFFIX"
}

fun formatSignedEuros(amount: Double): String {
    val sign = if (amount < 0) MINUS_SIGN else '+'
    return "$sign${formatEuros(amount)}"
}

private fun groupThousands(value: Long): String {
    val digits = value.toString()
    return buildString {
        digits.forEachIndexed { index, digit ->
            if (index > 0 && (digits.length - index) % GROUP_SIZE == 0) append('.')
            append(digit)
        }
    }
}
