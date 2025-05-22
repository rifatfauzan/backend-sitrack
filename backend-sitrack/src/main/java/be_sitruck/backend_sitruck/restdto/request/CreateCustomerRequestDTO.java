package be_sitruck.backend_sitruck.restdto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCustomerRequestDTO {
    
    @NotBlank(message = "Site ID tidak boleh kosong.")
    @Size(max = 10, message = "Site ID maksimal berisi 10 karakter.")
    private String siteId;

    @NotBlank(message = "Nama tidak boleh kosong.")
    @Size(max = 100, message = "Nama maksimal berisi 50 karakter.")
    private String name;

    @Size(max = 100, message = "Alamat maksimal berisi 100 karakter.")
    private String address;

    @Size(max = 20, message = "Nomor kontrak maksimal berisi 50 karakter.")
    private String contractNo;

    @Size(max = 100, message = "Kota asal maksimal berisi 50 karakter.")
    private String cityOrigin;

    @NotBlank(message = "Kota tujuan tidak boleh kosong.")
    @Size(max = 100, message = "Kota tujuan maksimal berisi 50 karakter.")
    private String cityDestination;

    @Size(max = 50, message = "Komoditas maksimal berisi 50 karakter.")
    private String commodity;
}
