package seungkyu.msa.service.common.valueObject

@JvmInline
value class Money(
    val amount: Long
){
    companion object {
        val ZERO = Money(0)
    }

    fun isGreaterThanZero() = amount >= 0

    fun isGreaterThan(money: Money) = this.amount >= money.amount

    operator fun plus(other: Money):Money = Money(this.amount + other.amount)

    operator fun minus(other: Money):Money = Money(this.amount - other.amount)

    operator fun times(multiplier: Int): Money = Money(this.amount * multiplier)
}