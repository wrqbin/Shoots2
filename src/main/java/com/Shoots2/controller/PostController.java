package com.Shoots2.controller;

import com.Shoots2.domain.PaginationResult;
import com.Shoots2.domain.Post;
import com.Shoots2.service.PostCommentService;
import com.Shoots2.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/post")
public class PostController {

    @Value("${my.savefolder}")
    private String SAVE_FOLDER;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private PostService postService;
    private PostCommentService postCommentService;

    public PostController(PostService postService, PostCommentService postCommentService) {
        this.postService = postService;
        this.postCommentService = postCommentService;
    }

    // 메인 페이지로 사용할 게시판 목록 - 루트 경로 추가
    @GetMapping(value = {"", "/", "/list"})
    public ModelAndView postlist(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(defaultValue = "A") String category,
            @RequestParam(defaultValue = "available") String status,
            @RequestParam(defaultValue = "") String search_word,
            ModelAndView mv,
            HttpSession session) {

        session.setAttribute("referer", "list");

        int limit = 10;
        int listcount = postService.getListCount(category, search_word);
        List<Post> list = postService.getPostList(page, limit, category, search_word, search_word);

        PaginationResult result = new PaginationResult(page, limit, listcount);

        mv.setViewName("post/post_list");
        mv.addObject("page", page);
        mv.addObject("maxpage", result.getMaxpage());
        mv.addObject("startpage", result.getStartpage());
        mv.addObject("endpage", result.getEndpage());
        mv.addObject("listcount", listcount);
        mv.addObject("postlist", list);
        mv.addObject("limit", limit);
        mv.addObject("pagination", result);
        mv.addObject("category", category);
        mv.addObject("status", status);
        mv.addObject("search_word", search_word);
        return mv;
    }

