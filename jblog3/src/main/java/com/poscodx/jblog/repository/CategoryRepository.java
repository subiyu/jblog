package com.poscodx.jblog.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.poscodx.jblog.vo.CategoryVo;

@Repository
public class CategoryRepository {
	private SqlSession sqlSession;

	public CategoryRepository(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public int insert(String blogId) {
		CategoryVo vo = new CategoryVo("미분류", "카테고리를 지정하지 않은 경우", blogId);
		return sqlSession.insert("category.insert", vo);
	}

	public List<CategoryVo> findAll(String blogId) {
		return sqlSession.selectList("category.findAll", blogId);
	}
}
