package org.example.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.Record;

import java.util.Map;

public interface IRecordService extends IService<Record>{

    void calculate();
}
