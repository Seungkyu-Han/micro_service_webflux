package seungkyu.msa.service.common.valueObject

@JvmInline
value class Money(
    val amount: Long
){
    fun isGreaterThanZero() = amount > 0

    operator fun plus(other: Money):Money = Money(this.amount - other.amount)

    operator fun minus(other: Money):Money = Money(this.amount - other.amount)

    operator fun times(multiplier: Int): Money = Money(this.amount * multiplier)
}