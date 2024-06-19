package com.poscodx.jblog.controller;

import java.util.ArrayList;
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
	private BlogService blogService;
	private PostService postService;
	private CategoryService categoryService;
	private FileUploadService fileUploadService;

	public BlogController(BlogService blogService, PostService postService,
			CategoryService categoryService, FileUploadService fileUploadService) {
		this.blogService = blogService;
		this.postService = postService;
		this.categoryService = categoryService;
		this.fileUploadService = fileUploadService;
	}

	@RequestMapping({"", "/{pathNo1}", "/{pathNo1}/{pathNo2}"})
	public String index(
			@AuthUser UserVo authUser,
			@PathVariable("id") String id,
			@PathVariable("pathNo1") Optional<Long> pathNo1,
			@PathVariable("pathNo2") Optional<Long> pathNo2,
			Model model) {
		//System.out.println("categoryNo = " + categoryNo);
		//System.out.println("postNo = " + postNo);
		BlogVo blogVo = blogService.getBlog(id);
		List<CategoryVo> categoryList = categoryService.getCategoryList(id);
		List<PostVo> postVoList = new ArrayList<>();
		PostVo postVo = new PostVo();
		Long categoryNo = 0L;
		Long postNo = 0L;
		
		if(pathNo2.isPresent()) {
			categoryNo = pathNo1.get();
			postNo = pathNo2.get();
		} else if (pathNo1.isPresent()) {
			categoryNo = pathNo1.get();
		}
		
		if(categoryNo == 0) { 								// categoryNo == 0 일때 기본 categoryNo 세팅
			postVoList = postService.getContentsList(id);
			if(!postVoList.isEmpty()) {
				postVo = postVoList.get(0);
			}
		} else {
			postVoList = postService.getContentsList(id, categoryNo);
			if(postNo == 0) {	
				if(!postVoList.isEmpty()) { 				// postNo == 0 일때 기본 postNo 세팅
					postVo = postVoList.get(0);
				}
			} else {
				postVo = postService.getContents(postNo);
			}
		}

		model.addAttribute("categoryList", categoryList);
		model.addAttribute("postVo", postVo);
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("owner", authUser.getId().equals(id) ? true : false);
		model.addAttribute("list", postVoList);
		
		return "blog/main";
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
	
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(@AuthUser UserVo authUser, @PathVariable("id") String blogId, Model model) { 	//여기도 @Auth 필요한가?
		if(authUser.getId().equals(blogId)) {
			List<CategoryVo> categoryList = categoryService.getCategoryList(blogId);
			model.addAttribute("categoryList", categoryList);
			return "blog/admin-write";			
		} else {
			return "redirect:/" + blogId;
		}
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
	public String adminCategory(@AuthUser UserVo authUser, @PathVariable("id") String id, Model model) {
		if(authUser.getId().equals(id)) {
			List<CategoryVo> list = categoryService.getCategoryList(id);
			model.addAttribute("list", list);
			return "blog/admin-category";
		} else {
			return "redirect:/" + id;
		}
	}
	
	@RequestMapping("/admin/category/delete")
	public String adminCategoryDelete(@AuthUser UserVo authUser, @PathVariable("id") String id, String no, String postCount) {
		if(authUser.getId().equals(id)) {
			if(Integer.parseInt(postCount) > 0) {
				return "redirect:/" + id;
			}
			categoryService.deleteCategory(Long.parseLong(no));
			return "redirect:/\" + id + \"/admin/category";
		} else {
			return "redirect:/" + id;
		}
	}
	
	@RequestMapping("/admin/category/add")
	public String adminCategoryAdd(
			@AuthUser UserVo authUser, 
			@PathVariable("id") String blogId, 
			CategoryVo vo) {
		if(authUser.getId().equals(blogId)) {
			categoryService.addCategory(vo, blogId);
			return "redirect:/\" + blogId + \"/admin/category";
		} else {
			return "redirect:/" + blogId;
		}
	}
}
