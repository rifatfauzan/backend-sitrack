package be_sitruck.backend_sitruck.restservice;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be_sitruck.backend_sitruck.model.Order;
import be_sitruck.backend_sitruck.model.Customer;
import be_sitruck.backend_sitruck.repository.OrderDb;
import be_sitruck.backend_sitruck.repository.CustomerDb;
import be_sitruck.backend_sitruck.restdto.request.ApproveOrderRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.CreateOrderRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateOrderResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.OrderDetailResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
public class OrderRestServiceImpl implements OrderRestService {

    @Autowired
    private OrderDb orderDb;

    @Autowired
    private CustomerDb customerDb;

    @Autowired
    private NotificationRestService notificationRestService;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    @Override
    public CreateOrderResponseDTO addOrder(CreateOrderRequestDTO request) {
        Customer customer = customerDb.findById(request.getCustomerId())
            .orElseThrow(() -> new ValidationException("Customer tidak ditemukan!"));

        String orderId = generateOrderId();
        String currentUser = jwtUtils.getCurrentUsername();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderDate(request.getOrderDate());
        order.setCustomer(customer);
        order.setQtyChassis20(request.getQtyChassis20());
        order.setQtyChassis40(request.getQtyChassis40());

        order.setSiteId(customer.getSiteId());
        
        order.setRemarksOperasional(request.getRemarksOperasional());
        order.setMoveType(request.getMoveType());
        order.setDownPayment(request.getDownPayment());
        //order.setApprovalStatus(0);

        order.setCreatedBy(currentUser);
        order.setCreatedDate(new Date());
        order.setOrderStatus(1);

        order.setQty120mtfl(request.getQty120mtfl());
        order.setQty120mt(request.getQty120mt());
        order.setQty220mtfl(request.getQty220mtfl());
        order.setQty220mt(request.getQty220mt());
        order.setQty140mtfl(request.getQty140mtfl());
        order.setQty140mt(request.getQty140mt());
        order.setQty120mt120fl(request.getQty120mt120fl());
        order.setQty120mt220fl(request.getQty120mt220fl());
        order.setQty220mt120fl(request.getQty220mt120fl());
        order.setQty220mt220fl(request.getQty220mt220fl());
        order.setQtyCh120fl(request.getQtyCh120fl());
        order.setQtyCh220fl(request.getQtyCh220fl());
        order.setQtyCh140fl(request.getQtyCh140fl());

        orderDb.save(order);

        notificationRestService.createOrderApprovalNotification(orderId, Arrays.asList(1L, 2L, 3L));;

        return new CreateOrderResponseDTO(orderId, "Order berhasil ditambahkan!");
    }

