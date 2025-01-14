package com.ssafy.happyhouse.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.util.model.News;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/crawling")
public class NewsController {
	static ArrayList<News> newsList = new ArrayList<>();
	static Elements blogContent;
	static Elements blogOption;

	@GetMapping
	public ResponseEntity<ArrayList<News>> startCrawl() throws IOException {
		crawLawData();
		getNewsInfo();
		//service 
		
		return ResponseEntity.ok().body(newsList);
	}
	
	@ApiOperation(value = "부동산 사이트 뉴스탭을 크롤링 해온다.")
	private void crawLawData() throws IOException {
		String address = "https://www.mk.co.kr/news/realestate/";// 부동산 뉴스 url
		Document rawData = Jsoup.connect(address).timeout(5000).get();
		blogContent = rawData.select(".desctxt");
		blogOption = rawData.select(".tit");
	}

	@ApiOperation(value = "크롤링한 정보를 가공하여 List 에 담는다")
	private void getNewsInfo() {
		for (Element option : blogOption) {
			Elements link = option.select("a");
			if (link.size() == 0)
				continue;
			newsList.add(new News(link.attr("href"), link.text(), ""));
		}
		
		int idx = 0;
		for (Element e : blogContent) {
			newsList.get(idx++).setContent(e.text());
		}
	}

}