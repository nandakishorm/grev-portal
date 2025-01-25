package com.nand.grievance.publisher.service;

import com.nand.grievance.publisher.dto.CategoryDTO;
import com.nand.grievance.publisher.entity.CategoryEntity;
import com.nand.grievance.publisher.entity.UserEntity;
import com.nand.grievance.publisher.repository.CategoryRepository;
import com.nand.grievance.publisher.repository.UserRepository;
import com.nand.grievance.publisher.util.AppUtil;
import com.nand.grievance.publisher.util.ModelMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CategoryService extends GenericService<CategoryEntity> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapperUtil modelMapperUtil;

    protected void setAuditInfo(CategoryEntity entity, UserEntity userEntity, Boolean isNew) {
        if (isNew) {
            entity.setUserCreated(userEntity.getUsername());
            entity.setDateCreated(new Date());
        }
        entity.setUserModified(userEntity.getUsername());
        entity.setDateModified(new Date());
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(AppUtil.generateRandomUUID());
        categoryDTO.setId(null);
        CategoryEntity categoryEntity = modelMapperUtil.convertToDto(categoryDTO, CategoryEntity.class);
        setAuditInfo(categoryEntity, getAutheticatedUserEntity(), true);

        categoryEntity = categoryRepository.save(categoryEntity);
        categoryDTO = modelMapperUtil.convertToDto(categoryEntity, CategoryDTO.class);

        log.info("New Category created, Category:" + categoryDTO.getCategoryName());
        return categoryDTO;
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        CategoryEntity originalCategoryEntity = categoryRepository.findByCategoryId(categoryDTO.getCategoryId());
        setAuditInfo(originalCategoryEntity, getAutheticatedUserEntity(), false);

        CategoryEntity categoryEntity = modelMapperUtil.convertToDto(categoryDTO, CategoryEntity.class);
        categoryEntity.setId(originalCategoryEntity.getId());

        log.info("Category updated, Category:" + categoryDTO.getCategoryName());
        return modelMapperUtil.convertToDto(categoryRepository.save(categoryEntity), CategoryDTO.class);
    }

    public CategoryDTO getCategoryById(String categoryId) {
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(categoryId);
        return modelMapperUtil.convertToDto(categoryEntity, CategoryDTO.class);
    }

    public List<CategoryDTO> getAllCategories() throws Exception {
        List<CategoryEntity> categoryEntityList = categoryRepository.findAll();
        return modelMapperUtil.convertToDto(categoryEntityList, CategoryDTO.class);
    }

    public Boolean deleteCategoryById(String categoryId) throws Exception {
        try {
            CategoryEntity categoryEntity = categoryRepository.findByCategoryId(categoryId);
            categoryRepository.delete(categoryEntity);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw e;
        }

        log.info("Category deleted, CategoryId:" + categoryId);
        return Boolean.TRUE;
    }

}
