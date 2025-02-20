package seungkyu.msa.service.restaurant.persistence.orderDetail.adapter

import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import seungkyu.msa.service.common.valueObject.Money
import seungkyu.msa.service.common.valueObject.OrderId
import seungkyu.msa.service.common.valueObject.ProductId
import seungkyu.msa.service.common.valueObject.RestaurantId
import seungkyu.msa.service.restaurant.domain.entity.OrderDetail
import seungkyu.msa.service.restaurant.persistence.orderDetail.entity.OrderDetailEntity
import seungkyu.msa.service.restaurant.persistence.orderDetail.repository.OrderDetailMongoRepository
import seungkyu.msa.service.restaurant.service.ports.output.repository.OrderDetailRepository

@Repository
class OrderDetailRepositoryImpl(
    private val orderDetailMongoRepository: OrderDetailMongoRepository
): OrderDetailRepository {

    override fun save(orderDetail: OrderDetail): Mono<OrderDetail> {
        return orderDetailMongoRepository.save(
            orderDetailToOrderDetailEntity(orderDetail)
        ).map(this::orderDetailEntityToOrderDetail)
    }

    private fun orderDetailToOrderDetailEntity(orderDetail: OrderDetail): OrderDetailEntity {
        return OrderDetailEntity(
            id = orderDetail.id.id,
            restaurantId = orderDetail.restaurantId.id,
            totalAmount = orderDetail.totalAmount.amount,
            orderProducts = orderDetail.orderProducts.mapKeys{it.key.id},
            orderStatus = orderDetail.orderStatus,
            orderApprovalStatus = orderDetail.orderApprovalStatus
        )
    }

    private fun orderDetailEntityToOrderDetail(orderDetailEntity: OrderDetailEntity): OrderDetail {
        return OrderDetail(
            id = OrderId(orderDetailEntity.id),
            restaurantId = RestaurantId(orderDetailEntity.restaurantId),
            totalAmount = Money(orderDetailEntity.totalAmount),
            orderProducts = orderDetailEntity.orderProducts.mapKeys{ProductId(it.key)}.toMap(HashMap()),
            orderStatus = orderDetailEntity.orderStatus,
            orderApprovalStatus = orderDetailEntity.orderApprovalStatus
        )
    }
}