package com.poscodx.jblog.service;

import java.util.List;

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
}
