package com.poscodx.jblog.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.poscodx.jblog.repository.PostRepository;
import com.poscodx.jblog.vo.PostVo;

@Service
public class PostService {
	private PostRepository postRepository;

	public PostService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	public List<PostVo> getContentsList(String blogId) {
		return postRepository.findAll(blogId);
	}

	public void addContents(PostVo vo) {
		postRepository.insert(vo);
	}

	public List<PostVo> getContentsList(String blogId, Long categoryNo) {
		return postRepository.findByBlogIdAndCategoryNo(blogId, categoryNo);
	}

	public PostVo getContents(Long no) {
		return postRepository.findByNo(no);
	}
}
