package com.poscodx.jblog.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poscodx.jblog.repository.CategoryRepository;
import com.poscodx.jblog.vo.CategoryVo;

@Service
public class CategoryService {
	private CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	
	public List<CategoryVo> getCategoryList(String blogId) {
		return categoryRepository.findAll(blogId);
	}
}
