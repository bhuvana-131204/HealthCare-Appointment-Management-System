package com.dataBase.automate.feignClients;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("USER-SERVICE")
public interface UserFeignClient {

}
