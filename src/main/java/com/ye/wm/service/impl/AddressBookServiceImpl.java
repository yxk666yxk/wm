package com.ye.wm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ye.wm.entity.AddressBook;
import com.ye.wm.mapper.AddressBookMapper;
import com.ye.wm.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
