package com.dataBase.automate.dto;

import com.dataBase.automate.model.Specialization;
import com.dataBase.automate.model.TimeSlots;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailabilityDto {
    private String availabilityId;
    private TimeSlots slots;
    private Date date;
    private String doctorId;
    private Specialization specialization;
}
