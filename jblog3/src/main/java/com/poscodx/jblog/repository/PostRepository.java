package com.poscodx.jblog.repository;

import java.util.List;

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
}
