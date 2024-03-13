package com.csu.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csu.backend.entity.DTO.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrEmail(String text);
}
