package be_sitruck.backend_sitruck.restservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be_sitruck.backend_sitruck.model.Chassis;
import be_sitruck.backend_sitruck.model.Customer;
import be_sitruck.backend_sitruck.model.Order;
import be_sitruck.backend_sitruck.model.SopirModel;
import be_sitruck.backend_sitruck.model.Spj;
import be_sitruck.backend_sitruck.model.Truck;
import be_sitruck.backend_sitruck.repository.ChassisDb;
import be_sitruck.backend_sitruck.repository.OrderDb;
import be_sitruck.backend_sitruck.repository.SopirDb;
import be_sitruck.backend_sitruck.repository.SpjDb;
import be_sitruck.backend_sitruck.repository.TruckDb;
import be_sitruck.backend_sitruck.restdto.request.ApproveSpjRequestDTO;
import be_sitruck.backend_sitruck.restdto.request.CreateSpjRequestDTO;
import be_sitruck.backend_sitruck.restdto.response.SpjResponseDTO;
import be_sitruck.backend_sitruck.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;

@Service
public class SpjRestServiceImpl implements SpjRestService {

    @Autowired
    private SpjDb spjDb;

    @Autowired
    private OrderDb orderDb;

    @Autowired
    private ChassisDb chassisDb;

    @Autowired
    private SopirDb sopirDb;

    @Autowired
    private TruckDb truckDb;

    @Autowired
    private NotificationRestService notificationRestService;

    @Autowired
    private JwtUtils jwtUtils;

    private String generateSpjId() {
        var optionalSpj = spjDb.findFirstByIdStartingWithOrderByIdDesc("SPJ");
        if (optionalSpj.isEmpty()) {
            return "SPJ00001";
        } else {
            String id = optionalSpj.get().getId();
            int number = Integer.parseInt(id.substring(3)) + 1;
            return String.format("SPJ%05d", number);
        }
    }

    @Override
    public Map<String, Integer> getAvailableChassisAndContainers(String orderId) {
        Order order = orderDb.findById(orderId)
            .orElseThrow(() -> new ValidationException("Order tidak ditemukan!"));

        List<Spj> spjList = spjDb.findByOrderAndStatusIn(order, List.of(1, 3, 4));

        int usedChassis20 = (int) spjList.stream()
            .filter(spj -> spj.getChassisSize() == 20)
            .count();

        int usedChassis40 = (int) spjList.stream()
            .filter(spj -> spj.getChassisSize() == 40)
            .count();

        Map<String, Integer> usedContainers = new HashMap<>();
        spjList.forEach(spj -> {
            String containerType = spj.getContainerType();
            usedContainers.put(containerType, usedContainers.getOrDefault(containerType, 0) + spj.getContainerQty());
        });

        Map<String, Integer> available = new HashMap<>();

        available.put("chassis20", (order.getQtyChassis20() != null ? order.getQtyChassis20() : 0) - usedChassis20);
        available.put("chassis40", (order.getQtyChassis40() != null ? order.getQtyChassis40() : 0) - usedChassis40);

        available.put("120mtfl", (order.getQty120mtfl() != null ? order.getQty120mtfl() : 0) - usedContainers.getOrDefault("120mtfl", 0));
        available.put("120mt", (order.getQty120mt() != null ? order.getQty120mt() : 0) - usedContainers.getOrDefault("120mt", 0));
        available.put("140mtfl", (order.getQty140mtfl() != null ? order.getQty140mtfl() : 0) - usedContainers.getOrDefault("140mtfl", 0));
        available.put("140mt", (order.getQty140mt() != null ? order.getQty140mt() : 0) - usedContainers.getOrDefault("140mt", 0));
        available.put("220mtfl", (order.getQty220mtfl() != null ? order.getQty220mtfl() : 0) - usedContainers.getOrDefault("220mtfl", 0));
        available.put("220mt", (order.getQty220mt() != null ? order.getQty220mt() : 0) - usedContainers.getOrDefault("220mt", 0));
        available.put("120mt120fl", (order.getQty120mt120fl() != null ? order.getQty120mt120fl() : 0) - usedContainers.getOrDefault("120mt120fl", 0));
        available.put("120mt220fl", (order.getQty120mt220fl() != null ? order.getQty120mt220fl() : 0) - usedContainers.getOrDefault("120mt220fl", 0));
        available.put("220mt120fl", (order.getQty220mt120fl() != null ? order.getQty220mt120fl() : 0) - usedContainers.getOrDefault("220mt120fl", 0));
        available.put("220mt220fl", (order.getQty220mt220fl() != null ? order.getQty220mt220fl() : 0) - usedContainers.getOrDefault("220mt220fl", 0));
        available.put("ch120fl", (order.getQtyCh120fl() != null ? order.getQtyCh120fl() : 0) - usedContainers.getOrDefault("ch120fl", 0));
        available.put("ch220fl", (order.getQtyCh220fl() != null ? order.getQtyCh220fl() : 0) - usedContainers.getOrDefault("ch220fl", 0));
        available.put("ch140fl", (order.getQtyCh140fl() != null ? order.getQtyCh140fl() : 0) - usedContainers.getOrDefault("ch140fl", 0));

        return available;
    }

