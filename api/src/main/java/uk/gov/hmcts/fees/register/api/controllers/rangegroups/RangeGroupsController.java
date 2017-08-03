package uk.gov.hmcts.fees.register.api.controllers.rangegroups;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;
import uk.gov.hmcts.fees.register.api.model.RangeGroupRepository;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;

import static java.util.stream.Collectors.toList;

@RestController
public class RangeGroupsController {

    private final RangeGroupsDtoMapper rangeGroupsDtoMapper;
    private final RangeGroupRepository rangeGroupRepository;

    @Autowired
    public RangeGroupsController(RangeGroupsDtoMapper rangeGroupsDtoMapper, RangeGroupRepository rangeGroupRepository) {
        this.rangeGroupsDtoMapper = rangeGroupsDtoMapper;
        this.rangeGroupRepository = rangeGroupRepository;
    }

    @GetMapping("/range-groups")
    public List<RangeGroupDto> getRangeGroups() {
        return rangeGroupRepository.findAll().stream().map(rangeGroupsDtoMapper::toRangeGroupDto).collect(toList());
    }

    @GetMapping("/range-groups/{code}")
    public RangeGroupDto getRangeGroup(@PathVariable("code") String code) {
        RangeGroup rangeGroup = rangeGroupRepository
            .findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Range group not found. Code: " + code));

        return rangeGroupsDtoMapper.toRangeGroupDto(rangeGroup);
    }

}
