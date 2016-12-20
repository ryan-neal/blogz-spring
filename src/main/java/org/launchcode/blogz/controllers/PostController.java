package org.launchcode.blogz.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.launchcode.blogz.models.Post;
import org.launchcode.blogz.models.User;
import org.launchcode.blogz.models.dao.PostDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController extends AbstractController {

	@RequestMapping(value = "/blog/newpost", method = RequestMethod.GET)
	public String newPostForm() {
		return "newpost";
	}
	
	@RequestMapping(value = "/blog/newpost", method = RequestMethod.POST)
	public String newPost(HttpServletRequest request, Model model) {
		
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		User author = getUserFromSession(request.getSession());
		
		if (author == null){
			model.addAttribute("error", "Please login");
			return "redirect:login";
		}
		
		Post newPost = new Post(title, body, author);
		postDao.save(newPost);
		
		model.addAttribute("post", newPost);		
		return "redirect:" + author.getUid() + "/" + newPost.getUid();
	}
	
	@RequestMapping(value = "/blog/{username}/{uid}", method = RequestMethod.GET)
	public String singlePost(@PathVariable String username, @PathVariable int uid, Model model) {
		Post post = postDao.findByUid(uid);
		model.addAttribute(post);
		return "post";
	}
	
	@RequestMapping(value = "/blog/{username}", method = RequestMethod.GET)
	public String userPosts(@PathVariable String username, Model model) {
		User user = userDao.findByUsername(username);
		List<Post> posts = user.getPosts();
		model.addAttribute("posts", posts);
		return "blog";
	}
	
}
