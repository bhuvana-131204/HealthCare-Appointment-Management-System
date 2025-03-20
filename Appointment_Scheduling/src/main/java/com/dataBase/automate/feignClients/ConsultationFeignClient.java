package com.dataBase.automate.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dataBase.automate.dto.ConsultationDto;
import com.dataBase.automate.dto.Response;
import javax.validation.Valid;
@FeignClient("CONSULTATION-SERVICE")
public interface ConsultationFeignClient {
	@PostMapping("/create")
	public ResponseEntity<Response<?>> addConsultation(@PathVariable  String appointmentId);              
}
