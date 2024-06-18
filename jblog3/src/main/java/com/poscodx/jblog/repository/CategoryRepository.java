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

	public Long findByNameAndBlogId(String categoryName, String blogId) {
		return sqlSession.selectOne("category.findByNameAndBlogId", Map.of("categoryName", categoryName, "blogId", blogId));
	}

	public int deleteByNo(long no) {
		return sqlSession.delete("category.deleteByNo", no);
	}

	public CategoryVo findByNo(long no) {
		return sqlSession.selectOne("category.findByNo");
	}

	public int insert(CategoryVo vo) {
		return sqlSession.insert("category.insert", vo);
	}
}