    @Override
    public List<SpjResponseDTO> getAllSpj() {
        var allSpj = spjDb.findAll();
        var spjResponseDTO = new ArrayList<SpjResponseDTO>();
        allSpj.forEach( spj -> {
            spjResponseDTO.add(SpjToSpjResponseDTO(spj));
        });
        
        return spjResponseDTO;
    }

    @Override
    public List<SpjResponseDTO> getAllVehicleOut() {
        var allSpj = spjDb.findByStatusIn(List.of(0, 1, 2));
        var spjResponseDTO = new ArrayList<SpjResponseDTO>();
        allSpj.forEach(spj -> {
            spjResponseDTO.add(SpjToSpjResponseDTO(spj));
        });

        return spjResponseDTO;
    }

    @Override
    public List<SpjResponseDTO> getAllVehicleIn() {
        var allSpj = spjDb.findByStatusIn(List.of(3, 4));
        var spjResponseDTO = new ArrayList<SpjResponseDTO>();
        allSpj.forEach(spj -> {
            spjResponseDTO.add(SpjToSpjResponseDTO(spj));
        });

        return spjResponseDTO;
    }

    @Override
    public SpjResponseDTO getSpjById(String id) {
        var spj = spjDb.findById(id).orElse(null);

        if (spj == null) {
            return null;
        }
        return SpjToSpjResponseDTO(spj);
    }

    @Override
    public SpjResponseDTO addSpj(CreateSpjRequestDTO spjDTO) {
        String currenUser = jwtUtils.getCurrentUsername();

        LocalDate today = new Date().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();
        LocalDate dateOut = spjDTO.getDateOut().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();
        LocalDate dateIn = spjDTO.getDateIn().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();

        if (dateOut.isBefore(today)) {
            throw new ValidationException("Tanggal keluar tidak boleh kurang dari hari ini.");
        }
        if (dateIn.isBefore(today)) {
            throw new ValidationException("Tanggal masuk tidak boleh kurang dari hari ini.");
        }

        Order order = orderDb.findById(spjDTO.getOrderId()).orElse(null);
        Customer customer = order.getCustomer();
        Truck vehicle = truckDb.findById(spjDTO.getVehicleId()).orElse(null);
        Chassis chassis = chassisDb.findById(spjDTO.getChassisId()).orElse(null);
        SopirModel driver = sopirDb.findById(spjDTO.getDriverId()).orElse(null);

        String spjId = generateSpjId();

        var spj = new Spj();
        spj.setId(generateSpjId());
        spj.setOrder(order);
        spj.setCustomer(customer);
        spj.setVehicle(vehicle);
        spj.setChassisSize(spjDTO.getChassisSize());
        spj.setChassis(chassis);
        spj.setContainerType(spjDTO.getContainerType());
        spj.setContainerQty(spjDTO.getContainerQty());
        spj.setDriver(driver);
        spj.setDateOut(spjDTO.getDateOut());
        spj.setDateIn(spjDTO.getDateIn());
        spj.setCommission(customer.getCommission());
        spj.setOthersCommission(spjDTO.getOthersCommission());
        spj.setRemarksOperasional(spjDTO.getRemarksOperasional());
        spj.setRemarksSupervisor(spjDTO.getRemarksSupervisor());
        spj.setStatus(1);
        spj.setInsertedBy(currenUser);
        spj.setInsertedDate(new Date());

        if (!"I".equals(vehicle.getRowStatus())) {
            vehicle.setRowStatus("I");
            truckDb.save(vehicle);
        }
        
        if (!"I".equals(chassis.getRowStatus())) {
            chassis.setRowStatus("I");
            chassisDb.save(chassis);
        }
        
        if (!"I".equals(driver.getRowStatus())) {
            driver.setRowStatus("I");
            sopirDb.save(driver);
        }        
        
        spjDb.save(spj);
        
        notificationRestService.createSpjApprovalNotification(spjId, Arrays.asList(1L, 2L, 3L));;
        return SpjToSpjResponseDTO(spj);
    }

