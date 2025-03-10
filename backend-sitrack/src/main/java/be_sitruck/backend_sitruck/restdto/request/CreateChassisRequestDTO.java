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

    @NotBlank(message = "ID chassis tidak boleh kosong!")
    @Size(max = 8, message = "ID chassis maksimal 8 karakter!")
    private String chassisId;

    @NotBlank(message = "Ukuran chassis tidak boleh kosong!")
    @Size(max = 2, message = "Ukuran chassis maksimal 2 karakter!")
    @Pattern(regexp = "^(20|40)$", message = "Ukuran chassis harus 20 atau 40!")
    private String chassisSize;

    @NotNull(message = "Tahun chassis tidak boleh kosong!")
    @Pattern(regexp = "^[0-9]{4}$", message = "Tahun chassis harus 4 digit angka!")
    private String chassisYear;

    @NotBlank(message = "Nomor chassis tidak boleh kosong!")
    @Size(max = 6, message = "Nomor chassis maksimal 6 karakter!")
    private String chassisNumber;

    @NotNull(message = "Jumlah axle tidak boleh kosong!")
    @Min(value = 1, message = "Jumlah axle minimal 1!")
    @Max(value = 99, message = "Jumlah axle maksimal 99!")
    private String chassisAxle;

    @NotBlank(message = "Nomor KIR tidak boleh kosong!")
    @Size(max = 20, message = "Nomor KIR maksimal 20 karakter!")
    private String chassisKIRNo;

    @NotNull(message = "Tanggal KIR tidak boleh kosong!")
    private Date chassisKIRDate;

    @NotBlank(message = "Tipe chassis tidak boleh kosong!")
    @Size(max = 1, message = "Tipe chassis hanya 1 karakter!")
    @Pattern(regexp = "^[FT]$", message = "Tipe chassis harus F (Flatbed) atau T (Trailer)!")
    private String chassisType;

    private String insertedBy;
    private Date insertedDate;

    private String updatedBy;
    private Date updatedDate;

    @NotBlank(message = "Site ID tidak boleh kosong!")
    @Size(max = 3, message = "Site ID maksimal 3 karakter!")
    private String siteId;
}
