package com.project.eat.shop;

import com.project.eat.address.AddService;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetUserAddrWithUserId {

    @Autowired
    private AddService addService;

    public String AddrGu(@NotNull String userId) {
        log.info("GetUserAddrWithUserId 에서 memberId 확인 userId: {}",userId);


        // userId 로 테이블:address 에서 주소조회 =>address 뽑아
        String strAddr ="";
        String addrGu = "";

        if(userId != null && !(userId.isEmpty())){
            strAddr = addService.selectAddressById(userId);

            String[] arr = strAddr.split(" ");

            for(String x : arr){
                if(x.charAt(x.length()-1)=='구'){
                    addrGu = x;
                }
            }
            log.info("GetUserAddrWithUserId 에서 집주소 확인 arrGu: {}",addrGu);
        }

        return addrGu;
    }

}
