package com.poscodx.jblog.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.jblog.vo.BlogVo;

@Repository
public class BlogRepository {
	private SqlSession sqlSession;
	
	public BlogRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int insert(String userId) {
		BlogVo vo = new BlogVo(userId, "기본 블로그 타이틀 입니다.", "/assets/images/spring-logo.jpg");
		return sqlSession.insert("blog.insert", vo);
	}

	public int insert(BlogVo vo) {
		return sqlSession.insert("blog.insert", vo);
	}
	
	public int update(BlogVo vo) {
		return sqlSession.update("blog.update", vo);
	}

	public BlogVo findById(String userId) {
		return sqlSession.selectOne("blog.findById", userId);
	}
}
