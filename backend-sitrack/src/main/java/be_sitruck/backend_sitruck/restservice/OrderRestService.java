package be_sitruck.backend_sitruck.restservice;

import java.util.List;

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

}
