import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Crawler {
    /**
     * jsoup方式 获取虎扑新闻列表页
     * @param url 虎扑新闻列表页url
     */
    public void jsoupList(String url){
        try {
            Document document = Jsoup.connect(url).get();
            // 使用 css选择器 提取列表新闻 a 标签
            // <a href="https://voice.hupu.com/nba/2484553.html" target="_blank">霍华德：夏休期内曾节食30天，这考验了我的身心</a>
            Elements elements = document.select("div.news-list > ul > li > div.list-hd > h4 > a");
            for ( Element element:elements){
                //                System.out.println(element);
                // 获取详情页链接
                String d_url = element.attr("href");
                // 获取标题
                String title = element.ownText();

                System.out.println("详情页链接："+d_url+" ,详情页标题："+title);

            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}