    @Override
    @Transactional
    public SpjResponseDTO approveSpj(ApproveSpjRequestDTO approveRequestDTO) {
        var spj = spjDb.findById(approveRequestDTO.getSpjId())
            .orElseThrow(() -> new ValidationException("SPJ tidak ditemukan!"));

        String currentUser = jwtUtils.getCurrentUsername();

        spj.setStatus(approveRequestDTO.getStatus());
        spj.setRemarksSupervisor(approveRequestDTO.getRemarksSupervisor());
        spj.setApprovedBy(currentUser);
        spj.setApprovedDate(new Date());

        if (approveRequestDTO.getStatus() == 2 || approveRequestDTO.getStatus() == 0) {
            var driver = spj.getDriver();
            var vehicle = spj.getVehicle();
            var chassis = spj.getChassis();

            driver.setRowStatus("A");
            chassis.setRowStatus("A");
            vehicle.setRowStatus("A");

            sopirDb.save(driver);
            chassisDb.save(chassis);
            truckDb.save(vehicle);
        }

        if (approveRequestDTO.getStatus() == 3) {
            Order order = spj.getOrder();
            if (!order.getSpjList().contains(spj)) {
                order.getSpjList().add(spj);
                orderDb.save(order);
            }
        }
    

        spjDb.save(spj);

        notificationRestService.createSpjStatusNotification(
            spj.getId(),
            approveRequestDTO.getStatus(),
            Arrays.asList(1L, 2L, 3L, 4L)
        );

        return SpjToSpjResponseDTO(spj);
    }

    @Transactional
    @Override
    public void markSpjAsDone(String spjId) {
        Spj spj = spjDb.findById(spjId)
            .orElseThrow(() -> new ValidationException("SPJ tidak ditemukan!"));

        if (spj.getStatus() != 3) {
            throw new ValidationException("SPJ hanya bisa di-mark as done jika statusnya Ongoing!");
        }

        String currentUser = jwtUtils.getCurrentUsername();

        var driver = spj.getDriver();
        var vehicle = spj.getVehicle();
        var chassis = spj.getChassis();

        driver.setRowStatus("A");
        chassis.setRowStatus("A");
        vehicle.setRowStatus("A");

        sopirDb.save(driver);
        chassisDb.save(chassis);
        truckDb.save(vehicle);

        spj.setStatus(4);
        spj.setActualDateIn(new Date());
        spj.setUpdatedBy(currentUser);
        spj.setUpdatedDate(new Date());

        spjDb.save(spj);
    }

    
    private SpjResponseDTO SpjToSpjResponseDTO(Spj spj) {
        var spjResponseDTO = new SpjResponseDTO();

        spjResponseDTO.setId(spj.getId());
        spjResponseDTO.setOrderId(spj.getOrder().getOrderId());
        spjResponseDTO.setCustomerId(spj.getCustomer().getId());
        spjResponseDTO.setVehicleId(spj.getVehicle().getVehicleId());
        spjResponseDTO.setChassisId(spj.getChassis().getChassisId());
        spjResponseDTO.setChassisSize(spj.getChassisSize());
        spjResponseDTO.setContainerType(spj.getContainerType());
        spjResponseDTO.setContainerQty(spj.getContainerQty());
        spjResponseDTO.setDriverId(spj.getDriver().getDriverId());
        spjResponseDTO.setDateOut(spj.getDateOut());
        spjResponseDTO.setDateIn(spj.getDateIn());
        spjResponseDTO.setActualDateIn(spj.getActualDateIn());
        spjResponseDTO.setCommission(spj.getCommission());
        spjResponseDTO.setOthersCommission(spj.getOthersCommission());
        spjResponseDTO.setRemarksOperasional(spj.getRemarksOperasional());
        spjResponseDTO.setRemarksSupervisor(spj.getRemarksSupervisor());
        spjResponseDTO.setStatus(spj.getStatus());
        spjResponseDTO.setInsertedBy(spj.getInsertedBy());
        spjResponseDTO.setInsertedDate(spj.getInsertedDate());
        spjResponseDTO.setUpdatedBy(spj.getUpdatedBy());
        spjResponseDTO.setUpdatedDate(spj.getUpdatedDate());
        spjResponseDTO.setApprovedBy(spj.getApprovedBy());
        spjResponseDTO.setApprovedDate(spj.getApprovedDate());

        return spjResponseDTO;
    }

