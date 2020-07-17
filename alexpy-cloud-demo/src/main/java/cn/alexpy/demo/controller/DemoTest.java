package cn.alexpy.demo.controller;

import cn.alexpy.common.aspect.response.ResponseFormat;
import cn.alexpy.common.dto.TestDTO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@ResponseBody
@RequestMapping(value = "/test")
public class DemoTest {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequestMapping(value = "/public/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String publicTest() {

        TestDTO result = TestDTO.of(null, sdf.format(new Date()));

        return ResponseFormat.success(result);
    }

    @RequestMapping(value = "/public/{param}", method = {RequestMethod.GET, RequestMethod.POST})
    public String test2(@PathVariable("param") String param) {

        TestDTO result = TestDTO.of("test public " + param, sdf.format(new Date()));

        return ResponseFormat.success(result);
    }

    @RequestMapping(value = "/public/list", method = {RequestMethod.GET, RequestMethod.POST})
    public String testList() {

        List<TestDTO> result = new ArrayList<>();

        TestDTO result1 = TestDTO.of("list item 1", sdf.format(new Date()));
        TestDTO result2 = TestDTO.of("list item 2", sdf.format(new Date()));
        TestDTO result3 = TestDTO.of("list item 3", sdf.format(new Date()));

        result.add(result1);
        result.add(result2);
        result.add(result3);

        PageInfo<TestDTO> page = new PageInfo<>();
        page.setList(result);
        page.setPageNum(2);
        page.setPageSize(10);
        page.setTotal(25);
        page.setPages(3);

        return ResponseFormat.success(page);
    }

}
