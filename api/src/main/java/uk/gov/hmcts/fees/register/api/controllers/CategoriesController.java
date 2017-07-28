package uk.gov.hmcts.fees.register.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.model.Category;
import uk.gov.hmcts.fees.register.api.model.CategoryRepository;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;

import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController()
@RequestMapping()
public class CategoriesController {


    private final CategoryDtoMapper categoryDtoMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoriesController(CategoryDtoMapper categoryDtoMapper, CategoryRepository categoryRepository) {
        this.categoryDtoMapper = categoryDtoMapper;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories1() {
        return categoryRepository.findAll().stream().map(categoryDtoMapper::toCategoryDto).collect(toList());
    }

    @GetMapping("/categories/{id}")
    public CategoryDto getCategory1(@PathVariable("id") Integer id) {
        Category category = categoryRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Category not found. Id: " + id));

        return categoryDtoMapper.toCategoryDto(category);
    }

}