    // AJAX 요청을 처리하여 게시글 목록 반환
    @GetMapping(value = "/list_ajax")
    @ResponseBody
    public Map<String, Object> postListAjax(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "A") String category,
            @RequestParam(defaultValue = "available") String status,
            @RequestParam(defaultValue = "") String search_word
    ) {

        int listcount = postService.getListCount(category, search_word);
        List<Post> list = postService.getPostList(page, limit, category, status, search_word);

        PaginationResult result = new PaginationResult(page, limit, listcount);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("maxpage", result.getMaxpage());
        map.put("startpage", result.getStartpage());
        map.put("endpage", result.getEndpage());
        map.put("listcount", listcount);
        map.put("postlist", list);
        map.put("limit", limit);
        map.put("pagination", result);
        map.put("category", category);
        map.put("status", status);
        map.put("search_word", search_word);
        return map;
    }

    //글쓰기
    @GetMapping(value = "/write")
    public String postWrite() {
        return "post/post_write";
    }

    @PostMapping("/add")
    public String add(Post post, HttpServletRequest request)
            throws Exception {

        MultipartFile uploadfile = post.getUploadfile();

        if (uploadfile != null && !uploadfile.isEmpty()) {
            String fileDBName = postService.saveUploadFile(uploadfile, SAVE_FOLDER);
            logger.info("파일 저장됨: " + fileDBName); // 로그 추가
            logger.info("실제 저장 경로: " + SAVE_FOLDER + fileDBName); // 로그 추가
            post.setPost_file(fileDBName);
            post.setPost_original(uploadfile.getOriginalFilename());
        }

        if (post.getCategory() == null) {
            post.setCategory("A");
        }

        if (post.getPrice() == null) {
            post.setPrice(0);
        }

        if ("B".equals(post.getCategory())) {
            // 중고게시판의 경우 가격 유지
        } else {
            post.setPrice(0);
        }

        if ("completed".equals(post.getStatus())) {
            post.setStatus(post.getStatus());
        } else {
            post.setStatus("available");
        }

        postService.insertPost(post);

        logger.info("Post added: " + post.toString());
        return "redirect:/post";  // 수정된 경로
    }

    @GetMapping("/detail")
    public ModelAndView Detail(
            int num, ModelAndView mv,
            HttpServletRequest request,
            @RequestHeader(value = "referer", required = false) String beforeURL, HttpSession session) {

        String sessionReferer = (String) session.getAttribute("referer");
        logger.info("referer:" + beforeURL);

        if (sessionReferer != null && sessionReferer.equals("list")) {
            if (beforeURL != null) {
                postService.setReadCountUpdate(num);
            }
            session.removeAttribute("referer");
        }

        Post post = postService.getDetail(num);

        if (post == null) {
            logger.info("상세보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "상세보기 실패입니다.");
        } else {
            logger.info("상세보기 성공");
            mv.setViewName("post/post_view");
            mv.addObject("postdata", post);
        }
        return mv;
    }

    @GetMapping("/modifyView")
    public ModelAndView PostModifyView(
            int num,
            ModelAndView mv,
            HttpServletRequest request
    ) {
        Post postdata = postService.getDetail(num);

        if (postdata == null) {
            logger.info("수정보기 실패");
            mv.setViewName("error/error");
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "수정보기 실패입니다.");
        } else {
            logger.info("(수정)상세보기 성공");
            mv.addObject("postdata", postdata);
            mv.setViewName("post/post_modify");
        }
        return mv;
    }

    @PostMapping("/modifyAction")
    public String PostModifyAction(
            Post postdata,
            @RequestParam(value = "check", required = false) String check,
            @RequestParam(value = "existingFilePath", required = false) String existingFilePath,
            @RequestParam(value = "existingFileName", required = false) String existingFileName,
            @RequestParam(value = "remove_file", required = false) String removeFile,
            Model mv,
            HttpServletRequest request,
            RedirectAttributes rattr
    ) throws Exception {
        boolean usercheck = postService.isPostWriter(postdata.getPost_idx());
        String url="";
        MultipartFile uploadfile = postdata.getUploadfile();
        String saveFolder = SAVE_FOLDER;

        if ("true".equals(removeFile) && existingFilePath != null && !existingFilePath.isEmpty()) {
            File fileToDelete = new File(saveFolder, existingFilePath);
            if (fileToDelete.exists()) {
                if (fileToDelete.delete()) {
                    logger.info("기존 파일 삭제 완료: " + existingFilePath);
                } else {
                    logger.warn("기존 파일 삭제 실패: " + existingFilePath);
                }
            }
            postdata.setPost_file("");
            postdata.setPost_original("");
        } else if (check != null && !check.equals("")) {
            logger.info("기존파일 그대로 사용합니다.");
            postdata.setPost_file(existingFilePath);
            postdata.setPost_original(existingFileName);
        } else {
            if (uploadfile != null && !uploadfile.isEmpty()) {
                logger.info("파일 변경되었습니다.");
                String fileDBName = postService.saveUploadFile(uploadfile, saveFolder);
                postdata.setPost_file(fileDBName);
                postdata.setPost_original(uploadfile.getOriginalFilename());
            } else if (existingFilePath != null && !existingFilePath.isEmpty()) {
                logger.info("파일 변경 없음, 기존 파일 유지.");
                postdata.setPost_file(existingFilePath);
                postdata.setPost_original(existingFileName);
            } else {
                logger.info("첨부된 파일 없음");
                postdata.setPost_file("");
                postdata.setPost_original("");
            }
        }

        int result = postService.postModify(postdata);

        if (result == 0) {
            logger.info("게시판 수정 실패");
            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "게시판 수정 실패");
            url = "error/error";
        } else {
            logger.info("게시판 수정 완료");
            url = "redirect:detail";
            rattr.addAttribute("num", postdata.getPost_idx());
        }
        return url;
    }

    @PostMapping("/delete")
    public String PostDelete(
            @RequestParam("num") int num,
            Model mv,
            RedirectAttributes rattr,
            HttpServletRequest request
    ) {

        int result = postService.postDelete(num);

        if (result == 0) {
            logger.info("게시판 삭제 실패");
            mv.addAttribute("url", request.getRequestURL());
            mv.addAttribute("message", "삭제 실패");
            return "error/error";
        } else {
            logger.info("삭제 성공");
            rattr.addFlashAttribute("result", "deleteSuccess");
            return "redirect:/post";  // 수정된 경로
        }
    }

    @ResponseBody
    @PostMapping("/down")
    public byte[] BoardFileDown(String filename,
                                HttpServletRequest request,
                                String original,
                                HttpServletResponse response) throws Exception {

        String sFilePath = SAVE_FOLDER + filename;
        File file = new File(sFilePath);
        byte[] bytes = FileCopyUtils.copyToByteArray(file);

        String sEncoding = new String(original.getBytes("utf-8"), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + sEncoding);
        response.setContentLength(bytes.length);
        return bytes;
    }

    @GetMapping(value="/setAvailable")
    public String setAvailable(int post_idx){
        postService.setAvailable(post_idx);
        return "redirect:/post/detail?num=" + post_idx;
    }

    @GetMapping(value="/setCompleted")
    public String setCompleted(int post_idx){
        postService.setCompleted(post_idx);
        return "redirect:/post/detail?num=" + post_idx;
    }
}