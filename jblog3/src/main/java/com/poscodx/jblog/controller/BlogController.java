package com.poscodx.jblog.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poscodx.jblog.security.AuthUser;
import com.poscodx.jblog.service.BlogService;
import com.poscodx.jblog.service.CategoryService;
import com.poscodx.jblog.service.PostService;
import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;
import com.poscodx.jblog.vo.PostVo;
import com.poscodx.jblog.vo.UserVo;

@Controller
@RequestMapping("/{id:(?!assets).*}")
public class BlogController {
	private BlogService blogService;
	private PostService postService;
	private CategoryService categoryService;
	
	public BlogController(BlogService blogService, PostService postService, CategoryService categoryService) {
		this.blogService = blogService;
		this.postService = postService;
		this.categoryService = categoryService;
	}

	@RequestMapping({"", "/{categoryNo}", "/{categoryNo}/{postNo}"})
	public String index(
			@AuthUser UserVo authUser,
			@PathVariable("id") String id,
			@PathVariable("categoryNo") Optional<Long> categoryNo,
			@PathVariable("postNo") Optional<Long> postNo,
			Model model) {
		
		BlogVo blogVo = blogService.getBlog(id);
		List<PostVo> postVoList = postService.getContentsList(id);
		model.addAttribute("postVo", postVoList.get(0));
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("owner", authUser.getId().equals(id) ? true : false);
		postVoList.remove(0);
		model.addAttribute("list", postVoList);
		
		return "blog/main";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.GET)
	public String write(@PathVariable("id") String blogId, Model model) { 	//여기도 @Auth 필요한가?
		List<CategoryVo> categoryList = categoryService.getCategoryList(blogId);
		model.addAttribute("categoryList", categoryList);
		
		return "blog/admin-write";
	}
	
	@RequestMapping(value="/write", method=RequestMethod.POST)
	public String write(PostVo vo, String category, Model model) {
		postService.addContents(vo);
		model.addAttribute("postVo", vo);
		
		return "redirect:/" + vo.getCategoryNo() + "/" + vo.getNo();
	}
	
	@RequestMapping("/admin/basic")
	public String adminBasic(@AuthUser UserVo authUser, @PathVariable("id") String id) {		
		if(authUser.getId().equals(id)) {
			return "blog/admin-basic";			
		} else {
			return "redirect:/" + id;
		}
	}
	
	@RequestMapping("/admin/category")
	public String adminCategory(@PathVariable("id") String id) {
		return "blog/admin-category";
	}
	
	@RequestMapping("/admin/write")
	public String adminWrite(@PathVariable("id") String id) {
		return "blog/admin-write";
	}
}
