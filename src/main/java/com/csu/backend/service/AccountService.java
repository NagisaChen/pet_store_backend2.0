package com.csu.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csu.backend.entity.DTO.Account;
import com.csu.backend.entity.VO.request.EmailRegisterVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrEmail(String text);
    String registerEmailVerifyCode(String type, String email, String ip);
    String registerEmailAccount(EmailRegisterVO vo);
}
