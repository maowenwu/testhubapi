package com.huobi.quantification.api.email;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SimpleMailReqDto;

public interface EmailService {


    ServiceResult sendSimpleMail(SimpleMailReqDto reqDto);
}