    @Override
    public SpjResponseDTO updateSPJ(String spjId, CreateSpjRequestDTO spjDTO) {
        String currentUser = jwtUtils.getCurrentUsername();
       
        LocalDate today = new Date().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();
        LocalDate dateOut = spjDTO.getDateOut().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();
        LocalDate dateIn = spjDTO.getDateIn().toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();

        if (dateOut.isBefore(today)) {
            throw new ValidationException("Tanggal keluar tidak boleh kurang dari hari ini.");
        }
        if (dateIn.isBefore(today)) {
            throw new ValidationException("Tanggal masuk tidak boleh kurang dari hari ini.");
        }

        Order order = orderDb.findById(spjDTO.getOrderId()).orElse(null);
        Customer customer = order.getCustomer();
        Truck vehicle = truckDb.findById(spjDTO.getVehicleId()).orElse(null);
        Chassis chassis = chassisDb.findById(spjDTO.getChassisId()).orElse(null);
        SopirModel driver = sopirDb.findById(spjDTO.getDriverId()).orElse(null);

        
        // Sebelum di Update 
        Spj currentSpj = spjDb.findById(spjId).orElse(null);
        if (currentSpj == null) {
            throw new ValidationException("SPJ tidak ditemukan!");
        }
        
        String driverId = currentSpj.getDriver().getDriverId();
        SopirModel currentDriver = sopirDb.findById(driverId).orElse(null);
        
        String vehicleId = currentSpj.getVehicle().getVehicleId();
        String chassisId = currentSpj.getChassis().getChassisId();

        Truck currentVehicle = truckDb.findById(vehicleId).orElse(null);
        Chassis currentChassis = chassisDb.findById(chassisId).orElse(null);


        currentSpj.setCustomer(customer);

        if (currentVehicle.getRowStatus().equals("I") && !currentVehicle.getVehicleId().equals(spjDTO.getVehicleId())) {
            currentVehicle.setRowStatus("A");
            currentSpj.setVehicle(vehicle);
        }
        currentSpj.setVehicle(vehicle);
        currentSpj.setChassisSize(spjDTO.getChassisSize());

        if (currentChassis.getRowStatus().equals("I") && !currentChassis.getChassisId().equals(spjDTO.getChassisId())) {
            currentChassis.setRowStatus("A");
            currentSpj.setChassis(chassis);
        }
        currentSpj.setChassis(chassis);
        currentSpj.setContainerType(spjDTO.getContainerType());
        currentSpj.setContainerQty(spjDTO.getContainerQty());

        if(currentDriver.getRowStatus().equals("I") && !currentDriver.getDriverId().equals(spjDTO.getDriverId())) {
            currentDriver.setRowStatus("A");
            currentSpj.setDriver(driver);
        }
        currentSpj.setDriver(driver);
        currentSpj.setDateOut(spjDTO.getDateOut());
        currentSpj.setDateIn(spjDTO.getDateIn());
        currentSpj.setCommission(customer.getCommission());
        currentSpj.setOthersCommission(spjDTO.getOthersCommission());
        currentSpj.setRemarksOperasional(spjDTO.getRemarksOperasional());
        currentSpj.setStatus(1);
        currentSpj.setUpdatedBy(currentUser);
        currentSpj.setUpdatedDate(new Date());
        
        if (!"I".equals(vehicle.getRowStatus())) {
            vehicle.setRowStatus("I");
            truckDb.save(vehicle);
        }

        if (!"I".equals(chassis.getRowStatus())) {
            chassis.setRowStatus("I");
            chassisDb.save(chassis);
        }
        
        if (!"I".equals(driver.getRowStatus())) {
            driver.setRowStatus("I");
            sopirDb.save(driver);
        }        

        spjDb.save(currentSpj);

        notificationRestService.createSpjApprovalNotification(spjId, Arrays.asList(1L, 2L, 3L));;
        return SpjToSpjResponseDTO(currentSpj);
    }
}
