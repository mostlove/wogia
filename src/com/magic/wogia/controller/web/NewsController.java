package com.magic.wogia.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.magic.wogia.bean.BannerBean;
import com.magic.wogia.service.BannerService;
import com.magic.wogia.bean.NewsBean;
import com.magic.wogia.bean.UserBean;
import com.magic.wogia.controller.BaseController;
import com.magic.wogia.exception.InterfaceCommonException;
import com.magic.wogia.service.NewsService;
import com.magic.wogia.util.ErrorMessage;
import com.magic.wogia.util.LoginHelper;
import com.magic.wogia.util.StatusConstant;
import com.magic.wogia.util.ViewData;

/**
 * 
 * 功能：web端新闻资讯控制器
 * 编写人员：lzh
 * 时间：2016年9月18日下午5:14:21
 */
@Controller
@RequestMapping("/web/news")
public class NewsController extends BaseController{
	
	@Resource
	private NewsService newsService;
	@Resource
	private BannerService bannerServiceImpl;
	/**
	 * 添加或者更新 新闻资讯
	 * @param newsBean
	 * @return
	 */
	@RequestMapping("/addOrUpdNews")
	@ResponseBody
	public ViewData addOrUpdNews(NewsBean newsBean){
		try {
			newsService.addOrUpdNews(newsBean);
		} catch (Exception e) {
			e.printStackTrace();
			return buildFailureJson(StatusConstant.Fail_CODE, ErrorMessage.Fail);
		}
		return buildSuccessCodeJson(StatusConstant.SUCCESS_CODE, ErrorMessage.SUCCESS);
	}
	
	/**
	 * 查询 新闻资讯
	 * @param pageNum 页码
	 * @param pageSize 每页显示多少条
	 * @return
	 */
	@RequestMapping("/findNews")
	@ResponseBody
	public ViewData findNews(Integer pageNum,Integer pageSize,HttpServletRequest req){
		UserBean user = LoginHelper.getCurrentAdmin(req);
		List<NewsBean> newsList = new ArrayList<NewsBean>();
		try {
			if (user == null) {
				return buildFailureJson(StatusConstant.NOTLOGIN, ErrorMessage.NO_LOGIN);
			}
			newsList = newsService.findNews(pageNum, pageSize);
		} catch (InterfaceCommonException e) {
			return buildFailureJson(e.getErrorCode(),e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return buildFailureJson(StatusConstant.Fail_CODE, ErrorMessage.Fail);
		}
		return buildSuccessJson(StatusConstant.SUCCESS_CODE, ErrorMessage.SUCCESS, newsList);
	}
	
	
	/**
	 * 查询 新闻资讯详情
	 * @return
	 */
	@RequestMapping("/findNewsById")
	@ResponseBody
	public ViewData findNewsById(Integer id,HttpServletRequest req){
		UserBean user = LoginHelper.getCurrentAdmin(req);
		NewsBean news = new NewsBean();
		try {
			if (user == null) {
				return buildFailureJson(StatusConstant.NOTLOGIN, ErrorMessage.NO_LOGIN);
			}
			news = newsService.findById(id);
		} catch (InterfaceCommonException e) {
			return buildFailureJson(e.getErrorCode(),e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			return buildFailureJson(StatusConstant.Fail_CODE, ErrorMessage.Fail);
		}
		return buildSuccessJson(StatusConstant.SUCCESS_CODE, ErrorMessage.SUCCESS, news);
	}
	
	
	
	/**
	 *  新增/更新banner
	 * @param image
	 * @param imgURL
	 * @return
	 */
	@RequestMapping("/addOrUpdBanner")
	@ResponseBody
	public ViewData addBanner(BannerBean banner){
		
		if(null == banner){
			return buildFailureJson(StatusConstant.FIELD_NOT_NULL, "字段不能为空");
		}
		if (banner.getId() != null) {
			bannerServiceImpl.updateBanner(banner);
		}else{
			bannerServiceImpl.addBanner(banner);
		}
		
		return buildSuccessCodeJson(StatusConstant.SUCCESS_CODE, "操作成功");
	}
	@RequestMapping("/countBanners")
	@ResponseBody
	public ViewData countBanners(){
		Integer count = bannerServiceImpl.countBanner();
		return buildSuccessJson(StatusConstant.SUCCESS_CODE, "获取成功", count);
	}
	/**
	 *  获取banner
	 * @return
	 */
	@RequestMapping("/getBanners")
	@ResponseBody
	public ViewData getBanners(){
		List<BannerBean> dataList = bannerServiceImpl.findAll();
		Map<String,Object> data = new HashMap<String, Object>();
		data.put("lists", dataList);
		return buildSuccessJson(StatusConstant.SUCCESS_CODE, "获取成功", data);
	}
	
	/**
	 *  获取banner
	 * @return
	 */
	@RequestMapping("/getBannersById")
	@ResponseBody
	public ViewData getBannersById(Integer id){
		if(null == id || id==0){
			return buildFailureJson(StatusConstant.Fail_CODE, "字段不合法");
		}
		BannerBean data = bannerServiceImpl.findById(id);
		return buildSuccessJson(StatusConstant.SUCCESS_CODE, "获取成功", data);
	}
	
	/**
	 *  删除Banner
	 * @param bannerId
	 * @return
	 */
	@RequestMapping("/delBanner")
	@ResponseBody
	public ViewData delBanner(Integer bannerId){
		if(null == bannerId){
			return buildFailureJson(StatusConstant.FIELD_NOT_NULL, "字段不能为空");
		}
		bannerServiceImpl.delBanner(bannerId);
		return buildSuccessCodeJson(StatusConstant.SUCCESS_CODE, "操作成功");
	}
	
	
	
	@RequestMapping("/delNews")
	@ResponseBody
	public ViewData delNews(Integer newsId){
		
		if(null == newsId){
			return buildFailureJson(StatusConstant.FIELD_NOT_NULL, "字段不能为空");
		}
		try {
			newsService.delNews(newsId);
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			return buildFailureJson(StatusConstant.Fail_CODE, "数据异常");
		}
		return buildSuccessCodeJson(StatusConstant.SUCCESS_CODE, "操作成功");
		
	}
	/**
	 *  增加阅读数
	 * @return
	 */
	@RequestMapping("/increaseReads")
	@ResponseBody
	public ViewData increaseReads(Integer newsId){
		if(null == newsId){
			return buildFailureJson(StatusConstant.FIELD_NOT_NULL, "字段不能为空");
		}
		NewsBean news = newsService.findById(newsId);
		if(null == news){
			return buildFailureJson(StatusConstant.OBJECT_NOT_EXIST, "对象不存在");
		}
		//  增加阅读数模块
		NewsBean tempNews = new NewsBean();
		tempNews.setId(newsId);
		tempNews.setRealReads(news.getRealReads() + 1);
		try {
			newsService.addOrUpdNews(tempNews);
		} catch (Exception e) {
			logger.debug(e.getMessage(),e);
			return buildFailureJson(StatusConstant.Fail_CODE, "操作失败");
		}
		return buildSuccessCodeJson(StatusConstant.SUCCESS_CODE, "阅读成功");
	}
}
