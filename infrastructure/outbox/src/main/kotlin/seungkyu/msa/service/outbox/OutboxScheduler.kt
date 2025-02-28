package seungkyu.msa.service.outbox

interface OutboxScheduler{

    fun processOutboxMessages()
}
