package com.poscodx.jblog.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.poscodx.jblog.security.AuthUser;
import com.poscodx.jblog.service.BlogService;
import com.poscodx.jblog.service.CategoryService;
import com.poscodx.jblog.service.FileUploadService;
import com.poscodx.jblog.service.PostService;
import com.poscodx.jblog.vo.BlogVo;
import com.poscodx.jblog.vo.CategoryVo;
import com.poscodx.jblog.vo.PostVo;
import com.poscodx.jblog.vo.UserVo;

@Controller
@RequestMapping("/{id:(?!assets).*}")
public class BlogController {
	private ServletContext servletContext;
	private BlogService blogService;
	private PostService postService;
	private CategoryService categoryService;
	private FileUploadService fileUploadService;

	public BlogController(ServletContext servletContext, BlogService blogService, PostService postService,
			CategoryService categoryService, FileUploadService fileUploadService) {
		this.servletContext = servletContext;
		this.blogService = blogService;
		this.postService = postService;
		this.categoryService = categoryService;
		this.fileUploadService = fileUploadService;
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
	
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(@PathVariable("id") String blogId, Model model) { 	//여기도 @Auth 필요한가?
		List<CategoryVo> categoryList = categoryService.getCategoryList(blogId);
		model.addAttribute("categoryList", categoryList);

		return "blog/admin-write";
	}
	
	@RequestMapping(value="/admin/write", method=RequestMethod.POST)
	public String write(
			@PathVariable("id") String blogId, 
			@RequestParam(value="category") String categoryName,
			PostVo vo, 
			Model model) {
		Long categoryNo = categoryService.getCategoryNo(categoryName, blogId);
		vo.setCategoryName(categoryName);
		vo.setCategoryNo(categoryNo);
		postService.addContents(vo);
		model.addAttribute("postVo", vo);
		
		return "redirect:/" + blogId;
	}
	
	@RequestMapping("/admin/basic")
	public String adminBasic(@AuthUser UserVo authUser, @PathVariable("id") String id, Model model) {		
		if(authUser.getId().equals(id)) {
			BlogVo blogVo = blogService.getBlog(id);
			model.addAttribute("blogVo", blogVo);
			return "blog/admin-basic";			
		} else {
			return "redirect:/" + id;
		}
	}
	
	@RequestMapping(value = "/admin/update", method=RequestMethod.POST)
	public String adminUpdate(@PathVariable("id") String id, BlogVo vo, MultipartFile file) {
		String profile = fileUploadService.restore(file);
		if(profile != null) {
			vo.setLogo(profile);
		}
		
		blogService.updateSite(vo);
		
		return "redirect:/" + id;
	}
	
	@RequestMapping("/admin/category")
	public String adminCategory(@PathVariable("id") String id, Model model) {
		List<CategoryVo> list = categoryService.getCategoryList(id);
		model.addAttribute("list", list);
		return "blog/admin-category";
	}
	
	@RequestMapping("/admin/category/delete")
	public String adminCategoryDelete(@PathVariable("id") String id, String no, String postCount) {
		if(Integer.parseInt(postCount) > 0) {
			return "redirect:/" + id;
		}
		
		categoryService.deleteCategory(Long.parseLong(no));
		
		return "redirect:/" + id + "/admin/category";
	}
	
	@RequestMapping("/admin/category/add")
	public String adminCategoryAdd(
			@PathVariable("id") String blogId, CategoryVo vo) {
		categoryService.addCategory(vo, blogId);
		return "redirect:/" + blogId + "/admin/category";
	}
}
