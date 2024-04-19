package com.project.eat.shop;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetUserAddrWithUserId {

    public String AddrGu(@NotNull String userId) {
        // userId 로 테이블:address 에서 주소조회 =>address 뽑아
        // String strAddr = 결과랎~
        /*
        String[] arr = strAddr.split(" ");
        String addrGu = "";
        for(String x : arr){
            if(x.charAt(x.length()-1)=='구'){
                addrGu = x;
            }
        }
        log.info("arrGu: {}",addrGu);
         */

        //더미
        String addrGu = "";
        if((userId).equals("tester1")){
            addrGu="서초구";
        }else {
            addrGu="강남구";
        }

        return addrGu;
    }

}
