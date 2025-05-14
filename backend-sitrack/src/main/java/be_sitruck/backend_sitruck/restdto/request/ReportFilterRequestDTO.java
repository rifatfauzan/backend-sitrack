package be_sitruck.backend_sitruck.restdto.request;

import java.util.Date;
import lombok.Data;

@Data
public class ReportFilterRequestDTO {
    private String reportType;
    private Date fromDate;
    private Date endDate;
} 