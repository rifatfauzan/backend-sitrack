package be_sitruck.backend_sitruck.restservice;

import java.util.List;
import java.util.Map;

import be_sitruck.backend_sitruck.restdto.request.ApproveOrderRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.CreateOrderRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateOrderResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.OrderDetailResponseDTO;

public interface OrderRestService {
    CreateOrderResponseDTO addOrder(CreateOrderRequestDTO request);
    List<OrderDetailResponseDTO> getAllOrders();
    OrderDetailResponseDTO getOrderById(String orderId);
    CreateOrderResponseDTO approveOrder(ApproveOrderRequestDTO request);
    CreateOrderRequestDTO updateOrder (String orderId, CreateOrderRequestDTO request);
    void markOrderAsDone(String orderId);
    List<Map<String, Object>> getMonthlyOrderStats(int year);
}
