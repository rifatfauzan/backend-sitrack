package be_sitruck.backend_sitruck.restcontroller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be_sitruck.backend_sitruck.restdto.request.ApproveOrderRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.CreateOrderRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.BaseResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.CreateOrderResponseDTO;
import be_sitruck.backend_sitruck.restdto.response.OrderDetailResponseDTO;
import be_sitruck.backend_sitruck.restservice.OrderRestService;
import jakarta.validation.Valid;
import lombok.experimental.var;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderRestService orderRestService;

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@Valid @RequestBody CreateOrderRequestDTO requestDTO) {
        BaseResponseDTO<CreateOrderResponseDTO> response = new BaseResponseDTO<>();
        try {
            CreateOrderResponseDTO result = orderRestService.addOrder(requestDTO);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(result.getMessage());
            response.setTimestamp(new Date());
            response.setData(result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // @GetMapping("/all")
    // public ResponseEntity<?> getAllOrders() {
    //     List<OrderDetailResponseDTO> orders = orderRestService.getAllOrders();
    //     return ResponseEntity.ok(orders);
    // }

    // @GetMapping("/detail/{orderId}")
    // public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
    //     return ResponseEntity.ok(orderRestService.getOrderById(orderId));
    // }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(){
        var baseResponseDTO = new BaseResponseDTO<List<OrderDetailResponseDTO>>();
        List<OrderDetailResponseDTO> orders = orderRestService.getAllOrders();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(orders);
        baseResponseDTO.setMessage(String.format("List order berhasil ditemukan sebanyak %d chassis", orders.size()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);

    }

    @GetMapping("/detail")
    public ResponseEntity<?> getChassisById(@RequestParam("id") String orderId) {
        var baseResponseDTO = new BaseResponseDTO<OrderDetailResponseDTO>();
        try {
            OrderDetailResponseDTO orderDetail = orderRestService.getOrderById(orderId);
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage("Order berhasil ditemukan!");
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(orderDetail);
            return ResponseEntity.ok(baseResponseDTO);

        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            baseResponseDTO.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body(baseResponseDTO);
        }   
    }

    @PutMapping("/approve")
    public ResponseEntity<?> approveOrder(@RequestBody ApproveOrderRequestDTO request) {
        BaseResponseDTO<String> response = new BaseResponseDTO<>();
        try {
            orderRestService.approveOrder(request);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Approval order berhasil diubah!");
            response.setTimestamp(new Date());
            response.setData("Success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable("orderId") String orderId, @RequestBody CreateOrderRequestDTO requestDTO) {
        try {
            var response = new BaseResponseDTO<>();
            response.setStatus(HttpStatus.OK.value());
            response.setMessage("Data Order berhasil diupdate!");
            response.setTimestamp(new Date());
            response.setData(orderRestService.updateOrder(orderId, requestDTO));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            var response = new BaseResponseDTO<>();
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(e.getMessage());
            response.setTimestamp(new Date());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}
