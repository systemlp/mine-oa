package com.mine.oa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mine.oa.dto.PositionDto;
import com.mine.oa.entity.PositionPO;
import com.mine.oa.service.PositionService;
import com.mine.oa.vo.CommonResultVo;

/***
 *
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 *
 * @author liupeng
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @PostMapping("/findPageByParam")
    public CommonResultVo findPageByParam(@RequestBody PositionDto param) {
        return positionService.findByParam(param);
    }

    @PostMapping("/merge")
    public CommonResultVo merge(@RequestBody PositionPO param, @RequestHeader String token) {
        return positionService.merge(param, token);
    }

    @GetMapping("/delete/{id}")
    public CommonResultVo delete(@PathVariable Integer id, @RequestHeader String token) {
        return positionService.delete(id, token);
    }

    @GetMapping("/enable/{id}")
    public CommonResultVo enable(@PathVariable Integer id, @RequestHeader String token) {
        return positionService.enable(id, token);
    }

    @PostMapping("/insert")
    public CommonResultVo insert(@RequestBody PositionDto param, @RequestHeader String token) {
        return positionService.insert(param, token);
    }

}
