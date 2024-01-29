package org.example.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("electricity_record.record")
@Data
public class Record {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Integer me;

    private Integer other;

    private Integer chargeMoney;

    private Date dateTime;
}
