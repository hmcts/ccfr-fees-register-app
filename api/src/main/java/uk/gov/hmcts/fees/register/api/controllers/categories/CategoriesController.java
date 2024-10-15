package uk.gov.hmcts.fees.register.api.controllers.categories;


import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.contract.CategoryUpdateDto;
import uk.gov.hmcts.fees.register.api.contract.ErrorDto;
import uk.gov.hmcts.fees.register.api.model.Category;
import uk.gov.hmcts.fees.register.api.model.CategoryRepository;
import uk.gov.hmcts.fees.register.api.model.exceptions.CategoryNotFoundException;
import uk.gov.hmcts.fees.register.api.model.exceptions.FeeNotFoundException;
import uk.gov.hmcts.fees.register.api.model.exceptions.RangeGroupNotFoundException;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@RestController
@Validated
public class CategoriesController {

    private final CategoryDtoMapper categoryDtoMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoriesController(CategoryDtoMapper categoryDtoMapper, CategoryRepository categoryRepository) {
        this.categoryDtoMapper = categoryDtoMapper;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream().map(categoryDtoMapper::toCategoryDto).collect(toList());
    }

    @GetMapping("/categories/{code}")
    public CategoryDto getCategory(@PathVariable("code") String code) {
        Category category = categoryRepository
            .findByCode(code)
            .orElseThrow(() -> new CategoryNotFoundException(code));

        return categoryDtoMapper.toCategoryDto(category);
    }

    @PutMapping("/categories/{code}")
    public CategoryDto createOrUpdateCategory(@Length(max = 50) @PathVariable("code") String code,
                                      @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        Category newCategoryModel = categoryDtoMapper.toCategory(code, categoryUpdateDto);
        Category existingCategory = categoryRepository.findByCode(code).orElse(new Category());

        copyProperties(newCategoryModel, existingCategory, "id");

        categoryRepository.save(existingCategory);

        return categoryDtoMapper.toCategoryDto(existingCategory);
    }

    @ExceptionHandler(FeeNotFoundException.class)
    public ResponseEntity feeNotFound() {
        return new ResponseEntity<>(new ErrorDto("feeCodes: contains unknown fee code"), BAD_REQUEST);
    }

    @ExceptionHandler(RangeGroupNotFoundException.class)
    public ResponseEntity rangeGroupNotFound() {
        return new ResponseEntity<>(new ErrorDto("rangeGroupCode: contains unknown range group code"), BAD_REQUEST);
    }

}
