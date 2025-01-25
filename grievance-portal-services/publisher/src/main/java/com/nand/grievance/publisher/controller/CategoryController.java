package com.nand.grievance.publisher.controller;

import com.nand.grievance.publisher.dto.CategoryDTO;
import com.nand.grievance.publisher.exception.ServiceException;
import com.nand.grievance.publisher.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory (
            @RequestBody CategoryDTO categoryDTO
    ) throws Exception {
        CategoryDTO persistedCategoryDTO = categoryService.createCategory(categoryDTO);
        if (persistedCategoryDTO == null) {
            throw new ServiceException("Unable to create the Category!!");
        } else {
            return new ResponseEntity<>(persistedCategoryDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() throws Exception {
        List<CategoryDTO> categoryDTOList = categoryService.getAllCategories();
        if (categoryDTOList == null) {
            throw new ServiceException("Unable to retrieve the Category list!!");
        } else {
            return new ResponseEntity<List<CategoryDTO>>(categoryDTOList, HttpStatus.OK);
        }
    }

    @GetMapping("/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<CategoryDTO> getCategoryById(
            @PathVariable(name = "categoryId") String categoryId
    ) throws Exception {
        CategoryDTO categoryDTO = categoryService.getCategoryById(categoryId);
        if (categoryDTO == null)
            throw new ServiceException("No Category not found for the id provided");
        else
            return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PutMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory (
            @RequestBody CategoryDTO categoryDTO
    ) throws Exception {
        CategoryDTO persistedCategoryDTO = categoryService.updateCategory(categoryDTO);
        if (persistedCategoryDTO == null) {
            throw new ServiceException("Unable to update the Category!!");
        } else {
            return new ResponseEntity<>(persistedCategoryDTO, HttpStatus.OK);
        }
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(
            @PathVariable(name = "categoryId") String categoryId
    ) throws Exception {
        Boolean isDeleted = categoryService.deleteCategoryById(categoryId);
        if (!isDeleted) {
            throw new ServiceException("Unable to delete the Category for the provided id = " + categoryId);
        } else {
            return new ResponseEntity<>("Successfully delete the Category for the provided id = " + categoryId, HttpStatus.OK);
        }
    }

}
