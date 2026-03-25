package com.clanhistory;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.clanhistory.mapper")
public class ClanHistoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClanHistoryApplication.class, args);
    }
}
