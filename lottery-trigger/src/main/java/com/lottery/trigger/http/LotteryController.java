package com.lottery.trigger.http;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.service.Lottery;
import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.trigger.api.LotteryService;
import com.lottery.trigger.api.dto.req.LotteryAwardListRequestDTO;
import com.lottery.trigger.api.dto.req.LotteryRequestDTO;
import com.lottery.trigger.api.dto.res.LotteryAwardListResponseDTO;
import com.lottery.trigger.api.dto.res.LotteryResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
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
@CrossOrigin("*")
@RequestMapping("/lottery")
public class LotteryController implements LotteryService {

    @Resource
    private StrategyArmory strategyArmory;
    @Resource
    private Lottery lottery;

    @Override
    @GetMapping("/strategy_armory")
    public BaseResponse<Boolean> strategyArmory(@RequestParam Long strategyId) {
        try {
            boolean result = strategyArmory.assembleLotteryStrategy(strategyId);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), result);
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/query_lottery_award_list")
    public BaseResponse<List<LotteryAwardListResponseDTO>> queryLotteryAwardList(@RequestBody LotteryAwardListRequestDTO requestDTO) {
        try {
            List<StrategyAwardEntity> strategyAwardEntities = lottery.queryLotteryAwardList(requestDTO.getStrategyId());
            List<LotteryAwardListResponseDTO> lotteryAwardListResponseDTOList = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                LotteryAwardListResponseDTO lotteryAwardListResponseDTO = new LotteryAwardListResponseDTO();
                BeanUtils.copyProperties(strategyAwardEntity, lotteryAwardListResponseDTO);
                lotteryAwardListResponseDTOList.add(lotteryAwardListResponseDTO);
            }
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), lotteryAwardListResponseDTOList);
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/random_lottery")
    public BaseResponse<LotteryResponseDTO> randomLottery(@RequestBody LotteryRequestDTO requestDTO) {
        try {
            LotteryResEntity lotteryResEntity = lottery.performLottery(LotteryReqEntity.builder().strategyId(requestDTO.getStrategyId()).build());
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), LotteryResponseDTO.builder()
                    .awardId(lotteryResEntity.getAwardId())
                    .awardIndex(lotteryResEntity.getSort())
                    .build());
        } catch (AppException e) {
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }
}
