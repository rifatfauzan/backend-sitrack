package be_sitruck.backend_sitruck.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponseDTO<T> {

  private int status;
  private String message;

  @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
  private Date timestamp;

  private T data;
  private List<T> dataList;
}
