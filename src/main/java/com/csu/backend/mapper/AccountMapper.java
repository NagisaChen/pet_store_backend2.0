package com.csu.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csu.backend.entity.DTO.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {
}
