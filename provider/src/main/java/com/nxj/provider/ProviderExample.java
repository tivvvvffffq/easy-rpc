package com.nxj.provider;

import com.nxj.common.service.UserService;
import com.nxj.rpc.bootstrap.ProviderBootStrap;
import com.nxj.rpc.model.ServiceRegisterInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务提供者示例
 */
@Slf4j
public class ProviderExample {
    public static void main(String[] args) {
        // 需要注册的服务
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 初始化
        ProviderBootStrap.init(serviceRegisterInfoList);
    }
}
