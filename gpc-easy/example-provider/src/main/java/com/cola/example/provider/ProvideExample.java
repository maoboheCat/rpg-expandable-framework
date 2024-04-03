package com.cola.example.provider;

import com.cola.example.common.service.UserService;
import com.cola.rpc.bootstrap.ProviderBootstrap;
import com.cola.rpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Maobohe
 * @createData 2024/3/24 9:59
 */
public class ProvideExample {

    public static void main(String[] args) {
        List<ServiceRegisterInfo<?>> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo<?> serviceRegisterInfo = new ServiceRegisterInfo<>(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
