package com.kou.lottery.interfaces.assembler;

import com.kou.lottery.domain.strategy.model.vo.DrawAwardVO;
import com.kou.lottery.rpc.dto.AwardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * @author MiManchi
 * Date: 2022/6/17 15:40
 * Package: com.kou.lottery.interfaces.assembler
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AwardMapping extends IMapping<DrawAwardVO, AwardDTO> {

    @Mapping(target = "userId", source = "uId")
    @Override
    AwardDTO sourceToTarget(DrawAwardVO var1);

    @Override
    DrawAwardVO targetToSource(AwardDTO var1);
}
