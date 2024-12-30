package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 日期集合转字符串
        String date = StringUtils.join(dateList,",");

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate d : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(d, LocalTime.MIN);//获取当天的开始时间：年月日时分秒
            LocalDateTime endTime = LocalDateTime.of(d, LocalTime.MAX);//获取当天的结束时间：年月日时分秒（无限接近于下一天的0时0分0秒）
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        String turnover = StringUtils.join(turnoverList,",");
        TurnoverReportVO trvo = TurnoverReportVO.builder().dateList(date).turnoverList(turnover).build();
        return trvo;
    }
}
