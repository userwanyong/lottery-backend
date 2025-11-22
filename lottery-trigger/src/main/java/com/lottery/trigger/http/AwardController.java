package com.lottery.trigger.http;

import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.AwardResponseVO;
import com.lottery.trigger.api.AwardService;
import com.lottery.trigger.api.dto.req.AwardRequestDTO;
import com.lottery.trigger.api.dto.res.AwardResponseDTO;
import com.lottery.types.annotation.DeleteOldCacheWithPrefixAsync;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 永
 */
@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/erp/award")
public class AwardController implements AwardService {
    @Resource
    private ErpRepository repository;
    @Override
    @GetMapping("/query_award")
    public BaseResponse<List<AwardResponseDTO>> queryAward() {
        log.info("======================[AwardController-queryAward]运营端 查询奖品开始 ======================");
        List<AwardResponseVO> awards = repository.queryAwardVOList();
        ArrayList<AwardResponseDTO> list = new ArrayList<>();
        for (AwardResponseVO awardResponseVO : awards) {
            AwardResponseDTO awardResponseDTO = new AwardResponseDTO();
            BeanUtils.copyProperties(awardResponseVO, awardResponseDTO);
            list.add(awardResponseDTO);
        }
        log.info("======================[AwardController-queryAward]运营端 查询奖品成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), list);
    }

    @Override
    @PostMapping("/add_award")
    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.STRATEGY_AWARD_LIST_KEY,Constants.RedisKey.STRATEGY_AWARD_KEY,Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY,Constants.RedisKey.AWARD_KEY})
    public BaseResponse<Boolean> addAward(@RequestBody AwardRequestDTO request) {
        log.info("======================[AwardController-addAward]运营端 添加奖品开始 ======================");
        AwardResponseVO awardResponseVO = new AwardResponseVO();
        BeanUtils.copyProperties(request, awardResponseVO);
        repository.addAwardVO(awardResponseVO);
        log.info("======================[AwardController-addAward]运营端 添加奖品成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/update_award")
    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.STRATEGY_AWARD_LIST_KEY,Constants.RedisKey.STRATEGY_AWARD_KEY,Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY,Constants.RedisKey.AWARD_KEY})
    public BaseResponse<Boolean> updateAward(@RequestBody AwardRequestDTO request) {
        log.info("======================[AwardController-updateAward]运营端 修改奖品开始 ======================");
        AwardResponseVO awardResponseVO = new AwardResponseVO();
        BeanUtils.copyProperties(request, awardResponseVO);
        repository.updateAwardVO(awardResponseVO);
        log.info("======================[AwardController-updateAward]运营端 添加奖品成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    @Override
    @PostMapping("/delete_award/{awardId}")
    @DeleteOldCacheWithPrefixAsync(key = {Constants.RedisKey.STRATEGY_AWARD_LIST_KEY,Constants.RedisKey.STRATEGY_AWARD_KEY,Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY,Constants.RedisKey.AWARD_KEY})
    public BaseResponse<Boolean> deleteAward(@PathVariable("awardId") Long awardId) {
        log.info("======================[AwardController-deleteAward]运营端 删除奖品开始 ======================");
        repository.deleteAwardVO(awardId);
        log.info("======================[AwardController-deleteAward]运营端 删除奖品成功 ======================");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}