    @Override
    public List<OrderDetailResponseDTO> getAllOrders() {
        return orderDb.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailResponseDTO getOrderById(String orderId) {
        Order order = orderDb.findById(orderId)
                .orElseThrow(() -> new ValidationException("Order tidak ditemukan!"));

        return toDTO(order);
    }

    private OrderDetailResponseDTO toDTO(Order order) {
        return new OrderDetailResponseDTO(
            
            order.getOrderId(),
            order.getOrderDate(),
            order.getCustomer().getId(),
            order.getQtyChassis20(),
            order.getQtyChassis40(),
            order.getSiteId(),
            order.getRemarksOperasional(),
            order.getRemarksSupervisor(),
            order.getMoveType(),
            order.getDownPayment(),
            order.getOrderStatus(),
            
            order.getQty120mtfl(),
            order.getQty120mt(),
            order.getQty220mtfl(),
            order.getQty220mt(),
            order.getQty140mtfl(),
            order.getQty140mt(),
            order.getQty120mt120fl(),
            order.getQty120mt220fl(),
            order.getQty220mt120fl(),
            order.getQty220mt220fl(),
            order.getQtyCh120fl(),
            order.getQtyCh220fl(),
            order.getQtyCh140fl(),

            order.getCreatedBy(),
            order.getCreatedDate(),
            order.getUpdatedBy(),
            order.getUpdatedDate(),
            order.getApprovedBy(),
            order.getApprovedDate()
        );
    }

    @Override
    @Transactional
    public CreateOrderResponseDTO approveOrder(ApproveOrderRequestDTO request) {
        Order order = orderDb.findById(request.getOrderId())
            .orElseThrow(() -> new ValidationException("Order tidak ditemukan!"));

        String currentUser = jwtUtils.getCurrentUsername();

        String orderId = order.getOrderId();

        order.setRemarksSupervisor(request.getRemarksSupervisor());
        order.setOrderStatus(request.getOrderStatus());
        order.setApprovedBy(currentUser);
        order.setApprovedDate(new Date());

        orderDb.save(order);

        notificationRestService.createOrderStatusNotification(
            order.getOrderId(), 
            request.getOrderStatus(),
            Arrays.asList(1L, 2L, 3L, 4L)
        );

        return new CreateOrderResponseDTO(orderId, "Approval dari order berhasil diubah!");
    }

    private String generateOrderId() {
        Order lastOrder = orderDb.findTopByOrderIdStartingWithOrderByOrderIdDesc("OR");
        int nextNumber = 1;

        if (lastOrder != null) {
            String lastOrderId = lastOrder.getOrderId();
            try {
                nextNumber = Integer.parseInt(lastOrderId.substring(3)) + 1;
            } catch (NumberFormatException ignored) {}
        }

        return String.format("OR%06d", nextNumber);
    }

    @Override
    public CreateOrderRequestDTO updateOrder(String orderId, CreateOrderRequestDTO request) {
       Order existingOrder = orderDb.findById(orderId).orElse(null);
       Customer customer = customerDb.findById(request.getCustomerId())
            .orElseThrow(() -> new ValidationException("Customer tidak ditemukan!"));

        if(existingOrder == null){
            throw new IllegalArgumentException("Order tidak ditemukan");
        }

        if (request.getMoveType().matches("[0-9]+")){
            throw new IllegalArgumentException("Move type berupa string");
        }
        existingOrder.setOrderDate(request.getOrderDate());
        existingOrder.setCustomer(customer);
        existingOrder.setQtyChassis20(request.getQtyChassis20());
        existingOrder.setQtyChassis40(request.getQtyChassis40());
        existingOrder.setSiteId(customer.getSiteId());
        existingOrder.setRemarksOperasional(request.getRemarksOperasional());
        existingOrder.setMoveType(request.getMoveType());
        existingOrder.setDownPayment(request.getDownPayment());
        existingOrder.setUpdatedDate(Date.from(java.time.Instant.now()));
        existingOrder.setUpdatedBy(jwtUtils.getCurrentUsername());
        existingOrder.setQty120mtfl(request.getQty120mtfl());
        existingOrder.setQty120mt(request.getQty120mt());
        existingOrder.setQty220mtfl(request.getQty220mtfl());
        existingOrder.setQty220mt(request.getQty220mt());
        existingOrder.setQty140mtfl(request.getQty140mtfl());
        existingOrder.setQty140mt(request.getQty140mt());
        existingOrder.setQty120mt120fl(request.getQty120mt120fl());
        existingOrder.setQty120mt220fl(request.getQty120mt220fl());
        existingOrder.setQty220mt120fl(request.getQty220mt120fl());
        existingOrder.setQty220mt220fl(request.getQty220mt220fl());
        existingOrder.setQtyCh120fl(request.getQtyCh120fl());
        existingOrder.setQtyCh220fl(request.getQtyCh220fl());
        existingOrder.setQtyCh140fl(request.getQtyCh140fl());
        existingOrder.setRemarksOperasional(request.getRemarksOperasional());
        existingOrder.setOrderStatus(1);

        orderDb.save(existingOrder);
        notificationRestService.createOrderApprovalNotification(orderId, Arrays.asList(1L, 2L, 3L));;

        return request;
    }
}
