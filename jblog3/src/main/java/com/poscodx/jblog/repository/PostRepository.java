package com.poscodx.jblog.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.jblog.vo.PostVo;

@Repository
public class PostRepository {
	private SqlSession sqlSession;

	public PostRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public List<PostVo> findAll(String blogId) {
		return sqlSession.selectList("post.findAll", blogId);
	}

	public int insert(PostVo vo) {
		return sqlSession.insert("post.insert", vo);
	}

	public List<PostVo> findByBlogIdAndCategoryNo(String blogId, Long categoryNo) {
		return sqlSession.selectList("post.findByBlogIdAndCategoryNo", Map.of("blogId", blogId, "categoryNo", categoryNo));
	}

	public PostVo findByNo(Long no) {
		return sqlSession.selectOne("post.findByNo", no);
	}
}
