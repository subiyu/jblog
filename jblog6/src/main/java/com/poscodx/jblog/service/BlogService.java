package com.poscodx.jblog.service;

import org.springframework.stereotype.Service;

import com.poscodx.jblog.repository.BlogRepository;
import com.poscodx.jblog.vo.BlogVo;

@Service
public class BlogService {
	private BlogRepository blogRepository;

	public BlogService(BlogRepository blogRepository) {
		this.blogRepository = blogRepository;
	}

	public BlogVo getBlog(String userId) {
		return blogRepository.findById(userId);
	}

	public void updateSite(BlogVo vo) {
		blogRepository.update(vo);
	}
	
}
