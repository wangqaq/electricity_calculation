package org.example.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.consoletable.ConsoleTable;
import org.example.consoletable.enums.Align;
import org.example.consoletable.table.Cell;
import org.example.entity.Record;
import org.example.mapper.RecordMapper;
import org.example.service.IRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {

    int errorCount = 0;

    @Override
    @PostConstruct
    public void calculate() {
        isNeedAddRecord();
        List<Record> records = list(Wrappers.<Record>query().lambda().orderByDesc(Record::getDateTime).last("limit 2"));
        Integer myUsed = 0;
        Integer otherUsed = 0;
        int money = 0;
        Date startTime = new Date();
        Date endTime = new Date();
        System.out.println("------------------------------------------------");
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            if (i == 0) {

                money = record.getChargeMoney();
                myUsed = record.getMe();
                otherUsed = record.getOther();
                endTime = record.getDateTime();
                System.out.println("|" + DateUtil.format(endTime, DatePattern.CHINESE_DATE_PATTERN) + ",楼下用电量：" + record.getOther() + ",楼上用电量：" + record.getMe());
                continue;
            }
            myUsed = myUsed - record.getMe();
            otherUsed = otherUsed - record.getOther();
            startTime = record.getDateTime();
            System.out.println("|" + DateUtil.format(startTime, DatePattern.CHINESE_DATE_PATTERN) + ",楼下用电量：" + record.getOther() + ",楼上用电量：" + record.getMe());
        }
        int total = myUsed + otherUsed;
        consoleOut(startTime, endTime, total, myUsed, otherUsed, money);

    }

    private void consoleOut(Date startTime, Date endTime, Integer total, Integer myUsed, Integer otherUsed, Integer money) {
        // 计算
        BigDecimal me_money = BigDecimal.valueOf(myUsed).divide(BigDecimal.valueOf(total), 5, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(money)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal other_money = BigDecimal.valueOf(otherUsed).divide(BigDecimal.valueOf(total), 5, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(money)).setScale(2, RoundingMode.HALF_UP);
        String startTimeStr = DateUtil.format(startTime, DatePattern.CHINESE_DATE_PATTERN);
        String endTimeStr = DateUtil.format(endTime, DatePattern.CHINESE_DATE_PATTERN);
        Long betweenDay = DateUtil.betweenDay(startTime, endTime, false);

        List<Cell> header = new ArrayList<Cell>() {{
            add(new Cell(Align.CENTER, "名称"));
            add(new Cell(Align.CENTER, "数值"));
            add(new Cell(Align.CENTER, "单位"));
        }};
        List<List<Cell>> body = new ArrayList<List<Cell>>() {{
            add(new ArrayList<Cell>() {{
                add(new Cell(Align.CENTER, "日期"));
                add(new Cell(Align.CENTER, startTimeStr + " 到 " + endTimeStr));
                add(new Cell(Align.CENTER, "/"));
            }});
            add(new ArrayList<Cell>() {{
                add(new Cell(Align.CENTER, "天数"));
                add(new Cell(Align.CENTER, DateUtil.betweenDay(startTime, endTime, false)));
                add(new Cell(Align.CENTER, "天"));
            }});
            add(new ArrayList<Cell>() {{
                add(new Cell(Align.CENTER, "总计电量"));
                add(new Cell(Align.CENTER, total + "(日平均" + BigDecimal.valueOf(total).divide(BigDecimal.valueOf(betweenDay), 2, RoundingMode.HALF_UP) + ")"));
                add(new Cell(Align.CENTER, "度"));
            }});
            add(new ArrayList<Cell>() {{
                add(new Cell(Align.CENTER, "楼上用电量"));
                add(new Cell(Align.CENTER, myUsed + "(日平均" + BigDecimal.valueOf(myUsed).divide(BigDecimal.valueOf(betweenDay), 2, RoundingMode.HALF_UP) + ")"));
                add(new Cell(Align.CENTER, "度"));
            }});
            add(new ArrayList<Cell>() {{
                add(new Cell(Align.CENTER, "楼下用电量"));
                add(new Cell(Align.CENTER, otherUsed + "(日平均" + BigDecimal.valueOf(otherUsed).divide(BigDecimal.valueOf(betweenDay), 2, RoundingMode.HALF_UP) + ")"));
                add(new Cell(Align.CENTER, "度"));
            }});
            add(new ArrayList<Cell>() {{
                add(new Cell(Align.CENTER, "楼上费用"));
                add(new Cell(Align.CENTER, me_money + "(日平均" + me_money.divide(BigDecimal.valueOf(betweenDay), 2, RoundingMode.HALF_UP) + ")"));
                add(new Cell(Align.CENTER, "元"));
            }});
            add(new ArrayList<Cell>() {{
                add(new Cell(Align.CENTER, "楼下费用"));
                add(new Cell(Align.CENTER, other_money + "(日平均" + other_money.divide(BigDecimal.valueOf(betweenDay), 2, RoundingMode.HALF_UP) + ")"));
                add(new Cell(Align.CENTER, "元"));
            }});
        }};
        new ConsoleTable.ConsoleTableBuilder().

                addHeaders(header).

                addRows(body).

                build().

                print();
    }

    private void isNeedAddRecord() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("是否需要添加用电记录(y/n)");
        String input = scanner.next();
        checkInput(input);
    }

    private void checkInput(String input) {
        switch (input) {
            case "y":
                inputRecord();
                break;
            case "n":
                break;
            default:
                System.out.println("输入有误,请重新输入");
                errorCount++;
                if (errorCount == 3) {
                    System.out.println("已输错三次，自动退出");
                    System.exit(0);
                    return;
                }
                isNeedAddRecord();
        }
        if (input.equals("y")) {
            inputRecord();
        }
    }

    private void inputRecord() {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入楼上用电量(整数)");
            int me_used = sc.nextInt();
            System.out.println("请输入楼下用电量(整数)");
            int down_used = sc.nextInt();
            System.out.println("请输入金额(整数)");
            int money = sc.nextInt();
            Record record = new Record();
            record.setMe(me_used);
            record.setOther(down_used);
            record.setChargeMoney(money);
            record.setDateTime(new Date());
            sc.close();
//        boolean isInsert = save(record);
//        if (isInsert) {
//            System.out.println("已插入一条新的记录");
//        } else {
//            System.out.println("插入失败");
//        }
        } catch (Exception e) {
            System.out.println("输入有误 请重新输入");
            inputRecord();
        }

    }
}
