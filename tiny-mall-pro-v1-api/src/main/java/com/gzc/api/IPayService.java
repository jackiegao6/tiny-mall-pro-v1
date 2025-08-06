package com.gzc.api;

import com.gzc.api.dto.req.CreatePayRequestDTO;
import com.gzc.api.response.Response;

public interface IPayService {

    Response<String> createPayOrder(CreatePayRequestDTO createPayRequestDTO);


}
