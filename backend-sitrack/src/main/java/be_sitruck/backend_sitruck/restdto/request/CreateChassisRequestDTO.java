package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateChassisRequestDTO {

    // @Size(max = 8, message = "ID chassis maksimal 8 karakter!")
    private String chassisId;

    @Size(max = 2, message = "Ukuran chassis maksimal 2 karakter!")
    private String chassisSize;

    @Pattern(regexp = "^[0-9]{4}$", message = "Tahun chassis harus 4 digit angka!")
    private String chassisYear;

    private String chassisNumber;
    
    private String chassisAxle;

    @NotBlank(message = "Nomor KIR tidak boleh kosong!")
    @Size(max = 20, message = "Nomor KIR maksimal 20 karakter!")
    private String chassisKIRNo;

    @Future(message = "KIR Expiration Date harus lebih dari hari ini!")
    @NotNull(message = "Tanggal KIR tidak boleh kosong!")
    private Date chassisKIRDate;

    @NotBlank(message = "Tipe chassis tidak boleh kosong!")
    @Size(max = 1, message = "Tipe chassis hanya 1 karakter!")
    @Pattern(regexp = "^[FT]$", message = "Tipe chassis harus F (Flatbed) atau T (Trailer)!")
    private String chassisType;

    private String chassisRemarks;

    private String insertedBy;
    private Date insertedDate;
    private String updatedBy;
    private Date updatedDate;

    @NotBlank(message = "Divisi tidak boleh kosong!")
    @Size(max = 2, message = "Divisi maksimal 2 karakter!")
    private String division;

    private String dept = "TR";
    private String rowStatus = "A";

    @Size(max = 3, message = "Site ID maksimal 3 karakter!")
    private String siteId;
}
