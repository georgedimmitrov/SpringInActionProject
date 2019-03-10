package tacos.repositories;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tacos.models.Order;

public interface OrderRepository
		extends CrudRepository<Order, Long> {
	List<Order> findByDeliveryZip(String deliveryZip);
	
	List<Order> readOrdersByDeliveryZipAndPlacedAtBetween(
			String deliveryZip, Date startDate, Date endDate);
	
	List<Order> findByDeliveryToAndDeliveryCityAllIgnoresCase(
			String deliveryTo, String deliveryCity);
	
	List<Order> findByDeliveryCityOrderByDeliveryTo(String city);
	
	@Query("Order o where o.deliveryCity='Seattle'")
	List<Order> readOrdersDeliveredInSeattle();
}
