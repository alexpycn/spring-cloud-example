package cn.alexpy.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TestDTO {

//    @NonNull
    private String remarks;

//    @NonNull
    private String currentTime;

}
