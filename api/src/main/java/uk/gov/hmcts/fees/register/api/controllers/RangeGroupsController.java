package uk.gov.hmcts.fees.register.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;
import uk.gov.hmcts.fees.register.api.model.RangeGroupRepository;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;

import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping("/fees-register")
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

    @GetMapping("/range-groups/{id}")
    public RangeGroupDto getRangeGroup(@PathVariable("id") Integer id) {
        RangeGroup rangeGroup = rangeGroupRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Range group not found. Id: " + id));

        return rangeGroupsDtoMapper.toRangeGroupDto(rangeGroup);
    }

}
